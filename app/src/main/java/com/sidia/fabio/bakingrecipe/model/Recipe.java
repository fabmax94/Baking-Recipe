package com.sidia.fabio.bakingrecipe.model;

import java.util.List;

public class Recipe {
    private int id;
    private String name;
    private int servings;
    private String image;
    private List<Ingredient> ingredients;
    private List<Step> steps;

    public Recipe(int id, String name, int servings, String image, List<Ingredient> ingredients, List<Step> steps) {
        this.id = id;
        this.name = name;
        this.servings = servings;
        this.image = image;
        this.ingredients = ingredients;
        this.steps = steps;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public List<Step> getSteps() {
        return steps;
    }

    public void setSteps(List<Step> steps) {
        this.steps = steps;
    }

    public Step getNextStep(Step currentStep) {
        int nextIndex = steps.indexOf(currentStep) + 1;
        if (nextIndex < steps.size()) {
            return steps.get(nextIndex);
        }
        return null;
    }

    public Step getBackStep(Step currentStep) {
        int nextIndex = steps.indexOf(currentStep) - 1;
        if (nextIndex >= 0) {
            return steps.get(nextIndex);
        }
        return null;
    }
}
