package dev.notify;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Login extends AppCompatActivity implements View.OnClickListener {

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

        signInBtn.setOnClickListener(this);
        registerLink.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.signInBtn: login();break;
            case R.id.registerText:
                startActivity(new Intent(this, Register.class));
                break;
        }
    }

    private void login() {
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
            getLogin(username, password);
        }


    }

    private void getLogin(final String username, String password) {
        String json = dataToJson(username,password);
        String url = "http://192.168.1.49:3000/api/Members/count?where="+json;
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("onFailure", e.getMessage());
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String jsonData = response.body().string();
                JSONObject json = null;
                Log.i("Json", jsonData);
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

                        //Go to admin page
                        if(username == "admin"){
                            Login.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    startActivity(new Intent(Login.this, Admin.class));
                                }
                            });
                        }
                        Login.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                startActivity(new Intent(Login.this, Member.class));
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

//                Log.i("Response", jsonData);
            }
        });

    }
    private String dataToJson(String username, String password){
        return "{\"username\":" +"\""+username+"\","+
                "\"password\":"+"\""+password+"\""+
                "}";
    }
}
