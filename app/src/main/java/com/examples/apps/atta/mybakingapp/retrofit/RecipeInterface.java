package com.examples.apps.atta.mybakingapp.retrofit;

import com.examples.apps.atta.mybakingapp.model.Recipe;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;

public interface RecipeInterface {
    @GET("baking.json")
    Call<ArrayList<Recipe>> getRecipe();
}
