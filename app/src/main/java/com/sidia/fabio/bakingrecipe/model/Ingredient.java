package com.sidia.fabio.bakingrecipe.model;

public class Ingredient {
    private int quantity;
    private String measure;
    private String name;

    public Ingredient(int quantity, String measure, String name) {
        this.quantity = quantity;
        this.measure = measure;
        this.name = name;
    }

    public String toString() {
        return quantity + " " + measure + " " + name;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
