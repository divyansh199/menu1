package com.example.recipe;

import java.util.ArrayList;

public class Recipe {
    private int ckng_time,servings;
    private ArrayList<Ingredients> arrayList;

    public Recipe(int ckng_time, int servings, ArrayList<Ingredients> arrayList) {
        this.ckng_time = ckng_time;
        this.servings = servings;
        this.arrayList = arrayList;
    }

    public int getCkng_time() {
        return ckng_time;
    }

    public int getServings() {
        return servings;
    }

    public ArrayList<Ingredients> getArrayList() {
        return arrayList;
    }
}
