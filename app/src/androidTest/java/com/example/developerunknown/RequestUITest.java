package com.example.developerunknown;

import android.app.Activity;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
public class RequestUITest {
    private Solo solo;

    @Rule
    public ActivityTestRule<MainActivity> rule =

            new ActivityTestRule<>(MainActivity.class, true, true);

    @Before
    public void setUp() throws Exception {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(),rule.getActivity());
    }

    @Test
    public void start() throws Exception {

        Activity activity = rule.getActivity();
    }

    @Test
    public void LoginTest() throws InterruptedException {
        solo.enterText((EditText)solo.getView(R.id.editUsername),"XZPshaw");
        solo.enterText((EditText)solo.getView(R.id.editPassward),"123456");
        solo.clickOnButton("LOGIN");
        solo.wait();
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
    }

    @Test
    public void SearchTest() throws InterruptedException {
        solo.enterText((EditText)solo.getView(R.id.editUsername),"XZPshaw");
        solo.enterText((EditText)solo.getView(R.id.editPassward),"123456");
        solo.clickOnButton("LOGIN");
        solo.waitForActivity(MainActivity.class);
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);

        solo.clickOnText("Search");

    }


    @After

    public void tearDown() throws Exception {

        solo.finishOpenedActivities();
    }
}