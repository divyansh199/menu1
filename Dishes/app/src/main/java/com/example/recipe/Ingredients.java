package com.example.recipe;

public class Ingredients {
    private String name;
    private  double time;

    public Ingredients(String name, double time) {
        this.name = name;
        this.time=time;
    }

    public String getName() {
        return name;
    }

    public double getTime() {
        return time;
    }
}
