package com.example.backingapp4;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.backingapp4.Model.Meals.Ingredient;
import com.example.backingapp4.detailsfragments.VedioFrag;
import com.example.backingapp4.viewmodels.RecipeCardsViewModel;
import com.example.backingapp4.viewmodels.StepsViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.ActionBar;

import android.view.MenuItem;
import android.widget.Toast;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

/**
 * An activity representing a single Meal detail screen. This
 * activity is only used on narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link MealListActivity}.
 */
public class MealDetailActivity extends AppCompatActivity {

    StepsViewModel stepsViewModel;
    private SharedPreferences sharedPreferences;
    int ID;
    String Name;
    ArrayList<Ingredient>ingredients;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        GlobalBus.getBus().register(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_detail);
        Toolbar toolbar = findViewById(R.id.detail_toolbar);
        toolbar.setTitle(MealListActivity.name);
        setSupportActionBar(toolbar);

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        if (savedInstanceState == null) {
            VedioFrag fragment = new VedioFrag(stepsViewModel);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.meal_detail_container, fragment)
                    .commit();
        }


    }
    @Override
    public void onDestroy() {
        GlobalBus.getBus().unregister(this);
        super.onDestroy();
    }


    @Subscribe(sticky = true)
    public void StViewmodel(Events.StepsMessage event){
        stepsViewModel = event.getMessage1();
    }
    @Subscribe(sticky = true)
    public void RevViewmodel(Events.ActivityActivityMessage event){
        RecipeCardsViewModel recipeCardsViewModel=(RecipeCardsViewModel) event.getMessage();
        ID = recipeCardsViewModel.id;
        Name = recipeCardsViewModel.name;
        ingredients = recipeCardsViewModel.ingredients;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.recipe_menu, menu);

        // persistence.  Set checked state based on the fetchPopular boolean
        sharedPreferences = getSharedPreferences(BuildConfig.APPLICATION_ID, MODE_PRIVATE);
        if ((sharedPreferences.getInt("ID", -1) == ID)){
            menu.findItem(R.id.mi_action_widget).setIcon(R.drawable.ic_star_white_48dp);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            navigateUpTo(new Intent(this, MealListActivity.class));
            return true;
        }

        if (id == R.id.mi_action_widget){
            boolean isRecipeInWidget = (sharedPreferences.getInt("PREFERENCES_ID", -1) == ID);

            // If recipe already in widget, remove it
            if (isRecipeInWidget){
                sharedPreferences.edit()
                        .remove("PREFERENCES_ID")
                        .remove("PREFERENCES_WIDGET_TITLE")
                        .remove("PREFERENCES_WIDGET_CONTENT")
                        .apply();

                item.setIcon(R.drawable.ic_star_border_white_48dp);
                Toast.makeText(this, "Recipe is removed from widget", Toast.LENGTH_SHORT).show();
            }
            // if recipe not in widget, then add it
            else{
                sharedPreferences
                        .edit()
                        .putInt("PREFERENCES_ID", ID)
                        .putString("PREFERENCES_WIDGET_TITLE",Name)
                        .putString("PREFERENCES_WIDGET_CONTENT", ingredientsString())
                        .apply();

                item.setIcon(R.drawable.ic_star_white_48dp);
                Toast.makeText(this, "Recipe is Added from widget", Toast.LENGTH_SHORT).show();
            }

            // Put changes on the Widget
            ComponentName provider = new ComponentName(this, RecipeWidget.class);
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
            int[] ids = appWidgetManager.getAppWidgetIds(provider);
            RecipeWidget recipeWidget = new RecipeWidget();
            recipeWidget.onUpdate(this, appWidgetManager, ids);
        }

        return super.onOptionsItemSelected(item);
    }

    private String ingredientsString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(ingredients.get(0).id+' '+ingredients.get(0).getQuantity()+' '+ingredients.get(0).getMeasure()+' '+ingredients.get(0).getIngredient());
        for (int i = 1; i < ingredients.size(); i++) {
            stringBuilder.append("\n");
            stringBuilder.append(ingredients.get(i).id + ' ' +ingredients.get(i).getQuantity() + ' ' +ingredients.get(i).getMeasure() + ' ' +ingredients.get(i).getIngredient());
        }
        return stringBuilder.toString();
    }

}
