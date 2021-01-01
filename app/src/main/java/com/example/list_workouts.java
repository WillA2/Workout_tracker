package com.example;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;

/*
controls editing, inserting, moving, and deleting exercises
 */
public class list_workouts extends AppCompatActivity {

    private RecyclerView recyclerView;
    private workoutAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<workout> workout_schedule;
    private Button create_button;
    private Button remove_button;
    private Button save;
    private int highlight_pos = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.schedule_activity);
        workout_schedule = getIntent().getParcelableArrayListExtra("workout_schedule");
        buildView();

        create_button = findViewById(R.id.Create);
        remove_button = findViewById(R.id.Remove);
        save = findViewById(R.id.save_button);


        create_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertItem();
            }
        });

        remove_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeItem(highlight_pos);
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(list_workouts.this, MainMenu.class);
                intent.putParcelableArrayListExtra("workout_schedule",  workout_schedule);
                startActivity(intent);
            }
        });
    }


    private void insertItem() {
        workout_schedule.add(new workout("Workout", 5, 5));
        adapter.notifyItemInserted(workout_schedule.size() - 1);
    }

    private void removeItem(int pos) {
        if (pos >= 0 && pos < workout_schedule.size()) {
            workout_schedule.remove(pos);
            adapter.setSelect_pos(-1);
            adapter.notifyItemRemoved(pos);
            highlight_pos = -1;
        }
    }

    private void buildView() {
        recyclerView = findViewById(R.id.workout_recycler);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        adapter = new workoutAdapter(workout_schedule);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        adapter.setOnItemClickListener(new workoutAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int pos, View v) {
                if (highlight_pos == pos){
                    highlight_pos = -1;
                }
                else {
                    highlight_pos = pos;
                }
                adapter.setSelect_pos(pos);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void editWorkout(int pos) {
                Intent intent = new Intent(list_workouts.this, workoutEditor.class);
                intent.putParcelableArrayListExtra("workout_schedule",  workout_schedule);
                intent.putExtra("workout pos", pos);
                startActivity(intent);
            }
        });
    }


    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP |
            ItemTouchHelper.DOWN | ItemTouchHelper.START | ItemTouchHelper.END, 0) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            int start_pos = viewHolder.getAdapterPosition();
            int dest_pos = target.getAdapterPosition();
            highlight_pos = dest_pos;
            Collections.swap(workout_schedule, start_pos, dest_pos);
            recyclerView.getAdapter().notifyItemMoved(start_pos, dest_pos);
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

        }
    };

}
