package com.example.owner.fitpro.WorkoutAndExercise.Workout;

/**Created by Oleh Hodovaniuk**/

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.example.owner.fitpro.R;
import com.example.owner.fitpro.WorkoutAndExercise.Exercise.ExerciseListItem;
import com.example.owner.fitpro.WorkoutAndExercise.Exercise.ExerciseListManager;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BeginWorkoutCustomDialog extends Dialog implements View.OnClickListener {
    //Exercise category application context
    private Context context;
    private String workoutCategoryName, workoutRoutineName;
    private ExerciseListManager listManager;

    //Initialise current application context and other variables that are required to manage workout routine
    BeginWorkoutCustomDialog(Context context, String workoutCategoryName) {
        super(context);
        this.context = context;
        this.workoutCategoryName = workoutCategoryName;
    }

    //Link/connect the custom dialog XML with the class logic
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.add_exercise_to_workout_routine_custom_dialog2);

        TextView dialogTitle = findViewById(R.id.add_exercise_to_workout_dialog_title);
        dialogTitle.setText("Begin Workout");

        listManager = new ExerciseListManager(context);
        //Copy of all exercises that are currently within the workout category selected
        List<ExerciseListItem> workoutRoutineExercise = listManager.getListOfWorkoutCategoryExercises(workoutCategoryName);
        String[] routineNames = new String[workoutRoutineExercise.size()];
        String[] uniqueRoutineNames  = new String[workoutRoutineExercise.size()];

        //Retrieve unique routine names
        for (int j = 0; j < workoutRoutineExercise.size(); j++) {
            routineNames[j] = workoutRoutineExercise.get(j).getWorkoutRoutineName();
            uniqueRoutineNames = Arrays.stream(routineNames).distinct().toArray(String[]::new);
        }

        if (uniqueRoutineNames.length > 0) {
            //Dynamically adjust the height of the dialog depending on the number of workout routines
            LinearLayout layout = findViewById(R.id.addExerciseToWorkoutRoutineId);
            layout.getLayoutParams().height = (int) ((181 + (39 * (uniqueRoutineNames.length - 1))) * context.getResources().getDisplayMetrics().density);
            layout.requestLayout();

            //Create custom radio buttons, populate the radio group dynamically
            RadioGroup radioGroup = findViewById(R.id.workout_routine);
            radioGroup.setOrientation(RadioGroup.VERTICAL);
            RadioButton[] radioButton = new RadioButton[20];
            if (uniqueRoutineNames.length <= 20) {
                for (int i = 0; i < uniqueRoutineNames.length; i++) {
                    radioButton[i] = new RadioButton(context);
                    RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(context, null);
                    params.setMargins(0, 15, 0, 7);
                    radioButton[i].setLayoutParams(params); //set margin
                    radioButton[i].setText(uniqueRoutineNames[i]);
                    radioButton[i].setTextSize(20);
                    radioButton[i].setTextColor(Color.parseColor("#" + Integer.toHexString(ContextCompat.getColor(context, R.color.primary_white))));
                    radioGroup.addView(radioButton[i]);
                }
            }
            else {
                Toast.makeText(context, "Dialog cannot contain more than 20 workout routines", Toast.LENGTH_SHORT).show();
            }

            //Check current state of the radio buttons, save routine selected
            radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
                RadioButton checkedRadioButton = group.findViewById(checkedId);
                if (checkedRadioButton.isChecked()) {
                    workoutRoutineName = (String) checkedRadioButton.getText();
                }
            });

            //Select the default radio button
            radioButton[0].setChecked(true);
        }
        else {
            Toast.makeText(context, "Create a workout routine first", Toast.LENGTH_SHORT).show();
        }

        //Set listener on dialog buttons
        Button cancel = findViewById(R.id.cancel_button);
        Button add = findViewById(R.id.add_button);
        cancel.setOnClickListener(this);
        add.setOnClickListener(this);
    }

    @SuppressLint("Assert")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //Close the dialog
            case R.id.cancel_button:
                dismiss();
                break;
            //Add exercise to the workout routine
            case R.id.add_button:
                //Generate workout routine groups
                List<ExerciseListItem> listOfRoutines;
                List<ArrayList<ExerciseListItem>> workoutRoutineGroups = new ArrayList<>();
                listOfRoutines = listManager.getListOfWorkoutRoutineExercises(workoutCategoryName, workoutRoutineName);
                workoutRoutineGroups.add((ArrayList<ExerciseListItem>) listOfRoutines);

                ArrayList<String> exerciseNames = new ArrayList<>();
                ArrayList<Integer> exerciseSets = new ArrayList<>();
                ArrayList<Integer> exerciseTimes = new ArrayList<>();

                //Retrieve exercises from unique workout routines, store workout routine headers and exercises
                for (int i = 0; i < workoutRoutineGroups.size(); i++) {
                    for (int j = 0; j < workoutRoutineGroups.get(i).size(); j++) {
                        exerciseNames.add(workoutRoutineGroups.get(i).get(j).getExerciseName());
                        exerciseSets.add(workoutRoutineGroups.get(i).get(j).getNumberOfSets());
                        exerciseTimes.add(workoutRoutineGroups.get(i).get(j).getSetEstimateTime());
                    }
                }

                //Pass the routine exercises to workout time manager
                Intent intent = new Intent(context, WorkoutTimeManager.class);
                intent.putStringArrayListExtra("exerciseNames", exerciseNames);
                intent.putIntegerArrayListExtra("exerciseNumberOfSets", exerciseSets);
                intent.putIntegerArrayListExtra("exerciseTimes", exerciseTimes);
                context.startActivity(intent);
                break;
            default:
                break;
        }
        //Close the dialog
        dismiss();
    }
}
