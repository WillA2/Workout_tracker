package com.example;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class workoutEditor extends AppCompatActivity {

    private ArrayList<workout> workout_schedule;
    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private CardView setrep_card, timer_card;
    private EditText sets, reps, minutes, hours;
    private Button save_workout;
    private EditText name;
    private int workout_pos;
    private workout temp;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_workout_activity);
        setrep_card = findViewById(R.id.setrep_cardview);
        timer_card = findViewById(R.id.timer_cardview);
        radioGroup = findViewById(R.id.radioGroup);
        name = findViewById(R.id.editTextTextPassword);
        name.setTransformationMethod(null);
        hours = findViewById(R.id.hour_password);
        hours.setTransformationMethod(null);
        minutes = findViewById(R.id.min_password);
        minutes.setTransformationMethod(null);
        sets = findViewById(R.id.set_passwordview);
        sets.setTransformationMethod(null);
        reps = findViewById(R.id.rep_password);
        reps.setTransformationMethod(null);

        handleIntent();
        save_workout = (Button) findViewById(R.id.save_workout_edits);
        save_workout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                temp = new workout();
                temp.setName(name.getText().toString());
                if (setrep_card.getVisibility() == View.VISIBLE){
                    temp.setSets(Integer.parseInt(sets.getText().toString()));
                    temp.setReps(Integer.parseInt(reps.getText().toString()));
                }
                else{
                    long ms = TimeUnit.HOURS.toMillis(Long.parseLong(hours.getText().toString()));
                    ms += TimeUnit.MINUTES.toMillis(Long.parseLong(minutes.getText().toString()));
                    temp.setTimer(ms);
                }

                workout_schedule.set(workout_pos, temp);

                Intent intent = new Intent(workoutEditor.this, list_workouts.class);
                intent.putParcelableArrayListExtra("workout_schedule",  workout_schedule);
                startActivity(intent);
            }
        });



    }

    public void checkButton(View v){
        if (v == findViewById(R.id.set_rep)){
            setrep_card.setVisibility(View.VISIBLE);
            timer_card.setVisibility(View.GONE);
        }
        else{
            setrep_card.setVisibility(View.GONE);
            timer_card.setVisibility(View.VISIBLE);
        }

    }

    public void handleIntent(){
        workout_schedule = getIntent().getParcelableArrayListExtra("workout_schedule");
        workout_pos = getIntent().getExtras().getInt("workout pos");
        temp = workout_schedule.get(workout_pos);

        name.setText(temp.getName());
        if (temp.getSets() > -1){
            sets.setText(String.valueOf(temp.getSets()));
            reps.setText(String.valueOf(temp.getReps()));

            setrep_card.setVisibility(View.VISIBLE);
            timer_card.setVisibility(View.GONE);
            radioButton = findViewById(R.id.set_rep);
            radioButton.setChecked(true);
        }
        else{
            long hour = TimeUnit.MILLISECONDS.toHours(temp.getTimer());
            long minute = TimeUnit.MILLISECONDS.toMinutes(temp.getTimer()) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(temp.getTimer()));
            this.hours.setText(String.valueOf(hour));
            this.minutes.setText(String.valueOf(minute));

            setrep_card.setVisibility(View.GONE);
            timer_card.setVisibility(View.VISIBLE);
            radioButton = findViewById(R.id.timer);
            radioButton.setChecked(true);
        }
    }

}
