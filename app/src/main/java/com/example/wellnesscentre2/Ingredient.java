package com.example.wellnesscentre2;

public class Ingredient {

    // class variables relating to an ingredient
    private String ingredientName;
    private int quantityInGrams;
    private int caloriesPer100Grams;



    private int totalCalories;


    // constructors for the class. Used when creating a new ingredient
    public Ingredient(String ingredientName, int quantityInGrams, int caloriesPer100Grams) {
        this.ingredientName = ingredientName;
        this.quantityInGrams = quantityInGrams;
        this.caloriesPer100Grams = caloriesPer100Grams;
        this.totalCalories = totalCalories(caloriesPer100Grams,quantityInGrams);

    }

    public Ingredient() {

    }

    // getters and setters for the class. Used for creating new ingredients or getting information
    // about an ingredient
    public String getIngredientName() {
        return ingredientName;
    }

    public void setIngredientName(String ingredientName) {
        this.ingredientName = ingredientName;
    }

    public int getQuantityInGrams() {
        return quantityInGrams;
    }

    public void setQuantityInGrams(int quantityInGrams) {
        this.quantityInGrams = quantityInGrams;
    }

    public int getCaloriesPer100Grams() {
        return caloriesPer100Grams;
    }

    public void setCaloriesPer100Grams(int caloriesPer100Grams) {
        this.caloriesPer100Grams = caloriesPer100Grams;
    }

    // calculates total calories the ingredient is comprised of.
    public int totalCalories (int caloriesPer100Grams, int quantityInGrams){

        int totalCalories = ((caloriesPer100Grams/100)*quantityInGrams);
        return totalCalories;
    }

    // returns total calories for this ingredient
    public int getTotalCalories() {
        return totalCalories;
    }
}
