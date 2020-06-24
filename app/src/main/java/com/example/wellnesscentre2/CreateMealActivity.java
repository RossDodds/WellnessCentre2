package com.example.wellnesscentre2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class CreateMealActivity extends AppCompatActivity {

    //declaration of class variables
    private Spinner spnIngredients;
    private EditText etMealName;
    private ListView lvIngredients;
    private Button btnAddIngredient, btnCreateIngredient, btnCreateMeal;
    private List<Ingredient> ingredientList = new ArrayList<Ingredient>();
    private List<Ingredient> addedIngredients = new ArrayList<Ingredient>();
    private ArrayAdapter ingredientSpinnerAdapter;
    private Ingredient selectedIngredient = new Ingredient();


    // onCreate method that runs when the activity is ran for the first time
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_meal);

        // links class variables to related activity objects
        spnIngredients = findViewById(R.id.spnIngredients);
        etMealName = findViewById(R.id.edit_mealName);
        lvIngredients = findViewById(R.id.lvIngredients);
        btnAddIngredient = findViewById(R.id.btnAddIngredient);
        btnCreateIngredient = findViewById(R.id.btnNewIngredient);
        btnCreateMeal = findViewById(R.id.btnCreateMeal);
        btnCreateIngredient = findViewById(R.id.btnNewIngredient);

        // fetched created ingredients
        IngredientDatabaseHelper ingredientDbHelper = new IngredientDatabaseHelper(this);
        ingredientList = ingredientDbHelper.getIngredients();


        // default ingredient to use when testing. Then added to ingredient list used by class
        Ingredient default1 = new Ingredient("Battered Fish",100,10);
        Ingredient default2 = new Ingredient("Jammie Dodgers",100,10);
        ingredientList.add(default1);
        ingredientList.add(default2);

        // creates on item selected listener for spinner
        spnIngredients.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            // overrides the onItemSelected function to set selected ingredient
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if(parent.getId() == R.id.spnIngredients){
                    String ingredientSelected = parent.getItemAtPosition(position).toString();

                    // loops through ingredient list, finds selected meal
                    for(int i =0; i<ingredientList.size();i++){
                        if (ingredientSelected == ingredientList.get(i).getIngredientName()){
                            // sets the chosen ingredient name to the selected ingredient object
                            selectedIngredient = ingredientList.get(i);

                        }
                    }

                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
               selectedIngredient = ingredientList.get(0);
            }
        });

        // gets the meal names from the objects and places them in a string array for
        // spinner to use
        List<String> ingredientNames = new ArrayList<String>();
        for(int i =0; i < ingredientList.size();i++){
            ingredientNames.add(ingredientList.get(i).getIngredientName());
        }

        // creates an array adapter for the spinner to show exercise names
        ingredientSpinnerAdapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,ingredientNames);
        ingredientSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnIngredients.setAdapter(ingredientSpinnerAdapter);

        // on click listener for the button to add ingredient to the list view
        btnAddIngredient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addIngredient(selectedIngredient);

            }
        });

        // on click listener to create a meal
        btnCreateMeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createMeal(addedIngredients);

            }
        });

        // on click listener to create ingredient
        btnCreateIngredient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createIngredient();

            }
        });
    }

    // adds the chosen ingredient to the listview
    public void addIngredient(Ingredient ingredient){
        List<String> ingredientNames = new ArrayList<String>();
        addedIngredients.add(ingredient);

        for(int i =0; i < addedIngredients.size();i++){
            ingredientNames.add(addedIngredients.get(i).getIngredientName());
        }
        ArrayAdapter ingredientAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,ingredientNames);
        // sets adapter for list view to show ingredients
        lvIngredients.setAdapter(ingredientAdapter);


    }

    // create meal function, takes a list of ingredients and uses them to create a new meal at a custom date
    // the date chosen is to hide the meal in the meal database so it wont show when it hasnt actually been eaten
    public void createMeal(List<Ingredient> addedIngredients){

        Meal meal = new Meal(etMealName.getText().toString(),addedIngredients,"01/01/1990",0);
        meal.calculateTotalCalories();

        MealDatabaseHelper dbHelper = new MealDatabaseHelper(CreateMealActivity.this);
        boolean successfullyAdded = dbHelper.addMeal(meal.getMealName(),meal.getTotalCalories(),"01/01/1990");
        Toast.makeText(this, "Created = " + successfullyAdded, Toast.LENGTH_SHORT).show();
    }

    public void createIngredient(){
        Intent intent = new Intent(this, CreateIngredientActivity.class);
        startActivity(intent);

    }

}