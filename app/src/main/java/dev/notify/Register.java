package dev.notify;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.R.attr.value;
import static dev.notify.R.string.firstname;
import static dev.notify.R.string.lastname;

public class Register extends AppCompatActivity implements View.OnClickListener{
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
        registerBtn.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.registerBtn : registerMember(); break;
        }
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
            String json = dataToJson(firstname, lastname, tel, email, username, password);
            postData(json);

        }

    }

    private String dataToJson(String firstname, String lastname, String tel, String email, String username, String password) {
        return "{" +
                "\"firstname\":\"" + firstname + "\","+
                "\"lastname\" : \"" + lastname + "\"," +
                "\"tel\" : \"" + tel + "\","+
                "\"email\" : \"" + email + "\","+
                "\"username\" : \"" + username + "\"," +
                "\"password\" : \"" + password + "\","+
                "\"admin\" : \"0\" "+
                "}";
    }

    private void postData(String json) {
        String url = "https://notify-160811.appspot.com/api/Members";
        OkHttpClient client = new OkHttpClient();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, json);
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
                            startActivity(new Intent(Register.this, Login.class));
                        }
                    });

                }
                else if (statusCode == 500){
                    Log.i("StatusCode", String.valueOf(statusCode));
                    Register.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            usernameEditText.setError("This username is used");
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
        if( password.equals(confirmPassword)){
            return true;
        }
        else{
            return false;
        }
    }

    private boolean isTelValid(String tel) {
        return tel.length() < 9;
    }

    private boolean isEmailValid(String email) {
        return email.contains("@");
    }
}


