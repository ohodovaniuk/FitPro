package com.example.owner.fitpro.WorkoutAndExercise.DietPlan;

/**Created by Oleh Hodovaniuk**/

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.example.owner.fitpro.R;
import com.example.owner.fitpro.WorkoutAndExercise.Workout.WorkoutCategory;

public class AddNewMealCategoryCustomDialog extends Dialog implements View.OnClickListener {
    //Diet plan application context
    private Context context;
    //Workout Category Name, meal category name
    private String  workoutCategoryName, mealCategoryName;
    //Meal Category Name edit text field
    private EditText inputMealCategoryName;
    //Meal Category edit mode status
    private boolean editModeOn = false;
    //Input validation status
    private boolean inputIsValid = false;

    //Initialise current application context and parent workout category name
    AddNewMealCategoryCustomDialog(Context context, String workoutCategoryName) {
        super(context);
        this.context = context;
        this.workoutCategoryName = workoutCategoryName;
    }

    //Set the local meal category instance variables with appropriate data (used to populate the edit text field)
    void editMealCategory(String mealCategoryName) {
        this.mealCategoryName = mealCategoryName;
        editModeOn = true;
    }

    //Link/connect the custom dialog XML with the class logic
    //Populates the layout with data if edit mode is on
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.add_new_workout_routine_custom_dialog);

        //Set the custom dialog header name
        TextView dialogTitle = findViewById(R.id.add_new_workout_routine_dialog_title);
        dialogTitle.setText(R.string.add_new_meal_category);
        inputMealCategoryName = findViewById(R.id.workout_routine_name);
        //Refresh the view and close any other expanding, swiping ... effects.
        WorkoutCategory.adapter.refreshWorkoutCategoryView();

        //Set listener on dialog buttons
        Button cancel = findViewById(R.id.cancel_button);
        Button add = findViewById(R.id.add_button);
        cancel.setOnClickListener(this);
        add.setOnClickListener(this);

        //Populate the dialog when edit mode is on
        if (editModeOn) {
            dialogTitle.setText(R.string.edit_meal_category);
            inputMealCategoryName.setText(mealCategoryName);
        }
    }

    //Listen to the custom dialog field and buttons, responds to dialog actions with class logic
    //Read from the custom dialog edit field if normal mode is on
    //Validate the edit text field according to the rules
    //Edit the existing meal category information if edit mode is on, saves the modifications
    //Clear input field junk data after the dialog has done its work
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //Clear input fields and close the dialog
            case R.id.cancel_button:
                inputMealCategoryName.getText().clear();
                inputIsValid = false;
                dismiss();
                break;
            //Read, validate and store the new/existing meal category name
            case R.id.add_button:
                if (inputMealCategoryName.getText().toString().isEmpty()) {
                    Toast.makeText(context, "Meal category name field is empty", Toast.LENGTH_SHORT).show();
                }
                else if (!inputMealCategoryName.getText().toString().matches("[a-zA-Z0-9\\s]+")) {
                    Toast.makeText(context, "Meal category name field must contain characters and digits only", Toast.LENGTH_SHORT).show();
                }
                else if (inputMealCategoryName.getText().toString().length() > 30) {
                    Toast.makeText(context, "Meal category name field must not be more than 30 characters long", Toast.LENGTH_SHORT).show();
                }
                else {
                    //Store valid data into temporary local variables
                    String tempMealCategoryName = inputMealCategoryName.getText().toString();

                    //Update meal category name
                    if (editModeOn) {
                        //No modifications - do nothing
                        if (tempMealCategoryName.equals(mealCategoryName)) {
                            dismiss();
                            Toast.makeText(context, mealCategoryName + " is already up to date", Toast.LENGTH_SHORT).show();
                        }
                        //Any edit text field changed - update the meal category name
                        else {
                            MealListItem item = new MealListItem(-1, tempMealCategoryName, "", 0, 0, 0, 0,"");
                            if (!DietPlan.listManager.mealCategoryIsDuplicate(workoutCategoryName, tempMealCategoryName)) {
                                DietPlan.listManager.updateMealCategory(item, workoutCategoryName, mealCategoryName);
                                //Refresh the diet plan view
                                DietPlan.adapter.refreshMealCategoryView();
                                Toast.makeText(context, mealCategoryName + " is successfully updated", Toast.LENGTH_SHORT).show();

                                //Clear the edit text fields, close the dialog, turn off the edit mode
                                inputIsValid = true;
                                editModeOn = false;
                            }
                            else {
                                Toast.makeText(context, tempMealCategoryName + " already exists", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                    //Create new meal category
                    else {
                        MealListItem item = new MealListItem(-1, tempMealCategoryName, "", 0, 0, 0,0, "");
                        if (!DietPlan.listManager.mealCategoryIsDuplicate(workoutCategoryName, tempMealCategoryName) && !DietPlan.listManager.addNewMealCategory(item)) {
                            //Refresh the diet plan view
                            DietPlan.adapter.refreshMealCategoryView();
                            Toast.makeText(context, tempMealCategoryName + " is successfully added", Toast.LENGTH_SHORT).show();

                            //Clear the edit text fields, close the dialog
                            inputIsValid = true;
                        }
                        else {
                            Toast.makeText(context, tempMealCategoryName + " already exists", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                break;
            default:
                break;
        }
        //Clear input fields and close the dialog
        if (inputIsValid) {
            inputMealCategoryName.getText().clear();
            inputIsValid = false;
            dismiss();
        }
    }
}
