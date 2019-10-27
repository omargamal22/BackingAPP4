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
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.backingapp4.Adapters.IngredientsAdapter;
import com.example.backingapp4.Adapters.StepsAdapte;
import com.example.backingapp4.ClickListener;
import com.example.backingapp4.DetailActivity;
import com.example.backingapp4.Events;
import com.example.backingapp4.GlobalBus;
import com.example.backingapp4.R;
import com.example.backingapp4.databinding.DetailsFragBinding;
import com.example.backingapp4.viewmodels.IngredientViewModel;
import com.example.backingapp4.viewmodels.RecipeCardsViewModel;
import com.example.backingapp4.viewmodels.StepsViewModel;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

public class DetailFrag extends Fragment {

    private StepsAdapte stepsAdapte;
    private DetailsFragBinding detailsFragBinding;
    private Activity THIS;

    public DetailFrag(){}

    @Override
    public void onDestroy() {
        GlobalBus.getBus().unregister(this);
        super.onDestroy();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        GlobalBus.getBus().register(this);
        View view = inflater.inflate(R.layout.details_frag,container,false);
        detailsFragBinding = DataBindingUtil.setContentView(getActivity(),R.layout.details_frag);
        THIS = getActivity();

        return view;
    }

    @Subscribe(sticky = true)
    public void ReciveViewModel(Events.ActivityActivityMessage event){
        RecipeCardsViewModel recipeCardsViewModel=(RecipeCardsViewModel) event.getMessage();
        StepsViewModel stepsViewModel = new ViewModelProvider(this,new Myfactory1()).get(StepsViewModel.class);
        stepsViewModel.MakeViewModel(recipeCardsViewModel.steps);
        stepsViewModel.getArrayListMutableLiveData().observe(this, new Observer<ArrayList<StepsViewModel>>() {
            @Override
            public void onChanged(ArrayList<StepsViewModel> stepsViewModels) {
                stepsAdapte = new StepsAdapte(stepsViewModels, detailsFragBinding.vsvSteps, THIS, new ClickListener() {
                    @Override
                    public void onItemClick(ViewModel viewModel) {
                    }

                    @Override
                    public void onCLICK() {
                        Fragment fragment = new VedioFrag();
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.Big_Countainer,fragment)
                                .addToBackStack("tag").commit();
                    }
                });
                detailsFragBinding.vsvSteps.setStepperAdapter(stepsAdapte);

            }
        });
    }

    class Myfactory1 implements ViewModelProvider.Factory {
        public Myfactory1() {
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return (T) new StepsViewModel();
        }
    }
}
