package com.example.owner.fitpro.WorkoutAndExercise.DietPlan;

/**Created by Oleh Hodovaniuk**/

public class MealListItem {
    private int id;
    private String mealCategoryName;
    private String mealName;
    private int proteins;
    private int carbs;
    private int fats;
    private int calories;
    private String description;

    //Initialise meal object data
    public MealListItem(int id, String mealCategoryName, String mealName, int proteins, int carbs, int fats, int calories, String description) {
        this.id = id;
        this.mealCategoryName = mealCategoryName;
        this.mealName = mealName;
        this.proteins = proteins;
        this.carbs = carbs;
        this.fats = fats;
        this.calories = calories;
        this.description = description;
    }

    //Return meal Id
    public Integer getId() {
        return id;
    }

    //Return meal category name
    public String getMealCategoryName() {
        return mealCategoryName;
    }

    //Return meal name
    public String getMealName() {
        return mealName;
    }

    //Return number of meal proteins
    public int getProteins() {
        return proteins;
    }

    //Return number of meal carbs
    public int getCarbs() {
        return carbs;
    }

    //Return number of meal fats
    public int getFats() {
        return fats;
    }

    //Return number of meal calories
    public int getCalories() {
        return calories;
    }

    //Return meal description
    public String getDescription() {
        return description;
    }
}
