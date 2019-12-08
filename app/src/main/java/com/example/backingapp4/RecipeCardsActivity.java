package com.example.backingapp4;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.backingapp4.Adapters.CardsAdapter;
import com.example.backingapp4.databinding.ActivityRecipeCardsBinding;
import com.example.backingapp4.viewmodels.RecipeCardsViewModel;
import com.wang.avi.AVLoadingIndicatorView;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;


public class RecipeCardsActivity extends AppCompatActivity {

    private ActivityRecipeCardsBinding activityRecipeCardsBinding;
    private CardsAdapter cardsAdapter;
    private RecipeCardsViewModel recipeCardsViewModel;
    private AVLoadingIndicatorView cat;
    private AppCompatActivity THIS;
    private Parcelable RecipeState;
    private LinearLayoutManager linearLayoutManager;
    private GridLayoutManager gridLayoutManager;
    private final String LIST_STATE_KEY = "ListState";

    boolean bigscreen ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_cards);



        THIS = this;
        activityRecipeCardsBinding = DataBindingUtil.setContentView(this,R.layout.activity_recipe_cards);
        if(findViewById(R.id.twobane)!=null){
            bigscreen = true;
            gridLayoutManager = new GridLayoutManager(this,3);
            if(savedInstanceState!=null){
                gridLayoutManager.onRestoreInstanceState(savedInstanceState.getParcelable(LIST_STATE_KEY));
            }
            activityRecipeCardsBinding.CardsID.setLayoutManager(gridLayoutManager);
        }
        else {
            linearLayoutManager = new LinearLayoutManager(this);
            if(savedInstanceState!=null){
                linearLayoutManager.onRestoreInstanceState(savedInstanceState.getParcelable(LIST_STATE_KEY));
            }
            activityRecipeCardsBinding.CardsID.setLayoutManager(linearLayoutManager);
        }
        activityRecipeCardsBinding.CardsID.setHasFixedSize(true);
        cardsAdapter = new CardsAdapter(this, new ClickListener() {
            @Override
            public void onItemClick(ViewModel viewModel) {
                //Make Intent to the detail fragment
                //Toast.makeText(getApplicationContext() ,"pos:"+position,Toast.LENGTH_SHORT).show();
                GlobalBus.getBus().postSticky(new Events.ActivityActivityMessage(viewModel));
                Intent i = new Intent(THIS,MealListActivity.class);
                startActivity(i);
            }

            @Override
            public void onCLICK() {

            }
        });
        activityRecipeCardsBinding.CardsID.setAdapter(cardsAdapter);

        recipeCardsViewModel = new ViewModelProvider(this , new Myfactory()).get(RecipeCardsViewModel.class);
        recipeCardsViewModel.getArrayListMutableLiveData().observe(this, new Observer<ArrayList<RecipeCardsViewModel>>() {
            @Override
            public void onChanged(ArrayList<RecipeCardsViewModel> recipeCardsViewModels) {
                cardsAdapter.setViewModels(recipeCardsViewModels);
                cat.hide();
                RescourcesState.getInstance().setState(true);

            }
        });
        cat = findViewById(R.id.AVI);
        cat.show();
        setSupportActionBar(activityRecipeCardsBinding.toolbar1);

    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if(bigscreen){
            outState.putParcelable(LIST_STATE_KEY,gridLayoutManager.onSaveInstanceState());
            return;
        }
        outState.putParcelable(LIST_STATE_KEY,linearLayoutManager.onSaveInstanceState());
    }

}
class Myfactory implements ViewModelProvider.Factory{
    public Myfactory() {
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new RecipeCardsViewModel();
    }


}
