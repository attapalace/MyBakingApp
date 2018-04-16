package com.examples.apps.atta.mybakingapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.examples.apps.atta.mybakingapp.model.Recipe;
import com.examples.apps.atta.mybakingapp.model.Step;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.examples.apps.atta.mybakingapp.RecipeDetailsActivity.STEPS_BUNDLE;
import static com.examples.apps.atta.mybakingapp.RecipeDetailsActivity.STEP_INDEX;
import static com.examples.apps.atta.mybakingapp.MainActivity.RECIPES_BUNDLE;

public class StepDetailActivity extends AppCompatActivity implements StepDetailFragment.StepClickListener{

    private ArrayList<Recipe> recipes;
    String recipeName;

    @BindView(R.id.toolbar_custom)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_step);

        ButterKnife.bind(this);


        if (savedInstanceState == null){
            Bundle bundle = getIntent().getExtras();
            recipes = (ArrayList<Recipe>) bundle.getSerializable(RECIPES_BUNDLE);
            recipeName = bundle.getString("Title");

            StepDetailFragment fragment = new StepDetailFragment();
            fragment.setArguments(bundle);
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().add(R.id.step_detail_container,fragment)
                    .commit();
        }else {
            recipeName = savedInstanceState.getString("Title");
        }

        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(recipeName);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onStepClick(ArrayList<Step> steps, int index) {
        Fragment fragment = new StepDetailFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();

        Bundle bundle = new Bundle();
        bundle.putSerializable(STEPS_BUNDLE , steps);
        bundle.putSerializable(RECIPES_BUNDLE , recipes);
        bundle.putInt(STEP_INDEX , index);
        fragment.setArguments(bundle);

        fragmentManager.beginTransaction()
                .replace(R.id.step_detail_container,fragment)
                .commit();
    }
}
