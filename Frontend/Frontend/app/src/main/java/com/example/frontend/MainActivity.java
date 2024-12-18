package com.example.frontend;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.frontend.AboutFragment;
import com.example.frontend.HomeFragment;
import com.example.frontend.LoginFragment;
import com.example.frontend.models.User;
import com.google.android.material.navigation.NavigationView;

import com.example.frontend.UserManager;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private String userRole; // Store the user role here

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set up the toolbar as the app bar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Initialize the DrawerLayout and NavigationView
        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Set up the ActionBarDrawerToggle to open/close the drawer with a toggle icon
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav, R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Fetch user role and update UI
        User user = UserManager.getInstance().getUser();
        if (user != null) {
            Log.d("MainActivity", "User found. Fetching role for user ID: " + user.getUserID());
            UserManager.getInstance().fetchUserRoleFromBackend(user.getUserID(), this, new UserManager.RoleFetchListener() {
                @Override
                public void onRoleFetched(String role) {
                    userRole = role; // Store the role
                    Log.d("MainActivity", "Role fetched: " + userRole); // Log the role to check if it's correct
                    updateNavigationMenu();
                }

                @Override
                public void onError(String error) {
                    // Handle error (e.g., show a toast message)
                    Log.d("MainActivity", "Error fetching role: " + error); // Log any errors
                    Toast.makeText(MainActivity.this, "Error fetching role: " + error, Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Log.d("MainActivity", "No user found."); // Log if no user is found
        }

        // Load the default fragment (HomeFragment) if there is no saved state
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_home);
        }

        // Update the header in the navigation drawer with user information
        updateNavigationHeader(navigationView);
    }

    private void updateNavigationHeader(NavigationView navigationView) {
        // Access the header view from the NavigationView
        View headerView = navigationView.getHeaderView(0);

        // Locate TextViews in the header for displaying username and email
        TextView userNameTextView = headerView.findViewById(R.id.userNameTextView);
        TextView userEmailTextView = headerView.findViewById(R.id.userEmailTextView);

        // Get user information from the UserManager
        User user = UserManager.getInstance().getUser();
        if (user != null) {
            userNameTextView.setText(user.getUsername());
            userEmailTextView.setText(user.getEmail());
        } else {
            userNameTextView.setText("Guest");
            userEmailTextView.setText("guest@example.com");
        }
    }

    private void updateNavigationMenu() {
        // Access the navigation menu to modify items based on the user's role
        NavigationView navigationView = findViewById(R.id.nav_view);
        Log.d("MainActivity", "Updating navigation menu. User role: " + userRole); // Log role to check what it is

        if (userRole != null) {
            // The Admin/Manager button is always visible, no matter the role
            Log.d("MainActivity", "User role: " + userRole + ". Always showing Admin/Manager button.");

            // Optionally, update the title or some other aspect of the menu item
            MenuItem adminManagerItem = navigationView.getMenu().findItem(R.id.nav_admin_manager);

            if ("user".equalsIgnoreCase(userRole)) {
                // If the role is 'user', modify the button title to "Unavailable"
                adminManagerItem.setTitle("Unavailable");
                Log.d("MainActivity", "User role is user. Setting menu item title to 'Unavailable'.");
            } else {
                // If the role is Admin or Manager, set the normal title
                adminManagerItem.setTitle("Admin/Manager");
                Log.d("MainActivity", "User role is Admin or Manager. Setting menu item title to 'Admin/Manager'.");
            }
        } else {
            Log.d("MainActivity", "User role is null.");
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        // Switch between fragments based on the selected item
        if (id == R.id.nav_home) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
        } else if (id == R.id.nav_roles) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new RolesFragment()).commit();
        } else if (id == R.id.nav_about) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AboutFragment()).commit();
        } else if (id == R.id.nav_logout) {
            // Handle the logout process
            Toast.makeText(this, "Logging out...", Toast.LENGTH_SHORT).show();
            logoutUser();
        } else if (id == R.id.nav_admin_manager) {
            // Handle Admin/Manager action based on user role
            Log.d("MainActivity", "Admin/Manager menu item clicked.");
            if ("user".equalsIgnoreCase(userRole)) {
                // Show unavailable message if role is user
                Log.d("MainActivity", "User role is 'user'. Showing 'Unavailable' message.");
                Toast.makeText(MainActivity.this, "Not available for users.", Toast.LENGTH_SHORT).show();
            } else if ("admin".equalsIgnoreCase(userRole)) {
                // Navigate to Admin screen
                Log.d("MainActivity", "Navigating to Admin screen.");
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AdminFragment()).commit();
            } else if ("manager".equalsIgnoreCase(userRole)) {
                // Navigate to Manager screen
                Log.d("MainActivity", "Navigating to Manager screen.");
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ManagerFragment()).commit();
            }
        }

        // Close the navigation drawer after item selection
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void logoutUser() {
        // Clear the current user data from UserManager
        UserManager.getInstance().setUser(null);

        // Notify the user of a successful logout
        Toast.makeText(this, "Successfully logged out!", Toast.LENGTH_SHORT).show();

        // Replace the current fragment with the LoginFragment and add it to the back stack
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new LoginFragment())
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}