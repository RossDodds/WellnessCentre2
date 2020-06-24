package com.example.wellnesscentre2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class IngredientDatabaseHelper extends SQLiteOpenHelper {

    // declaration of class variables, used when creating SQL querries
    public static final String INGREDIENT_NAME = "INGREDIENT_NAME";
    public static final String CALORIES_PER_100_GRAMS = "CALORIES_PER_100_GRAMS";
    public static final String QUANTITY = "QUANTITY";
    private final String INGREDIENT_TABLE = "INGREDIENT_TABLE";

    // mandatory constructor for SQLite
    public IngredientDatabaseHelper(@Nullable Context context){
        super(context, "ingredient.db", null, 1);

    }


    // onCreateMethod that creates a new table in the database
    @Override
    public void onCreate(SQLiteDatabase db) {
        //SQL querry to create the new table
        String createTable ="CREATE TABLE " + INGREDIENT_TABLE + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " + INGREDIENT_NAME + " TEXT, " + CALORIES_PER_100_GRAMS + " INT, " + QUANTITY + " INT )";
        // executes the querry creating a table
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    // addIngredient function for adding a new ingredient to the database
    public boolean addIngredient(String ingredientName, int caloriesPer100, int quantity){
        // creates a connection to database that allows for information to be written to it
        SQLiteDatabase db = this.getWritableDatabase();
        // creates content values to put in based on ingredient information passed in
        ContentValues cv = new ContentValues();
        cv.put(INGREDIENT_NAME, ingredientName);
        cv.put(CALORIES_PER_100_GRAMS, caloriesPer100);
        cv.put(QUANTITY, quantity);
        // inserts into the table, long returned to indicate if it is successful
        long insert = db.insert(INGREDIENT_TABLE, null, cv);

        // returns boolean values indicating the outcome
        if(insert == -1){
            return false;
        }else{
            return true;
        }

    }

    // getIngredients function to return an array list of ingredients, to be used when populaing
    // spinners and for getting the information from the database
    public List<Ingredient> getIngredients(){
        // creates ingredient list to hold the data
        List<Ingredient> dbIngredientList = new ArrayList<Ingredient>();

        // query creation to select all from database
        String dbQuery = "SELECT * FROM " + INGREDIENT_TABLE;
        // creates a connection to the database to allow data to be read
        SQLiteDatabase db = this.getReadableDatabase();

        // creates a cursor for scanning the data and outputting it to variables
        Cursor cursor = db.rawQuery(dbQuery,null);
        if(cursor.moveToFirst()){
            do{
                // assigns variables to be used in creating the ingredient object
                String dbIngredientName = cursor.getString(1);
                int dbCaloriesPer100 = cursor.getInt(2);
                int dbQuantity = cursor.getInt(3);
                Ingredient dbNewIngredient = new Ingredient(dbIngredientName, dbCaloriesPer100,dbQuantity);
                // adds ingredient to the list
                dbIngredientList.add(dbNewIngredient);


            }while(cursor.moveToNext());
        }else{

        }
        // closes the cursor connection and database
        cursor.close();
        db.close();
        // returns the ingredient list
        return dbIngredientList;
    }
}
