package com.example;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

/*
Tracks the number of sets or the current time for a workout
 */
public class TrackActivity extends AppCompatActivity {

    private ArrayList<workout> workout_schedule;
    /*workout vars*/
    private int count;
    private int max_set;
    private int workout_index;
    private long desired_time;

    /*timer vars*/
    private CountDownTimer timer;
    private boolean timer_running;

    /*views*/
    private Button inc_button;
    private TextView count_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track);

        count_view = (TextView) findViewById((R.id.count_view));
        inc_button = (Button) findViewById(R.id.inc_button);
        call_intent();

        inc_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (max_set > -1 && desired_time == -1) {
                  handle_setrep();
                  inc_button.setText("finished a set");
                }
                else{
                    if(timer_running){
                        pauseTimer();
                    }
                    else{
                        startTimer();
                    }
                }
            }
        });

        Button finish_button = (Button) findViewById(R.id.Finish_Early_Button);
        finish_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                finish_workout();
            }
        });

        update_count_text();
    }



    /*Get variables from the previous activity*/
    private void call_intent(){
        if (getIntent().hasExtra("workout_schedule")){
            TextView workout_name = (TextView) findViewById(R.id.Workout_name_TextView2);
            workout_index = getIntent().getExtras().getInt("workout_index");
            workout_schedule = getIntent().getParcelableArrayListExtra("workout_schedule");
            workout_name.setText(workout_schedule.get(workout_index).getName());
            max_set = workout_schedule.get(workout_index).getSets();
            desired_time = workout_schedule.get(workout_index).getTimer();

        }
        count = 0;
        workout_index = getIntent().getExtras().getInt("workout_index");

        if (max_set > -1 && desired_time == -1) {
            inc_button.setText("Pause");
        }
        else{
            inc_button.setText("Start");
        }

    }

    /*control flow for workouts that uses sets and reps*/
    private void handle_setrep(){
        print_incremented_count();
        if (count >= max_set) {
            finish_workout();
        }
    }

    /*send intent once requirements are finished*/
    private void finish_workout(){
        Intent intent = new Intent(TrackActivity.this, Begin_Workout.class);
        workout_index++; /*go to the next workout*/
        intent.putExtra("workout_index", workout_index);
        intent.putParcelableArrayListExtra("workout_schedule",  workout_schedule);
        startActivity(intent);
    }
    /*increment count and then print it to count_view*/
    private void print_incremented_count(){

        count++;
        String countToString = "" + count + "/" + max_set;
        count_view.setText(countToString);
    }

    /*start the timer*/
    private void startTimer(){
            timer = new CountDownTimer(desired_time, 1000) {


            public void onTick(long millisUntilFinished) {
                desired_time = millisUntilFinished;
                update_count_text();
            }

            public void onFinish() {
                timer_running = false;
                inc_button.setText("Start");
                finish_workout();
            }

        }.start();
        timer_running = true;
        inc_button.setText("Pause");
    }

    private void pauseTimer(){
        timer_running = false;
        timer.cancel();
        inc_button.setText("Start");
    }

    /*update text every tick*/
    private void update_count_text(){
        int minutes = (int) (desired_time/1000)/60;
        int seconds = (int) (desired_time/1000)%60;
        String format_time = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        count_view.setText(format_time);
    }
}