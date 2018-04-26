package com.example.owner.fitpro.WorkoutAndExercise.DietPlan;

/**Created by Oleh Hodovaniuk**/

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.widget.TextView;
import com.example.owner.fitpro.R;

public class MealDetails extends AppCompatActivity {
    //Global variables that are populated from external class
    public static final String itemName = "Meal Name";
    public static final String itemProteins = "Meal Proteins";
    public static final String itemCarbs = "Meal Carbs";
    public static final String itemFats = "Meal Fats";
    public static final String itemCalories = "Meal Calories";
    public static final String itemDescription = "Meal Description";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.meal_details);

        //Link external diet plan class with the meal details view
        Intent intent = getIntent();
        final Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(intent.getStringExtra(itemName));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Link XML layout with local variables
        TextView mealName = (TextView) findViewById(R.id.name);
        TextView proteins = (TextView) findViewById(R.id.proteins);
        TextView carbs = (TextView) findViewById(R.id.carbs);
        TextView fats = (TextView) findViewById(R.id.fats);
        TextView calories = (TextView) findViewById(R.id.calories);
        TextView description = (TextView) findViewById(R.id.description);

        //Populate the Text Views with meal object data
        //Create labels with different colors, concatenate data into text views
        mealName.setText(Html.fromHtml(
      "<font color=" + ContextCompat.getColor(this, R.color.primary_yellow) + ">Name:</font> <font color=" +
              ContextCompat.getColor(this, R.color.primary_white) + ">" + intent.getStringExtra(itemName) + "</font>"
        ));
        proteins.setText(Html.fromHtml(
      "<font color=" + ContextCompat.getColor(this, R.color.primary_yellow) + ">Proteins:</font> <font color=" +
              ContextCompat.getColor(this, R.color.primary_white) + ">" + intent.getStringExtra(itemProteins) + "</font>"
        ));
        carbs.setText(Html.fromHtml(
      "<font color=" + ContextCompat.getColor(this, R.color.primary_yellow) + ">Carbs:</font> <font color=" +
              ContextCompat.getColor(this, R.color.primary_white) + ">" + intent.getStringExtra(itemCarbs) + "</font>"
        ));
        fats.setText(Html.fromHtml(
      "<font color=" + ContextCompat.getColor(this, R.color.primary_yellow) + ">Fats:</font> <font color=" +
              ContextCompat.getColor(this, R.color.primary_white) + ">" + intent.getStringExtra(itemFats) + "</font>"
        ));
        calories.setText(Html.fromHtml(
       "<font color=" + ContextCompat.getColor(this, R.color.primary_yellow) + ">Calories:</font> <font color=" +
               ContextCompat.getColor(this, R.color.primary_white) + ">" + intent.getStringExtra(itemCalories) + "</font>"
        ));
        description.setText(Html.fromHtml(
      "<font color=" + ContextCompat.getColor(this, R.color.primary_yellow) + ">Description:</font> <font color=" +
              ContextCompat.getColor(this, R.color.primary_white) + ">" + intent.getStringExtra(itemDescription) + "</font>"
        ));
    }
}