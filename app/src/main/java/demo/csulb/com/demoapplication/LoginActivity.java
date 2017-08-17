package demo.csulb.com.demoapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private TextInputEditText userNameEditText;
    private TextInputEditText passwordEditText;

    private static final String TAG = "LoginActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userNameEditText = (TextInputEditText) findViewById(R.id.userNameEditText);
        passwordEditText = (TextInputEditText) findViewById(R.id.passwordEditText);
        AppCompatButton sign_in_button = (AppCompatButton) findViewById(R.id.sign_in_button);

//        userNameEditText.setText("csulb");
//        passwordEditText.setText("csulb");

        sign_in_button.setOnClickListener(this);
    }

    private void login() {
        String userName = userNameEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        boolean validationFailed = false;

        if (TextUtils.isEmpty(userName)) {
            userNameEditText.setError("User Name is Required!");
            validationFailed = true;
        }
        if (TextUtils.isEmpty(password)) {
            passwordEditText.setError("Password is Required!");
            validationFailed = true;
        }
        Log.i(TAG, "login: Validation Status: " + validationFailed);
        if (!validationFailed) {
            if (userName.equals("csulb") && password.equals("csulb")) {
                Toast.makeText(this, "Login Successful!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(LoginActivity.this, CountryActivity.class));
                finish();
            } else {
                Toast.makeText(this, "Login Failed! Please check your credentials", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button:
                login();
                break;
        }
    }
}

