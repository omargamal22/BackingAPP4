package com.example.backingapp4;

import android.util.DisplayMetrics;

import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.IdlingResource;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.runner.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import com.example.backingapp4.detailsfragments.VedioFrag;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class TwoPaneTeast {


    @Rule
    public ActivityTestRule<MealListActivity> mActivityTestRule
            = new ActivityTestRule<>(MealListActivity.class);
    @Before
    public void setup() {
        IdlingRegistry.getInstance().register(RescourcesState.getInstance1());
    }
    @Test
    public void coolTextDisplayed() {


        DisplayMetrics displayMetrics = mActivityTestRule.getActivity().getApplicationContext().getResources().getDisplayMetrics();
        float dpHeight = displayMetrics.heightPixels / displayMetrics.density;
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        VedioFrag vedioFrag = new VedioFrag();
        mActivityTestRule.getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.meal_detail_container, vedioFrag)
                .commit();
        if(dpWidth > 200.0) {
            onView(withId(R.id.video_view)).check(matches(isDisplayed()));
        }
    }

    @After
    public void unregisterIdlingResource() {
        IdlingRegistry.getInstance().unregister(RescourcesState.getInstance1());
    }
}
