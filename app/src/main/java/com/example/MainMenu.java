package com.example;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.w3c.dom.Text;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.TooManyListenersException;

/*
 * main menu
 */
public class MainMenu extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    private ArrayList<ArrayList<workout>> workout_schedule;
    private int day;
    private TextView today;
    Calendar calendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        loadData();
        handleIntent();

        setuptoday();
        Spinner spinner = findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.days, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);



        Button begin_button = (Button) findViewById(R.id.Begin_workout_view);
        begin_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start_workout();
            }
        });

        Button make_schedule_button = (Button) findViewById(R.id.Create_workout_view);
        make_schedule_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenu.this, list_workouts.class);
                saveData();
                intent.putParcelableArrayListExtra("workout_schedule", workout_schedule.get(day));
                startActivity(intent);
            }
        });

    }

    /*
     Sets up the date for today
     */
    private void setuptoday(){
        today = findViewById(R.id.Day_view);
        switch(calendar.get(Calendar.DAY_OF_WEEK)){
            case Calendar.SUNDAY:
                today.setText("Sunday");
                break;
            case Calendar.MONDAY:
                today.setText("Monday");
                break;
            case Calendar.TUESDAY:
                today.setText("Tuesday");
                break;
            case Calendar.WEDNESDAY:
                today.setText("Wednesday");
                break;
            case Calendar.THURSDAY:
                today.setText("Thursday");
                break;
            case Calendar.FRIDAY:
                today.setText("Friday");
                break;
            case Calendar.SATURDAY:
                today.setText("Saturday");
                break;
        }
    }


    private void handleIntent(){
        if (getIntent().hasExtra("workout_schedule")) {
            workout_schedule.set(day, getIntent().getParcelableArrayListExtra("workout_schedule"));
        }
    }

    /*
    upon first time launch, allocate memory to arraylist
     */
    private void initialCreation(){
        if (workout_schedule == null) {
            workout_schedule = new ArrayList<ArrayList<workout>>(7);
            for (int n = 0; n < 8; n++) {
                workout_schedule.add(new ArrayList<workout>());
            }

        }
    }

    /*
    start today's workout
     */
    private void start_workout(){

        Intent intent = new Intent(MainMenu.this, Begin_Workout.class);
        saveData();
        intent.putParcelableArrayListExtra("workout_schedule", workout_schedule.get(calendar.get(Calendar.DAY_OF_WEEK)));
        startActivity(intent);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String day_string = parent.getItemAtPosition(position).toString();

        switch (day_string){
            case "Monday":
                day = 2;
                break;
            case "Tuesday":
                day = 3;
                break;
            case "Wednesday":
                day = 4;
                break;
            case "Thursday":
                day = 5;
                break;
            case "Friday":
                day = 6;
                break;
            case "Saturday":
                day = 7;
                break;
            case "Sunday":
                day = 1;
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void saveData() {
        SharedPreferences sharedPreferences = getSharedPreferences("sharedPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(workout_schedule);
        editor.putString("task list", json);
        editor.putInt("day", day);
        editor.apply();

        /*Debug*/
        //Toast.makeText(this, "" + day + " has been saved", Toast.LENGTH_SHORT).show();
        /*End Debug */



    }

    private void loadData(){
        SharedPreferences sharedPreferences = getSharedPreferences("sharedPrefs", MODE_PRIVATE);

        Gson gson = new Gson();
        String json = sharedPreferences.getString("task list", null);
        Type type = new TypeToken<ArrayList<ArrayList<workout>>>() {}.getType();
        workout_schedule = gson.fromJson(json, type);
        day = sharedPreferences.getInt("day", 0);
        /*Debug*/
        //Toast.makeText(this, "" + day + " has been loaded", Toast.LENGTH_SHORT).show();
        /*End Debug */
        if (workout_schedule == null){
            initialCreation();
        }

    }
}