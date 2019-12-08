package com.example.backingapp4.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.backingapp4.Events;
import com.example.backingapp4.GlobalBus;
import com.example.backingapp4.MealDetailActivity;
import com.example.backingapp4.MealListActivity;
import com.example.backingapp4.Model.Meals.Step;
import com.example.backingapp4.R;
import com.example.backingapp4.ShowListener;
import com.example.backingapp4.detailsfragments.VedioFrag;
import com.example.backingapp4.viewmodels.StepsViewModel;

import java.util.ArrayList;

import moe.feng.common.stepperview.IStepperAdapter;
import moe.feng.common.stepperview.VerticalStepperItemView;
import moe.feng.common.stepperview.VerticalStepperView;

public class StepsAdapter implements IStepperAdapter {

    private int last_step_index = 0;
    private ArrayList<StepsViewModel> stepsViewModels;
    private VerticalStepperView mVerticalStepperView;
    private final MealListActivity mParentActivity;
    private final boolean mTwoPane;
    private VedioFrag fragment;
    private final ShowListener showListener = new ShowListener() {
        @Override
        public void show_vid(StepsViewModel stepsViewModel) {
            if (mTwoPane) {
                if(fragment == null) {
                    fragment = new VedioFrag(stepsViewModel);
                    mParentActivity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.meal_detail_container, fragment)
                            .commit();
                }
                else{
                    fragment.buildMediaSource(Uri.parse(stepsViewModel.getVideoURL()));
                }
            } else {
                Intent intent = new Intent(mParentActivity, MealDetailActivity.class);
                GlobalBus.getBus().postSticky(new Events.StepsMessage(stepsViewModel));
                mParentActivity.startActivity(intent);
            }
        }
    };

    public StepsAdapter(ArrayList<StepsViewModel> stepsViewModels, VerticalStepperView mVerticalStepperView,MealListActivity parent,boolean twoPane) {
        mParentActivity = parent;
        mTwoPane = twoPane;
        this.stepsViewModels = stepsViewModels;
        this.mVerticalStepperView = mVerticalStepperView;
        if(mTwoPane){
            showListener.show_vid(stepsViewModels.get(last_step_index));
        }
    }

    @NonNull
    @Override
    public CharSequence getTitle(int i) {
        return stepsViewModels.get(i).getShortDescription();
    }

    @Nullable
    @Override
    public CharSequence getSummary(int i) {
        return stepsViewModels.get(i).getDescription();
    }

    @Override
    public int size() {
        return stepsViewModels.size();
    }

    @Override
    public View onCreateCustomView(final int i, Context context, VerticalStepperItemView verticalStepperItemView) {
        View view = LayoutInflater.from(context).inflate(R.layout.step_three_buttons,verticalStepperItemView,false);
        Button nextStep = view.findViewById(R.id.next_step);
        Button PreviousStep = view.findViewById(R.id.Previous_step);
        if( !mVerticalStepperView.canNext()){
            nextStep.setEnabled(false);
        }
        else if( !mVerticalStepperView.canPrev()){
            PreviousStep.setEnabled(false);
        }
        nextStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mVerticalStepperView.nextStep();
                last_step_index++;
                if(mTwoPane){
                    showListener.show_vid(stepsViewModels.get(last_step_index));
                }
            }
        });
        PreviousStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mVerticalStepperView.prevStep();
                last_step_index--;
                if(mTwoPane){
                    showListener.show_vid(stepsViewModels.get(last_step_index));
                }
            }
        });
        if(!mTwoPane) {
            Button show = view.findViewById(R.id.show_video);
            show.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(stepsViewModels.get(i).getVideoURL() == null || stepsViewModels.get(i).getVideoURL().isEmpty() )
                    {
                        //Toast.makeText(activity,"Can't play this vedio",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    showListener.show_vid(stepsViewModels.get(i));
                }
            });
        }
        return view ;
    }

    @Override
    public void onShow(int i) {

    }

    @Override
    public void onHide(int i) {

    }
}
