package com.examples.apps.atta.mybakingapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.examples.apps.atta.mybakingapp.adapters.RecipeAdapter;
import com.examples.apps.atta.mybakingapp.model.Recipe;
import com.examples.apps.atta.mybakingapp.retrofit.RecipeInterface;
import com.examples.apps.atta.mybakingapp.retrofit.RetrofitClientInstance;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainFragment extends Fragment {

    @BindView(R.id.recipes_recycler_view)
    RecyclerView recyclerView;

    public MainFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootview = inflater.inflate(R.layout.fragment_main,container,false);

        ButterKnife.bind(this,rootview);

        final RecipeAdapter adapter = new RecipeAdapter(getContext(),(MainActivity) getActivity());

        recyclerView.setAdapter(adapter);

        if(rootview.getTag()!= null && rootview.getTag().equals("layout_land")){
            GridLayoutManager layoutManager = new GridLayoutManager(getContext(),3);
            recyclerView.setLayoutManager(layoutManager);
        }else {
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
            recyclerView.setLayoutManager(linearLayoutManager);
        }

        RecipeInterface recipeInterface = RetrofitClientInstance.getInstance()
                .create(RecipeInterface.class);
        final Call<ArrayList<Recipe>> recipe = recipeInterface.getRecipe();

        recipe.enqueue(new Callback<ArrayList<Recipe>>() {
            @Override
            public void onResponse(Call<ArrayList<Recipe>> call, Response<ArrayList<Recipe>> response) {

                    ArrayList<Recipe> recipes = response.body();
                    adapter.setRecipeData(recipes,getContext());
                    adapter.notifyDataSetChanged();

                    Log.d(getString(R.string.successful_response) , String.valueOf(response.body()));

            }

            @Override
            public void onFailure(Call<ArrayList<Recipe>> call, Throwable t) {
                Log.v(getString(R.string.failed_json_parsing) , t.toString() );
            }
        });
        return rootview;
    }


}
