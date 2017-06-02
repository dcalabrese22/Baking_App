package com.example.dan.baking_app;

import android.app.Fragment;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dan.baking_app.Interfaces.PassRecipeDataHandler;
import com.example.dan.baking_app.Interfaces.StepClickHandler;
import com.example.dan.baking_app.objects.Ingredient;
import com.example.dan.baking_app.objects.Step;

import java.util.ArrayList;

public class RecipeFragment extends Fragment {

    ArrayList<Ingredient> mIngredients;
    ArrayList<Step> mSteps;
    String mRecipeName;

    RecyclerView mRecyclerview;
    PassRecipeDataHandler mHandler;

    StepClickHandler mStepClickHandler;

    private static final String SAVED_STATE_INGREDIENTS = "ingredients";
    private static final String SAVED_STATE_STEPS = "steps";


    public RecipeFragment() {}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_recipe_detail, container, false);
        mRecyclerview = (RecyclerView) rootview.findViewById(R.id.recyclerview_single_recipe);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerview.setLayoutManager(linearLayoutManager);

        RecipeAdapter adapter = new RecipeAdapter(mStepClickHandler);

        if (savedInstanceState != null) {
            mSteps = savedInstanceState.getParcelableArrayList(SAVED_STATE_STEPS);
            mIngredients = savedInstanceState.getParcelableArrayList(SAVED_STATE_INGREDIENTS);
        }

        adapter.setData(mIngredients, mSteps);

        mRecyclerview.setAdapter(adapter);

        broadcast();
        return rootview;

    }

    public void broadcast() {
        Intent intent = new Intent();
        intent.putExtra(MainActivity.RECIPE_NAME_EXTRA, mRecipeName);
        intent.putParcelableArrayListExtra("ingredients", mIngredients);
        intent.setAction(BakingWidgetProvider.WIDGET_EXTRA_INTENT);
        getActivity().sendBroadcast(intent);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(SAVED_STATE_INGREDIENTS, mIngredients);
        outState.putParcelableArrayList(SAVED_STATE_STEPS, mSteps);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mStepClickHandler = (StepClickHandler) context;
        mHandler = (PassRecipeDataHandler) context;
        mRecipeName = mHandler.passRecipeName();
        mIngredients = mHandler.passIngredients();
        mSteps = mHandler.passSteps();
    }
}
