package com.example.owner.fitpro.WorkoutAndExercise.Exercise;

/**Created by Oleh Hodovaniuk**/

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.widget.TextView;
import com.example.owner.fitpro.R;

public class ExerciseDetails  extends AppCompatActivity {
    //Global variables that are populated from external class
    public static final String itemName = "Exercise Name";
    public static final String itemNumberOfSets = "Exercise Number Of Sets";
    public static final String itemRepsPerSet = "Exercise Reps per Set";
    public static final String itemSetEstimateTime = "Exercise Set Estimate Time";
    public static final String itemDescription = "Exercise Description";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exercise_details);

        //Link external exercise class with the exercise details view
        Intent intent = getIntent();
        final Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(intent.getStringExtra(itemName));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Link XML layout with local variables
        TextView exerciseName = (TextView) findViewById(R.id.exercise_name);
        TextView numberOfSets = (TextView) findViewById(R.id.number_of_sets);
        TextView repsPerSet = (TextView) findViewById(R.id.reps_per_set);
        TextView setEstimateTime = (TextView) findViewById(R.id.set_estimate_time);
        TextView description = (TextView) findViewById(R.id.description);

        //Populate the Text Views with exercise object data
        //Create labels with different colors, concatenate data into text views
        exerciseName.setText(Html.fromHtml(
      "<font color=" + ContextCompat.getColor(this, R.color.primary_yellow) + ">Name:</font> <font color=" +
              ContextCompat.getColor(this, R.color.primary_white) + ">" + intent.getStringExtra(itemName) + "</font>"
        ));
        numberOfSets.setText(Html.fromHtml(
      "<font color=" + ContextCompat.getColor(this, R.color.primary_yellow) + ">Sets:</font> <font color=" +
              ContextCompat.getColor(this, R.color.primary_white) + ">" + intent.getStringExtra(itemNumberOfSets) + "</font>"
        ));
        repsPerSet.setText(Html.fromHtml(
      "<font color=" + ContextCompat.getColor(this, R.color.primary_yellow) + ">Reps:</font> <font color=" +
              ContextCompat.getColor(this, R.color.primary_white) + ">" + intent.getStringExtra(itemRepsPerSet) + "</font>"
        ));
        setEstimateTime.setText(Html.fromHtml(
      "<font color=" + ContextCompat.getColor(this, R.color.primary_yellow) + ">Time:</font> <font color=" +
              ContextCompat.getColor(this, R.color.primary_white) + ">" + intent.getStringExtra(itemSetEstimateTime) + "</font>"
        ));
        description.setText(Html.fromHtml(
      "<font color=" + ContextCompat.getColor(this, R.color.primary_yellow) + ">Description:</font> <font color=" +
              ContextCompat.getColor(this, R.color.primary_white) + ">" + intent.getStringExtra(itemDescription) + "</font>"
        ));
    }
}