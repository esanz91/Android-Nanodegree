package com.esanz.nano.ezbaking;

import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.esanz.nano.ezbaking.ui.MainActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class MainActivityBasicTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void clickRecipeCard_runRecipeDetailActivity() {
        // main activity action
        ViewInteraction recyclerView = onView(withId(R.id.recipes));
        recyclerView.perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        // recipe detail assertion
        onView(withId(R.id.details)).check(matches(isDisplayed()));
    }

}
