package com.example.owner.fitpro.RegistrationLogin.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.owner.fitpro.MainActivity;
import com.example.owner.fitpro.R;
import com.example.owner.fitpro.RegistrationLogin.apis.APIs;
import com.example.owner.fitpro.RegistrationLogin.services.AuthServices;

import java.util.function.Function;

//Hongcheng Zhang
public class LoginActivity extends AppCompatActivity {

    TextInputLayout emailLayout, passwordLayout;
    TextInputEditText emailInput, passwordInput;
    Button loginButton;
    APIs apis;
    AuthServices authServices;
    //login progressBar
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        Window window = this.getWindow();

        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        window.setStatusBarColor(Color.parseColor("#00796B"));


        this.emailLayout = (TextInputLayout) findViewById(R.id.lEmailTIL);
        this.passwordLayout = (TextInputLayout) findViewById(R.id.lPasswordTIL);
        this.emailInput = (TextInputEditText) findViewById(R.id.lEmailTIET);
        this.passwordInput = (TextInputEditText) findViewById(R.id.lPasswordTIET);
        this.loginButton = (Button) findViewById(R.id.loginButton);
        //login progressBar
        this.progressBar = (ProgressBar) findViewById(R.id.progressbar);

        this.apis = new APIs();
        this.authServices = new AuthServices();

        if (this.apis.getUsersAPI().getCurrentUser() != null) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }

        this.checkEmptyEditTexts();

        this.emailInput.addTextChangedListener(new ValidateTextWatcher(this.emailInput));
        this.passwordInput.addTextChangedListener(new ValidateTextWatcher(this.passwordInput));


        //Pre-populate Login
        this.emailInput.setText("user1@example.com");
        this.passwordInput.setText("123123");




    }

    public void onClickLogin(View view) {
        String email = this.emailInput.getText().toString();
        String password = this.passwordInput.getText().toString();
        progressBar.setVisibility(View.VISIBLE);

        // Toast.makeText(getApplicationContext(), "Attempting to log in...", Toast.LENGTH_LONG).show();
        this.authServices.login(this, email, password,
                new Function<Void, Void>() {
                    @Override
                    public Void apply(Void aVoid) {
                        progressBar.setVisibility(View.GONE);
                        //Toast.makeText(getApplicationContext(), "Logged In...", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        finish();
                        return null;
                    }
                }, new Function<String, Void>() {
                    @Override
                    public Void apply(String s) {
                        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
                        return null;
                    }
                });
    }

    public void onClickToRegister(View view) {
        this.startActivity(new Intent(this, RegisterActivity.class));
    }

    public void onClickToForgotPassword(View view) {
        this.startActivity(new Intent(this, ForgotPasswordActivity.class));
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private void checkEmptyEditTexts() {
        if (TextUtils.isEmpty(this.emailInput.getText()) || TextUtils.isEmpty(this.passwordInput.getText())) {
            this.loginButton.setEnabled(false);
        } else {
            this.loginButton.setEnabled(true);
        }
    }

    //validation for email
    private boolean validateEmail() {
        if (this.emailInput.getText().toString().equals("")) {
            this.emailLayout.setErrorEnabled(false);
        }
        else if(emailInput.getText().toString().isEmpty()){
            this.emailLayout.setError("Email Can't be empty!");
            requestFocus(this.emailInput);
            return false;
        }
        else if(!emailInput.getText().toString().matches("^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+com$")){
            this.emailLayout.setError("Please enter a valid email\n(EX：firstname.lastname@example.com)");
            requestFocus(this.emailInput);
            return false;
        }
        else {
            this.emailLayout.setErrorEnabled(false);
        }

        return true;
    }

    //validation for passoword
    private boolean validatePassword() {
        if (this.passwordInput.getText().toString().equals("")) {
            this.passwordLayout.setErrorEnabled(false);
        }
        else if(passwordInput.getText().toString().isEmpty()){
            this.passwordLayout.setError("Passoword Can't be empty!");
            requestFocus(this.passwordInput);
            return false;
        }
        else if(!passwordInput.getText().toString().matches("[a-zA-Z0-9]+")){
            this.passwordLayout.setError("Password contain character, digital only");
            requestFocus(this.passwordInput);
            return false;
        }
        else if ( this.passwordInput.getText().length() < 5 ) {
            this.passwordLayout.setError("The length of password shold be more than 5");
            requestFocus(this.passwordInput);
            return false;
        }
        else {
            this.passwordLayout.setErrorEnabled(false);
        }

        return true;
    }

    private class ValidateTextWatcher implements TextWatcher {

        private View view;

        private ValidateTextWatcher(View view) {
            this.view = view;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            checkEmptyEditTexts();
        }

        @Override
        public void afterTextChanged(Editable s) {
            switch (view.getId()) {
                case R.id.lEmailTIET:
                    validateEmail();
                    break;
                case R.id.lPasswordTIET:
                    validatePassword();
                    break;

            }
        }
    }
}
