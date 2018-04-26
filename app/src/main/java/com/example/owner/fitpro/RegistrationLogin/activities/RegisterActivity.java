package com.example.owner.fitpro.RegistrationLogin.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.example.owner.fitpro.MainActivity;
import com.example.owner.fitpro.R;
import com.example.owner.fitpro.RegistrationLogin.apis.APIs;
import com.example.owner.fitpro.RegistrationLogin.services.AuthServices;

import java.util.function.Function;


//Hongcheng Zhang

public class RegisterActivity extends AppCompatActivity {

    public  Uri image;
    TextInputLayout nameLayout, emailLayout, phoneLayout, passwordLayout, rePasswordLayout;
    TextInputEditText nameInput, emailInput, phoneInput, passwordInput, rePasswordInput;
    Button registerButton;
    APIs apis;
    AuthServices authServices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_new);

        this.nameLayout = (TextInputLayout) findViewById(R.id.rNameTIL);
        this.emailLayout = (TextInputLayout) findViewById(R.id.rEmailTIL);
        this.phoneLayout = (TextInputLayout) findViewById(R.id.rPhoneTIL);
        this.passwordLayout = (TextInputLayout) findViewById(R.id.rPasswordTIL);
        this.rePasswordLayout = (TextInputLayout) findViewById(R.id.rRePasswordTIL);
        this.nameInput = (TextInputEditText) findViewById(R.id.rNameTIET);
        this.emailInput = (TextInputEditText) findViewById(R.id.rEmailTIET);
        this.phoneInput = (TextInputEditText) findViewById(R.id.rPhoneTIET);
        this.passwordInput = (TextInputEditText) findViewById(R.id.rPasswordTIET);
        this.rePasswordInput = (TextInputEditText) findViewById(R.id.rRePasswordTIET);
        this.registerButton = (Button) findViewById(R.id.registerButton);

        this.apis = new APIs();
        this.authServices = new AuthServices();

        this.checkEmptyEditTexts();

        this.nameInput.addTextChangedListener(new ValidateTextWatcher(this.nameInput));
        this.emailInput.addTextChangedListener(new ValidateTextWatcher(this.emailInput));
        this.phoneInput.addTextChangedListener(new ValidateTextWatcher(this.phoneInput));
        this.passwordInput.addTextChangedListener(new ValidateTextWatcher(this.passwordInput));
        this.rePasswordInput.addTextChangedListener(new ValidateTextWatcher(this.rePasswordInput));
    }

    public void onClickRegister(View view) {
        final String name = this.nameInput.getText().toString();
        final String email = this.emailInput.getText().toString();
        final String phone = this.phoneInput.getText().toString();
        final String password = this.passwordInput.getText().toString();

        if ( !this.validateName() && !this.validateEmail() && !this.validatePhone() &&
                !this.validatePassword() && !this.validateRePassword() ) {
            return;
        }


        String uriPath = "android.resource://" + getPackageName() + "/drawable/ic_placeholder_profile";
        Uri image = Uri.parse(uriPath);



        this.authServices.register(this, image, name, email, phone, password,
                new Function<Void, Void>() {
                    @Override
                    public Void apply(Void user) {
                        Toast.makeText(getApplicationContext(), "User Created.", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
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

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private void checkEmptyEditTexts() {
        if ( TextUtils.isEmpty(this.nameInput.getText()) || TextUtils.isEmpty(this.emailInput.getText()) ||
                TextUtils.isEmpty(this.phoneInput.getText()) || TextUtils.isEmpty(this.passwordInput.getText()) ||
                TextUtils.isEmpty(this.rePasswordInput.getText()) ) {
            this.registerButton.setEnabled(false);
        } else {
            this.registerButton.setEnabled(true);
        }
    }



    //validation for name
    private boolean validateName() {
        if(nameInput.getText().toString().isEmpty()){
            this.nameLayout.setError("Name Can't be empty!");
            this.requestFocus(this.nameInput);
            return false;
        } else if ( this.nameInput.getText().toString().trim().length() < 2 ) {
            this.nameLayout.setError("The length of name can not less than two character");
            this.requestFocus(this.nameInput);
            return false;
        } else if(!nameInput.getText().toString().matches("[a-zA-Z\\s]+")){
            this.nameLayout.setError("name field must contain characters only");
            this.requestFocus(this.nameInput);
            return false;
        }else {
            this.nameLayout.setErrorEnabled(false);
        }
        return true;
    }


    //validation for email
    private boolean validateEmail() {
        if(emailInput.getText().toString().isEmpty()){
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



    //validation for Phone
    private boolean validatePhone() {
        if(phoneInput.getText().toString().isEmpty()){
            this.phoneLayout.setError("Phone Can't be empty!");
            this.requestFocus(this.phoneLayout);
            return false;
        }
        else if( this.phoneInput.getText().toString().trim().length() < 10){
            this.phoneLayout.setError("The length of name can not short than two character");
            this.requestFocus(this.phoneLayout);
            return false;
        }
        else if(!phoneInput.getText().toString().matches("[0-9]{10}+")) {
            this.phoneLayout.setError("Please enter a valid phone number\n(EX:6475508888)");
            requestFocus(this.phoneInput);
            return false;
        }
        else {
            this.phoneLayout.setErrorEnabled(false);
        }

        return true;
    }

    //validation for password
    private boolean validatePassword() {
        if(passwordInput.getText().toString().isEmpty()){
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
        } else {
            this.passwordLayout.setErrorEnabled(false);
        }

        return true;
    }

    //validation for comfirm password
    private boolean validateRePassword() {
        if ( !this.passwordInput.getText().toString().equals(this.rePasswordInput.getText().toString()) ) {
            this.rePasswordLayout.setError("Password do not match...");
            requestFocus(this.rePasswordInput);
            return false;
        } else {
            this.rePasswordLayout.setErrorEnabled(false);
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

                case R.id.rNameTIET:
                    validateName();
                    break;
                case R.id.rEmailTIET:
                    validateEmail();
                    break;
                case R.id.rPhoneTIET:
                    validatePhone();
                    break;
                case R.id.rPasswordTIET:
                    validatePassword();
                    break;
                case R.id.rRePasswordTIET:
                    validateRePassword();
                    break;

            }
        }
    }

}