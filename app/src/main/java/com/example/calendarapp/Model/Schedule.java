package com.example.calendarapp.Model;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class Schedule {

    public String key;
    public Map<String, Object> td = new HashMap<>();

    public Schedule() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }

    public Schedule(String key, Map<String, Object> td) {
        this.key = key;
        this.td = td;
    }


}
