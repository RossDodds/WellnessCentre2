package com.example.wellnesscentre2;

import java.util.ArrayList;
import java.util.List;

public class Meal {


    // declares class variables for the meal
    private String mealName, mealDate;
    private List<Ingredient> ingredientList = new ArrayList<Ingredient>();
    private int totalCalories;

    // constructor for the meal object
    public Meal(String mealName, List<Ingredient> ingredientList, String mealDate, int totalCalories) {
       // initialises meal object with parameters passed in the calling function
        this.mealName = mealName;
        this.ingredientList = ingredientList;
        this.mealDate = mealDate;
        this.totalCalories = totalCalories;
    }

    // empty constructor
    public Meal() {

    }

    // returns meal name
    public String getMealName() {
        return mealName;
    }

    // sets meal name to passed in parameter
    public void setMealName(String mealName) {
        this.mealName = mealName;
    }

    // returns ingredient list of the object
    public List<Ingredient> getIngredientList() {
        return ingredientList;
    }

    // sets the ingredient list of the object using a ingredient list passed in
    public void setIngredientList(List<Ingredient> ingredientList) {
        this.ingredientList = ingredientList;
    }

    // returns total calories
    public int getTotalCalories() {
        return totalCalories;
    }

    // sets the date the meal was consumed
    public void setMealDate(String mealDate){
        this.mealDate = mealDate;

    }

    // returns the date the meal was consumed
    public String getMealDate(){
        return this.mealDate;
    }

    // calculates the total calories and assigns it to the class variable
    public void calculateTotalCalories() {
        int caloriesSum = 0;
        if(!(totalCalories == 0)){
            for(int i = 0; i < ingredientList.size(); i++){
                caloriesSum += ingredientList.get(i).getCaloriesPer100Grams();
            }
        }


        this.totalCalories = caloriesSum;


    }
}
