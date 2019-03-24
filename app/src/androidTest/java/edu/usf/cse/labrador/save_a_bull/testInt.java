package edu.usf.cse.labrador.save_a_bull;

import android.app.Activity;
import android.app.Instrumentation;
import android.support.test.espresso.Espresso;
import android.support.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.IntentMatchers.isInternal;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.not;

public class testInt {

    @Rule
    public ActivityTestRule<SignUpScreen> activityTestRule =
            new ActivityTestRule<>(SignUpScreen.class);


    //Test to try to make a new account for the application
    @Test
    public void makeAccount_Test() {

        // Filling in form to create account
        onView(withId(R.id.firstNameEntry)).perform(typeText("John")).
                check(matches(withText("John")));
        onView(withId(R.id.lastNameEntry)).perform(typeText("Smith")).
                check(matches(withText("Smith")));

        // To allow Espresso to view the next two fields
        Espresso.closeSoftKeyboard();

        onView(withId(R.id.emailEntry)).perform(typeText("test@mail.com")).
                check(matches(withText("test@mail.com")));
        onView(withId(R.id.passwordEntry)).perform(typeText("testPassword1")).
                check(matches(withText("testPassword1")));

        // To allow Espresso to view the "CREATE ACCOUNT" button
        Espresso.closeSoftKeyboard();

        // Creating the account with click function
        onView(withId(R.id.createAcctBtn)).perform(click());

    }
}