package com.example.backingapp4.detailsfragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.backingapp4.Events;
import com.example.backingapp4.GlobalBus;
import com.example.backingapp4.R;
import com.example.backingapp4.viewmodels.IngredientViewModel;
import com.example.backingapp4.viewmodels.RecipeCardsViewModel;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

public class SmallDetailfrag extends Fragment {
    Toolbar toolbar;
    private String Name;


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        GlobalBus.getBus().unregister(this);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        GlobalBus.getBus().register(this);
        View view = inflater.inflate(R.layout.small_detail_frag,container,false);
        toolbar = view.findViewById(R.id.toolbar2);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        if(Name != null){
            ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(Name);
        }
        return view;
    }
    @Subscribe(sticky = true)
    public void ReciveViewModel(Events.ActivityActivityMessage event){
        RecipeCardsViewModel recipeCardsViewModel=(RecipeCardsViewModel) event.getMessage();
        if(SmallDetailfrag.this.isVisible()) {
            toolbar.setTitle(recipeCardsViewModel.name);
        }
        else{
            Name = recipeCardsViewModel.name;
        }
    }
}
