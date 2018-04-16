package com.examples.apps.atta.mybakingapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.examples.apps.atta.mybakingapp.adapters.StepsAdapter;
import com.examples.apps.atta.mybakingapp.model.Ingredient;
import com.examples.apps.atta.mybakingapp.model.Recipe;
import com.examples.apps.atta.mybakingapp.model.Step;
import com.examples.apps.atta.mybakingapp.widget.RecipeWidgetProvider;
import com.google.gson.Gson;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.examples.apps.atta.mybakingapp.MainActivity.RECIPES_BUNDLE;

public class RecipeDetailsActivity extends AppCompatActivity implements StepsAdapter.StepClickHandler
,StepDetailFragment.StepClickListener{

    private ArrayList<Recipe> recipes;
    private String recipeTitle;
    static final String STEPS_BUNDLE = "steps";
    static final String STEP_INDEX = "index";
    public static final String RECIPE_ID = "recipeId";
    public static final String SHARED_PREFS_KEY = "SHARED_PREFS_KEY";


    @BindView(R.id.toolbar_custom)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ButterKnife.bind(this);

        if (savedInstanceState == null){

            Bundle bundle = getIntent().getExtras();

            recipes = new ArrayList<>();
            recipes = (ArrayList<Recipe>) bundle.getSerializable(RECIPES_BUNDLE);
            recipeTitle = recipes.get(0).getName();


            RecipeDetailsFragment fragment = new RecipeDetailsFragment();
            fragment.setArguments(bundle);
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .add(R.id.fragment_container,fragment)
                    .commit();


        }else {
            recipeTitle = savedInstanceState.getString("Title");
            recipes = (ArrayList<Recipe>) savedInstanceState.getSerializable(RECIPES_BUNDLE);
        }

        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(recipeTitle);

        makeData();
        sendBroadcast();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onClick(List<Step> steps, int index) {
        Bundle bundle = new Bundle();

        bundle.putSerializable(STEPS_BUNDLE, (Serializable) steps);
        bundle.putSerializable(RECIPES_BUNDLE , recipes);
        bundle.putString("Title",recipeTitle);
        bundle.putInt(STEP_INDEX,index);

        if (findViewById(R.id.tablet_recipe_layout) != null){
            StepDetailFragment stepDetailFragment = new StepDetailFragment();
            stepDetailFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.step_fragment_container,stepDetailFragment)
                    .commit();
        }else {

            Intent intent = new Intent(this , StepDetailActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }

    private void makeData() {
        ArrayList<Ingredient> ingredients = (ArrayList<Ingredient>) recipes.get(0).getIngredients();
        Gson gson = new Gson();
        String json = gson.toJson(ingredients);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(SHARED_PREFS_KEY, json).apply();
    }

    private void sendBroadcast() {

        Intent intent = new Intent(this, RecipeWidgetProvider.class);
        intent.setAction("android.appwidget.action.APPWIDGET_UPDATE");
        sendBroadcast(intent);
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("Title",recipeTitle);
        outState.putSerializable(RECIPES_BUNDLE,recipes);
    }

    @Override
    public void onStepClick(ArrayList<Step> steps, int stepIndex) {
        Fragment fragment = new StepDetailFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();

        Bundle bundle = new Bundle();
        bundle.putSerializable(STEPS_BUNDLE , steps);
        bundle.putSerializable(RECIPES_BUNDLE , recipes);
        bundle.putInt(STEP_INDEX , stepIndex);
        fragment.setArguments(bundle);

        fragmentManager.beginTransaction()
                .replace(R.id.step_fragment_container,fragment)
                .commit();
    }
}
