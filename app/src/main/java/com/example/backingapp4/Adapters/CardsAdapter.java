package com.example.backingapp4.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.backingapp4.R;
import com.example.backingapp4.ClickListener;
import com.example.backingapp4.viewmodels.RecipeCardsViewModel;
import com.example.backingapp4.databinding.RecipeCardBinding;

import java.util.ArrayList;

public class CardsAdapter extends RecyclerView.Adapter<CardsAdapter.MyViewHolder> {
    private Context context;
    private  ArrayList<RecipeCardsViewModel>recipeCardsViewModel;
    private ClickListener clickListener;

    public CardsAdapter(Context context , ClickListener clickListener) {
        this.context = context;
        this.recipeCardsViewModel = new ArrayList<>();
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        final RecipeCardBinding recipeCardBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.recipe_card,parent,false);
        return  new MyViewHolder(recipeCardBinding);
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        holder.recipeCardBinding.setViewModel(recipeCardsViewModel.get(position));
        holder.recipeCardBinding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.onItemClick(recipeCardsViewModel.get(position));
            }
        });
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
