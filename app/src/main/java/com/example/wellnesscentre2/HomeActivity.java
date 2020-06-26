package com.example.wellnesscentre2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private Button exerciseBtn,addMealBtn;
    private TextView txtCaloriesToday;
    private List<Meal> mealList = new ArrayList<Meal>();
    private BarChart caloriesBarChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        exerciseBtn = findViewById(R.id.btn_exercise);
        addMealBtn = findViewById(R.id.btn_addMeal);
        txtCaloriesToday = findViewById(R.id.txt_caloriesToday);
        caloriesBarChart = findViewById(R.id.bar_dailyCalories);
        exerciseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openExercise();
            }
        });
        addMealBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAddMeal();
            }
        });

        // fetch meals on db
        MealDatabaseHelper mealDbHelper = new MealDatabaseHelper(this);
        mealList = mealDbHelper.getMeals();

        // get total calories for todays meals and display on home screen
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        String date = day + "/" + (month+1) +"/" + year;
        // calculates total calories for current date and displays it
        int totalCalories = 0;
        String text = "help";

        for(int i = 0; i < mealList.size();i++){

            if(mealList.get(i).getMealDate().equals(date)){
                totalCalories = totalCalories + mealList.get(i).getTotalCalories();
            }
        }
        txtCaloriesToday.setText(Integer.toString(totalCalories));

        // creates an array of strings holding the dates for the current week to be displayed
        // in the bar chart.

        DateFormat format = new SimpleDateFormat("dd/mm/yyyy");
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        ArrayList<String> weekDays = new ArrayList<>();
        for(int i =0; i < 7; i++){
            year = cal.get(Calendar.YEAR);
            month = cal.get(Calendar.MONTH);
            day = cal.get(Calendar.DAY_OF_MONTH);
            date = day + "/" + (month+1) +"/" + year;
            weekDays.add(date);
            cal.add(Calendar.DAY_OF_MONTH,1);
        }


        // creates an array list of integers holding the total calories for each date;
        int[] weekdayCalories = new int[7];

        // loops through the 7 days of the week and calculates the total calories for a given day
        for(int i = 0; i < 7;i++){
            totalCalories = 0;

            // loops through the meal list looking for meals on the current day and totals the
            // calories for that day
            for(int y = 0; y < mealList.size();y++){

                if(mealList.get(y).getMealDate().equals(weekDays.get(i))){
                    totalCalories = totalCalories + mealList.get(y).getTotalCalories();
                }
            }
            // stores the total calories for the given date in the array
            weekdayCalories[i] = totalCalories;
        }


        // Creates a bar chart with the weekday calories as y value and weekday as x axis
        ArrayList<BarEntry> barEntries = new ArrayList<>();
        for(int i =0; i < 7;i++){
            barEntries.add(new BarEntry((float) i,(float) weekdayCalories[i]));
        }
        BarDataSet barDataSet = new BarDataSet(barEntries,"Total Calories");
        BarData theData = new BarData(barDataSet);
        theData.setBarWidth(0.9f);
        caloriesBarChart.setData(theData);

    }

    // opens the add exercise activity
    public void openExercise(){
        Intent intent = new Intent(this, ExerciseActivity.class);
        startActivity(intent);

    }

    // opens the add meal activity
    public void openAddMeal(){
        Intent intent = new Intent(this, AddMealActivity.class);
        startActivity(intent);

    }




}
