package com.example.backingapp4;
import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.example.backingapp4.Adapters.StepsAdapter;
import com.example.backingapp4.Model.Meals.Ingredient;
import com.example.backingapp4.Model.Meals.Step;
import com.example.backingapp4.detailsfragments.VedioFrag;
import com.example.backingapp4.viewmodels.IngredientViewModel;
import com.example.backingapp4.viewmodels.RecipeCardsViewModel;
import com.example.backingapp4.viewmodels.StepsViewModel;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.Window;
import android.view.WindowManager;
import androidx.core.app.NavUtils;
import androidx.appcompat.app.ActionBar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import android.view.MenuItem;
import android.widget.Toast;

import org.greenrobot.eventbus.Subscribe;
import java.util.ArrayList;
import moe.feng.common.stepperview.VerticalStepperView;

/**
 * An activity representing a list of Step. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link MealDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class MealListActivity extends AppCompatActivity {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;
    StepsAdapter stepsAdapte;
    VerticalStepperView verticalStepperView;
    StepsViewModel steps;
    RecipeCardsViewModel recipeCardsViewModel;
    public static String name;

    private SharedPreferences sharedPreferences;
    int ID;
    ArrayList<Ingredient>ingredients;
    @Override
    public void onDestroy() {
        GlobalBus.getBus().unregister(this);
        super.onDestroy();
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if(savedInstanceState!=null){

        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("stepID",verticalStepperView.getCurrentStep());
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {

        GlobalBus.getBus().register(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_list);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(name);
        setSupportActionBar(toolbar);


        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        if (findViewById(R.id.meal_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }
        final MealListActivity mealListActivity = this;
        verticalStepperView = findViewById(R.id.meal_list);
        steps = new ViewModelProvider(this,new MealListActivity.Myfactory()).get(StepsViewModel.class);
        steps.MakeViewModel(recipeCardsViewModel.steps);
        steps.getArrayListMutableLiveData().observe(this, new Observer<ArrayList<StepsViewModel>>() {
            @Override
            public void onChanged(ArrayList<StepsViewModel> stepsViewModels) {
                stepsAdapte = new StepsAdapter(stepsViewModels, verticalStepperView,mealListActivity,mTwoPane);
                verticalStepperView.setStepperAdapter(stepsAdapte);
                RescourcesState.getInstance1().setState(true);
                if(savedInstanceState!=null){
                    verticalStepperView.setCurrentStep(savedInstanceState.getInt("stepID"));
                }
            }
        });

       // View recyclerView = findViewById(R.id.meal_list);
        //assert recyclerView != null;
        //setupRecyclerView((RecyclerView) recyclerView);
    }


    @Subscribe(sticky = true)
    public void RevViewmodel(Events.ActivityActivityMessage event){
        recipeCardsViewModel=(RecipeCardsViewModel) event.getMessage();
        name = recipeCardsViewModel.name;
        ID = recipeCardsViewModel.id;
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
            NavUtils.navigateUpFromSameTask(this);
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
                        .putString("PREFERENCES_WIDGET_TITLE",name)
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

    class Myfactory implements ViewModelProvider.Factory {
        public Myfactory() {
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return (T) new StepsViewModel();
        }
    }

}
