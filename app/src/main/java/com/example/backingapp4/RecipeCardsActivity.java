package com.example.backingapp4;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.widget.Toast;

import com.example.backingapp4.viewmodels.RecipeCardsViewModel;
import com.example.backingapp4.databinding.ActivityRecipeCardsBinding;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;


public class RecipeCardsActivity extends AppCompatActivity {

    private ActivityRecipeCardsBinding activityRecipeCardsBinding;
    private CardsAdapter cardsAdapter;
    private RecipeCardsViewModel recipeCardsViewModel;
    private AVLoadingIndicatorView cat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_cards);

        activityRecipeCardsBinding = DataBindingUtil.setContentView(this,R.layout.activity_recipe_cards);
        activityRecipeCardsBinding.CardsID.setLayoutManager(new LinearLayoutManager(this));
        activityRecipeCardsBinding.CardsID.setHasFixedSize(true);
        cardsAdapter = new CardsAdapter(this, new RecyclerViewClickListener() {
            @Override
            public void onItemClick(int position) {
                //Make Intent to the detail fragment
                Toast.makeText(getApplicationContext() ,"pos:"+position,Toast.LENGTH_SHORT).show();
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
