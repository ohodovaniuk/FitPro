package com.example.owner.fitpro.WorkoutAndExercise.Exercise;

/**Created by Oleh Hodovaniuk**/

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.util.List;
import com.example.owner.fitpro.WorkoutAndExercise.Workout.WorkoutCategory;
import com.example.owner.fitpro.R;

public class AddNewExerciseCustomDialog extends Dialog implements android.view.View.OnClickListener {
    //Exercise category/workout routine application context
    private Context context;
    //Exercise object
    private List<ExerciseListItem> exercise;
    //Workout category exercise object
    private List<WorkoutCategory.WorkoutAdapter.WorkoutListItem> workoutRoutineExercise;
    //Workout routine name, exercise name and exercise description
    private String workoutRoutineName, exerciseCategoryName, exerciseName, description;
    //Exercise id, number of sets, reps per set, set estimate time, position
    private Integer exerciseId, numberOfSets, repsPerSet, setEstimateTime, position;
    //Exercise edit text input fields
    private EditText inputExerciseName, inputNumberOfSets, inputRepsPerSet, inputSetEstimateTime, inputDescription;
    //Description edit text background
    private TextInputLayout descriptionBackground;
    //Exercise category/workout routine edit mode status
    private boolean editModeOn, editModeOn2 = false;
    //Input validation status
    private boolean inputIsValid = false;

    //Initialise current application context
    public AddNewExerciseCustomDialog(Context context) {
        super(context);
        this.context = context;
    }

    //Set the local exercise instance variables with appropriate data (used to populate the edit text fields)
    void editExercise(String exerciseCategoryName, int position, List<ExerciseListItem> exercise, int id, String exerciseName, int numberOfSets, int repsPerSet, int setEstimateTime, String description) {
        this.exerciseCategoryName = exerciseCategoryName;
        this.position = position;
        this.exercise = exercise;
        this.exerciseId = id;
        this.exerciseName = exerciseName;
        this.numberOfSets = numberOfSets;
        this.repsPerSet = repsPerSet;
        this.setEstimateTime = setEstimateTime;
        this.description = description;
        editModeOn = true;
    }

    //Set the local workout routine exercise instance variables with appropriate data (used to populate the edit text fields)
    public void editWorkoutRoutineExercise(int position, List<WorkoutCategory.WorkoutAdapter.WorkoutListItem> workoutRoutineExercise, int id, String workoutRoutineName, String exerciseName, int numberOfSets, int repsPerSet, int setEstimateTime, String description) {
        this.position = position;
        this.workoutRoutineExercise = workoutRoutineExercise;
        this.exerciseId = id;
        this.workoutRoutineName = workoutRoutineName;
        this.exerciseName = exerciseName;
        this.numberOfSets = numberOfSets;
        this.repsPerSet = repsPerSet;
        this.setEstimateTime = setEstimateTime;
        this.description = description;
        editModeOn2 = true;
    }

    //Link/connect the custom dialog XML with the class logic
    //Populate the layout with current exercise object data if edit mode is on
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.add_new_exercise_custom_dialog);

        TextView dialogTitle = findViewById(R.id.add_new_exercise_dialog_title);
        dialogTitle.setText(R.string.add_new_exercise);
        inputExerciseName = findViewById(R.id.exercise_name);
        inputNumberOfSets = findViewById(R.id.number_of_sets);
        inputRepsPerSet = findViewById(R.id.reps_per_set);
        inputSetEstimateTime = findViewById(R.id.set_estimate_time);
        inputDescription = findViewById(R.id.description);
        descriptionBackground = findViewById(R.id.description_background);

        //Change description edit text background border color
        inputDescription.setOnFocusChangeListener((view, hasfocus) -> {
            if (hasfocus) {
                descriptionBackground.setBackgroundResource(R.drawable.add_new_exercise_description_border_yellow);
            }
            else {
                descriptionBackground.setBackgroundResource(R.drawable.add_new_exercise_description_border_white);
            }
        });

        //Set listener on dialog buttons
        Button cancel = findViewById(R.id.cancel_button);
        Button add = findViewById(R.id.add_button);
        cancel.setOnClickListener(this);
        add.setOnClickListener(this);

        //Populate the dialog when edit mode is on
        if (editModeOn || editModeOn2) {
            dialogTitle.setText(R.string.edit_exercise);
            inputExerciseName.setText(exerciseName);
            inputNumberOfSets.setText(numberOfSets.toString());
            inputRepsPerSet.setText(repsPerSet.toString());
            inputSetEstimateTime.setText(setEstimateTime.toString());
            inputDescription.setText(description);
        }
    }

    //Listen to the custom dialog fields and buttons, responds to dialog actions with class logic
    //Read from the custom dialog edit fields if normal mode is on
    //Validate the edit text fields according to the rules, stores the input into appropriate exercise object
    //Edit the existing exercise information if edit mode is on, saves the modifications
    //Clear input fields junk data after the dialog has done its work
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //Clear input fields and close the dialog
            case R.id.cancel_button:
                inputExerciseName.getText().clear();
                inputNumberOfSets.getText().clear();
                inputRepsPerSet.getText().clear();
                inputSetEstimateTime.getText().clear();
                inputDescription.getText().clear();
                inputIsValid = false;
                dismiss();
                break;
            //Read, validate and store the new/existing exercise object data
            case R.id.add_button:
                if (inputExerciseName.getText().toString().isEmpty()) {
                    Toast.makeText(context, "Exercise name field is empty", Toast.LENGTH_SHORT).show();
                }
                else if (!inputExerciseName.getText().toString().matches("[a-zA-Z0-9\\s]+")) {
                    Toast.makeText(context, "Exercise name field must contain characters and digits only", Toast.LENGTH_SHORT).show();
                }
                else if (inputExerciseName.getText().toString().length() > 30) {
                    Toast.makeText(context, "Exercise name field must not be more than 30 characters long", Toast.LENGTH_SHORT).show();
                }
                else {
                    if (inputNumberOfSets.getText().toString().isEmpty()) {
                        Toast.makeText(context, "Number of sets field is empty", Toast.LENGTH_SHORT).show();
                    }
                    else if (!inputNumberOfSets.getText().toString().matches("[0-9]+")) {
                        Toast.makeText(context, "Number of sets field must contain positive digits only", Toast.LENGTH_SHORT).show();
                    }
                    else if (Integer.parseInt(inputNumberOfSets.getText().toString()) < 1) {
                        Toast.makeText(context, "Number of sets field cannot be less than 1", Toast.LENGTH_SHORT).show();
                    }
                    else if (Integer.parseInt(inputNumberOfSets.getText().toString()) > 100) {
                        Toast.makeText(context, "Number of sets field cannot be more than 100", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        if (inputRepsPerSet.getText().toString().isEmpty()){
                            Toast.makeText(context, "Reps per set field is empty", Toast.LENGTH_SHORT).show();
                        }
                        else if (!inputRepsPerSet.getText().toString().matches("[0-9]+")) {
                            Toast.makeText(context, "Reps per set field must contain positive digits only", Toast.LENGTH_SHORT).show();
                        }
                        else if (Integer.parseInt(inputRepsPerSet.getText().toString()) < 1) {
                            Toast.makeText(context, "Reps per set field cannot be less than 1", Toast.LENGTH_SHORT).show();
                        }
                        else if (Integer.parseInt(inputRepsPerSet.getText().toString()) > 1000) {
                            Toast.makeText(context, "Reps per set field cannot be more than 1000", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            if (inputSetEstimateTime.getText().toString().isEmpty()) {
                                Toast.makeText(context, "Estimate time field is empty", Toast.LENGTH_SHORT).show();
                            }
                            else if (!inputSetEstimateTime.getText().toString().matches("[0-9]+")) {
                                Toast.makeText(context, "Estimate time field must contain positive digits only", Toast.LENGTH_SHORT).show();
                            }
                            else if (Integer.parseInt(inputSetEstimateTime.getText().toString()) < 10) {
                                Toast.makeText(context, "Set estimate time field cannot be less than 10 seconds long", Toast.LENGTH_SHORT).show();
                            }
                            else if (Integer.parseInt(inputSetEstimateTime.getText().toString()) > 3600) {
                                Toast.makeText(context, "Set estimate time field cannot be more than 3600 seconds long", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                if (inputDescription.getText().toString().isEmpty()) {
                                    Toast.makeText(context, "Description field is empty", Toast.LENGTH_SHORT).show();
                                }
                                else if (inputDescription.getText().toString().length() >= 500) {
                                    Toast.makeText(context, "Description field must not be more than 500 characters long", Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    //Store valid data into temporary local variables
                                    String tempExerciseName = inputExerciseName.getText().toString();
                                    int tempNumberOfSets = Integer.parseInt(inputNumberOfSets.getText().toString());
                                    int tempRepsPerSet = Integer.parseInt(inputRepsPerSet.getText().toString());
                                    int tempSetEstimateTime = Integer.parseInt(inputSetEstimateTime.getText().toString());
                                    String tempDescription = inputDescription.getText().toString();

                                    //Update the exercise category by recreating the existing exercise object (remove, create)
                                    if (editModeOn) {
                                        //No modifications - do nothing
                                        if (tempExerciseName.equals(exerciseName) && tempNumberOfSets == numberOfSets && tempRepsPerSet == repsPerSet && tempSetEstimateTime == setEstimateTime && tempDescription.equals(description)) {
                                            dismiss();
                                            Toast.makeText(context, exerciseName + " is already up to date", Toast.LENGTH_SHORT).show();
                                        }
                                        //Any edit text field changed - update the exercise object
                                        else {
                                            //Recreate the exercise object and store the modifications into the database
                                            //If the exercise name has changed then check if it is a duplicate, otherwise just update the exercise details
                                            if(!tempExerciseName.equals(exerciseName)) {
                                                if (!ExerciseCategory.listManager.exerciseIsDuplicate(tempExerciseName)) {
                                                    ExerciseListItem item = new ExerciseListItem(-1, tempExerciseName, tempNumberOfSets, tempRepsPerSet, tempSetEstimateTime, tempDescription);
                                                    ExerciseCategory.listManager.addNewExercise(item);
                                                    //Delete the old copy of the exercise
                                                    if (ExerciseCategory.listManager.deleteExercise(exerciseId)) {
                                                        exercise.remove(exercise.get(position));
                                                        ExerciseCategory.adapter.notifyDataSetChanged();
                                                    }
                                                    else {
                                                        Toast.makeText(context, "Something went wrong. Try again", Toast.LENGTH_SHORT).show();
                                                    }

                                                    //Refresh the exercise category view
                                                    ExerciseCategory.adapter.refreshExerciseItem(ExerciseCategory.listManager.getListOfExerciseCategoryExercises(ExerciseCategory.categoryName));
                                                    Toast.makeText(context, exerciseName + " is successfully updated", Toast.LENGTH_SHORT).show();

                                                    //Clear edit text fields, close the dialog, turn off the edit mode
                                                    inputIsValid = true;
                                                    editModeOn = false;
                                                } else {
                                                    Toast.makeText(context, tempExerciseName + " already exists", Toast.LENGTH_SHORT).show();
                                                    inputIsValid = false;
                                                }
                                            }
                                            else {
                                                ExerciseListItem item = new ExerciseListItem(-1, tempExerciseName, tempNumberOfSets, tempRepsPerSet, tempSetEstimateTime, tempDescription);
                                                ExerciseCategory.listManager.addNewExercise(item);
                                                //Delete the old copy of the exercise
                                                if (ExerciseCategory.listManager.deleteExercise(exerciseId)) {
                                                    exercise.remove(exercise.get(position));
                                                    ExerciseCategory.adapter.notifyDataSetChanged();
                                                } else {
                                                    Toast.makeText(context, "Something went wrong. Try again", Toast.LENGTH_SHORT).show();
                                                }

                                                //Refresh the exercise category view
                                                ExerciseCategory.adapter.refreshExerciseItem(ExerciseCategory.listManager.getListOfExerciseCategoryExercises(ExerciseCategory.categoryName));
                                                Toast.makeText(context, exerciseName + " is successfully updated", Toast.LENGTH_SHORT).show();

                                                //Clear edit text fields, close the dialog, turn off the edit mode
                                                inputIsValid = true;
                                                editModeOn = false;
                                            }
                                        }
                                    }
                                    //Update the workout routine exercise by recreating the existing exercise object (remove, create)
                                    else if (editModeOn2) {
                                        //No modifications - do nothing
                                        if (tempExerciseName.equals(exerciseName) && tempNumberOfSets == numberOfSets && tempRepsPerSet == repsPerSet && tempSetEstimateTime == setEstimateTime && tempDescription.equals(description)) {
                                            dismiss();
                                            Toast.makeText(context, exerciseName + " is already up to date", Toast.LENGTH_SHORT).show();
                                        }
                                        //Any edit text field changed - update the exercise object
                                        else {
                                            //Recreate the workout routine exercise object and store the modifications into the database
                                            //If the exercise name has changed then check if it is a duplicate, otherwise just update the exercise details
                                            if(!tempExerciseName.equals(exerciseName)) {
                                                if (!WorkoutCategory.listManager.workoutRoutineExerciseIsDuplicate(workoutRoutineName, tempExerciseName)) {
                                                    ExerciseListItem item = new ExerciseListItem(-1, workoutRoutineName, tempExerciseName, tempNumberOfSets, tempRepsPerSet, tempSetEstimateTime, tempDescription);
                                                    WorkoutCategory.listManager.addNewWorkoutRoutine(item);
                                                    //Delete the old copy of the exercise
                                                    if (WorkoutCategory.listManager.deleteExercise(exerciseId)) {
                                                        workoutRoutineExercise.remove(workoutRoutineExercise.get(position));
                                                        WorkoutCategory.adapter.notifyDataSetChanged();
                                                    } else {
                                                        Toast.makeText(context, "Something went wrong. Try again", Toast.LENGTH_SHORT).show();
                                                    }

                                                    //Refresh the workout routine view
                                                    WorkoutCategory.adapter.refreshWorkoutCategoryView();
                                                    Toast.makeText(context, exerciseName + " is successfully updated", Toast.LENGTH_SHORT).show();

                                                    //Clear the edit text fields, close the dialog, turn off the edit mode
                                                    inputIsValid = true;
                                                    editModeOn = false;
                                                } else {
                                                    Toast.makeText(context, tempExerciseName + " already exists", Toast.LENGTH_SHORT).show();
                                                    inputIsValid = false;
                                                }
                                            }
                                            else {
                                                ExerciseListItem item = new ExerciseListItem(-1, workoutRoutineName, tempExerciseName, tempNumberOfSets, tempRepsPerSet, tempSetEstimateTime, tempDescription);
                                                WorkoutCategory.listManager.addNewWorkoutRoutine(item);
                                                //Delete the old copy of the exercise
                                                if (WorkoutCategory.listManager.deleteExercise(exerciseId)) {
                                                    workoutRoutineExercise.remove(workoutRoutineExercise.get(position));
                                                    WorkoutCategory.adapter.notifyDataSetChanged();
                                                } else {
                                                    Toast.makeText(context, "Something went wrong. Try again", Toast.LENGTH_SHORT).show();
                                                }

                                                //Refresh the workout routine view
                                                WorkoutCategory.adapter.refreshWorkoutCategoryView();
                                                Toast.makeText(context, exerciseName + " is successfully updated", Toast.LENGTH_SHORT).show();

                                                //Clear the edit text fields, close the dialog, turn off the edit mode
                                                inputIsValid = true;
                                                editModeOn = false;
                                            }
                                        }
                                    }
                                    //Create new exercise object and save it into the database
                                    else {
                                        if (!ExerciseCategory.listManager.exerciseIsDuplicate(tempExerciseName)) {
                                            ExerciseListItem item = new ExerciseListItem(-1, tempExerciseName, tempNumberOfSets, tempRepsPerSet, tempSetEstimateTime, tempDescription);
                                            ExerciseCategory.listManager.addNewExercise(item);
                                            //Refresh the exercise category view
                                            ExerciseCategory.adapter.refreshExerciseItem(ExerciseCategory.listManager.getListOfExerciseCategoryExercises(ExerciseCategory.categoryName));
                                            Toast.makeText(context, tempExerciseName + " is successfully added", Toast.LENGTH_SHORT).show();
                                            inputIsValid = true;
                                        }
                                        else {
                                            Toast.makeText(context, tempExerciseName + " already exists", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                break;
            default:
                break;
        }
        //Clear input fields and close the dialog
        if (inputIsValid) {
            inputExerciseName.getText().clear();
            inputNumberOfSets.getText().clear();
            inputRepsPerSet.getText().clear();
            inputSetEstimateTime.getText().clear();
            inputDescription.getText().clear();
            inputIsValid = false;
            dismiss();
        }
    }
}
