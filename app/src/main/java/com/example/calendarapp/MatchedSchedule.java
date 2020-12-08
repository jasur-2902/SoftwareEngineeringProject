package com.example.calendarapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.calendarapp.Model.Schedule;
import com.example.calendarapp.calendar.extensions.TextViewKt;
import com.google.firebase.FirebaseError;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MatchedSchedule extends AppCompatActivity {

    private DatabaseReference mDatabase, mDatabaseReciever;

    HashMap<Integer, String> daysOfWeek = new HashMap<>();

    HashMap<Integer, HashMap<Integer, Boolean>> available = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matched_schedule);

        daysOfWeek.put(1, "Monday");
        daysOfWeek.put(2, "Tuesday");
        daysOfWeek.put(3, "Wednesday");
        daysOfWeek.put(4, "Thursday");
        daysOfWeek.put(5, "Friday");
        daysOfWeek.put(6, "Saturday");
        daysOfWeek.put(7, "Sunday");


        for (int i = 1; i<8; i++){
            HashMap<Integer, Boolean> available_hours = new HashMap<>();

            for(int j=0; j<24; j++) {
                available_hours.put(j, true);
            }
            available.put(i, available_hours);

        }



        final TextView tvTest = findViewById(R.id.tvTest);



        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(getIntent().getStringExtra("senderUserID")).child("schedule");
        mDatabaseReciever = FirebaseDatabase.getInstance().getReference().child("Users").child(getIntent().getStringExtra("receiverUserID")).child("schedule");

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                    ArrayList<Object> td = (ArrayList<Object>) dataSnapshot.getValue();

                    for(int i = 0; i< td.size(); i++){
                        if(td.get(i) != null) {
                            Map<String, Object> arr = (Map<String, Object>) td.get(i);



                            for (int j = 0; j < 25; j++) {
                                if (arr.containsKey(String.valueOf(j))) {
                                    int x =j;
                                    Map<String, Object> arr2 = (Map<String, Object>) arr.get(String.valueOf(j));
                                    if(arr2.get("endHour") != null) {
                                        while (Integer.valueOf(arr2.get("endHour").toString()) >= x) {
                                            Log.d("NEWTESSST", String.valueOf(j));
                                            available.get(i).put(x, false);
                                            x++;
                                        }
                                        j = x;
                                    }
                                }
                            }
                        }
                    }

                    String result = "";

                    for (int acc : available.keySet()) {
                        result = result + daysOfWeek.get(acc) + ":\n";
                        HashMap<Integer, Boolean> abb = available.get(acc);
                        for(int i = 0; i<25; i++){
//                            if(abb.containsKey(i) && abb.get(i) == true)
                                result = result  + i + " - " + abb.get(i) + "\n";
                        }
                        result = result + "\n";
                    }
                     // }

                    tvTest.setText(result);


                // ...
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        mDatabase.addValueEventListener(postListener);
        mDatabaseReciever.addValueEventListener(postListener);

    }
}