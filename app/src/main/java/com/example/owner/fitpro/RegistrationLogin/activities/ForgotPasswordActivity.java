package com.example.owner.fitpro.RegistrationLogin.activities;

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

import com.example.owner.fitpro.R;
import com.example.owner.fitpro.RegistrationLogin.services.AuthServices;

import java.util.function.Function;



public class ForgotPasswordActivity extends AppCompatActivity {

    TextInputLayout emailLayout;
    TextInputEditText emailInput;
    Button resetButton;
    AuthServices authServices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        this.emailLayout = (TextInputLayout) findViewById(R.id.fEmailTIL);
        this.emailInput = (TextInputEditText) findViewById(R.id.fEmailTIET);
        this.resetButton = (Button) findViewById(R.id.resetButton);

        this.authServices = new AuthServices();

        this.checkEmptyEditTexts();

        this.emailInput.addTextChangedListener(new ValidateTextWatcher(this.emailInput));
    }

    public void onClickReset(View view) {
        String email = this.emailInput.getText().toString();

        if ( !this.validateEmail() ) {
            return;
        }

        this.authServices.reset(this, email,
                new Function<Void, Void>() {
                    @Override
                    public Void apply(Void aVoid) {
                        Toast.makeText(getApplicationContext(), "Password Sent...", Toast.LENGTH_LONG).show();
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
        if ( TextUtils.isEmpty(this.emailInput.getText()) ) {
            this.resetButton.setEnabled(false);
        } else {
            this.resetButton.setEnabled(true);
        }
    }

    private boolean validateEmail() {
        if ( !android.util.Patterns.EMAIL_ADDRESS.matcher(this.emailInput.getText().toString()).matches() ) {
            this.emailLayout.setError("Please enter a valid email...");
            requestFocus(this.emailInput);
            return false;
        } else {
            this.emailLayout.setErrorEnabled(false);
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
                case R.id.fEmailTIET:
                    validateEmail();
                    break;
            }
        }
    }
}
