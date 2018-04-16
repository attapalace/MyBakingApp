package com.examples.apps.atta.mybakingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.examples.apps.atta.mybakingapp.IdlingResource.SimpleIdlingResource;
import com.examples.apps.atta.mybakingapp.adapters.RecipeAdapter;
import com.examples.apps.atta.mybakingapp.model.Recipe;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity
        implements RecipeAdapter.RecipeClickHandler {

    static final String RECIPES_BUNDLE = "recipes";

    @Nullable
    private SimpleIdlingResource mIdleResource;

    @BindView(R.id.toolbar_custom)
    Toolbar toolbar;

    @VisibleForTesting
    @NonNull
    public IdlingResource getIdleResource(){
        if (mIdleResource == null){
            mIdleResource = new SimpleIdlingResource();
        }
        return mIdleResource;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(false);
        getSupportActionBar().setTitle(getApplicationInfo().labelRes);

        getIdleResource();
    }

    @Override
    public void onClick(Recipe recipe) {

        Bundle bundle = new Bundle();
        ArrayList<Recipe> recipes = new ArrayList<>();
        recipes.add(recipe);
        bundle.putSerializable(RECIPES_BUNDLE , recipes);

        Intent intent = new Intent(this , RecipeDetailsActivity.class);
        intent.putExtras(bundle);

        startActivity(intent);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}
