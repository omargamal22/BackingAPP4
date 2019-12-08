package com.example.backingapp4.detailsfragments;

import android.os.Bundle;
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
import com.example.backingapp4.Events;
import com.example.backingapp4.GlobalBus;
import com.example.backingapp4.R;
import com.example.backingapp4.databinding.IngredientsFragBinding;
import com.example.backingapp4.viewmodels.IngredientViewModel;
import com.example.backingapp4.viewmodels.RecipeCardsViewModel;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

public class IngredientsFrag extends Fragment {
    private IngredientsAdapter ingredientsAdapter;
    private IngredientsFragBinding ingredientsFragBinding;
    private LinearLayoutManager linearLayoutManager;
    private final String LIST_STATE_KEY = "IngredientState";
    private RecipeCardsViewModel recipeCardsViewModel;
    IngredientViewModel ingredientViewModel;


    @Override
    public void onDestroy() {
        GlobalBus.getBus().unregister(this);
        super.onDestroy();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ingredients_frag,container,false);
        ingredientsAdapter = new IngredientsAdapter(getContext());
        ingredientViewModel = new ViewModelProvider(this,new Myfactory()).get(IngredientViewModel.class);
        GlobalBus.getBus().register(this);
        return view;
    }

    private void updateAdapter(ArrayList<IngredientViewModel> ingredientViewModels) {
        ingredientsFragBinding = DataBindingUtil.setContentView(getActivity(),R.layout.ingredients_frag);
        linearLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        ingredientsFragBinding.ingredientsCards.setLayoutManager(linearLayoutManager);
        ingredientsAdapter.setViewModels(ingredientViewModels);
        ingredientsFragBinding.ingredientsCards.setAdapter(ingredientsAdapter);
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
        ingredientViewModel.MakeViewModel(recipeCardsViewModel.ingredients);
        ingredientViewModel.getArrayListMutableLiveData().observe(this, new Observer<ArrayList<IngredientViewModel>>() {
            @Override
            public void onChanged(ArrayList<IngredientViewModel> ingredientViewModels) {
                updateAdapter(ingredientViewModels);
                //ingredientsAdapter.setViewModels(ingredientViewModels);

            }
        });
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
