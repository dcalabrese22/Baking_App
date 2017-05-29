package com.example.dan.baking_app;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.example.dan.baking_app.Interfaces.PassRecipeDataHandler;
import com.example.dan.baking_app.Interfaces.StepClickHandler;
import com.example.dan.baking_app.objects.Ingredient;
import com.example.dan.baking_app.objects.Step;

import java.util.ArrayList;

public class RecipeDetailActivity extends AppCompatActivity
        implements PassRecipeDataHandler, StepClickHandler{

    public static final String DESC_STEP_EXTRA = "desc_extra";
    public static final String URL_STEP_EXTRA = "url_extra";
    public static final String ID_STEP_EXTRA = "id_extra";
    public static final String TWO_PANE_EXTRA = "two_pane_extra";
    public static final String STEP_KEY = "step_fragment";
    public static final String RECIPE_KEY = "recipe_fragment";

    StepFragment mStepFragment;
    RecipeFragment mRecipeFragment;

    ArrayList<Ingredient> mIngredients;
    ArrayList<Step> mSteps;

    private boolean mTwoPane;
    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setMyTitle();
        setSupportActionBar(mToolbar);

        View decorView = getWindow().getDecorView();
        decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int i) {
                if ((i & View.SYSTEM_UI_FLAG_FULLSCREEN) == View.VISIBLE) {
                    getSupportActionBar().show();
                } else {
                    getSupportActionBar().hide();
                }
            }
        });
        FragmentManager fragmentManager = getFragmentManager();
        if (findViewById(R.id.two_pane) != null) {
            Log.d("TWO PANE", "IN TWO PANE");
            mTwoPane = true;
            if (savedInstanceState != null) {
                if (savedInstanceState.containsKey(STEP_KEY)) {
                    mStepFragment = (StepFragment) getFragmentManager()
                            .getFragment(savedInstanceState, STEP_KEY);
                } else {
                    mRecipeFragment = (RecipeFragment) getFragmentManager()
                            .getFragment(savedInstanceState, RECIPE_KEY);
                }
            } else {

                mRecipeFragment = new RecipeFragment();

                fragmentManager.beginTransaction()
                        .replace(R.id.recipe_list_container, mRecipeFragment, RECIPE_KEY)
                        .commit();
            }
            } else {
            mTwoPane = false;
            Log.d("TWO PANE", "NOT IN TWO PANE");
            if (savedInstanceState != null) {
                if (savedInstanceState.containsKey(STEP_KEY)) {
                    mStepFragment = (StepFragment) getFragmentManager()
                            .getFragment(savedInstanceState, STEP_KEY);
                } else if (savedInstanceState.containsKey(RECIPE_KEY)) {
                    mRecipeFragment = (RecipeFragment) getFragmentManager()
                            .getFragment(savedInstanceState, RECIPE_KEY);
                }
            } else {

                mRecipeFragment = new RecipeFragment();


                fragmentManager.beginTransaction()
                        .replace(R.id.activity_recipe_detail, mRecipeFragment, RECIPE_KEY)
                        .commit();
            }
        }
    }

    public void setMyTitle() {
        if (mTwoPane) {
            mToolbar.setTitle(getResources().getString(R.string.app_name));
        } else {
            mToolbar.setTitle(getIntent().getExtras().getString(MainActivity.RECIPE_NAME_EXTRA));
        }
    }

    @Override
    public ArrayList<Ingredient> passIngredients() {
        mIngredients = getIntent().getExtras().getParcelableArrayList(MainActivity.INGREDIENT_EXTRA);
        return mIngredients;
    }

    @Override
    public ArrayList<Step> passSteps() {
        mSteps = getIntent().getExtras().getParcelableArrayList(MainActivity.STEP_EXTRA);
        return mSteps;
    }

    @Override
    public void onStepClick(Step step) {
        FragmentManager fragmentManager = getFragmentManager();
        String desc = step.getDescription();
        String url = step.getVideoUrl();
        int id = step.getId();
        Bundle bundle = new Bundle();
        bundle.putString(DESC_STEP_EXTRA, desc);
        bundle.putString(URL_STEP_EXTRA, url);
        bundle.putInt(ID_STEP_EXTRA, id);
        bundle.putBoolean(TWO_PANE_EXTRA, mTwoPane);
        mStepFragment = new StepFragment();
        mStepFragment.setArguments(bundle);
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (mTwoPane) {
            fragmentTransaction.add(R.id.step_container, mStepFragment, STEP_KEY);
            fragmentTransaction.commit();
        } else {
            fragmentTransaction.replace(R.id.activity_recipe_detail, mStepFragment, STEP_KEY);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }

    }
}
