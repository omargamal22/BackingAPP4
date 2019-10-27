package com.example.backingapp4;

import androidx.lifecycle.ViewModel;



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
}
