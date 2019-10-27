package com.example.backingapp4.viewmodels;

import android.widget.ImageView;

import androidx.databinding.BindingAdapter;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.backingapp4.Model.Meals.Meal;
import com.example.backingapp4.Model.Meals.Step;
import com.example.backingapp4.Network.Retrofit_Service;
import com.example.backingapp4.R;

import java.util.ArrayList;

public class StepsViewModel extends ViewModel {

    public String getId() {
        return id;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public String getDescription() {
        return description;
    }

    public String getVideoURL() {
        return videoURL;
    }

    String id;
    String shortDescription;
    String description;
    String videoURL;

    private static MutableLiveData<ArrayList<StepsViewModel>> arrayListMutableLiveData = new MutableLiveData();

    public StepsViewModel() {
    }

    public StepsViewModel(Step step){
        this.description = step.getDescription();
        this.id = step.getId();
        this.shortDescription = step.getShortDescription();
        this.videoURL = step.getVideoURL();
    }

    public void MakeViewModel(ArrayList<Step> steps) {
        ArrayList<StepsViewModel>stepsViewModels = new ArrayList<>();
        StepsViewModel stepViewModel;
        Step step;
        for(int i =0;i<steps.size();i++){
            step = steps.get(i);
            stepViewModel = new StepsViewModel(step);
            stepsViewModels.add(stepViewModel);
        }
        arrayListMutableLiveData.setValue(stepsViewModels);
    }

    public MutableLiveData<ArrayList<StepsViewModel>> getArrayListMutableLiveData() {
        //here we ger the data from network.
        return arrayListMutableLiveData;
    }

}
