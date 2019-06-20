package com.sidia.fabio.bakingrecipe.viewModel;

import android.arch.lifecycle.ViewModel;
import android.support.v4.app.Fragment;

import com.sidia.fabio.bakingrecipe.model.Recipe;
import com.sidia.fabio.bakingrecipe.model.Step;

import java.util.List;

public class RecipeViewModel extends ViewModel {
    private Step mCurrentStep;
    private Recipe mCurrentRecipe;
    private Fragment mCurrentFragment;
    private List<Recipe> mRecipes;

    public Step getCurrentStep() {
        return mCurrentStep;
    }

    public void setCurrentStep(Step mCurrentStep) {
        this.mCurrentStep = mCurrentStep;
    }

    public Recipe getCurrentRecipe() {
        return mCurrentRecipe;
    }

    public void setCurrentRecipe(Recipe mCurrentRecipe) {
        this.mCurrentRecipe = mCurrentRecipe;
    }

    public Fragment getCurrentFragment() {
        return mCurrentFragment;
    }

    public void setCurrentFragment(Fragment mCurrentFragment) {
        this.mCurrentFragment = mCurrentFragment;
    }

    public List<Recipe> getRecipes() {
        return mRecipes;
    }

    public void setRecipes(List<Recipe> mRecipes) {
        this.mRecipes = mRecipes;
    }

    public void setRecipeByWidget(int recipeByWidget) {
        for (Recipe recipe : mRecipes) {
            if (recipe.getId() == recipeByWidget) {
                setCurrentRecipe(recipe);
                return;
            }
        }
    }
}
