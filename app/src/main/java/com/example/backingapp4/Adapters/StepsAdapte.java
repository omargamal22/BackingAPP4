package com.example.backingapp4.Adapters;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.backingapp4.R;
import com.example.backingapp4.ClickListener;
import com.example.backingapp4.viewmodels.StepsViewModel;

import java.util.ArrayList;

import moe.feng.common.stepperview.IStepperAdapter;
import moe.feng.common.stepperview.VerticalStepperItemView;
import moe.feng.common.stepperview.VerticalStepperView;

public class StepsAdapte implements IStepperAdapter {

    private static  int last_step_index = 0;
    private ArrayList<StepsViewModel> stepsViewModels;
    private VerticalStepperView mVerticalStepperView;
    private Activity activity;
    private ClickListener clickListener;

    public StepsAdapte (ArrayList<StepsViewModel> vals , VerticalStepperView mVerticalStepperView , Activity activity , ClickListener clickListener){
        stepsViewModels = vals;
        this.activity = activity;
        this.mVerticalStepperView = mVerticalStepperView;
        this.clickListener = clickListener;
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
        Button show = view.findViewById(R.id.show_video);
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
            }
        });
        PreviousStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mVerticalStepperView.prevStep();
                last_step_index--;
            }
        });
       /* show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(stepsViewModels.get(i).getVideoURL() == null || stepsViewModels.get(i).getVideoURL().isEmpty() )
                {
                    Toast.makeText(activity,"Can't play this vedio",Toast.LENGTH_SHORT).show();
                    return;
                }
                clickListener.onCLICK();
            }
        });*/
        return view ;
    }

    @Override
    public void onShow(int i) {

    }

    @Override
    public void onHide(int i) {

    }

}
