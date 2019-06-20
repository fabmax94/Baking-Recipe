package com.sidia.fabio.bakingrecipe;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.constraint.Guideline;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.sidia.fabio.bakingrecipe.model.Ingredient;
import com.sidia.fabio.bakingrecipe.model.Recipe;
import com.sidia.fabio.bakingrecipe.model.Step;
import com.sidia.fabio.bakingrecipe.viewModel.RecipeViewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ListFragment.OnFragmentInteractionListener, DetailFragment.OnFragmentInteractionListener, StepFragment.OnFragmentInteractionListener {

    private RecipeViewModel mRecipeViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecipeViewModel = ViewModelProviders.of(this).get(RecipeViewModel.class);

        if (mRecipeViewModel.getRecipes() == null) {
            mRecipeViewModel.setRecipes(loadRecipes());
        }

        int recipeId = getIntent().getIntExtra(RecipeWidgetProvider.EXTRA_WORD, -1);

        if (savedInstanceState == null && recipeId == -1) {
            mRecipeViewModel.setCurrentFragment(new ListFragment());
            loadFragment(false, R.id.list_container);
        }

        if (savedInstanceState != null && mRecipeViewModel.getCurrentFragment() instanceof DetailFragment) {
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        //carrega receita apartir do widget
        if (recipeId != -1) {
            mRecipeViewModel.setRecipeByWidget(recipeId);
            onListClick(mRecipeViewModel.getCurrentRecipe());
        }
    }

    //carregar o fragment no container recebido como parametro
    private void loadFragment(boolean hasHomeButton, int containerId) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(containerId, mRecipeViewModel.getCurrentFragment()).commit();

        getSupportActionBar().setHomeButtonEnabled(hasHomeButton);
        getSupportActionBar().setDisplayHomeAsUpEnabled(hasHomeButton);
    }

    private String loadJSONFromAsset() {
        String json;
        try {
            InputStream is = getAssets().open("recipes.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    private List<Recipe> loadRecipes() {
        List<Recipe> result = new ArrayList<>();
        try {
            JSONObject obj = new JSONObject(loadJSONFromAsset());
            JSONArray m_jArry = obj.getJSONArray("recipes");
            for (int i = 0; i < m_jArry.length(); i++) {
                JSONObject jo_inside = m_jArry.getJSONObject(i);
                int id = jo_inside.getInt("id");
                int servings = jo_inside.getInt("id");
                String name = jo_inside.getString("name");
                String image = jo_inside.getString("image");
                result.add(new Recipe(id, name, servings, image, loadIngredients(jo_inside.getJSONArray("ingredients")), loadSteps(jo_inside.getJSONArray("steps"))));

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    private List<Step> loadSteps(JSONArray steps) {
        List<Step> result = new ArrayList<>();
        try {
            for (int i = 0; i < steps.length(); i++) {
                JSONObject jo_inside = steps.getJSONObject(i);
                int id = jo_inside.getInt("id");
                String shortDescription = jo_inside.getString("shortDescription");
                String description = jo_inside.getString("description");
                String videoURL = jo_inside.getString("videoURL");
                String thumbnailURL = jo_inside.getString("thumbnailURL");
                result.add(new Step(id, shortDescription, description, videoURL, thumbnailURL));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    private List<Ingredient> loadIngredients(JSONArray ingredients) {
        List<Ingredient> result = new ArrayList<>();
        try {
            for (int i = 0; i < ingredients.length(); i++) {
                JSONObject jo_inside = ingredients.getJSONObject(i);
                int quantity = jo_inside.getInt("quantity");
                String measure = jo_inside.getString("measure");
                String name = jo_inside.getString("ingredient");
                result.add(new Ingredient(quantity, measure, name));

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public void onListClick(Recipe recipe) {
        mRecipeViewModel.setCurrentRecipe(recipe);
        mRecipeViewModel.setCurrentFragment(DetailFragment.newInstance());
        loadFragment(true, R.id.list_container);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return true;
    }

    @Override
    public void backToList() {
        mRecipeViewModel.setCurrentFragment(new ListFragment());
        loadFragment(false, R.id.list_container);
    }

    @Override
    public void openStep(Step step) {
        mRecipeViewModel.setCurrentStep(step);
        mRecipeViewModel.setCurrentFragment(StepFragment.newInstance(step));
        if (isTablet()) {
            Guideline guideline = findViewById(R.id.verticalHalf);
            guideline.setGuidelinePercent((float) 0.5);
            loadFragment(true, R.id.step_container);
        } else {
            loadFragment(true, R.id.list_container);
        }
    }

    private boolean isTablet() {
        return findViewById(R.id.step_container) != null;
    }

    @Override
    public void backStep() {
        Step step = mRecipeViewModel.getCurrentRecipe().getBackStep(mRecipeViewModel.getCurrentStep());
        if (step != null) {
            openStep(step);
        }
    }

    @Override
    public void nextStep() {
        Step step = mRecipeViewModel.getCurrentRecipe().getNextStep(mRecipeViewModel.getCurrentStep());
        if (step != null) {
            openStep(step);
        }
    }

    @Override
    public void backToRecipe() {
        mRecipeViewModel.setCurrentFragment(new DetailFragment());
        loadFragment(true, R.id.list_container);
    }

    @Override
    public void onBackPressed() {
        if (mRecipeViewModel.getCurrentFragment() instanceof DetailFragment) {
            backToList();
        } else if (mRecipeViewModel.getCurrentFragment() instanceof StepFragment) {
            if (isTablet()) {
                Guideline guideline = findViewById(R.id.verticalHalf);
                guideline.setGuidelinePercent((float) 0.99999);
            }
            onListClick(mRecipeViewModel.getCurrentRecipe());
        } else {
            finish();
        }
    }
}
