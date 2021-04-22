package com.example;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.jar.Attributes;

public class workout implements Parcelable {

    private String name;
    private int sets;
    private int reps;
    private long timer;

    /*constructor*/
    public workout() {
        sets = -1;
        reps = -1;
        timer = -1;
        name = null;
    }

    /*constructor for set and reps*/
    public workout(String name, int sets, int reps){
        this.sets = sets;
        this.reps = reps;
        this.name = name;
        this.timer = -1;
    }

    /*constructor for timer*/
    public workout(String name, long timer){
        this.timer = timer;
        this.name = name;
        this.sets = -1;
        this.reps = -1;
    }

    protected workout(Parcel source){
        this.name = source.readString();
        this.sets = source.readInt();
        this.reps = source.readInt();
        this.timer= source.readLong();
    }
    public static final Creator<workout> CREATOR = new Creator<workout>() {
        @Override
        public workout createFromParcel(Parcel source) {
            return new workout(source);
        }

        @Override
        public workout[] newArray(int size) {
            return new workout[size];
        }
    };

    /*getters*/
    public void set_sets(int sets){
        this.sets = sets;
    }

    public int getSets() {
        return sets;
    }

    public int getReps() {
        return reps;
    }

    public String getName() {
        return name;
    }

    public long getTimer() {
        return timer;
    }

    /*setters*/
    public void setSets(int sets) {
        this.sets = sets;
    }

    public void setReps(int reps) {
        this.reps = reps;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTimer(long timer) {
        this.timer = timer;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeInt(sets);
        dest.writeInt(reps);
        dest.writeLong(timer);

    }
}
