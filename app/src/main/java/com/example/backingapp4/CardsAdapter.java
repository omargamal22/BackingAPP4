package com.example.backingapp4;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.backingapp4.viewmodels.RecipeCardsViewModel;
import com.example.backingapp4.databinding.RecipeCardBinding;

import java.util.ArrayList;

public class CardsAdapter extends RecyclerView.Adapter<CardsAdapter.MyViewHolder> {
    private Context context;
    private  ArrayList<RecipeCardsViewModel>recipeCardsViewModel;

    public CardsAdapter(Context context) {
        this.context = context;
        this.recipeCardsViewModel = new ArrayList<>();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        RecipeCardBinding recipeCardBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),R.layout.recipe_card,parent,false);
        MyViewHolder myViewHolder = new MyViewHolder(recipeCardBinding);
        return myViewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.recipeCardBinding.setViewModel(recipeCardsViewModel.get(position));
        holder.recipeCardBinding.executePendingBindings();
    }

    public void setViewModels(  ArrayList<RecipeCardsViewModel> newValus){
        recipeCardsViewModel.clear();
        recipeCardsViewModel.addAll(newValus);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return recipeCardsViewModel.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        RecipeCardBinding recipeCardBinding;
        public MyViewHolder(@NonNull RecipeCardBinding itemview) {
            super(itemview.getRoot());
            recipeCardBinding = itemview;
        }
    }
}
