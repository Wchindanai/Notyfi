package dev.notify;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Register extends AppCompatActivity {
    private static final String TAG = "Register";
    EditText firstnameEditText;
    EditText lastnameEditText;
    EditText emailEditText;
    EditText telEditText;
    EditText usernameEditText;
    EditText passwordEditText;
    EditText confirmPasswordEditText;
    Button registerBtn;
    View registerForm;
    View registerProgress;
    CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        firstnameEditText = (EditText)findViewById(R.id.firstnameEditText);
        lastnameEditText = (EditText)findViewById(R.id.lastnameEditText);
        emailEditText = (EditText)findViewById(R.id.emailEditText);
        telEditText = (EditText)findViewById(R.id.telEditText);
        usernameEditText = (EditText)findViewById(R.id.usernameEditText);
        passwordEditText = (EditText)findViewById(R.id.passwordEditText);
        confirmPasswordEditText = (EditText)findViewById(R.id.confirmPasswordEditText);
        registerBtn = (Button)findViewById(R.id.registerBtn);
        registerForm = findViewById(R.id.register_form);
        registerProgress = findViewById(R.id.register_progress);

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinator);

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerMember();
            }
        });


    }


    public void registerMember(){
        String firstname = firstnameEditText.getText().toString();
        String lastname = lastnameEditText.getText().toString();
        String email = emailEditText.getText().toString();
        String tel = telEditText.getText().toString();
        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String confirmPassword = confirmPasswordEditText.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if(TextUtils.isEmpty(firstname)){
            firstnameEditText.setError("Fill this field");
            focusView = firstnameEditText;
            cancel = true;
        }
        if(TextUtils.isEmpty(lastname)){
            lastnameEditText.setError("Fill this field");
            focusView = lastnameEditText;
            cancel = true;
        }
        if(TextUtils.isEmpty(email)){
            emailEditText.setError("Fill this field");
            focusView = emailEditText;
            cancel = true;
        }
        else if (!isEmailValid(email)){
            emailEditText.setError("Not valid email");
            focusView = emailEditText;
            cancel = true;
        }
        if(TextUtils.isEmpty(tel)){
            telEditText.setError("Fill this field");
            focusView = telEditText;
            cancel = true;
        }
        else if(!isTelValid(tel)){
            telEditText.setError("Not valid Telephone Number");
            focusView = telEditText;
            cancel = true;
        }
        if(TextUtils.isEmpty(username)){
            usernameEditText.setError("Fill this field");
            focusView = usernameEditText;
            cancel = true;
        }
        if(TextUtils.isEmpty(password)){
            passwordEditText.setError("Fill this field");
            focusView = passwordEditText;
            cancel = true;
        }
        if(TextUtils.isEmpty(confirmPassword)){
            confirmPasswordEditText.setError("Fill this field");
            focusView = confirmPasswordEditText;
            cancel = true;
        }
        if(!isValidPassword(password, confirmPassword)){
            confirmPasswordEditText.setError("Confirm password not match");
            focusView = confirmPasswordEditText;
            cancel = true;
        }

        if(cancel){
            focusView.requestFocus();
        }
        else{
            showProgress(true);
            JSONObject json = setJsonObject(firstname, lastname, tel, email, username, password);
            postData(json);

        }

    }

    private JSONObject setJsonObject(String firstname, String lastname, String tel, String email, String username, String password) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("username", username);
            jsonObject.put("password", password);
            jsonObject.put("first_name", firstname);
            jsonObject.put("last_name", lastname);
            jsonObject.put("email", email);
            jsonObject.put("mobile_no", tel);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;

    }


    private void postData(JSONObject json) {
        String url = "https://notify-163706.appspot.com/api/users";
        OkHttpClient client = new OkHttpClient();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, json.toString());
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                Log.i("DebugFail", e.getMessage());
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                int statusCode = response.code();
                if(statusCode == 200){
                    Log.i("StatusCode", String.valueOf(statusCode));
                    Register.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "Register Success", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(getApplicationContext(), Login.class));
                            finish();
                        }
                    });

                }
                else if (statusCode == 500){
                    showProgress(false);
                    Log.i("StatusCode", String.valueOf(statusCode));
                    Register.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            usernameEditText.setError("This username Or Email is used");
                            View focusView = usernameEditText;
                            focusView.requestFocus();
                        }
                    });
                }
            }
        });
    }


    private void showProgress(final boolean show) {
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);
        registerForm.setVisibility(show ? View.VISIBLE : View.GONE);
        registerForm.animate().setDuration(shortAnimTime).alpha(show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                registerForm.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });
        registerProgress.setVisibility(show ? View.VISIBLE : View.GONE);
        registerProgress.animate().setDuration(shortAnimTime).alpha(
                show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                registerProgress.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });
    }

    private boolean isValidPassword(String password, String confirmPassword){
        return password.equals(confirmPassword);
    }

    private boolean isTelValid(String tel) {
        return tel.length() > 9;
    }

    private boolean isEmailValid(String email) {
        return email.contains("@");
    }
}


