package com.example.backingapp4.Network;



import androidx.lifecycle.MutableLiveData;

import com.example.backingapp4.Model.Meals.Meal;
import com.example.backingapp4.viewmodels.RecipeCardsViewModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Retrofit_Service  {


    private static MutableLiveData<ArrayList<RecipeCardsViewModel>> res = new MutableLiveData<>() ;
    private static String E;

    public static MutableLiveData<ArrayList<RecipeCardsViewModel>>  Get_Retrofit_Service(){
        Retrofit retrofit = new Retrofit.Builder().baseUrl(API.BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        API api = retrofit.create(API.class);
        Call<List<Meal>> call_ = api.GET_MEALS();
        call_.enqueue(new Callback<List<Meal>>() {
            @Override
            public void onResponse(Call<List<Meal>> call, Response<List<Meal>> response) {

                GetData(response.body());
            }

            @Override
            public void onFailure(Call<List<Meal>> call, Throwable t) {
                E=t.getMessage();
            }
        });
        return res;
    }
    public static void GetData( List<Meal> items){
        Meal Mtemp;
        RecipeCardsViewModel Rtemp;
        ArrayList<RecipeCardsViewModel> models = new ArrayList<>();

        for(int i=0;i<items.size();i++){
            Mtemp = items.get(i);
            Rtemp = new RecipeCardsViewModel(Mtemp);
            models.add(Rtemp);
        }

        res.setValue(models);
        return ;
    }
}
