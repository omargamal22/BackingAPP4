package com.example.backingapp4;

import androidx.annotation.Nullable;
import androidx.test.espresso.IdlingResource;

import java.util.concurrent.atomic.AtomicBoolean;

public class RescourcesState implements IdlingResource {

    @Nullable
    private volatile IdlingResource.ResourceCallback mCallback;

    private AtomicBoolean state = new AtomicBoolean(false);
    private static RescourcesState rescourcesState;
    private static RescourcesState rescourcesState1;

    public static  RescourcesState getInstance(){
        if(rescourcesState == null){
            rescourcesState = new RescourcesState();
        }
        return rescourcesState;
    }

    public static  RescourcesState getInstance1(){
        if(rescourcesState1 == null){
            rescourcesState1 = new RescourcesState();
        }
        return rescourcesState1;
    }

    @Override
    public String getName() {
        return this.getClass().getName();
    }

    @Override
    public boolean isIdleNow() {
        return state.get();
    }

    @Override
    public void registerIdleTransitionCallback(ResourceCallback callback) {
        mCallback = callback;
    }
    public void setState(boolean isIdleNow) {
        state.set(isIdleNow);
        if (isIdleNow && mCallback != null) {
            mCallback.onTransitionToIdle();
        }
    }
}
