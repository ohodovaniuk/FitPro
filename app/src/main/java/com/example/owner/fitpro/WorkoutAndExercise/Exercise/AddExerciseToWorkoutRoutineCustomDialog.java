package com.example.owner.fitpro.WorkoutAndExercise.Exercise;

/**Created by Oleh Hodovaniuk**/

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import java.util.Arrays;
import java.util.List;
import com.example.owner.fitpro.R;

public class AddExerciseToWorkoutRoutineCustomDialog extends Dialog implements android.view.View.OnClickListener {
    //Exercise category application context
    private Context context;
    //Workout category name, exercise parent category name, exercise name
    private String workoutCategoryName, exerciseCategoryName, exerciseName;

    //Initialise current application context and exercise parent category name
    AddExerciseToWorkoutRoutineCustomDialog(Context context, String exerciseCategoryName) {
        super(context);
        this.context = context;
        this.exerciseCategoryName = exerciseCategoryName;
    }

    //Link/connect the custom dialog XML with the class logic
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.add_exercise_to_workout_routine_custom_dialog);

        RadioGroup radioGroup = findViewById(R.id.workout_days);
        RadioButton defaultRadioButton = findViewById(R.id.monday);
        defaultRadioButton.setChecked(true);
        workoutCategoryName = (String) defaultRadioButton.getText();

        //Check the current state of the radio buttons, save day selected
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton checkedRadioButton = group.findViewById(checkedId);
            if (checkedRadioButton.isChecked()) {
                workoutCategoryName = (String) checkedRadioButton.getText();
            }
        });

        //Set listener on dialog buttons
        Button cancel = findViewById(R.id.cancel_button);
        Button next = findViewById(R.id.next_button);
        cancel.setOnClickListener(this);
        next.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //Close the dialog
            case R.id.cancel_button:
                dismiss();
                break;
            //Prepare exercise object data, pass the data to the second dialog (select routine dialog)
            case R.id.next_button:
                ExerciseListManager listManager = new ExerciseListManager(context);
                //Copy of all exercises that are currently within the workout category selected
                List<ExerciseListItem> workoutRoutineExercise = listManager.getListOfWorkoutCategoryExercises(workoutCategoryName);
                String[] routineNames = new String[workoutRoutineExercise.size()];
                String[] uniqueRoutineNames  = new String[workoutRoutineExercise.size()];

                //Retrieve unique routine names
                for (int j = 0; j < workoutRoutineExercise.size(); j++) {
                    routineNames[j] = workoutRoutineExercise.get(j).getWorkoutRoutineName();
                    uniqueRoutineNames = Arrays.stream(routineNames).distinct().toArray(String[]::new);
                }

                //Display second dialog only if at least one routine exist inside the workout day
                //Pass required data for further processing
                if (uniqueRoutineNames.length > 0) {
                    AddExerciseToWorkoutRoutineCustomDialog2 addExerciseToWorkoutCustomDialog2 = new AddExerciseToWorkoutRoutineCustomDialog2(context, workoutCategoryName, exerciseCategoryName, uniqueRoutineNames, exerciseName);
                    addExerciseToWorkoutCustomDialog2.show();
                }
                else {
                    Toast.makeText(context, "Create a workout routine first", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
        //Close the dialog
        dismiss();
    }

    //Set current exercise name
    void setExerciseName(final String name) {
        exerciseName = name;
    }
}
