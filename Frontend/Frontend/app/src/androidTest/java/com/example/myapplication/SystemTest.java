package com.example.myapplication;


import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.action.ViewActions;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import android.util.Log;



import com.example.frontend.MainActivity;
import com.example.frontend.R;

@RunWith(AndroidJUnit4.class)
public class SystemTest {

    @Rule
    public ActivityScenarioRule<MainActivity> activityScenarioRule
            = new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void testNavigateToLogin() {
        Log.d("Test", "Clicking on login button");
        onView(withId(R.id.main_login_btn))
                .perform(ViewActions.click());

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

//        Log.d("Test", "Verifying login screen");
//        onView(withText("Login")).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }

    @Test
    public void testNavigateToSignup() {
        // click on the Signup button
        onView(withId(R.id.main_signup_btn))
                .perform(ViewActions.click());



//        // Optionally: Add a check to verify the correct fragment or UI text appears
//        onView(withText("Signup")).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }



    @Test
    public void testLoginWithCredentials() {
        // Navigate to login screen
        onView(withId(R.id.main_login_btn))
                .perform(ViewActions.click());

        // Optionally wait for the login screen to load
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // Type "jakenickel" into the username field
        onView(withId(R.id.login_username_edt))  // Replace with the actual ID of the username field
                .perform(ViewActions.typeText("jakenickel"));

        // Type "123456" into the password field
        onView(withId(R.id.login_password_edt))  // Replace with the actual ID of the password field
                .perform(ViewActions.typeText("123456"));

        // Optionally: Close the keyboard
        onView(withId(R.id.login_password_edt))
                .perform(ViewActions.closeSoftKeyboard());

        // Optionally: Add a check to verify the correct credentials were entered or proceed to the next screen
        // Example: Verify that the login button is enabled
        onView(withId(R.id.login_login_btn))
                .check(ViewAssertions.matches(ViewMatchers.isEnabled()));
    }

}