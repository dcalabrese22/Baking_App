package com.example.dan.baking_app.helpers;

import com.example.dan.baking_app.objects.Ingredient;
import com.example.dan.baking_app.objects.Recipe;
import com.example.dan.baking_app.objects.Step;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Helper class for parsing JSON data
 */
public class JsonResponseParser {

    private static final String NAME = "name";

    private static final String INGREDIENTS = "ingredients";
    private static final String QUANTITY = "quantity";
    private static final String MEASURE = "measure";
    private static final String INGREDIENT = "ingredient";

    private static final String STEPS = "steps";
    private static final String ID = "id";
    private static final String SHORT_DESCRIPTION = "shortDescription";
    private static final String DESCRIPTION = "description";
    private static final String VIDEO_URL = "videoURL";
    private static final String THUMBNAIL_URL = "thumbnailURL";

    private static final String SERVINGS = "servings";

    /**
     * Parses the top level JSON response data
     *
     * @param response HTTP response as a JSONArray
     * @return ArrayList of Recipe objects
     */
    public static ArrayList<Recipe> parseTopLevelJsonRecipeData(JSONArray response) {
        ArrayList<Recipe> recipes = new ArrayList<>();

        try {
            JSONArray recipeJsonArray = new JSONArray(response.toString());

            String recipeName;
            int servings;

            for (int i = 0; i < recipeJsonArray.length(); i++) {
                JSONObject recipeData = recipeJsonArray.getJSONObject(i);
                recipeName = recipeData.getString(NAME);
                servings = recipeData.getInt(SERVINGS);

                Recipe recipeObject = new Recipe(recipeName, servings,
                        parseIngredientsJsonData(recipeData), parseStepJsonData(recipeData));

                recipes.add(recipeObject);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return recipes;
    }

    /**
     * Parses JSON response data that correspond to recipe ingredients
     *
     * @param response HTTP response as JSONObject
     * @return ArrayList of Ingredient objects
     */
    public static ArrayList<Ingredient> parseIngredientsJsonData(JSONObject response) {
        ArrayList<Ingredient> ingredientsList = new ArrayList<>();

        try {
            JSONArray ingredientsJsonArray = response.getJSONArray(INGREDIENTS);

            double quantity;
            String measure;
            String name;

            for (int i = 0; i < ingredientsJsonArray.length(); i++) {
                JSONObject ingredientData = ingredientsJsonArray.getJSONObject(i);
                quantity = ingredientData.getDouble(QUANTITY);
                measure = ingredientData.getString(MEASURE);
                name = ingredientData.getString(INGREDIENT);

                Ingredient ingredientObject = new Ingredient(quantity, measure, name);
                ingredientsList.add(ingredientObject);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return ingredientsList;
    }

    /**
     * Parses JSON response data that corresponds to recipe steps
     *
     * @param response HTTP response as JSONObject
     * @return ArrayList of Step Objects
     */
    public static ArrayList<Step> parseStepJsonData(JSONObject response) {
        ArrayList<Step> stepsList = new ArrayList<>();

        try {
            JSONArray ingredientsJsonArray = response.getJSONArray(STEPS);

            int id;
            String shortDesc;
            String desc;
            String vidUrl;
            String thumbUrl;

            for (int i = 0; i < ingredientsJsonArray.length(); i++) {
                JSONObject ingredientData = ingredientsJsonArray.getJSONObject(i);
                id = ingredientData.getInt(ID);
                shortDesc = ingredientData.getString(SHORT_DESCRIPTION);
                desc = ingredientData.getString(DESCRIPTION);
                vidUrl = ingredientData.getString(VIDEO_URL);
                thumbUrl = ingredientData.getString(THUMBNAIL_URL);
                Step stepObject = new Step(id, shortDesc, desc, vidUrl, thumbUrl);
                stepsList.add(stepObject);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return stepsList;
    }

}
