package com.example.frontend;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class ManagerFragment extends Fragment {

    private EditText banUserIdEditText;
    private EditText updateUserRoleIdEditText, updateUserRoleEditText;
    private Button banButton, updateRoleButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_manager, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize views
        banUserIdEditText = view.findViewById(R.id.banUserIdEditText);
        updateUserRoleIdEditText = view.findViewById(R.id.updateUserRoleIdEditText);
        updateUserRoleEditText = view.findViewById(R.id.updateUserRoleEditText);
        banButton = view.findViewById(R.id.banButton);
        updateRoleButton = view.findViewById(R.id.updateRoleButton);

        // Handle the ban button click
        banButton.setOnClickListener(v -> {
            String banUserIdText = banUserIdEditText.getText().toString();
            if (!banUserIdText.isEmpty()) {
                int userId = Integer.parseInt(banUserIdText);
                banUser(userId);
            } else {
                Toast.makeText(getContext(), "Please enter a user ID to ban", Toast.LENGTH_SHORT).show();
            }
        });

        // Handle the update role button click
        updateRoleButton.setOnClickListener(v -> {
            String updateUserRoleIdText = updateUserRoleIdEditText.getText().toString();
            String newRole = updateUserRoleEditText.getText().toString();
            if (!updateUserRoleIdText.isEmpty() && !newRole.isEmpty()) {
                int userId = Integer.parseInt(updateUserRoleIdText);
                updateUserRole(userId, newRole);
            } else {
                Toast.makeText(getContext(), "Please enter a user ID and a role", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Ban a user by ID
    private void banUser(int userId) {
        String url = "http://coms-3090-022.class.las.iastate.edu:8080/users/delete/" + userId;

        StringRequest request = new StringRequest(Request.Method.DELETE, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getContext(), "User banned successfully", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), "Failed to ban user", Toast.LENGTH_SHORT).show();
                    }
                });

        Volley.newRequestQueue(getContext()).add(request);
    }

    private void updateUserRole(int userId, String newRole) {
        String url = "http://coms-3090-022.class.las.iastate.edu:8080/users/roles/update/" + userId;

        // Send the new role name directly as a plain text string
        StringRequest request = new StringRequest(Request.Method.PUT, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getContext(), "Role updated successfully", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), "Failed to update role", Toast.LENGTH_SHORT).show();
                    }
                }) {
            // Override the getBody method to send the newRole as a plain string
            @Override
            public byte[] getBody() {
                return newRole.getBytes(); // Send the role as a plain string in the body
            }

            // Set the Content-Type header to indicate plain text
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "text/plain"); // Use text/plain since we're sending a raw string
                return headers;
            }
        };

        Volley.newRequestQueue(getContext()).add(request);
    }
}

