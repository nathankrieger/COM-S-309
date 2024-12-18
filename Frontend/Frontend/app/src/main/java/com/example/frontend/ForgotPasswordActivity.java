package com.example.frontend;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ForgotPasswordActivity extends AppCompatActivity {

    private TextView usernameTextView;
    private EditText passwordEditText;
    private Button submitButton;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);  // Ensure you are using your existing layout file

        // Initialize the views
        usernameTextView = findViewById(R.id.username_text_view);  // Username display
        passwordEditText = findViewById(R.id.login_password_edt);  // Password input
        submitButton = findViewById(R.id.login_login_btn);  // Submit button

        // Initialize the Volley request queue
        requestQueue = Volley.newRequestQueue(this);

        // Get the username from the Intent or other source (passed from previous activity)
        String username = getIntent().getStringExtra("USERNAME");

        // Display the username in the TextView
        if (username != null && !username.isEmpty()) {
            usernameTextView.setText("Username: " + username);
        } else {
            usernameTextView.setText("No username provided");
        }

        // Set the click listener for the submit button
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the new password entered by the user
                String newPassword = passwordEditText.getText().toString();

                // Ensure the new password is not empty and has at least 6 characters
                if (newPassword.isEmpty()) {
                    Toast.makeText(ForgotPasswordActivity.this, "Please enter a new password", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Check if the new password is at least 6 characters long
                if (newPassword.length() < 6) {
                    Toast.makeText(ForgotPasswordActivity.this, "Password must be at least 6 characters long", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Send request to update the password
                sendUpdatePasswordRequest(username, newPassword);
            }
        });
    }

    // Method to send a request to update the user's password
    private void sendUpdatePasswordRequest(final String username, final String newPassword) {
        String url = "http://coms-3090-022.class.las.iastate.edu:8080/users/ForgotPassword";  // API URL

        // Create JSON object for the request body
        Map<String, String> params = new HashMap<>();
        params.put("name", username);
        params.put("password", newPassword);
        JSONObject jsonBody = new JSONObject(params);

        // Create a PUT request
        JsonObjectRequest putRequest = new JsonObjectRequest(Request.Method.PUT, url, jsonBody,
                response -> {
                    try {
                        // Parse the response and check if the update was successful
                        int userId = response.getInt("id");

                        // Check if the user ID is -1 (unsuccessful password update)
                        if (userId == -1) {
                            Toast.makeText(ForgotPasswordActivity.this, "Failed to update password: Username does not exist", Toast.LENGTH_LONG).show();
                        } else {
                            // Successful password update
                            Toast.makeText(ForgotPasswordActivity.this, "Password updated successfully!", Toast.LENGTH_SHORT).show();

                            // After a successful password reset, navigate back to MainActivity
                            Intent intent = new Intent(ForgotPasswordActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();  // Finish the current activity so the user cannot navigate back
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(ForgotPasswordActivity.this, "Error parsing response", Toast.LENGTH_LONG).show();
                    }
                },
                error -> {
                    // Handle error response
                    Toast.makeText(ForgotPasswordActivity.this, "Failed to update password: " + error.getMessage(), Toast.LENGTH_LONG).show();
                }
        );

        // Add the request to the Volley request queue
        requestQueue.add(putRequest);
    }
}
