package com.example.backingapp4.Network;



import com.example.backingapp4.Model.Meals.Meal;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface API {
    String BASE_URL = "https://d17h27t6h515a5.cloudfront.net/";

    @GET("topher/2017/May/59121517_baking/baking.json")
    Call<List<Meal>> GET_MEALS();

}
