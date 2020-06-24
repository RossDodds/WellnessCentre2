package com.example.wellnesscentre2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ExerciseActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, AddNewExerciseDialog.AddExerciseDialogListener {

    // declares class variables
    private TextView txtExerciseTime,txtRestTime,txtDurationValue,txtIntervalValue,txtNumberOfReps,txtNumberOfSets,txtExerciseTitle;
    private Spinner spnExerciseList;
    private List<ExerciseRoutine> exerciseList = new ArrayList<ExerciseRoutine>();
    private Button btnStartStop, btnReset, btnAddNewExercise;
    private CountDownTimer exerciseTimer, intervalTimer;
    private long exerciseTimeMilliseconds, intervalTimeMilliseconds;
    private boolean timerRunningExercise, timerRunningInterval;
    private boolean exerciseTurn = true;
    private ArrayAdapter exerciseAdapter;
    @Override

        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_exercise);
            // initialise text references and create array for exercises
            txtExerciseTime = findViewById(R.id.txtExerciseTime);
            txtDurationValue = findViewById(R.id.txtDurationValue);
            txtRestTime = findViewById(R.id.txtRestTime);
            txtIntervalValue = findViewById(R.id.txtIntervalValue);
            txtNumberOfReps = findViewById(R.id.txtNumberOfRepsValue);
            txtNumberOfSets = findViewById(R.id.txtNumberOfSetsValue);
            txtExerciseTitle = findViewById(R.id.txtExerciseTitle);
            spnExerciseList = findViewById(R.id.spnExercises);
            btnStartStop = findViewById(R.id.btn_startExercise);
            btnAddNewExercise = findViewById(R.id.btn_addNewExercise);


            // fetched created exercise's
            ExerciseDatabaseHelper exerciseDbHelper = new ExerciseDatabaseHelper(this);
            exerciseList = exerciseDbHelper.getExercises();

            // default exercise
            ExerciseRoutine defaultExercise = new ExerciseRoutine("Push Ups",10,3,10,5);
            ExerciseRoutine defaultExercise2 = new ExerciseRoutine("Squats",20,5,120,60);
            exerciseList.add(defaultExercise);
            exerciseList.add(defaultExercise2);



            // change values displayed depending on which exercise is selected
            spnExerciseList.setOnItemSelectedListener(this);

            // populates a string array with exercise names to be used for selecting
            String[] exerciseNames = new String[exerciseList.size()];
            for(int i =0; i < exerciseList.size();i++){
                exerciseNames[i] = exerciseList.get(i).getExerciseName();
            }
            // creates an array adapter for the spinner to show exercise names
            exerciseAdapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,exerciseNames);
            exerciseAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spnExerciseList.setAdapter(exerciseAdapter);

            btnStartStop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startStop();
                }
            });
            btnAddNewExercise.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openDialog();
                }
            });

        }

        // handles the selection of an exercise and populates the text fields with their variables
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        if(parent.getId() == R.id.spnExercises){
                String exerciseSelected = parent.getItemAtPosition(position).toString();

                // sets the text values of the fields to that of the chosen exercise
                for(int i =0; i<exerciseList.size();i++){
                    if (exerciseSelected == exerciseList.get(i).getExerciseName()){
                        txtDurationValue.setText(Integer.toString(exerciseList.get(i).getDuration()));
                        txtIntervalValue.setText(Integer.toString(exerciseList.get(i).getInterval()));
                        txtNumberOfSets.setText(Integer.toString(exerciseList.get(i).getNumberOfSets()));
                        txtNumberOfReps.setText(Integer.toString(exerciseList.get(i).getNumberOfReps()));
                        txtExerciseTime.setText(Integer.toString(exerciseList.get(i).getDuration()));
                        txtRestTime.setText(Integer.toString(exerciseList.get(i).getInterval()));

                        exerciseTimeMilliseconds = (exerciseList.get(i).getDuration() * 1000);
                        intervalTimeMilliseconds = (exerciseList.get(i).getInterval() * 1000);

                    }
                }
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }

        // starts and stops the timer
        public void startStop(){
            if(timerRunningExercise && exerciseTurn){
                stopTimerExercise();

            }else if(!timerRunningExercise && exerciseTurn){
                startTimerExercise();
            }else if(timerRunningInterval && !exerciseTurn){
                stopTimerInterval();
            }else if(!timerRunningInterval && !exerciseTurn){
                startTimerInterval();
            }



        }

        // starts the timer for exercise period
        public void startTimerExercise(){
            exerciseTimer = new CountDownTimer(exerciseTimeMilliseconds, 1000) {
                // overrides the on tick function to update the exercise time to the current time left
                @Override
                public void onTick(long millisUntilFinished) {
                    exerciseTimeMilliseconds = millisUntilFinished;
                    updateExerciseTime();

                }

                // overrides the on finish function to decrease the number of sets left and resets
                // the time left and rest period left
                @Override
                public void onFinish() {
                    if(Integer.parseInt((String) txtNumberOfSets.getText()) > 0){
                        int setsLeft = Integer.parseInt((String) txtNumberOfSets.getText()) - 1;
                        txtNumberOfSets.setText(Integer.toString(setsLeft));
                        exerciseTimer.cancel();
                        txtExerciseTime.setText(txtDurationValue.getText());
                        exerciseTimeMilliseconds = (Integer.parseInt((String) txtDurationValue.getText())* 1000);
                        txtExerciseTitle.setText("Rest");
                        startTimerInterval();
                        exerciseTurn = false;


                    }
                }
            }.start();
            btnStartStop.setText("Pause");
            btnStartStop.setBackgroundColor(Color.RED);
            timerRunningExercise = true;
        }

        // stops the exercise timer, used when pausing or in the resting period
        public void stopTimerExercise(){
        exerciseTimer.cancel();
        timerRunningExercise = false;
        btnStartStop.setText("Start");
        btnStartStop.setBackgroundColor(Color.GREEN);


        }

        // updates the exercise time to show the time left
        public void updateExerciseTime(){
        int seconds = (int) exerciseTimeMilliseconds /1000;

        String timeLeft;
        timeLeft = Integer.toString(seconds);
        txtExerciseTime.setText(timeLeft);
        }


    public void startTimerInterval(){
        intervalTimer = new CountDownTimer(intervalTimeMilliseconds, 1000) {
            // overrides the on tick function to update the exercise time to the current time left
            @Override
            public void onTick(long millisUntilFinished) {
                intervalTimeMilliseconds = millisUntilFinished;
                updateIntervalTime();

            }
            // overrides the on finish function to decrease the number of sets left and resets
            // the time left and rest period left
            @Override
            public void onFinish() {

                if(Integer.parseInt((String) txtNumberOfSets.getText()) > 0){

                    intervalTimer.cancel();
                    txtRestTime.setText(txtIntervalValue.getText());
                    intervalTimeMilliseconds = (Integer.parseInt((String) txtIntervalValue.getText())* 1000);
                    txtExerciseTitle.setText("Exercise");
                    startTimerExercise();
                    exerciseTurn = true;


                }else{
                    txtExerciseTitle.setText("Exercise Finished!!!");
                }


            }
        }.start();
        btnStartStop.setText("Pause");
        btnStartStop.setBackgroundColor(Color.RED);
        timerRunningInterval = true;
    }

    // stops the exercise timer, used when pausing or in the resting period
    public void stopTimerInterval(){
        intervalTimer.cancel();
        timerRunningInterval = false;
        btnStartStop.setText("Start");
        btnStartStop.setBackgroundColor(Color.GREEN);


    }

    // updates the exercise time to show the time left
    public void updateIntervalTime(){
        int seconds = (int) intervalTimeMilliseconds /1000;

        String timeLeft;
        timeLeft = Integer.toString(seconds);
        txtRestTime.setText(timeLeft);
    }

    // opens the dialog for creating a new exercise
    public void openDialog(){
        AddNewExerciseDialog newExerciseDialog = new AddNewExerciseDialog();
        newExerciseDialog.show(getSupportFragmentManager(),"Create Exercise");
    }

    // adds the new exercise to the database and shows it in the spinner object
    @Override
    public void applyNewExercise(String name, int noOfReps, int noOfSets, int duration, int interval) {

        ExerciseRoutine newExercise = new ExerciseRoutine(name,noOfReps,noOfSets,duration,interval);
        exerciseList.add(newExercise);
        String[] exerciseNames = new String[exerciseList.size()];
        for(int i =0; i < exerciseList.size();i++){
            exerciseNames[i] = exerciseList.get(i).getExerciseName();
        }
        // creates an array adapter for the spinner to show exercise names
        for(int i =0; i < exerciseList.size();i++){
            exerciseNames[i] = exerciseList.get(i).getExerciseName();
        }
        // creates an array adapter for the spinner to show exercise names
        exerciseAdapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,exerciseNames);
        spnExerciseList.setAdapter(exerciseAdapter);

        ExerciseDatabaseHelper dbHelper = new ExerciseDatabaseHelper(ExerciseActivity.this);
        boolean successfullyAdded = dbHelper.addExercise(newExercise);
        Toast.makeText(this, "Success + " + successfullyAdded, Toast.LENGTH_SHORT).show();


    }
}
