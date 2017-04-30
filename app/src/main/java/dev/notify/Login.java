package dev.notify;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Login extends AppCompatActivity  {
    private static final String TAG = "Login";
    public static final String MyPREFERENCES = "notify" ;
    SharedPreferences sharedPreferences;

    Button signInBtn;
    TextView registerLink;
    EditText usernameEditText;
    EditText passwordEditText;
    CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        usernameEditText = (EditText)findViewById(R.id.usernameEditText);
        passwordEditText = (EditText)findViewById(R.id.passwordEditText);
        signInBtn = (Button)findViewById(R.id.signInBtn);
        registerLink = (TextView)findViewById(R.id.registerText);
        coordinatorLayout = (CoordinatorLayout)findViewById(R.id.coordinator);

        //Setting Share Preference
        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        //When Click SignIn
        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        //When Click Register
        registerLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Register.class));
            }
        });

        //Check Login Status From SharedPreference
        int loginStatus = sharedPreferences.getInt("login", 0);
        if(loginStatus == 1){
            String user_type = sharedPreferences.getString("user_type", null);
            if(Objects.equals(user_type, "admin")){
                goToAdmin();
            }
            else{
                goToMember();
            }
        }


    }

    private void goToMember() {
        startActivity(new Intent(getApplicationContext(), Member.class));
        finish();
    }

    private void goToAdmin() {
        startActivity(new Intent(getApplicationContext(), Admin.class));
        finish();
    }

    private UsersModel login() {
        boolean cancel = false;
        View focusView = null;
        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        if(TextUtils.isEmpty(username)){
            cancel = true;
            focusView = usernameEditText;
        }
        else if(TextUtils.isEmpty(password)){
            cancel = true;
            focusView = passwordEditText;
        }
        if(cancel){
            focusView.requestFocus();
        }
        else{
            try {
                getLogin(username, password);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    private void getLogin(final String username, String password) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("username", username);
        jsonObject.put("password", password);
        String url = "https://notify-163706.appspot.com/api/users/count?where="+jsonObject.toString();
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Login.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Snackbar snackbar = Snackbar.make(coordinatorLayout, "Please Check Your Internet Connection", Snackbar.LENGTH_LONG);
                        snackbar.show();
                    }
                });
                Log.e("onFailure", e.getMessage());
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String jsonData = response.body().string();
                JSONObject json ;
                try {
                    json = new JSONObject(jsonData);
                    String count = json.getString("count");
                    if(count == "0"){
                        //Fail Login
                        Login.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Snackbar snackbar = Snackbar.make(coordinatorLayout,"Username Or Password Incorrect", Snackbar.LENGTH_LONG);
                                snackbar.show();
                            }
                        });
                    }
                    else{
                        //Success Login
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        //Go to admin page
                        if(Objects.equals(username, "admin")){
                            Log.i(TAG, "user_type: ADMIN");
                            //Set Share Preference
                            editor.putInt("login", 1);
                            editor.putString("user_type", "admin");
                            editor.putString("username", "admin");
                            editor.commit();
                            Login.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Intent intent = new Intent(Login.this, Admin.class);
                                    startActivity(intent);
                                    finish();
                                }
                            });
                        }
                        else {
                            Log.i(TAG, "user_type: member");
                            //Set Share Preference
                            editor.putInt("login", 1);
                            editor.putString("user_type", "member");
                            editor.putString("username", username);
                            editor.commit();

                            Login.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Intent intent = new Intent(Login.this, Member.class);
                                    startActivity(intent);
                                    finish();
                                }
                            });
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

//                Log.i("Response", jsonData);
            }
        });

    }

}
