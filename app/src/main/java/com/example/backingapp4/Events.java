package com.example.backingapp4;

import androidx.lifecycle.ViewModel;

import com.example.backingapp4.Model.Meals.Meal;
import com.example.backingapp4.Model.Meals.Step;
import com.example.backingapp4.viewmodels.StepsViewModel;

import java.util.ArrayList;


public class Events {
    public static class ActivityActivityMessage {
        private ViewModel message;



        public ActivityActivityMessage(ViewModel message) {
            this.message = message;
        }

        public ViewModel getMessage() {
            return message;
        }
    }

    public static class StepsMessage{
        private StepsViewModel message1;

        public StepsViewModel getMessage1() {
            return message1;
        }

        public StepsMessage(StepsViewModel message1) {
            this.message1 = message1;
        }
    }
}
