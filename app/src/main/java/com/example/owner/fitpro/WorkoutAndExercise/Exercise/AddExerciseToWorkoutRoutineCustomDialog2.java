package com.example.owner.fitpro.WorkoutAndExercise.Exercise;

/**Created by Oleh Hodovaniuk**/

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.graphics.Color;
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

public class AddExerciseToWorkoutRoutineCustomDialog2 extends Dialog implements android.view.View.OnClickListener {
    //Exercise category application context
    private Context context;
    //Workout routine name, workout routine name (target), exercise name, exercise parent category name (source)
    private String workoutCategoryName, exerciseCategoryName, workoutRoutineName, exerciseName;
    //List of workout routines names
    private String[] workoutRoutines;

    //Initialise current application context and other variables that are required to add an exercise to routine
    AddExerciseToWorkoutRoutineCustomDialog2(Context context, String workoutCategoryName, String exerciseCategoryName, String[] workoutRoutines, String exerciseName) {
        super(context);
        this.context = context;
        this.workoutCategoryName = workoutCategoryName;
        this.exerciseCategoryName = exerciseCategoryName;
        this.workoutRoutines = workoutRoutines;
        this.exerciseName = exerciseName;
    }

    //Link/connect the custom dialog XML with the class logic
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.add_exercise_to_workout_routine_custom_dialog2);

        TextView dialogTitle = findViewById(R.id.add_exercise_to_workout_dialog_title);
        dialogTitle.setText("Add to " + workoutCategoryName);
        //Dynamically adjust the height of the dialog depending on the number of workout routines
        LinearLayout layout = findViewById(R.id.addExerciseToWorkoutRoutineId);
        layout.getLayoutParams().height = (int) ((181 + (39 * (workoutRoutines.length - 1))) * context.getResources().getDisplayMetrics().density);
        layout.requestLayout();

        //Create custom radio buttons, populate the radio group dynamically
        RadioGroup radioGroup = findViewById(R.id.workout_routine);
        radioGroup.setOrientation(RadioGroup.VERTICAL);
        RadioButton[] radioButton = new RadioButton[20];
        if (workoutRoutines.length <= 20) {
            for (int i = 0; i < workoutRoutines.length; i++) {
                radioButton[i] = new RadioButton(context);
                RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(context, null);
                params.setMargins(0, 15, 0, 7);
                radioButton[i].setLayoutParams(params); //set margin
                radioButton[i].setText(workoutRoutines[i]);
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

        //Set listener on dialog buttons
        Button cancel = findViewById(R.id.cancel_button);
        Button add = findViewById(R.id.add_button);
        cancel.setOnClickListener(this);
        add.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //Close the dialog
            case R.id.cancel_button:
                dismiss();
                break;
            //Add exercise to the workout routine
            case R.id.add_button:
                //Add to the workout routine only if exercise in not a duplicate
                if (!ExerciseCategory.listManager.workoutRoutineExerciseIsDuplicate(workoutCategoryName, workoutRoutineName, exerciseName)) {
                    ExerciseCategory.listManager.addExerciseToWorkoutRoutine(exerciseCategoryName, workoutCategoryName, workoutRoutineName, exerciseName);
                    Toast.makeText(context, exerciseName + " successfully added to " + workoutRoutineName, Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(context, exerciseName + " already exists in " + workoutRoutineName, Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
        //Close the dialog
        dismiss();
    }
}
