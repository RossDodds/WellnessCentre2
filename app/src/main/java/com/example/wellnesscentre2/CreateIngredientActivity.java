package com.example.wellnesscentre2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CreateIngredientActivity extends AppCompatActivity {
    // declaration of class variables
    private EditText etIngredientName, etQuantity, etCaloriesPer100;
    Button btnCreateIngredient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_ingredient);

        //assigning class variables to their relating activity object
        etIngredientName = findViewById(R.id.edit_ingredientName);
        etQuantity = findViewById(R.id.edit_ingredientQuantity);
        etCaloriesPer100 = findViewById(R.id.edit_ingredientCalories);
        btnCreateIngredient = findViewById(R.id.btnCreateIngredient);

        // on click listener for creating new ingredient
        btnCreateIngredient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // creates new ingredient with given values in the edit text objects
                createIngredient(etIngredientName.getText().toString(),Integer.parseInt(etQuantity.getText().toString()),Integer.parseInt(etCaloriesPer100.getText().toString()));

            }
        });
    }

    // create ingredient function that takes given values, adds them to the database
    public void createIngredient(String name, int quantity, int calories){

        IngredientDatabaseHelper dbHelper = new IngredientDatabaseHelper(CreateIngredientActivity.this);
        boolean successfullyAdded = dbHelper.addIngredient(name,calories,quantity);
        Toast.makeText(this, "Success + " + successfullyAdded, Toast.LENGTH_SHORT).show();

    }
}
