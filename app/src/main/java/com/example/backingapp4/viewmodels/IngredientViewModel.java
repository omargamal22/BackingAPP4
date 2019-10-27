package com.example.backingapp4.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.backingapp4.Model.Meals.Ingredient;

import java.util.ArrayList;

public class IngredientViewModel extends ViewModel {

    public String quantity;
    public String measure;
    public String ingredient;

    private static MutableLiveData<ArrayList<IngredientViewModel>> arrayListMutableLiveData = new MutableLiveData();

    public MutableLiveData<ArrayList<IngredientViewModel>> getArrayListMutableLiveData() {
        return arrayListMutableLiveData;
    }

    public IngredientViewModel(){}

    public IngredientViewModel(Ingredient ingredient) {
        this.quantity = ingredient.getQuantity();
        this.measure = ingredient.getMeasure();
        this.ingredient = ingredient.getIngredient();
    }

    public void MakeViewModel(ArrayList<Ingredient> ingredients) {
        ArrayList<IngredientViewModel>ingredientViewModels = new ArrayList<>();
        IngredientViewModel ingredientViewModel;
        Ingredient ingredient;
       for(int i =0;i<ingredients.size();i++){
           ingredient = ingredients.get(i);
           ingredientViewModel = new IngredientViewModel(ingredient);
           ingredientViewModels.add(ingredientViewModel);
       }
       arrayListMutableLiveData.setValue(ingredientViewModels);
    }

}
