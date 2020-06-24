package com.example.wellnesscentre2;

import android.content.Context;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.List;

public class MealDatabaseHelper extends SQLiteOpenHelper {

    // declaration of class variables
    public static final String MEAL_NAME = "MEAL_NAME";
    public static final String TOTAL_CALORIES = "TOTAL_CALORIES";
    public static final String DATE_OF_MEAL = "DATE_OF_MEAL";
    public static final String MEAL_INGREDIENTS = "MEAL_INGREDIENTS";
    private final String MEAL_TABLE = "MEAL_TABLE";

    // constructor for the database
    public MealDatabaseHelper(@Nullable Context context){
        super(context, "meal.db", null, 1);

    }


    // creates the database on first launch
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable ="CREATE TABLE " + MEAL_TABLE + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " + MEAL_NAME + " TEXT, " + DATE_OF_MEAL + " TEXT, " + MEAL_INGREDIENTS + " TEXT, " + TOTAL_CALORIES + " INT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    // add meal function used to add meals to the database
    public boolean addMeal(String mealName, String mealIngredients, String dateOfMeal, int totalCalories){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        // assigns content values to the parameters passed in and links them to the field in the database
        cv.put(MEAL_NAME, mealName);
        cv.put(DATE_OF_MEAL, dateOfMeal);
        cv.put(MEAL_INGREDIENTS, mealIngredients);
        cv.put(TOTAL_CALORIES,totalCalories);
        // inserts the content value into the database
        long insert = db.insert(MEAL_TABLE, null, cv);

        // result of double from inserting into the database indicates if the operation was
        // successful. used to return true/false to indicate this
        if(insert == -1){
            return false;
        }else{
            return true;
        }

    }

    // returns the meals stored in the database
    public List<Meal> getMeals(){
        // creates a meal list
        List<Meal> dbMealList = new ArrayList<Meal>();
        // creates an ingredient list that is set to blank
        List<Ingredient> dbIngredientList = new ArrayList<Ingredient>();
        Ingredient blankIngredient = new Ingredient("Blank",0,0);
        // Creates a string with current date and sets the date text to show it

        dbIngredientList.add(blankIngredient);
        // SQL query to select all entries in the database
        String dbQuery = "SELECT * FROM " + MEAL_TABLE;
        // creates a readable connection to the database
        SQLiteDatabase db = this.getReadableDatabase();

        // cursor used to navigate database entries
        Cursor cursor = db.rawQuery(dbQuery,null);
        if(cursor.moveToFirst()){
            do{
                // assigns meal object variables to relevant database column values
                String dbMealName = cursor.getString(1);
                int dbTotalCalories = cursor.getInt(2);
                String dbMealDate = cursor.getString(3);
                // creates new meal based on above values
                Meal dbNewMeal = new Meal(dbMealName, dbIngredientList,dbMealDate);
                // adds the meal to the temporary list
                dbMealList.add(dbNewMeal);


            }while(cursor.moveToNext());
        }else{

        }
        // closes connections
        cursor.close();
        db.close();
        // returns the temporary list;
        return dbMealList;
    }
}
