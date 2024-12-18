package com.example.frontend;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.frontend.MovieDashboardFragment;
import com.example.frontend.UserManager;
import com.example.frontend.models.User;

/**
 * A fragment representing the home screen of the application.
 * <p>
 * This fragment handles user authentication states by displaying either login/signup buttons
 * for unauthenticated users or transitioning to the movie browsing dashboard for authenticated users.
 */
public class HomeFragment extends Fragment {

    /**
     * Default constructor required for instantiating the fragment.
     */
    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Inflates the layout for the fragment and initializes the UI based on the user's authentication state.
     *
     * @param inflater  The LayoutInflater object that can be used to inflate any views in the fragment.
     * @param container The parent view that this fragment's UI should be attached to.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state.
     * @return The View for the fragment's UI.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Get references to the buttons
        Button loginButton = view.findViewById(R.id.main_login_btn);
        Button signupButton = view.findViewById(R.id.main_signup_btn);

        // Check if the user is logged in
        User currentUser = UserManager.getInstance().getUser();

        if (currentUser == null) {
            // User is not logged in, show the login/signup buttons
            loginButton.setVisibility(View.VISIBLE);
            signupButton.setVisibility(View.VISIBLE);

            // Set up the Login button listener
            loginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Replace current fragment with LoginFragment
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragment_container, new LoginFragment());  // Assuming container ID is fragment_container
                    transaction.addToBackStack(null);  // Add to back stack to allow back navigation
                    transaction.commit();
                }
            });

            // Set up the Sign Up button listener
            signupButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Replace current fragment with SignupFragment
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragment_container, new SignupFragment());  // Assuming container ID is fragment_container
                    transaction.addToBackStack(null);  // Add to back stack to allow back navigation
                    transaction.commit();
                }
            });
        } else {
            // User is logged in, show the movie browsing dashboard
            loginButton.setVisibility(View.GONE);
            signupButton.setVisibility(View.GONE);

            // Replace the current view with the movie browsing dashboard fragment
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, new MovieDashboardFragment());  // Assuming there's a MovieDashboardFragment
            transaction.commit();
        }

        return view;
    }
}
