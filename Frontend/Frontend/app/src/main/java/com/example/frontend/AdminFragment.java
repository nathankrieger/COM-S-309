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

import java.util.HashMap;
import java.util.Map;

public class AdminFragment extends Fragment {

    private EditText adminUpdateUserRoleIdEditText, adminUpdateUserRoleEditText;
    private Button adminUpdateRoleButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for the fragment
        View view = inflater.inflate(R.layout.fragment_admin, container, false);

        // Initialize views with updated IDs
        adminUpdateUserRoleIdEditText = view.findViewById(R.id.adminUpdateUserRoleIdEditText);
        adminUpdateUserRoleEditText = view.findViewById(R.id.adminUpdateUserRoleEditText);
        adminUpdateRoleButton = view.findViewById(R.id.adminUpdateRoleButton);

        // Handle the update role button click
        adminUpdateRoleButton.setOnClickListener(v -> {
            String updateUserRoleIdText = adminUpdateUserRoleIdEditText.getText().toString();
            String newRole = adminUpdateUserRoleEditText.getText().toString();
            if (!updateUserRoleIdText.isEmpty() && !newRole.isEmpty()) {
                int userId = Integer.parseInt(updateUserRoleIdText);
                updateUserRole(userId, newRole);
            } else {
                Toast.makeText(getContext(), "Please enter a user ID and a role", Toast.LENGTH_SHORT).show();
            }
        });

        return view; // Return the inflated view
    }

    // Update the user's role
    private void updateUserRole(int userId, String newRole) {
        String url = "http://coms-3090-022.class.las.iastate.edu:8080/users/roles/update/" + userId;

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
            // Adding the body to the request
            @Override
            public byte[] getBody() {
                return newRole.getBytes(); // Send the role as a plain string
            }

            // Set the Content-Type to indicate plain text
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
