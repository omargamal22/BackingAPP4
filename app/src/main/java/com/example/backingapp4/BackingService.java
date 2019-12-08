package com.example.backingapp4;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.example.backingapp4.Model.Meals.Meal;
import com.example.backingapp4.Network.API;
import com.example.backingapp4.Network.Retrofit_Service;
import com.example.backingapp4.viewmodels.RecipeCardsViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BackingService extends RemoteViewsService{


    @Override
    public RemoteViewsService.RemoteViewsFactory onGetViewFactory(Intent intent) {
       return (new WidgetRemoteViewsFactory(this.getApplicationContext(), intent));
    }

    public class WidgetRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
        private Context context = null;
        private int appWidgetId;

        private ArrayList<Meal> widgetList;

        public WidgetRemoteViewsFactory(Context context, Intent intent) {
            this.context = context;
            appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
            Log.d("AppWidgetId", String.valueOf(appWidgetId));
        }

        private void updateWidgetListView() {
            Retrofit retrofit = new Retrofit.Builder().baseUrl(API.BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
            API api = retrofit.create(API.class);
            Call<List<Meal>> call_ = api.GET_MEALS();
            call_.enqueue(new Callback<List<Meal>>() {
                @Override
                public void onResponse(Call<List<Meal>> call, Response<List<Meal>> response) {

                    Log.println(Log.ASSERT,"OMAR","DataCame");
                    widgetList = new ArrayList<>();
                    widgetList.addAll(response.body());
                    AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
                    int[] appWidgetIds = appWidgetManager.getAppWidgetIds(
                            new ComponentName(context, RecipeWidget.class));
                    appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.WidgetList);

                }

                @Override
                public void onFailure(Call<List<Meal>> call, Throwable t) {
                }
            });
        }

        @Override
        public int getCount() {
            if(widgetList == null){
                return 0;
            }
            return widgetList.size();
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public RemoteViews getLoadingView() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public RemoteViews getViewAt(int position) {
            if (widgetList == null || widgetList.size() == 0) {
                return null;
            }
            Log.println(Log.ASSERT,"OMAR","making views");
            RemoteViews remoteView = new RemoteViews(context.getPackageName(),
                    R.layout.recipe_item_widget);

            remoteView.setTextViewText(R.id.RecipeName, widgetList.get(position).getName());
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(widgetList.get(position).getIngredients().get(0).id+' '+widgetList.get(position).getIngredients().get(0).getQuantity()+' '+widgetList.get(position).getIngredients().get(0).getMeasure()+' '+widgetList.get(position).getIngredients().get(0).getIngredient());
            for (int i = 1; i < widgetList.get(position).getIngredients().size(); i++) {
                stringBuilder.append("\n");
                stringBuilder.append(widgetList.get(position).getIngredients().get(i).id + ' ' + widgetList.get(position).getIngredients().get(i).getQuantity() + ' ' + widgetList.get(position).getIngredients().get(i).getMeasure() + ' ' + widgetList.get(position).getIngredients().get(i).getIngredient());
            }
            remoteView.setTextViewText(R.id.Ings, stringBuilder.toString());
            return remoteView;
        }

        @Override
        public int getViewTypeCount() {
            // TODO Auto-generated method stub
            return 1;
        }

        @Override
        public boolean hasStableIds() {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public void onCreate() {
            // TODO Auto-generated method stub
            Log.println(Log.ASSERT,"OMAR","created");
            updateWidgetListView();
        }

        @Override
        public void onDataSetChanged() {
            // TODO Auto-generated method stub
            Log.println(Log.ASSERT,"OMAR","DataChanged");
        }

        @Override
        public void onDestroy() {
            // TODO Auto-generated method stub
        }
    }
}