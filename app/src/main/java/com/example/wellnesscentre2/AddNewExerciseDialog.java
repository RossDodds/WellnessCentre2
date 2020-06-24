package com.example.wellnesscentre2;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

public class AddNewExerciseDialog extends AppCompatDialogFragment {

    // declares class variables for the new exercise dialog
    private EditText etExerciseName,etExerciseSets,etExerciseReps,etExerciseDuration,etExerciseInterval;
    private AddExerciseDialogListener listener;

    // overrides onCreateDialog to populate the dialog
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.add_exercise_dialog,null);

        // creates the dialog screen
        builder.setView(view)
                .setTitle("Add Exercise")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        // uses try catch to attempt to get details about an exercise
                        try {
                            String name = etExerciseName.getText().toString();
                            Integer noOfReps = Integer.parseInt(etExerciseReps.getText().toString());
                            Integer noOfSets = Integer.parseInt(etExerciseSets.getText().toString());
                            Integer exerciseDuration = Integer.parseInt(etExerciseDuration.getText().toString());
                            Integer exerciseInterval = Integer.parseInt(etExerciseInterval.getText().toString());
                            listener.applyNewExercise(name,noOfReps,noOfSets,exerciseDuration,exerciseInterval);
                        }catch (Exception e){

                        }

                    }
                });
        // declares relationship between class variables and related activity objects
        etExerciseName = view.findViewById(R.id.edit_exerciseName);
        etExerciseReps = view.findViewById(R.id.edit_exerciseNoReps);
        etExerciseSets = view.findViewById(R.id.edit_exerciseNoSets);
        etExerciseDuration = view.findViewById(R.id.edit_exerciseDuration);
        etExerciseInterval = view.findViewById(R.id.edit_exerciseInterval);
        return  builder.create();

    }

    // overrides the onAttach function which attaches the listener to the dialog
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener =(AddExerciseDialogListener)  context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement NewExerciseDialogListener");
        }

    }

    // adds an interface taking in the details about an exercise from the dialog to be used in previous
    // activity
    public interface AddExerciseDialogListener{
        void applyNewExercise(String name, int noOfReps, int noOfSets, int duration, int interval);
    }
}
