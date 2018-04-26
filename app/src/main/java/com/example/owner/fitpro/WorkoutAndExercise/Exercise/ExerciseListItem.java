package com.example.owner.fitpro.WorkoutAndExercise.Exercise;

/**Created by Oleh Hodovaniuk**/

public class ExerciseListItem {
    private int id;
    private String workoutRoutineName;
    private String exerciseName;
    private int numberOfSets;
    private int repsPerSet;
    private int setEstimateTime;
    private String description;

    //Initialise exercise object data
    public ExerciseListItem(int id, String exerciseName, int numberOfSets, int repsPerSet, int estimateTime, String description) {
        this.id = id;
        this.exerciseName = exerciseName;
        this.numberOfSets = numberOfSets;
        this.repsPerSet = repsPerSet;
        this.setEstimateTime = estimateTime;
        this.description = description;
    }

    //Initialise workout routine exercise object data
    public ExerciseListItem(int id, String workoutRoutineName, String exerciseName, int numberOfSets, int repsPerSet, int setEstimateTime, String description) {
        this.id = id;
        this.workoutRoutineName = workoutRoutineName;
        this.exerciseName = exerciseName;
        this.numberOfSets = numberOfSets;
        this.repsPerSet = repsPerSet;
        this.setEstimateTime = setEstimateTime;
        this.description = description;
    }

    //Return exercise Id
    public Integer getId() {
        return id;
    }

    //Return workout routine exercise name
    public String getWorkoutRoutineName() {
        return workoutRoutineName;
    }

    //Return exercise name
    public String getExerciseName() {
        return exerciseName;
    }

    //Return exercise number of sets
    public int getNumberOfSets() {
        return numberOfSets;
    }

    //Return exercise reps per set
    public int getRepsPerSet() {
        return repsPerSet;
    }

    //Return exercise set estimate time
    public int getSetEstimateTime() {
        return setEstimateTime;
    }

    //Return exercise description
    public String getDescription() {
        return description;
    }
}
