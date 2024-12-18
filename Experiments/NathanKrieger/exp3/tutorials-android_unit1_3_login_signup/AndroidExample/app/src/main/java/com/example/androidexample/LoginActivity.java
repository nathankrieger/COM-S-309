package com.example.androidexample;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {

    private EditText usernameEditText;  // define username edittext variable
    private EditText passwordEditText;  // define password edittext variable
    private Button loginButton;         // define login button variable
    private Button signupButton;        // define signup button variable

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);  // link to Login activity XML

        // Initialize UI elements
        usernameEditText = findViewById(R.id.login_username_edt);
        passwordEditText = findViewById(R.id.login_password_edt);
        loginButton = findViewById(R.id.login_login_btn);    // link to login button in the Login activity XML
        signupButton = findViewById(R.id.login_signup_btn);  // link to signup button in the Login activity XML

        // Initially disable the login button
        loginButton.setEnabled(false);

        // Create a TextWatcher to enable or disable the login button
        TextWatcher inputWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                // Check if both fields are not empty
                boolean isUsernameValid = usernameEditText.getText().toString().trim().length() > 0;
                boolean isPasswordValid = passwordEditText.getText().toString().trim().length() > 0;

                // Enable the login button if both fields have text
                loginButton.setEnabled(isUsernameValid && isPasswordValid);
            }
        };

        // Attach TextWatcher to both EditTexts
        usernameEditText.addTextChangedListener(inputWatcher);
        passwordEditText.addTextChangedListener(inputWatcher);

        // Click listener for login button pressed
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Grab strings from user inputs
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                // Use intent to switch to MainActivity
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.putExtra("USERNAME", username);  // key-value to pass to the MainActivity
                intent.putExtra("PASSWORD", password);  // key-value to pass to the MainActivity
                startActivity(intent);  // go to MainActivity with the key-value data
            }
        });

        // Click listener for signup button pressed
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Switch to SignupActivity
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);  // go to SignupActivity
            }
        });
    }
}
