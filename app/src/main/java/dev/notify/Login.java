package dev.notify;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Login extends AppCompatActivity implements View.OnClickListener {

    Button signInBtn;
    TextView registerLink;
    EditText usernameEditText;
    EditText passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameEditText = (EditText)findViewById(R.id.usernameEditText);
        passwordEditText = (EditText)findViewById(R.id.passwordEditText);
        signInBtn = (Button)findViewById(R.id.signInBtn);
        registerLink = (TextView)findViewById(R.id.registerText);

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

    private void getLogin(String username, String password) {
        String json = dataToJson(username,password);

    }
    private String dataToJson(String username, String password){

    }
}
