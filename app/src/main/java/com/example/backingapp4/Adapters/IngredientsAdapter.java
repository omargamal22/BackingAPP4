package com.example.backingapp4.Adapters;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.backingapp4.R;
import com.example.backingapp4.databinding.IngredientsItemBinding;
import com.example.backingapp4.viewmodels.IngredientViewModel;

import java.util.ArrayList;

public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.MyIngredientsViewHolder> {

    private ArrayList<IngredientViewModel> ingredientViewModels;
    Context context;
    public IngredientsAdapter (Context context){
        this.context = context;
        ingredientViewModels = new ArrayList<>();
    }

    @NonNull
    @Override
    public MyIngredientsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final IngredientsItemBinding ingredientsItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.ingredients_item,parent,false);
        ViewGroup.LayoutParams layoutParams = ingredientsItemBinding.IngCard.getLayoutParams();
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        layoutParams.width = (int) (displayMetrics.heightPixels * 0.5);
        ingredientsItemBinding.IngCard.setLayoutParams(layoutParams);
        return new MyIngredientsViewHolder(ingredientsItemBinding);
    }

    public void setViewModels(ArrayList<IngredientViewModel> newVals){
        ingredientViewModels.clear();
        ingredientViewModels.addAll(newVals);
        notifyDataSetChanged();


    }

    @Override
    public void onBindViewHolder(@NonNull MyIngredientsViewHolder holder, final int position) {
        holder.ingredientsItemBinding.setViewModel(ingredientViewModels.get(position));
        holder.ingredientsItemBinding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return ingredientViewModels.size();
    }

    class MyIngredientsViewHolder extends RecyclerView.ViewHolder{

        IngredientsItemBinding ingredientsItemBinding;
        public MyIngredientsViewHolder(@NonNull IngredientsItemBinding itemView) {
            super(itemView.getRoot());
            this.ingredientsItemBinding = itemView;
        }
    }
}
