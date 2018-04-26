package com.example.owner.fitpro.WorkoutAndExercise.Workout;

/**Created by Oleh Hodovaniuk**/

import android.annotation.SuppressLint;
import com.example.owner.fitpro.WorkoutAndExercise.Database.DatabaseHelper;
import com.example.owner.fitpro.WorkoutAndExercise.Exercise.ExerciseListManager;
import com.example.owner.fitpro.R;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WorkoutCategories extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ArrayList<String> listOfWorkoutCategories = new ArrayList<>(); //list of main workout categories inside the workout tab

        //Populate the list of workout categories based on the table names initialised in the database
        Collections.addAll(listOfWorkoutCategories, DatabaseHelper.workoutCategoryNames);

        //Link, initialise and display the workout categories list view
        RecyclerView recyclerView = (RecyclerView) inflater.inflate(R.layout.category, container, false);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        recyclerView.setAdapter(new WorkoutAdapter(listOfWorkoutCategories));

        return recyclerView;
    }

    //Link XML layout and workout tab logic, populates layout with data
    public class WorkoutAdapter extends RecyclerView.Adapter<WorkoutAdapter.ViewHolder> {
        private List<String> workoutCategories;

        //Initialise workout category list items
        WorkoutAdapter(List<String> workoutCategories) {
            this.workoutCategories = workoutCategories;
        }

        //Link XML layout attributes with local variables
        class ViewHolder extends RecyclerView.ViewHolder {
            private View workoutCategoryItemView;
            private TextView workoutCategoryTitle;
            private TextView numberOfWorkouts;

            ViewHolder(View view) {
                super(view);
                workoutCategoryItemView = view;
                workoutCategoryTitle = view.findViewById(R.id.workout_category_title);
                numberOfWorkouts = view.findViewById(R.id.number_of_workouts);
            }
        }

        //Inflate workout tab view
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.workout_tab_category, parent, false);
            return new ViewHolder(view);
        }

        //Bind workout category data with corresponding XML layout attributes
        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            //Set workout category list item with appropriate title
            holder.workoutCategoryTitle.setText(workoutCategories.get(position));

            //Count number of workout routines inside the workout category
            ExerciseListManager listManager = new ExerciseListManager(getContext());
            listManager.countNumberOfWorkoutRoutines(DatabaseHelper.workoutCategoryNames, position);
            holder.numberOfWorkouts.setText(getNumberOfWorkoutRoutines(position) + " workouts");

            //Display list of workout routines that belong to the workout category clicked
            holder.workoutCategoryItemView.setOnClickListener(view -> {
                Context context = view.getContext();
                Intent intent = new Intent(context, WorkoutCategory.class);
                intent.putExtra(WorkoutCategory.categoryName, workoutCategories.get(position));
                context.startActivity(intent);
            });
        }

        //Return number of workout categories
        @Override
        public int getItemCount() {
            return workoutCategories.size();
        }

        //Return number of workout routines that belong to particular workout category
        long  getNumberOfWorkoutRoutines(int workoutCategoryPosition) {
            if (workoutCategoryPosition == 0) {
                return ExerciseListManager.getNumberOfWorkoutRoutines();
            }
            else if (workoutCategoryPosition == 1) {
                return ExerciseListManager.getNumberOfWorkoutRoutines();
            }
            else if (workoutCategoryPosition == 2) {
                return ExerciseListManager.getNumberOfWorkoutRoutines();
            }
            else if (workoutCategoryPosition == 3) {
                return ExerciseListManager.getNumberOfWorkoutRoutines();
            }
            else if (workoutCategoryPosition == 4) {
                return ExerciseListManager.getNumberOfWorkoutRoutines();
            }
            else if (workoutCategoryPosition == 5) {
                return ExerciseListManager.getNumberOfWorkoutRoutines();
            }
            else if (workoutCategoryPosition == 6) {
                return ExerciseListManager.getNumberOfWorkoutRoutines();
            }
            else if (workoutCategoryPosition == 7) {
                return ExerciseListManager.getNumberOfWorkoutRoutines();
            }
            else if (workoutCategoryPosition== 8) {
                return ExerciseListManager.getNumberOfWorkoutRoutines();
            }
            else {
                return 0;
            }
        }
    }
}
