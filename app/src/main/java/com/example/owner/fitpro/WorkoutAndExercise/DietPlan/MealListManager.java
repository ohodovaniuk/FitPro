package com.example.owner.fitpro.WorkoutAndExercise.DietPlan;

/**Created by Oleh Hodovaniuk**/

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.example.owner.fitpro.WorkoutAndExercise.Database.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class MealListManager {
    private DatabaseHelper SQLLiteDatabaseHelper;

    //Initialise the database
    public MealListManager(Context context) {
        SQLLiteDatabaseHelper = DatabaseHelper.getInstance(context);
    }

    //Retrieve and return complete/full listing of meals that exist within the diet plan
    public List<MealListItem> getListOfDietPlanMeals(String table) {
        SQLiteDatabase db = SQLLiteDatabaseHelper.getReadableDatabase();
        DatabaseHelper.table = table + "Meals";
        Cursor cursor = null;
        List<MealListItem> items = new ArrayList<>();

        try {
            cursor = db.rawQuery("SELECT * FROM " + DatabaseHelper.table,null);
        }
        catch(SQLException e) {
            Log.d("SQL exception caught in: ", "Meal List Manager getListOfDietPlanMeals method");
        }

        assert cursor != null;
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                MealListItem item = new MealListItem(
                    cursor.getInt(cursor.getColumnIndex("_id")),
                    cursor.getString(cursor.getColumnIndex("meal_category_name")),
                    cursor.getString(cursor.getColumnIndex("meal_name")),
                    cursor.getInt(cursor.getColumnIndex("proteins")),
                    cursor.getInt(cursor.getColumnIndex("carbs")),
                    cursor.getInt(cursor.getColumnIndex("fats")),
                    cursor.getInt(cursor.getColumnIndex("calories")),
                    cursor.getString(cursor.getColumnIndex("description"))
                );
                items.add(item);
                cursor.moveToNext();
            }
        }

        cursor.close();
        return items;
    }

    //Retrieve and return complete/full listing of meals that exist within the meal category
    public List<MealListItem> getListOfDietPlanMeals(String table, String mealCategoryName) {
        SQLiteDatabase db = SQLLiteDatabaseHelper.getReadableDatabase();
        DatabaseHelper.table = table + "Meals";
        Cursor cursor = null;
        List<MealListItem> items = new ArrayList<>();

        try {
            cursor = db.rawQuery("SELECT * FROM " + DatabaseHelper.table  + " WHERE meal_category_name = ?", new String[] {String.valueOf(mealCategoryName)});
        }
        catch(SQLException e) {
            Log.d("SQL exception caught in: ", "Meal List Manager getListOfDietPlanMeals method");
        }

        assert cursor != null;
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                MealListItem item = new MealListItem(
                    cursor.getInt(cursor.getColumnIndex("_id")),
                    cursor.getString(cursor.getColumnIndex("meal_category_name")),
                    cursor.getString(cursor.getColumnIndex("meal_name")),
                    cursor.getInt(cursor.getColumnIndex("proteins")),
                    cursor.getInt(cursor.getColumnIndex("carbs")),
                    cursor.getInt(cursor.getColumnIndex("fats")),
                    cursor.getInt(cursor.getColumnIndex("calories")),
                    cursor.getString(cursor.getColumnIndex("description"))
                );
                items.add(item);
                cursor.moveToNext();
            }
        }

        cursor.close();
        return items;
    }

    //Create new meal if the meal to create is not a duplicate
    public void addNewMeal(MealListItem item) {
        SQLiteDatabase db = SQLLiteDatabaseHelper.getWritableDatabase();
        ContentValues newItem = new ContentValues();
        newItem.put("meal_category_name", item.getMealCategoryName());
        newItem.put("meal_name", item.getMealName());
        newItem.put("proteins", item.getProteins());
        newItem.put("carbs", item.getCarbs());
        newItem.put("fats", item.getFats());
        newItem.put("calories", item.getCalories());
        newItem.put("description", item.getDescription());

        try {
            db.insertOrThrow(DatabaseHelper.table, null, newItem);
        }
        catch(SQLException e) {
            Log.d("SQL exception caught in: ", "Meal List Manager addNewMeal method");
        }
    }

    //Create new meal category if the meal category to create is not a duplicate
    public boolean addNewMealCategory(MealListItem item) {
        SQLiteDatabase db = SQLLiteDatabaseHelper.getWritableDatabase();
        ContentValues newItem = new ContentValues();
        newItem.put("meal_category_name", item.getMealCategoryName());
        newItem.put("meal_name", item.getMealName());
        newItem.put("proteins", item.getProteins());
        newItem.put("carbs", item.getCarbs());
        newItem.put("fats", item.getFats());
        newItem.put("calories", item.getCalories());
        newItem.put("description", item.getDescription());

        try {
            db.insertOrThrow(DatabaseHelper.table, null, newItem);
        }
        catch(SQLException e) {
            return true;
        }
        return false;
    }

    //Update meal category name
    public void updateMealCategory(MealListItem item, String table, String mealCategoryName) {
        SQLiteDatabase db = SQLLiteDatabaseHelper.getWritableDatabase();
        DatabaseHelper.table = table + "Meals";
        ContentValues editItem = new ContentValues();
        editItem.put("meal_category_name", item.getMealCategoryName());

        try {
            db.update(DatabaseHelper.table, editItem, "meal_category_name = ?", new String[] {String.valueOf(mealCategoryName)});
        }
        catch(SQLException e) {
            Log.d("SQL exception caught in: ", "Meal List Manager updateMealCategory method");
        }
    }

    //Delete a meal and its content
    public boolean deleteMeal(long id) {
        SQLiteDatabase db = SQLLiteDatabaseHelper.getWritableDatabase();

        try {
            db.delete(DatabaseHelper.table, "_id = ?", new String[] {String.valueOf(id)});
        }
        catch(SQLException e) {
            return false;
        }

        return true;
    }

    //Delete meal category and its content (meals)
    public boolean deleteMealCategory(String mealCategoryName) {
        SQLiteDatabase db = SQLLiteDatabaseHelper.getWritableDatabase();

        try {
            db.delete(DatabaseHelper.table, "meal_category_name = ?", new String[] {String.valueOf(mealCategoryName)});
        }
        catch(SQLException e) {
            return false;
        }

        return true;
    }

    //Check if the meal category already exists inside the diet plan
    public boolean mealCategoryIsDuplicate(String table, String mealCategoryName) {
        SQLiteDatabase db = SQLLiteDatabaseHelper.getReadableDatabase();
        DatabaseHelper.table = table + "Meals";

        @SuppressLint("Recycle") Cursor cursor = db.rawQuery("SELECT * FROM " + DatabaseHelper.table + " WHERE meal_category_name = ?", new String[] {String.valueOf(mealCategoryName)});

        return cursor.getCount() > 0;
    }

    //Check if the meal already exists inside the diet plan
    public boolean mealIsDuplicate(String mealCategoryName, String mealName) {
        SQLiteDatabase db = SQLLiteDatabaseHelper.getReadableDatabase();

        @SuppressLint("Recycle") Cursor cursor = db.rawQuery("SELECT * FROM " + DatabaseHelper.table + " WHERE meal_category_name = ? AND meal_name = ?", new String[] {String.valueOf(mealCategoryName), String.valueOf(mealName)});

        return cursor.getCount() > 0;
    }
}
