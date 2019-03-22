package edu.usf.cse.labrador.save_a_bull;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class MainScreenTest {

    @Rule
    public ActivityTestRule<MainScreen> mActivityRule =
            new ActivityTestRule<>(MainScreen.class);

    @Test
    public void testGreet() {

    }


}