package com.example.frontend;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.frontend.models.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.AccessController;

public class UserManager {
    private static UserManager instance;
    private User user;  // Custom user object, you define this class

    private UserManager() {
        // Private constructor to prevent instantiation
    }

    public static synchronized UserManager getInstance() {
        if (instance == null) {
            instance = new UserManager();
        }
        return instance;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public boolean isLoggedIn() {
        return user != null;
    }

    public void fetchUserRoleFromBackend(int userId, Context context, final RoleFetchListener listener) {
        String url = "http://coms-3090-022.class.las.iastate.edu:8080/roles/user/" + userId;

        // Create a request to get the role from the server
        StringRequest roleRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Log the raw response to see what the backend returns
                        Log.d("MainActivity", "Raw response: " + response);

                        try {
                            JSONObject jsonResponse = new JSONObject(response);

                            // Now use "roleName" instead of "role" as the field name
                            if (jsonResponse.has("roleName")) {
                                String role = jsonResponse.getString("roleName");
                                listener.onRoleFetched(role);  // Callback with the role
                            } else {
                                listener.onError("Role not found in response.");
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("MainActivity", "Failed to parse role. JSON exception: " + e.getMessage());
                            listener.onError("Failed to parse role.");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        listener.onError("Network error: " + error.getMessage());
                    }
                }
        );

        // Add the request to the Volley request queue
        Volley.newRequestQueue(context).add(roleRequest);  // Use the passed context
    }

    // Interface to handle the role fetch callback
    public interface RoleFetchListener {
        void onRoleFetched(String role);
        void onError(String error);
    }
}
