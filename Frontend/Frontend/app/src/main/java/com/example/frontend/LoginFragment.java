package com.example.frontend;

// Imports for required Android and Java libraries
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.frontend.models.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * LoginFragment handles user login functionality, including username validation,
 * sending login requests, navigating to other fragments, and handling server responses.
 */
public class LoginFragment extends Fragment {

    // UI components for login form
    private EditText usernameEditText; // Input for username
    private EditText passwordEditText; // Input for password
    private Button loginButton; // Login action button
    private Button signupButton; // Signup navigation button
    private TextView forgotPasswordTextView; // Forgot password link

    // For managing network requests
    private RequestQueue requestQueue;

    /**
     * Inflates the login fragment UI and initializes components.
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        // Bind UI components to their respective layout elements
        usernameEditText = view.findViewById(R.id.login_username_edt);
        passwordEditText = view.findViewById(R.id.login_password_edt);
        loginButton = view.findViewById(R.id.login_login_btn);
        signupButton = view.findViewById(R.id.login_signup_btn);
        forgotPasswordTextView = view.findViewById(R.id.forgot_password_tv);

        // Initialize the Volley request queue
        requestQueue = Volley.newRequestQueue(requireContext());

        // Disable login button initially
        loginButton.setEnabled(false);

        // Add TextWatcher to enable login button only when input is valid
        TextWatcher inputWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                boolean isUsernameValid = !usernameEditText.getText().toString().trim().isEmpty();
                boolean isPasswordValid = !passwordEditText.getText().toString().trim().isEmpty();
                loginButton.setEnabled(isUsernameValid && isPasswordValid);
            }
        };
        usernameEditText.addTextChangedListener(inputWatcher);
        passwordEditText.addTextChangedListener(inputWatcher);

        // Handle login button click
        loginButton.setOnClickListener(v -> {
            String username = usernameEditText.getText().toString();
            String password = passwordEditText.getText().toString();
            sendLoginRequest(username, password); // Initiates the login process
        });

        // Handle signup button click to navigate to SignupFragment
        signupButton.setOnClickListener(v ->
                requireActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new SignupFragment())
                        .addToBackStack(null)
                        .commit()
        );

        // Handle forgot password click
        forgotPasswordTextView.setOnClickListener(v -> {
            String enteredUsername = usernameEditText.getText().toString();
            if (enteredUsername.isEmpty()) {
                Toast.makeText(requireContext(), "Please enter your username", Toast.LENGTH_LONG).show();
            } else {
                checkUsernameExistence(enteredUsername); // Verifies username existence
            }
        });

        return view;
    }

    /**
     * Sends a login request to the server using a POST request.
     */
    private void sendLoginRequest(final String username, final String password) {
        String url = "http://coms-3090-022.class.las.iastate.edu:8080/users/login";

        // Prepare the request body
        Map<String, String> params = new HashMap<>();
        params.put("name", username);
        params.put("password", password);
        JSONObject jsonBody = new JSONObject(params);

        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, url, jsonBody,
                response -> {
                    try {
                        int userId = response.getInt("id");
                        if (userId == -1) {
                            Toast.makeText(requireContext(), "Invalid username or password", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(requireContext(), "Login successful!", Toast.LENGTH_SHORT).show();
                            User user = new User(username, password);
                            user.setUserID(userId);

                            UserManager.getInstance().setUser(user); // Save user in singleton

                            // Navigate to MainActivity
                            Intent intent = new Intent(getActivity(), MainActivity.class);
                            intent.putExtra("USERNAME", username);
                            intent.putExtra("USER_ID", userId);
                            startActivity(intent);
                            requireActivity().finish();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(requireContext(), "Error parsing response", Toast.LENGTH_LONG).show();
                    }
                },
                error -> Toast.makeText(requireContext(), "Login failed: " + error.getMessage(), Toast.LENGTH_LONG).show()
        );

        requestQueue.add(postRequest); // Add request to queue
    }

    /**
     * Checks if a username exists on the server via a GET request.
     */
    private void checkUsernameExistence(final String username) {
        String url = "http://coms-3090-022.class.las.iastate.edu:8080/users/name/" + username;

        StringRequest getRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        int userId = jsonResponse.getInt("id");
                        if (userId == -1) {
                            Toast.makeText(requireContext(), "Username does not exist", Toast.LENGTH_LONG).show();
                        } else {
                            // Navigate to ForgotPasswordFragment
                            ForgotPasswordFragment forgotPasswordFragment = new ForgotPasswordFragment();
                            Bundle bundle = new Bundle();
                            bundle.putString("USERNAME", username);
                            forgotPasswordFragment.setArguments(bundle);

                            requireActivity().getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.fragment_container, forgotPasswordFragment)
                                    .addToBackStack(null)
                                    .commit();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(requireContext(), "Error parsing response", Toast.LENGTH_LONG).show();
                    }
                },
                error -> Toast.makeText(requireContext(), "Error checking username: " + error.getMessage(), Toast.LENGTH_LONG).show()
        );

        requestQueue.add(getRequest); // Add request to queue
    }
}
