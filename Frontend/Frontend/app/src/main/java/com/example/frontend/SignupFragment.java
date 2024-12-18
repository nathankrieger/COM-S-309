package com.example.frontend;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class SignupFragment extends Fragment {

    private EditText usernameEditText;  // define username edittext variable
    private EditText passwordEditText;  // define password edittext variable
    private EditText confirmEditText;   // define confirm edittext variable
    private Button loginButton;         // define login button variable
    private Button signupButton;        // define signup button variable

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_signup, container, false);

        // Initialize UI elements
        usernameEditText = view.findViewById(R.id.signup_username_edt);
        passwordEditText = view.findViewById(R.id.signup_password_edt);
        confirmEditText = view.findViewById(R.id.signup_confirm_edt);
        loginButton = view.findViewById(R.id.signup_login_btn);
        signupButton = view.findViewById(R.id.signup_signup_btn);

        // Click listener on login button
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Switch to LoginFragment (assuming it's handled with FragmentTransaction)
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new LoginFragment()) // Assuming container ID is fragment_container
                        .addToBackStack(null) // Allows user to go back to previous fragment
                        .commit();
            }
        });

        // Click listener on signup button
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Grab strings from user inputs
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                String confirm = confirmEditText.getText().toString();

                if (password.length() < 6) {
                    Toast.makeText(getContext(), "Password must be at least 6 characters long", Toast.LENGTH_LONG).show();
                } else if (password.equals(confirm)) {
                    Toast.makeText(getContext(), "Signing up", Toast.LENGTH_LONG).show();
                    signupUser(username, password); // Call the signup method
                } else {
                    Toast.makeText(getContext(), "Passwords do not match", Toast.LENGTH_LONG).show();
                }
            }
        });

        return view;
    }

    // Method to handle user signup
    private void signupUser(String username, String password) {
        String url = "http://coms-3090-022.class.las.iastate.edu:8080/users/create";

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("name", username);
            jsonObject.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Error creating JSON object", Toast.LENGTH_LONG).show();
            return;
        }

        // Volley POST request
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            int userId = response.getInt("id");

                            // Handle different response scenarios
                            if (userId == -2) {
                                // Username already exists
                                Toast.makeText(getContext(), "Username already exists", Toast.LENGTH_LONG).show();
                            } else if (userId == -1) {
                                // Other errors
                                Toast.makeText(getContext(), "Error creating user. Please try again.", Toast.LENGTH_LONG).show();
                            } else {
                                // Successful response: Handle new user
                                //String message = response.getString("message");
                                Toast.makeText(getContext(), "Success!", Toast.LENGTH_LONG).show();

                                // Redirect to LoginFragment after successful signup
                                navigateToLoginFragment();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getContext(), "Error", Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle network error
                        Toast.makeText(getContext(), "Network error: " + error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
        );

        requestQueue.add(jsonObjectRequest);
    }

    private void navigateToLoginFragment() {
        // Simplifies the navigation logic
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new LoginFragment())
                .addToBackStack(null)
                .commit();
    }

}
