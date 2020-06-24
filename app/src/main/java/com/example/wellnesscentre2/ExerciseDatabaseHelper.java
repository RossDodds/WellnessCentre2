package com.example.wellnesscentre2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ExerciseDatabaseHelper extends SQLiteOpenHelper {

    //declaration of class variables
    public static final String EXERCISE_NAME = "EXERCISE_NAME";
    public static final String NUMBER_OF_REPS = "NUMBER_OF_REPS";
    public static final String NUMBER_OF_SETS = "NUMBER_OF_SETS";
    public static final String DURATION = "DURATION";
    public static final String INTERVAL = "INTERVAL";
    private final String EXERCISE_TABLE = "EXERCISE_TABLE";
    private String help ="this isnt working";

    // constructor for database
    public ExerciseDatabaseHelper(@Nullable Context context){
        super(context, "exercise.db", null, 2);
    }

    //called first time database is accessed.
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable ="CREATE TABLE " + EXERCISE_TABLE + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " + EXERCISE_NAME + " TEXT, " + NUMBER_OF_REPS + " INT, " + NUMBER_OF_SETS + " INT, " + DURATION + " INT, " + INTERVAL + " INT )";
        db.execSQL(createTable);
    }

    // updates the database if version number changes
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    // add exercise function that takes an exercise routine and adds it to the database
    public boolean addExercise(ExerciseRoutine exerciseRoutine){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(EXERCISE_NAME, exerciseRoutine.getExerciseName());
        cv.put(NUMBER_OF_REPS, exerciseRoutine.getNumberOfReps());
        cv.put(NUMBER_OF_SETS, exerciseRoutine.getNumberOfSets());
        cv.put(DURATION, exerciseRoutine.getDuration());
        cv.put(INTERVAL, exerciseRoutine.getInterval());

        long insert = db.insert(EXERCISE_TABLE, null, cv);


        if(insert == -1){
            return false;
        }else{
            return true;
        }

    }

    // returns all exercise routines saved in the database
    public List<ExerciseRoutine> getExercises(){
        List<ExerciseRoutine> dbExerciseList = new ArrayList<ExerciseRoutine>();
        String dbQuery = "SELECT * FROM " + EXERCISE_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(dbQuery,null);
        if(cursor.moveToFirst()){
            do{
                String dbExerciseName = cursor.getString(1);
                int dbNumberOfReps = cursor.getInt(2);
                int dbNumberOfSets = cursor.getInt(3);
                int dbDuration = cursor.getInt(4);
                int dbInterval = cursor.getInt(5);
                ExerciseRoutine dbNewExercise = new ExerciseRoutine(dbExerciseName,dbNumberOfReps,dbNumberOfSets,dbDuration,dbInterval);
                dbExerciseList.add(dbNewExercise);


            }while(cursor.moveToNext());
        }else{

        }
        cursor.close();
        db.close();
        return dbExerciseList;
    }

}
