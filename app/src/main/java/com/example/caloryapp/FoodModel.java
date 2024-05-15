package com.example.caloryapp;

public class FoodModel {
    private String name;
    private double calorie;

    public FoodModel(String name, double calorie) {
        this.name = name;
        this.calorie = calorie;
    }

    // Getter ve setter metotları
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getCalorie() {
        return calorie;
    }

    public void setCalorie(double calorie) {
        this.calorie = calorie;
    }
}

