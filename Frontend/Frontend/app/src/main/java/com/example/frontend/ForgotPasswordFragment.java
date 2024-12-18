package com.example.frontend;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.frontend.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ForgotPasswordFragment extends Fragment {

    private TextView usernameTextView;
    private EditText passwordEditText;
    private Button submitButton;
    private RequestQueue requestQueue;
    private String username;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the fragment's layout
        return inflater.inflate(R.layout.fragment_forgot_password, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize the views
        usernameTextView = view.findViewById(R.id.username_text_view);
        passwordEditText = view.findViewById(R.id.login_password_edt);
        submitButton = view.findViewById(R.id.login_login_btn);

        // Initialize the Volley request queue
        requestQueue = Volley.newRequestQueue(requireContext());

        // Get the username from arguments (passed from previous fragment/activity)
        Bundle args = getArguments();
        if (args != null) {
            username = args.getString("USERNAME");
        }

        // Display the username in the TextView
        if (username != null && !username.isEmpty()) {
            usernameTextView.setText("Username: " + username);
        } else {
            usernameTextView.setText("No username provided");
        }

        // Set the click listener for the submit button
        submitButton.setOnClickListener(v -> {
            String newPassword = passwordEditText.getText().toString();

            if (newPassword.isEmpty()) {
                Toast.makeText(requireContext(), "Please enter a new password", Toast.LENGTH_SHORT).show();
                return;
            }

            if (newPassword.length() < 6) {
                Toast.makeText(requireContext(), "Password must be at least 6 characters long", Toast.LENGTH_SHORT).show();
                return;
            }

            sendUpdatePasswordRequest(username, newPassword);
        });
    }

    // Method to send a request to update the user's password
    private void sendUpdatePasswordRequest(final String username, final String newPassword) {
        String url = "http://coms-3090-022.class.las.iastate.edu:8080/users/ForgotPassword";

        Map<String, String> params = new HashMap<>();
        params.put("name", username);
        params.put("password", newPassword);
        JSONObject jsonBody = new JSONObject(params);

        // Create a PUT request
        JsonObjectRequest putRequest = new JsonObjectRequest(Request.Method.PUT, url, jsonBody,
                response -> {
                    try {
                        int userId = response.getInt("id");

                        if (userId == -1) {
                            Toast.makeText(requireContext(), "Failed to update password: Username does not exist", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(requireContext(), "Password updated successfully!", Toast.LENGTH_SHORT).show();

                            // Navigate back to MainActivity
                            Intent intent = new Intent(requireContext(), MainActivity.class);
                            startActivity(intent);
                            requireActivity().finish();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(requireContext(), "Error parsing response", Toast.LENGTH_LONG).show();
                    }
                },
                error -> Toast.makeText(requireContext(), "Failed to update password: " + error.getMessage(), Toast.LENGTH_LONG).show()
        );

        requestQueue.add(putRequest);
    }
}
