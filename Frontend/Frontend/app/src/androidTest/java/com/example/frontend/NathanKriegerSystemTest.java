package com.example.frontend;

import static androidx.test.espresso.Espresso.closeSoftKeyboard;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.assertEquals;

import android.content.Context;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;



import org.junit.Test;
import org.junit.Rule;
import org.junit.runner.RunWith;


@RunWith(AndroidJUnit4.class)
public class NathanKriegerSystemTest {


    @Rule
    public ActivityScenarioRule<MainActivity> activityScenarioRule
            = new ActivityScenarioRule<>(MainActivity.class);


    /*public void tearDown() throws InterruptedException {
        Thread.sleep(2000);
        onView(withId(R.id.nav_logout)).perform(click());
    }*/

    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.example.frontend", appContext.getPackageName());
    }

    @Test
    public void testNavigateToLogin() throws InterruptedException {

        onView(withId(R.id.main_login_btn))
                .check(matches(ViewMatchers.isDisplayed()));

        onView(withId(R.id.main_login_btn))
                .perform(click());

        onView(withText("Forgot Password?"))
                .check(matches(ViewMatchers.isDisplayed()));

        //tearDown();

    }

/*
    @Test
    public void testLogin() throws InterruptedException {

        // Ensure the login button is displayed
        onView(withId(R.id.main_login_btn))
                .check(matches(ViewMatchers.isDisplayed()));

        // Navigate to the login screen
        onView(withId(R.id.main_login_btn))
                .perform(click());

        // Ensure the username and password fields are displayed
        onView(withId(R.id.login_username_edt))
                .check(matches(ViewMatchers.isDisplayed()));

        onView(withId(R.id.login_password_edt))
                .check(matches(ViewMatchers.isDisplayed()));

        // Enter the username
        onView(withId(R.id.login_username_edt))
                .perform(typeText("1"));

        // Enter the password
        onView(withId(R.id.login_password_edt))
                .perform(typeText("1"));

        // Close the keyboard
        closeSoftKeyboard();

        // Click the login button
        onView(withId(R.id.login_login_btn))
                .perform(click());

        // Add an assertion to verify successful login (modify based on your logic)
        onView(withText("Moana 2"))
                .check(matches(ViewMatchers.isDisplayed()));

        //tearDown();

    }

    @Test
    public void testAdvancedSearchOpens() throws InterruptedException {

        // Ensure the login button is displayed
        onView(withId(R.id.main_login_btn))
                .check(matches(ViewMatchers.isDisplayed()));

        // Navigate to the login screen
        onView(withId(R.id.main_login_btn))
                .perform(click());

        // Ensure the username and password fields are displayed
        onView(withId(R.id.login_username_edt))
                .check(matches(ViewMatchers.isDisplayed()));

        onView(withId(R.id.login_password_edt))
                .check(matches(ViewMatchers.isDisplayed()));

        // Enter the username
        onView(withId(R.id.login_username_edt))
                .perform(typeText("1"));

        // Enter the password
        onView(withId(R.id.login_password_edt))
                .perform(typeText("1"));

        // Close the keyboard
        closeSoftKeyboard();

        // Click the login button
        onView(withId(R.id.login_login_btn))
                .perform(click());


        onView(withId(R.id.advanced_search_btn))
                .perform(click());

        onView(withText("Advanced Search"))
                .check(matches(ViewMatchers.isDisplayed()));

        //tearDown();
    }

        @Test
        public void testIncorrectLogin() throws InterruptedException {

            // Ensure the login button is displayed
        onView(withId(R.id.main_login_btn))
                .check(matches(ViewMatchers.isDisplayed()));

        // Navigate to the login screen
        onView(withId(R.id.main_login_btn))
                .perform(click());

        // Ensure the username and password fields are displayed
        onView(withId(R.id.login_username_edt))
                .check(matches(ViewMatchers.isDisplayed()));

        onView(withId(R.id.login_password_edt))
                .check(matches(ViewMatchers.isDisplayed()));

        // Enter the username
        onView(withId(R.id.login_username_edt))
                .perform(typeText("2"));

        // Enter the password
        onView(withId(R.id.login_password_edt))
                .perform(typeText("2"));

        // Close the keyboard
        closeSoftKeyboard();

        // Click the login button
        onView(withId(R.id.login_login_btn))
                .perform(click());

        //closeSoftKeyboard();


        Thread.sleep(2000);

        onView(withText("Forgot Password?"))
                .check(matches(ViewMatchers.isDisplayed()));

        //tearDown();
    }*/

}
