package com.example.frontend;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class RoleActivity extends AppCompatActivity {

    private EditText roleNameEditText; // To input role name
    private EditText roleIdEditText; // To input role ID for update/delete
    private Button createRoleButton; // Button to create role
    private Button readRoleButton; // Button to read role
    private Button updateRoleButton; // Button to update role
    private Button deleteRoleButton; // Button to delete role

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roles);

        // Initialize UI elements
        roleNameEditText = findViewById(R.id.role_name_edt);
        roleIdEditText = findViewById(R.id.role_id_edt);
        createRoleButton = findViewById(R.id.create_role_btn);
        readRoleButton = findViewById(R.id.read_role_btn);
        updateRoleButton = findViewById(R.id.update_role_btn);
        deleteRoleButton = findViewById(R.id.delete_role_btn);

        createRoleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String roleName = roleNameEditText.getText().toString();
                createRole(roleName);
            }
        });

        readRoleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String roleId = roleIdEditText.getText().toString();
                readRole(roleId);
            }
        });

        updateRoleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String roleId = roleIdEditText.getText().toString();
                String roleName = roleNameEditText.getText().toString();
                updateRole(roleId, roleName);
            }
        });

        deleteRoleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String roleId = roleIdEditText.getText().toString();
                deleteRole(roleId);
            }
        });
    }

    private void createRole(String roleName) {
        String url = "http://coms-3090-022.class.las.iastate.edu:8080/roles";

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("roleName", roleName);
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Error creating JSON object", Toast.LENGTH_LONG).show();
            return;
        }

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(getApplicationContext(), "Role created successfully", Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Network error: " + error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
        );
        requestQueue.add(jsonObjectRequest);
    }

    private void readRole(String roleId) {
        String url = "http://coms-3090-022.class.las.iastate.edu:8080/roles/" + roleId;

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Assuming response contains the role name
                        try {
                            String roleName = response.getString("roleName");
                            Toast.makeText(getApplicationContext(), "Role: " + roleName, Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "Error processing response", Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Network error: " + error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
        );
        requestQueue.add(jsonObjectRequest);
    }

    private void updateRole(String roleId, String roleName) {
        String url = "http://coms-3090-022.class.las.iastate.edu:8080/roles/" + roleId;

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("roleName", roleName);
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Error creating JSON object", Toast.LENGTH_LONG).show();
            return;
        }

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, url, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(getApplicationContext(), "Role updated successfully", Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Network error: " + error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
        );
        requestQueue.add(jsonObjectRequest);
    }

    private void deleteRole(String roleId) {
        String url = "http://coms-3090-022.class.las.iastate.edu:8080/roles/" + roleId;

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.DELETE, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(getApplicationContext(), "Role deleted successfully", Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Network error: " + error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
        );
        requestQueue.add(jsonObjectRequest);
    }
}
