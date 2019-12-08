package com.example.backingapp4;

import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.IdlingResource;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.runner.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
@RunWith(AndroidJUnit4.class)
public class RecycleViewTeast {
    @Rule
    public ActivityTestRule<RecipeCardsActivity> mActivityTestRule
            = new ActivityTestRule<>(RecipeCardsActivity.class);

    @Before
    public void registerIdlingResource() {
        // we have idling resources since we have data that will be downloaded to populate our recipes recycler view
        IdlingRegistry.getInstance().register(RescourcesState.getInstance());
    }

    @Test
    public void clickRecipe_OpenRecipeInfoActivity(){
        // when the data is downloaded, click on the first element of the recycler view
        onView(withId(R.id.CardsID))
                .perform( RecyclerViewActions.actionOnItemAtPosition(0, click()));
    }

    @After
    public void unregisterIdlingResource() {
            IdlingRegistry.getInstance().unregister(RescourcesState.getInstance());
    }
}
