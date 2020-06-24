package com.example.wellnesscentre2;

public class ExerciseRoutine {

    // declaration of class variables
        private String exerciseName;
        private int numberOfReps,numberOfSets,duration,interval;

        // constructor for the exercise routine, takes in its variables and initialises them
    public ExerciseRoutine(String exerciseName, int numberOfReps, int numberOfSets, int duration, int interval) {
        this.exerciseName = exerciseName;
        this.numberOfReps = numberOfReps;
        this.numberOfSets = numberOfSets;
        this.duration = duration;
        this.interval = interval;
    }

    public ExerciseRoutine() {

    }
    // returns exercise name
    public String getExerciseName() {
        return exerciseName;
    }
    // sets exercise name
    public void setExerciseName(String exerciseName) {
        this.exerciseName = exerciseName;
    }

    // gets number of reps
    public int getNumberOfReps() {
        return numberOfReps;
    }

    // sets number of reps
    public void setNumberOfReps(int numberOfReps) {
        this.numberOfReps = numberOfReps;
    }

    // gets number of sets
    public int getNumberOfSets() {
        return numberOfSets;
    }

    // sets number of sets
    public void setNumberOfSets(int numberOfSets) {
        this.numberOfSets = numberOfSets;
    }

    //gets duration of the exercise
    public int getDuration() {
        return duration;
    }

    // sets the duration of the exercise
    public void setDuration(int duration) {
        this.duration = duration;
    }

    // gets the interval length of the exercise
    public int getInterval() {
        return interval;
    }

    // sets the interval length of the exercise
    public void setInterval(int interval) {
        this.interval = interval;
    }
}
