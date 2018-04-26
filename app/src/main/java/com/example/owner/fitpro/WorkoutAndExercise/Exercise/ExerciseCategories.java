package com.example.owner.fitpro.WorkoutAndExercise.Exercise;

/**Created by Oleh Hodovaniuk**/

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import com.example.owner.fitpro.WorkoutAndExercise.Database.DatabaseHelper;
import com.example.owner.fitpro.R;

public class ExerciseCategories extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ArrayList<String> listOfExerciseCategories = new ArrayList<>();

        //Populate the list of exercise categories based on the table names initialised in the database
        Collections.addAll(listOfExerciseCategories, DatabaseHelper.exerciseCategoryNames);

        //Link, initialise and display the exercise categories list view
        RecyclerView recyclerView = (RecyclerView) inflater.inflate(R.layout.category, container, false);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        recyclerView.setAdapter(new ExerciseAdapter(listOfExerciseCategories));

        return recyclerView;
    }

    //Link XML layout and exercise tab logic, populates layout with data
    public class ExerciseAdapter extends RecyclerView.Adapter<ExerciseAdapter.ViewHolder> {
        private List<String> exerciseCategories;

        //Initialise exercise category list items
        ExerciseAdapter(List<String> exerciseCategories) {
            this.exerciseCategories = exerciseCategories;
        }

        //Link XML layout attributes with local variables
        class ViewHolder extends RecyclerView.ViewHolder {
            private View exerciseTabView;
            private ImageView exerciseCategoryAvatar;
            private TextView exerciseCategoryTitle;
            private TextView numberOfExercises;

            ViewHolder(View view) {
                super(view);
                exerciseTabView = view;
                exerciseCategoryAvatar = view.findViewById(R.id.exercise_avatar);
                exerciseCategoryTitle = view.findViewById(R.id.exercise_category_title);
                numberOfExercises = view.findViewById(R.id.number_of_workouts);
            }
        }

        //Inflate exercise tab view
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.exercise_tab_category, parent, false);
            return new ViewHolder(view);
        }

        //Bind exercise category data with corresponding XML layout attributes
        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            //Set exercise category list item with appropriate category avatar
            holder.exerciseCategoryAvatar.setImageResource(getExerciseCategoryAvatar(position));
            //Set exercise category list item with appropriate title
            holder.exerciseCategoryTitle.setText(exerciseCategories.get(position));

            //Count number of exercises inside the exercise category
            ExerciseListManager listManager = new ExerciseListManager(getContext());
            listManager.countNumberOfExercises(DatabaseHelper.exerciseCategoryNames, position);
            holder.numberOfExercises.setText(getNumberOfExercises(position) + " exercises");

            //Display list of exercises that belong to the exercise category clicked
            holder.exerciseTabView.setOnClickListener(view -> {
                Context context = view.getContext();
                Intent intent = new Intent(context, ExerciseCategory.class);
                intent.putExtra(ExerciseCategory.categoryName, exerciseCategories.get(position));
                context.startActivity(intent);
            });
        }

        //Return number of exercise categories
        @Override
        public int getItemCount() {
            return exerciseCategories.size();
        }

        //Return exercise category list item with appropriate category avatar
        int getExerciseCategoryAvatar(int exerciseCategoryPosition){
            if (exerciseCategoryPosition == 0) {
                return R.drawable.ic_exercise_abdominals;
            }
            else if (exerciseCategoryPosition == 1) {
                return R.drawable.ic_exercise_chest;
            }
            else if (exerciseCategoryPosition == 2) {
                return R.drawable.ic_exercise_biceps;
            }
            else if (exerciseCategoryPosition == 3) {
                return R.drawable.ic_exercise_shoulders;
            }
            else if (exerciseCategoryPosition == 4) {
                return R.drawable.ic_exercise_triceps;
            }
            else if (exerciseCategoryPosition == 5) {
                return R.drawable.ic_exercise_legs;
            }
            else if (exerciseCategoryPosition == 6) {
                return R.drawable.ic_exercise_back;
            }
            else if (exerciseCategoryPosition == 7) {
                return R.drawable.ic_exercise_traps;
            }
            else if (exerciseCategoryPosition == 8) {
                return R.drawable.ic_exercise_cardio;
            }
            else {
                return 0;
            }
        }

        //Return number of exercises that belong to particular exercise category
        long getNumberOfExercises(int exerciseCategoryPosition) {
            if (exerciseCategoryPosition == 0) {
                return ExerciseListManager.getNumberOfExercises();
            }
            else if (exerciseCategoryPosition == 1) {
                return ExerciseListManager.getNumberOfExercises();
            }
            else if (exerciseCategoryPosition == 2) {
                return ExerciseListManager.getNumberOfExercises();
            }
            else if (exerciseCategoryPosition == 3) {
                return ExerciseListManager.getNumberOfExercises();
            }
            else if (exerciseCategoryPosition == 4) {
                return ExerciseListManager.getNumberOfExercises();
            }
            else if (exerciseCategoryPosition == 5) {
                return ExerciseListManager.getNumberOfExercises();
            }
            else if (exerciseCategoryPosition == 6) {
                return ExerciseListManager.getNumberOfExercises();
            }
            else if (exerciseCategoryPosition == 7) {
                return ExerciseListManager.getNumberOfExercises();
            }
            else if (exerciseCategoryPosition == 8) {
                return ExerciseListManager.getNumberOfExercises();
            }
            else {
                return 0;
            }
        }
    }
}
