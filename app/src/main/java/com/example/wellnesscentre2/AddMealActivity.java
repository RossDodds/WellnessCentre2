package com.example.wellnesscentre2;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AddMealActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    // declare class variables
    private Spinner spnMealList;
    private TextView txtSelectedMeal,txtDatePicker;
    private ListView lvIngredients;
    private Button btnAddMeal, btnCreateMeal;
    private List<Meal> mealList = new ArrayList<Meal>();
    private DatePickerDialog.OnDateSetListener txtDateSetListener;
    private ArrayAdapter mealSpinnerAdapter;
    private Meal selectedMeal = new Meal();





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_meal);

        // Target activity components and link with class variables
        spnMealList = findViewById(R.id.spnPastMeals);
        txtSelectedMeal = findViewById(R.id.txtMealNameValue);
        txtDatePicker = findViewById(R.id.txtDatePicker);
        lvIngredients = findViewById(R.id.lvIngredientsAddMeal);
        btnAddMeal = findViewById(R.id.btnAddMeal);
        btnCreateMeal = findViewById(R.id.btnNewMeal);

        // Runs initDate function which will initialise the date text to show current date.
        // This prevents users having to needlessly select current date when quickly adding a new meal
        initDate();




        // Default meal creation to demonstrate meals along with ingredient creation to be used by meals.

        // Default Meal 1
        Ingredient potatoes = new Ingredient("Champ Potatoes",900,77);
        Ingredient milk = new Ingredient("Full Fat Milk",150,66);
        Ingredient springOnion = new Ingredient("Spring Onion", 100, 32);
        Ingredient butter = new Ingredient("Butter" , 85,717);
        Ingredient test = new Ingredient("test" , 85,717);

        List<Ingredient> champIngredient = new ArrayList<Ingredient>();
        champIngredient.add(milk);
        champIngredient.add(springOnion);
        champIngredient.add(butter);
        champIngredient.add(potatoes);

        Meal defaultMeal = new Meal("Champ",champIngredient,txtDatePicker.getText().toString());


        //Default Meal 2
        Ingredient biscuit = new Ingredient("Digestive Biscuit",75,568);

        List<Ingredient> digestiveBiscuitIngredient = new ArrayList<Ingredient>();
        digestiveBiscuitIngredient.add(biscuit);
        Meal defaultMeal2 = new Meal("Digestive Biscuit",digestiveBiscuitIngredient,txtDatePicker.getText().toString());

        // Adds meals to meal list
        mealList.add(defaultMeal);
        mealList.add(defaultMeal2);

        // fetched created meals and added them from the database
        MealDatabaseHelper tempMealDbHelper = new MealDatabaseHelper(this);
        List<Meal> tempMeal = new ArrayList<Meal>();
        tempMeal = tempMealDbHelper.getMeals();
        boolean alreadyAdded = false;
        // loops through the pulled meals from the database and ensures each unique meal is only added
        // once. This is to prevent the same meal showing over and over again on the spinner dropdown
        for(int i =0;i<tempMeal.size();i++){

            for(int y =0;y< mealList.size();y++){
                if(mealList.get(y).getMealName().equals(tempMeal.get(i).getMealName())){
                    alreadyAdded = true;
                }
            }

            if(!alreadyAdded){
                mealList.add(tempMeal.get(i));
            }

            alreadyAdded = false;
        }

        // creates a spinner listener for selected item
        spnMealList.setOnItemSelectedListener(this);

        // gets the meal names from the objects and places them in a string array for
        // spinner to use
        String[] mealNames = new String[mealList.size()];
        for(int i =0; i < mealList.size();i++){
            mealNames[i] = mealList.get(i).getMealName();
        }

        // creates an array adapter for the spinner to show exercise names
        mealSpinnerAdapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,mealNames);
        mealSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnMealList.setAdapter(mealSpinnerAdapter);

        // Click listener for the date selector. Shows a date picker dialog to allow users to select a
        // new date for when the meal was had.
        txtDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Creates a calendar object getting current date
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                // Creates date picker dialog to select current date
                DatePickerDialog dateDialog = new DatePickerDialog(AddMealActivity.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth,txtDateSetListener,year,month,day);
                dateDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dateDialog.show();
            }
        });

        // Takes the date the user selected and converts it to a string. Sets the date value to this
        // string
        txtDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                // Increments month by 1 as it is based on 0, Allows the user to identify the correct
                // month rather than it being 1 more than how it would appear
                month = month + 1;
                String date = dayOfMonth + "/" + month +"/" + year;
                txtDatePicker.setText(date);

            }
        };

        // adds meal button listener
        btnAddMeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            addNewMeal(selectedMeal);

            }
        });

        // create meal button listener
        btnCreateMeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createMeal();

            }
        });

    }

    // Function that gets the current date and sets the shown date to this date.
    // Prevents users having to constantly select current date when adding a new meal for the
    // current day
    public void initDate(){
        // Creates a Calendar object with current date values
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        // Creates a string with current date and sets the date text to show it
        String date = day + "/" + (month+1) +"/" + year;
        txtDatePicker.setText(date);
    }


    // spinner selector, initialises list view with the ingredients of selected meal
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(parent.getId() == R.id.spnPastMeals){
            String mealSelected = parent.getItemAtPosition(position).toString();

            // loops through meal list, finds selected meal
            for(int i =0; i<mealList.size();i++){
                if (mealSelected == mealList.get(i).getMealName()){
                   List<String> ingredientNames = new ArrayList<String>();
                    mealList.get(i).calculateTotalCalories();
                    selectedMeal = mealList.get(i);
                    //gets ingredients of the meal
                    for(int y = 0; y < mealList.get(i).getIngredientList().size();y++){
                    ingredientNames.add(mealList.get(i).getIngredientList().get(y).getIngredientName());

                    }
                    // sets text for selected meal
                    txtSelectedMeal.setText(mealList.get(i).getMealName());
                    ArrayAdapter ingredientAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,ingredientNames);
                    // sets adapter for list view to show ingredients
                    lvIngredients.setAdapter(ingredientAdapter);
                }
            }

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    // adds new meal to the SQLite database
    public void addNewMeal(Meal meal){
        meal.calculateTotalCalories();
        MealDatabaseHelper dbHelper = new MealDatabaseHelper(AddMealActivity.this);
        Gson gson = new Gson();
        String jsonIngredients = gson.toJson(meal.getIngredientList());
        boolean successfullyAdded = dbHelper.addMeal(meal.getMealName(),txtDatePicker.getText().toString(),jsonIngredients,meal.getTotalCalories());
        Toast.makeText(this, "Success + " + successfullyAdded, Toast.LENGTH_SHORT).show();
        Toast.makeText(this, "total calories = " + meal.getTotalCalories(), Toast.LENGTH_SHORT).show();
    }

    // create meal function takes the user to the create meal activity
    public void createMeal(){
        Intent intent = new Intent(this, CreateMealActivity.class);
        startActivity(intent);

    }


}
