package com.example;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class workoutAdapter extends RecyclerView.Adapter<workoutAdapter.workoutViewHolder> {
    private ArrayList<workout> workout_schedule;
    private OnItemClickListener listener;
    private int select_pos = -1;
    public interface OnItemClickListener{
        void onItemClick(int pos, View v);
        void editWorkout(int pos);
    }
    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

    public static class workoutViewHolder extends RecyclerView.ViewHolder{

        public TextView textView;
        public ImageView edit_button;
        public workoutViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);
            itemView.setClickable(true);
            textView = itemView.findViewById(R.id.textview);
            edit_button = itemView.findViewById(R.id.edit_view);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null){
                        int pos = getAdapterPosition();
                        if (pos != RecyclerView.NO_POSITION){
                            listener.onItemClick(pos, v);
                        }
                    }
                }
            });
            edit_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null){
                        int pos = getAdapterPosition();
                        if (pos != RecyclerView.NO_POSITION){
                            listener.editWorkout(pos);
                        }
                    }
                }
            });
        }

    }

    public workoutAdapter (ArrayList<workout> ws){
        workout_schedule = ws;
    }
    @NonNull
    @Override
    public workoutViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.workout_item, parent, false);
        workoutViewHolder wvh = new workoutViewHolder(v, this.listener);
        return wvh;
    }

    @Override
    public void onBindViewHolder(@NonNull workoutViewHolder holder, int position) {
        workout currentItem = workout_schedule.get(position);
        //holder.textView.setSelected(true);
        if (select_pos == position) {
            holder.textView.setTextColor(Color.parseColor("#99ccff"));
        }
        else{
            holder.textView.setTextColor(Color.parseColor("#000000"));
        }

        holder.textView.setText(currentItem.getName());
    }

    @Override
    public int getItemCount() {
        return workout_schedule.size();
    }

    public void setSelect_pos(int n){
        if (select_pos == n){
            select_pos = -1;
        }
        else {
            select_pos = n;
        }
    }
}
