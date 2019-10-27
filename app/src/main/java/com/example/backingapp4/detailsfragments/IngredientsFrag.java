package com.example.backingapp4.detailsfragments;

import android.app.Activity;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.backingapp4.Adapters.IngredientsAdapter;
import com.example.backingapp4.Adapters.StepsAdapte;
import com.example.backingapp4.Events;
import com.example.backingapp4.GlobalBus;
import com.example.backingapp4.R;
import com.example.backingapp4.databinding.IngredientsFragBinding;
import com.example.backingapp4.viewmodels.IngredientViewModel;
import com.example.backingapp4.viewmodels.RecipeCardsViewModel;
import com.example.backingapp4.viewmodels.StepsViewModel;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

public class IngredientsFrag extends Fragment {
    private IngredientsAdapter ingredientsAdapter;
    private IngredientsFragBinding ingredientsFragBinding;
    private LinearLayoutManager linearLayoutManager;
    private final String LIST_STATE_KEY = "IngredientState";
    private RecipeCardsViewModel recipeCardsViewModel;
    private boolean made = false;


    @Override
    public void onDestroy() {
        GlobalBus.getBus().unregister(this);
        super.onDestroy();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        GlobalBus.getBus().register(this);
        View view = inflater.inflate(R.layout.ingredients_frag,container,false);
        ingredientsFragBinding = DataBindingUtil.setContentView(getActivity(),R.layout.ingredients_frag);
        linearLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        ingredientsFragBinding.ingredientsCards.setLayoutManager(linearLayoutManager);
        ingredientsAdapter = new IngredientsAdapter();
        ingredientsFragBinding.ingredientsCards.setAdapter(ingredientsAdapter);

        if(!made){
            IngredientViewModel ingredientViewModel = new ViewModelProvider(this,new Myfactory()).get(IngredientViewModel.class);
            ingredientViewModel.MakeViewModel(recipeCardsViewModel.ingredients);
            ingredientViewModel.getArrayListMutableLiveData().observe(this, new Observer<ArrayList<IngredientViewModel>>() {
                @Override
                public void onChanged(ArrayList<IngredientViewModel> ingredientViewModels) {
                    ingredientsAdapter.setViewModels(ingredientViewModels);
                }
            });
        }

        return view;
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if(savedInstanceState!=null){
            linearLayoutManager.onRestoreInstanceState( savedInstanceState.getParcelable(LIST_STATE_KEY));

        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(LIST_STATE_KEY,linearLayoutManager.onSaveInstanceState());

    }



    @Subscribe(sticky = true  )
    public void ReciveViewModel(Events.ActivityActivityMessage event){
        recipeCardsViewModel=(RecipeCardsViewModel) event.getMessage();
        if(IngredientsFrag.this.isVisible()){
            IngredientViewModel ingredientViewModel = new ViewModelProvider(this,new Myfactory()).get(IngredientViewModel.class);
            ingredientViewModel.MakeViewModel(recipeCardsViewModel.ingredients);
            ingredientViewModel.getArrayListMutableLiveData().observe(this, new Observer<ArrayList<IngredientViewModel>>() {
                @Override
                public void onChanged(ArrayList<IngredientViewModel> ingredientViewModels) {
                    ingredientsAdapter.setViewModels(ingredientViewModels);
                }
            });
            made = true;
        }
    }

    class Myfactory implements ViewModelProvider.Factory {
        public Myfactory() {
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return (T) new IngredientViewModel();
        }
    }

}
