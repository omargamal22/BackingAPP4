package com.example.backingapp4;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;

import com.example.backingapp4.Adapters.CardsAdapter;
import com.example.backingapp4.viewmodels.RecipeCardsViewModel;
import com.example.backingapp4.databinding.ActivityRecipeCardsBinding;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;


public class RecipeCardsActivity extends AppCompatActivity {

    private ActivityRecipeCardsBinding activityRecipeCardsBinding;
    private CardsAdapter cardsAdapter;
    private RecipeCardsViewModel recipeCardsViewModel;
    private AVLoadingIndicatorView cat;
    private Activity THIS;
    private Parcelable RecipeState;
    private LinearLayoutManager linearLayoutManager;
    private final String LIST_STATE_KEY = "ListState";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_cards);

        THIS = this;

        activityRecipeCardsBinding = DataBindingUtil.setContentView(this,R.layout.activity_recipe_cards);
        linearLayoutManager = new LinearLayoutManager(this);
        activityRecipeCardsBinding.CardsID.setLayoutManager(linearLayoutManager);
        activityRecipeCardsBinding.CardsID.setHasFixedSize(true);
        cardsAdapter = new CardsAdapter(this, new ClickListener() {
            @Override
            public void onItemClick(ViewModel viewModel) {
                //Make Intent to the detail fragment
                //Toast.makeText(getApplicationContext() ,"pos:"+position,Toast.LENGTH_SHORT).show();
                GlobalBus.getBus().postSticky(new Events.ActivityActivityMessage(viewModel));
                Intent i = new Intent(THIS,DetailActivity.class);
                startActivity(i);
            }

            @Override
            public void onCLICK() {

            }
        });
        activityRecipeCardsBinding.CardsID.setAdapter(cardsAdapter);

        recipeCardsViewModel = new ViewModelProvider(this , new Myfactory()).get(RecipeCardsViewModel.class);
        recipeCardsViewModel.getData();
        recipeCardsViewModel.getArrayListMutableLiveData().observe(this, new Observer<ArrayList<RecipeCardsViewModel>>() {
            @Override
            public void onChanged(ArrayList<RecipeCardsViewModel> recipeCardsViewModels) {
                cardsAdapter.setViewModels(recipeCardsViewModels);
                cat.hide();

            }
        });
        cat = (AVLoadingIndicatorView)findViewById(R.id.AVI);
        cat.show();
        setSupportActionBar(activityRecipeCardsBinding.toolbar1);

    }
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if(savedInstanceState!=null){
            linearLayoutManager = savedInstanceState.getParcelable(LIST_STATE_KEY);
        }
    }
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
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
