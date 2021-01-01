package com.example;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class Begin_Workout extends AppCompatActivity {

    private ArrayList<workout> workout_schedule;
    private int workout_index;
    private TextView workout_name;
    private TextView sets;
    private TextView reps;
    /*Temp store data*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.begin_workout_activity);



        /*setup texts*/
        workout_name = (TextView) findViewById(R.id.Workout_name_TextView);
        sets = (TextView) findViewById(R.id.Set_TextView);
        reps = (TextView) findViewById(R.id.Rep_TextView);

        /*need to check for finish*/
        call_intent();
        if (workout_index < workout_schedule.size())
            display();
        Button start_button = (Button) findViewById(R.id.start_button);
        start_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Begin_Workout.this, TrackActivity.class);
                intent.putParcelableArrayListExtra("workout_schedule",  workout_schedule);
                intent.putExtra("workout_index", workout_index);
                startActivity(intent);
            }
        });
        Button finish_early_button = (Button) findViewById(R.id.Finish_Early_Button);
        finish_early_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Begin_Workout.this, MainMenu.class);
                Toast.makeText(Begin_Workout.this, "Finished!!!", Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        });

    }

    private void call_intent(){
        if (getIntent().hasExtra("workout_schedule")) {
            workout_schedule = getIntent().getParcelableArrayListExtra("workout_schedule");
        }
        else{
            workout_schedule = new ArrayList<>();
            workout_schedule.add(new workout("Do Nothing", 1));
        }
        if (getIntent().hasExtra("workout_index")){
            workout_index = getIntent().getExtras().getInt("workout_index");
        }
        else{
            workout_index = 0;
        }
        if(workout_index >= workout_schedule.size()){
            Intent intent = new Intent(Begin_Workout.this, MainMenu.class);
            Toast.makeText(Begin_Workout.this, "Finished!!!", Toast.LENGTH_SHORT).show();
            startActivity(intent);
        }
        System.out.println ("index = " + workout_index);
        System.out.println ("size = " + workout_schedule.size());
    }
    private void display(){

        workout_name.setText(workout_schedule.get(workout_index).getName());
        int set_count = workout_schedule.get(workout_index).getSets();
        int rep_count = workout_schedule.get(workout_index).getReps();
        long timer = workout_schedule.get(workout_index).getTimer();

        if (set_count > -1 && timer == -1){
            String set_toString = "Sets: " + set_count;
            String rep_toString = "Reps: " + rep_count;
            sets.setText(set_toString);
            reps.setText(rep_toString);
        }
        else{
            int minutes = (int) (timer/1000)/60;
            int seconds = (int) (timer/1000)%60;
            String format_time = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
            String sets_text = "Time: ";
            String reps_text = "" + format_time;
            sets.setText(sets_text);
            reps.setText(reps_text);
        }

    }
}