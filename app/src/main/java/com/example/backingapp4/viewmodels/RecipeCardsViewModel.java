package com.example.backingapp4.viewmodels;

import android.widget.ImageView;

import androidx.databinding.BindingAdapter;
import androidx.databinding.BaseObservable;
import androidx.databinding.library.baseAdapters.BR;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.backingapp4.Model.Meals.Ingredient;
import com.example.backingapp4.Model.Meals.Meal;
import com.example.backingapp4.Model.Meals.Step;

import com.example.backingapp4.Network.Retrofit_Service;
import com.example.backingapp4.R;

import java.util.ArrayList;

public class RecipeCardsViewModel extends ViewModel {

    public int id;
    public String name;
    public ArrayList<Ingredient> ingredients;
    public ArrayList<Step> steps;
    public String servings;
    public String imageURL;
    private static MutableLiveData<ArrayList<RecipeCardsViewModel>> arrayListMutableLiveData = new MutableLiveData();


    public RecipeCardsViewModel(){
        getData();
    }

    public RecipeCardsViewModel(Meal meal){
        this.id = meal.getId();
        this.name = meal.getName();
        this.imageURL = meal.getImage();
        this.ingredients = meal.getIngredients();
        this.servings = meal.getServings().concat(" Servings");
        this.steps = meal.getSteps();
    }

    public void getData(){
        arrayListMutableLiveData=Retrofit_Service.Get_Retrofit_Service();

    }

    public MutableLiveData<ArrayList<RecipeCardsViewModel>> getArrayListMutableLiveData() {
        //here we ger the data from network.
        return arrayListMutableLiveData;
    }

    public String getImage(){
        return imageURL;
    }

    @BindingAdapter({"bind:image"})

    public static void loadImage(ImageView view , String image){
        view.setImageResource(R.drawable.brownies);
    }
}
