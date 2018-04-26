package com.example.owner.fitpro.WorkoutAndExercise.Exercise;

/**Created by Oleh Hodovaniuk**/

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;
import com.example.owner.fitpro.WorkoutAndExercise.Database.DatabaseHelper;
import com.google.firebase.database.DatabaseReference;

public class ExerciseListManager {
    private DatabaseHelper SQLLiteDatabaseHelper;
    private static long numberOfExercises, numberOfWorkoutRoutines;

    //Initialise the database
    public ExerciseListManager(Context context) {
        SQLLiteDatabaseHelper = DatabaseHelper.getInstance(context);
    }

    //Retrieve and return complete/full listing of exercises that exist within the exercise category
    public List<ExerciseListItem> getListOfExerciseCategoryExercises(String table) {
        SQLiteDatabase db = SQLLiteDatabaseHelper.getReadableDatabase();
        DatabaseHelper.table = table;
        Cursor cursor = null;
        List<ExerciseListItem> items = new ArrayList<>();

        try {
            cursor = db.rawQuery("SELECT * FROM " + DatabaseHelper.table,null);
        }
        catch(SQLException e) {
            Log.d("SQL exception caught in: ", "Exercise List Manager getListOfExerciseCategoryExercises method");
        }

        assert cursor != null;
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                ExerciseListItem item = new ExerciseListItem(
                    cursor.getInt(cursor.getColumnIndex("_id")),
                    cursor.getString(cursor.getColumnIndex("exercise_name")),
                    cursor.getInt(cursor.getColumnIndex("number_of_sets")),
                    cursor.getInt(cursor.getColumnIndex("reps_per_set")),
                    cursor.getInt(cursor.getColumnIndex("estimate_time")),
                    cursor.getString(cursor.getColumnIndex("description"))
                );
                items.add(item);
                cursor.moveToNext();
            }
        }

        cursor.close();
        return items;
    }

    //Retrieve and return complete/full listing of exercises that exist within the workout category
    public List<ExerciseListItem> getListOfWorkoutCategoryExercises(String table) {
        SQLiteDatabase db = SQLLiteDatabaseHelper.getReadableDatabase();
        DatabaseHelper.table = table;
        Cursor cursor = null;
        List<ExerciseListItem> items = new ArrayList<>();

        try {
            cursor = db.rawQuery("SELECT * FROM " + DatabaseHelper.table,null);
        }
        catch(SQLException e) {
            Log.d("SQL exception caught in: ", "Exercise List Manager getListOfWorkoutCategoryExercises method");
        }

        assert cursor != null;
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                ExerciseListItem item = new ExerciseListItem(
                    cursor.getInt(cursor.getColumnIndex("_id")),
                    cursor.getString(cursor.getColumnIndex("workout_routine_name")),
                    cursor.getString(cursor.getColumnIndex("exercise_name")),
                    cursor.getInt(cursor.getColumnIndex("number_of_sets")),
                    cursor.getInt(cursor.getColumnIndex("reps_per_set")),
                    cursor.getInt(cursor.getColumnIndex("estimate_time")),
                    cursor.getString(cursor.getColumnIndex("description"))
                );
                items.add(item);
                cursor.moveToNext();
            }
        }

        cursor.close();
        return items;
    }

    //Retrieve and return complete/full listing of exercises that exist within the workout routine
    public List<ExerciseListItem> getListOfWorkoutRoutineExercises(String table, String workoutRoutineName) {
        SQLiteDatabase db = SQLLiteDatabaseHelper.getReadableDatabase();
        DatabaseHelper.table = table;
        Cursor cursor = null;
        List<ExerciseListItem> items = new ArrayList<>();

        try {
            cursor = db.rawQuery("SELECT * FROM " + DatabaseHelper.table  + " WHERE workout_routine_name = ?", new String[] {String.valueOf(workoutRoutineName)});
        }
        catch(SQLException e) {
            Log.d("SQL exception caught in: ", "Exercise List Manager getListOfWorkoutRoutineExercises method");
        }

        assert cursor != null;
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                ExerciseListItem item = new ExerciseListItem(
                    cursor.getInt(cursor.getColumnIndex("_id")),
                    cursor.getString(cursor.getColumnIndex("workout_routine_name")),
                    cursor.getString(cursor.getColumnIndex("exercise_name")),
                    cursor.getInt(cursor.getColumnIndex("number_of_sets")),
                    cursor.getInt(cursor.getColumnIndex("reps_per_set")),
                    cursor.getInt(cursor.getColumnIndex("estimate_time")),
                    cursor.getString(cursor.getColumnIndex("description"))
                );
                items.add(item);
                cursor.moveToNext();
            }
        }

        cursor.close();
        return items;
    }

    //Create new exercise if the exercise to create is not a duplicate
    public void addNewExercise(ExerciseListItem item) {
        SQLiteDatabase db = SQLLiteDatabaseHelper.getWritableDatabase();
        ContentValues newItem = new ContentValues();
        newItem.put("exercise_name", item.getExerciseName());
        newItem.put("number_of_sets", item.getNumberOfSets());
        newItem.put("reps_per_set", item.getRepsPerSet());
        newItem.put("estimate_time", item.getSetEstimateTime());
        newItem.put("description", item.getDescription());

        try {
            db.insertOrThrow(DatabaseHelper.table, null, newItem);
        }
        catch(SQLException e) {
            Log.d("SQL exception caught in: ", "Exercise List Manager addNewExercise method");
        }
    }

    //Create new workout routine if the routine to create is not a duplicate
    public void addNewWorkoutRoutine(ExerciseListItem item) {
        SQLiteDatabase db = SQLLiteDatabaseHelper.getWritableDatabase();
        ContentValues newItem = new ContentValues();
        newItem.put("workout_routine_name", item.getWorkoutRoutineName());
        newItem.put("exercise_name", item.getExerciseName());
        newItem.put("number_of_sets", item.getNumberOfSets());
        newItem.put("reps_per_set", item.getRepsPerSet());
        newItem.put("estimate_time", item.getSetEstimateTime());
        newItem.put("description", item.getDescription());

        try {
            db.insertOrThrow(DatabaseHelper.table, null, newItem);
        }
        catch(SQLException e) {
            Log.d("SQL exception caught in: ", "Exercise List Manager addNewWorkoutRoutine method");
        }
    }

    //Copy an appropriate exercise to the workout routine if the exercise to add is not a duplicate
    public void addExerciseToWorkoutRoutine(String sourceTable, String targetTable, String workoutRoutineName, String exerciseName) {
        SQLiteDatabase db = SQLLiteDatabaseHelper.getWritableDatabase();
        Cursor cursor = null;
        ContentValues newItem = new ContentValues();

        try {
            cursor = db.rawQuery("SELECT * FROM " + sourceTable + " WHERE exercise_name = ?", new String[] {String.valueOf(exerciseName)});
        }
        catch(SQLException e) {
            Log.d("SQL exception caught in: ", "Exercise List Manager addExerciseToWorkoutRoutine method");
        }

        assert cursor != null;
        while(cursor.moveToNext()) {
            newItem.put("workout_routine_name", workoutRoutineName);
            newItem.put("exercise_name", cursor.getString(cursor.getColumnIndex("exercise_name")));
            newItem.put("number_of_sets", cursor.getInt(cursor.getColumnIndex("number_of_sets")));
            newItem.put("reps_per_set", cursor.getInt(cursor.getColumnIndex("reps_per_set")));
            newItem.put("estimate_time", cursor.getInt(cursor.getColumnIndex("estimate_time")));
            newItem.put("description", cursor.getString(cursor.getColumnIndex("description")));
        }

        try {
            db.insertOrThrow(targetTable, null, newItem);
        }
        catch(SQLException e) {
            return;
        }

        cursor.close();
    }

    //Update workout routine name
    public void updateWorkoutRoutine(ExerciseListItem item, String table, String workoutRoutineName) {
        SQLiteDatabase db = SQLLiteDatabaseHelper.getWritableDatabase();
        ContentValues editItem = new ContentValues();
        editItem.put("workout_routine_name", item.getWorkoutRoutineName());

        try {
            db.update(DatabaseHelper.table, editItem, "workout_routine_name = ?", new String[] {String.valueOf(workoutRoutineName)});
        }
        catch(SQLException e) {
            Log.d("SQL exception caught in: ", "Exercise List Manager updateWorkoutRoutine method");
        }
    }

    //Delete an exercise and its content
    public boolean deleteExercise(long id) {
        SQLiteDatabase db = SQLLiteDatabaseHelper.getWritableDatabase();

        try {
            db.delete(DatabaseHelper.table, "_id = ?", new String[] {String.valueOf(id)});
        }
        catch(SQLException e) {
            return false;
        }

        return true;
    }

    //Delete workout routine and its content (exercises)
    public boolean deleteWorkoutRoutine(String workoutRoutineName) {
        SQLiteDatabase db = SQLLiteDatabaseHelper.getWritableDatabase();

        try {
            db.delete(DatabaseHelper.table, "workout_routine_name = ?", new String[] {String.valueOf(workoutRoutineName)});
        }
        catch(SQLException e) {
            return false;
        }

        return true;
    }

    //Check if the workout routine already exists inside the workout category
    public boolean workoutRoutineIsDuplicate(String workoutRoutineName) {
        SQLiteDatabase db = SQLLiteDatabaseHelper.getReadableDatabase();

        @SuppressLint("Recycle") Cursor cursor = db.rawQuery("SELECT * FROM " + DatabaseHelper.table + " WHERE workout_routine_name = ?", new String[] {String.valueOf(workoutRoutineName)});

        return cursor.getCount() > 0;
    }

    //Check if the exercise already exists inside the exercise category
    public boolean exerciseIsDuplicate(String exerciseName) {
        SQLiteDatabase db = SQLLiteDatabaseHelper.getReadableDatabase();

        @SuppressLint("Recycle") Cursor cursor = db.rawQuery("SELECT * FROM " + DatabaseHelper.table + " WHERE exercise_name = ?", new String[] {String.valueOf(exerciseName)});

        return cursor.getCount() > 0;
    }

    //Check if the exercise already exists inside the workout routine - 2 parameters
    public boolean workoutRoutineExerciseIsDuplicate(String workoutRoutineName, String exerciseName) {
        SQLiteDatabase db = SQLLiteDatabaseHelper.getReadableDatabase();

        @SuppressLint("Recycle") Cursor cursor = db.rawQuery("SELECT * FROM " + DatabaseHelper.table + " WHERE workout_routine_name = ? AND exercise_name = ?", new String[] {String.valueOf(workoutRoutineName), String.valueOf(exerciseName)});

        return cursor.getCount() > 0;
    }

    //Check if the exercise already exists inside the workout routine - 3 parameters
    public boolean workoutRoutineExerciseIsDuplicate(String table, String workoutRoutineName, String exerciseName) {
        SQLiteDatabase db = SQLLiteDatabaseHelper.getReadableDatabase();
        DatabaseHelper.table = table;

        @SuppressLint("Recycle") Cursor cursor = db.rawQuery("SELECT * FROM " + DatabaseHelper.table + " WHERE workout_routine_name = ? AND exercise_name = ?", new String[] {String.valueOf(workoutRoutineName), String.valueOf(exerciseName)});

        return cursor.getCount() > 0;
    }

    //Count the number of exercises within exercise category
    public void countNumberOfExercises(String [] tables, int position) {
        SQLiteDatabase db = SQLLiteDatabaseHelper.getReadableDatabase();
        DatabaseHelper.table = tables[position];
        Cursor cursor = null;

        try {
            cursor = db.rawQuery("SELECT * FROM " + DatabaseHelper.table,null);
        }
        catch(SQLException e) {
            Log.d("SQL exception caught in: ", "Exercise List Manager countNumberOfExercises method");
        }

        assert cursor != null;
        setNumberOfExercises(cursor.getCount());
        cursor.close();
    }

    //Count the number of workout routines within a workout category
    public void countNumberOfWorkoutRoutines(String [] tables, int position) {
        SQLiteDatabase db = SQLLiteDatabaseHelper.getReadableDatabase();
        DatabaseHelper.table = tables[position];
        Cursor cursor = null;

        try {
            cursor = db.rawQuery("SELECT DISTINCT workout_routine_name FROM " + DatabaseHelper.table,null);
        }
        catch(SQLException e) {
            Log.d("SQL exception caught in: ", "Exercise List Manager countNumberOfWorkoutRoutines method");
        }

        assert cursor != null;
        setNumberOfWorkoutRoutines(cursor.getCount());
        cursor.close();
    }

    //Reset current number of exercises
    public void setNumberOfExercises(long count) {
        numberOfExercises = count;
    }

    //Reset current number of workout routines
    public void setNumberOfWorkoutRoutines(long count) {
        numberOfWorkoutRoutines = count;
    }

    //Return current number of exercises
    public static long getNumberOfExercises() {
        return numberOfExercises;
    }

    //Return current number of workout routines
    public static long getNumberOfWorkoutRoutines() {
        return numberOfWorkoutRoutines;
    }
}
