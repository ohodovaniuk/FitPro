package com.example.owner.fitpro.WorkoutAndExercise.Workout;

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
import com.example.owner.fitpro.WorkoutAndExercise.Exercise.ExerciseListItem;
import com.example.owner.fitpro.R;

public class AddNewWorkoutRoutineCustomDialog extends Dialog implements View.OnClickListener {
    //Workout routine application context
    private Context context;
    //Workout routine name, workout category name (parent table)
    private String workoutCategoryName, workoutRoutineName;
    //Workout routine name edit text field
    private EditText inputWorkoutRoutineName;
    //Workout routine edit mode status
    private boolean editModeOn = false;
    //Input validation status
    private boolean inputIsValid = false;

    //Initialise current application context
    AddNewWorkoutRoutineCustomDialog(Context context) {
        super(context);
        this.context = context;
    }

    //Set local workout routine instance variables with appropriate data (used to populate the edit text field)
    void editWorkoutRoutine(String workoutRoutineName, String workoutCategoryName) {
        this.workoutRoutineName = workoutRoutineName;
        this.workoutCategoryName = workoutCategoryName;
        editModeOn = true;
    }

    //Link/connect the custom dialog XML with the class logic
    //Populates the layout with data if edit mode is on
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.add_new_workout_routine_custom_dialog);

        //Set the  custom dialog Title name
        TextView dialogTitle = findViewById(R.id.add_new_workout_routine_dialog_title);
        dialogTitle.setText(R.string.add_new_workout_routine);
        inputWorkoutRoutineName = findViewById(R.id.workout_routine_name);
        //Refresh the view and close any other expanding, swiping ... effects.
        WorkoutCategory.adapter.refreshWorkoutCategoryView();

        //Set listener on dialog buttons
        Button cancel = findViewById(R.id.cancel_button);
        Button add = findViewById(R.id.add_button);
        cancel.setOnClickListener(this);
        add.setOnClickListener(this);

        //Populate the dialog when edit mode is on
        if (editModeOn) {
            dialogTitle.setText(R.string.edit_workout_routine);
            inputWorkoutRoutineName.setText(workoutRoutineName);
        }
    }

    //Listen to the custom dialog field and buttons, responds to dialog actions with class logic
    //Read from the custom dialog edit field if normal mode is on
    //Validate the edit text field according to the rules, stores the input into appropriate workout routine object
    //Edit the existing workout routine information if edit mode is on, saves the modifications
    //Clear input field junk data after the dialog has done its work
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //Clear input fields and close the dialog
            case R.id.cancel_button:
                inputWorkoutRoutineName.getText().clear();
                inputIsValid = false;
                dismiss();
                break;
            //Read, validate and store the new/existing workout routine object data
            case R.id.add_button:
                if (inputWorkoutRoutineName.getText().toString().isEmpty()) {
                    Toast.makeText(context, "Routine name field is empty", Toast.LENGTH_SHORT).show();
                }
                else if (!inputWorkoutRoutineName.getText().toString().matches("[a-zA-Z0-9\\s]+")) {
                    Toast.makeText(context, "Routine name field must contain characters and digits only", Toast.LENGTH_SHORT).show();
                }
                else if (inputWorkoutRoutineName.getText().toString().length() > 30) {
                    Toast.makeText(context, "Routine name field must not be more than 30 characters long", Toast.LENGTH_SHORT).show();
                }
                else {
                    //Store valid data into temporary local variables
                    String tempRoutineName = inputWorkoutRoutineName.getText().toString();

                    //Update the workout routine
                    if (editModeOn) {
                        //No modifications - do nothing
                        if (tempRoutineName.equals(workoutRoutineName)) {
                            dismiss();
                            Toast.makeText(context, workoutRoutineName + " is already up to date", Toast.LENGTH_SHORT).show();
                        }
                        //Any edit text field changed - update the workout routine object
                        else {
                            ExerciseListItem item = new ExerciseListItem(-1, tempRoutineName, "", 0, 0, 0, "");
                            if (!WorkoutCategory.listManager.workoutRoutineIsDuplicate(tempRoutineName)) {
                                WorkoutCategory.listManager.updateWorkoutRoutine(item, workoutCategoryName, workoutRoutineName);
                                //Refresh the workout routine view
                                WorkoutCategory.adapter.refreshWorkoutCategoryView();
                                Toast.makeText(context, workoutRoutineName + " is successfully updated", Toast.LENGTH_SHORT).show();

                                //Clear the edit text fields, close the dialog, turn off the edit mode
                                inputIsValid = true;
                                editModeOn = false;
                            }
                            else {
                                Toast.makeText(context, tempRoutineName + " already exists", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                    //Create new workout routine
                    else {
                        ExerciseListItem item = new ExerciseListItem(-1, tempRoutineName, "", 0, 0, 0, "");
                        if (!WorkoutCategory.listManager.workoutRoutineIsDuplicate(tempRoutineName)) {
                            WorkoutCategory.listManager.addNewWorkoutRoutine(item);
                            //Refresh the workout routine view
                            WorkoutCategory.adapter.refreshWorkoutCategoryView();
                            Toast.makeText(context, tempRoutineName + " is successfully added", Toast.LENGTH_SHORT).show();

                            //Clear the edit text fields, close the dialog
                            inputIsValid = true;
                        }
                        else {
                            Toast.makeText(context, tempRoutineName + " already exists", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                break;
            default:
                break;
        }
        //Clear input fields and close the dialog
        if (inputIsValid) {
            inputWorkoutRoutineName.getText().clear();
            inputIsValid = false;
            dismiss();
        }
    }
}
