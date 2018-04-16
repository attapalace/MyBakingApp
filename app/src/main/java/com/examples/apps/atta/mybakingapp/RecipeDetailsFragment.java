package com.examples.apps.atta.mybakingapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.examples.apps.atta.mybakingapp.adapters.StepsAdapter;
import com.examples.apps.atta.mybakingapp.model.Ingredient;
import com.examples.apps.atta.mybakingapp.model.Recipe;
import com.examples.apps.atta.mybakingapp.model.Step;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.examples.apps.atta.mybakingapp.MainActivity.RECIPES_BUNDLE;

public class RecipeDetailsFragment extends Fragment {

    ArrayList<Recipe> recipe;
    String recipeTitle;
    @BindView(R.id.ingredients_detail)
    TextView ingredientsDetails;
    @BindView(R.id.steps_recyclerView)
    RecyclerView stepsRecyclerView;
    StepsAdapter stepsAdapter;

    public RecipeDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_recipe_details, container, false);

        ButterKnife.bind(this,rootView);

        if (savedInstanceState != null){
            recipe = (ArrayList<Recipe>) savedInstanceState.getSerializable(RECIPES_BUNDLE);
        }else {
            recipe = (ArrayList<Recipe>) getArguments().getSerializable(RECIPES_BUNDLE);
        }

        List<Ingredient> ingredients = recipe.get(0).getIngredients();
        List<Step> steps = recipe.get(0).getSteps();

        recipeTitle = recipe.get(0).getName();

        for (Ingredient ingredient : ingredients){
            ingredientsDetails.append("\u2022" + " " +ingredient.getIngredient());
            ingredientsDetails.append(" (" + ingredient.getQuantity() +" ");
            ingredientsDetails.append(ingredient.getMeasure() + ")" + "\n");
        }


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext()
                ,LinearLayoutManager.VERTICAL,false){
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        stepsRecyclerView.addItemDecoration(new DividerItemDecoration(stepsRecyclerView.getContext()
                ,DividerItemDecoration.VERTICAL));
        stepsRecyclerView.setLayoutManager(linearLayoutManager);
        stepsRecyclerView.setFocusable(false);

        stepsAdapter = new StepsAdapter(getContext(), (RecipeDetailsActivity)getActivity());
        stepsAdapter.setStepsData(steps,getContext());
        stepsRecyclerView.setAdapter(stepsAdapter);

        return rootView;
    }


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(RECIPES_BUNDLE,recipe);
        outState.putString("Title" , recipeTitle);
    }
}
