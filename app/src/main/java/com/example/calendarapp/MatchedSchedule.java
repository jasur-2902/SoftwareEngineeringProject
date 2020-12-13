package com.example.calendarapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

//import com.example.calendarapp.Adapter.DayListAdapter;
//import com.example.calendarapp.Adapter.MatchActivityAdapter;
import com.example.calendarapp.Adapter.MatchActivityAdapter;
import com.example.calendarapp.Model.DayAnswer;
import com.example.calendarapp.Model.Schedule;
import com.example.calendarapp.Model.ScheduleResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MatchedSchedule extends AppCompatActivity {

    //MatchActivityAdapter matchActivityAdapter;

    private DatabaseReference mDatabase, mDatabaseReciever;

    HashMap<Integer, String> daysOfWeek = new HashMap<>();

    HashMap<Integer, HashMap<Integer, Boolean>> available = new HashMap<>();

    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matched_schedule);

        /**
         *
         * Algorithm for schedule mathcing starts here
         *
         * */


        daysOfWeek.put(1, "Monday");
        daysOfWeek.put(2, "Tuesday");
        daysOfWeek.put(3, "Wednesday");
        daysOfWeek.put(4, "Thursday");
        daysOfWeek.put(5, "Friday");
        daysOfWeek.put(6, "Saturday");
        daysOfWeek.put(7, "Sunday");

        for (int i = 1; i<8; i++){
            HashMap<Integer, Boolean> available_hours = new HashMap<>();  //this is hash for available hour listing

            for(int j=0; j<24; j++) {
                available_hours.put(j, true);
            }
            available.put(i, available_hours);

        }

        //final TextView tvTest = findViewById(R.id.tvTest);
        //final TextView dayTest = findViewById(R.id.textview_section_header_yes);

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

                    /**
                     *
                     * Algorithm for schedule matching ends here
                     *
                     * */



                    // *** IMPORTANT *** //
                    //Result saved in nested hashmap available


                    // Structure of the Result Hashmap
                    // HashMap<Integer, HashMap<Integer, Boolean>> available = new HashMap<>();
                    /**
                     * 1:
                     *   0: true
                     *   1: true
                     *   2: true
                     *   3: false
                     *   4: true
                     *   5: true
                     *   ...
                     *   23: true
                     * 2:
                     *  0
                     *  1
                     *  2
                     *  3
                     *  4
                     *  ...
                     *
                     *  1 stands for monday
                     *  7 stands for sunday
                     *
                     *
                     *
                    */

                    List<ScheduleResult> parentListing = new ArrayList<>();
                    //List<DayAnswer> childListening = new ArrayList<>();
                    for (int acc : available.keySet()) {
                        HashMap<Integer, Boolean> abb = available.get(acc);
                        List<DayAnswer> childListening = new ArrayList<>();

                        //for loop of childlistening here
                        for(int i = 0; i<24; i++){
                            assert abb != null;
                            if (abb.get(i) == true){
                                int x = numLogic(i);
                                if (x <= 0 && i <= 11) {
                                    childListening.add(new DayAnswer(i, abb.get(i), "AM")); //this is for AM timing
                                } else if (x > 0 && i <= 11){
                                    childListening.add(new DayAnswer(x, abb.get(i), "AM")); //this is for 12 AM, the first hour
                                } else if (x == 0 && i == 12){
                                    childListening.add(new DayAnswer(i, abb.get(i), "PM"));
                                } else {
                                    childListening.add(new DayAnswer(x, abb.get(i), "PM")); //this is for PM timing
                                }
                            }
                            //childListening.add(new DayAnswer(i, abb.get(i)));
                        }
                        parentListing.add(new ScheduleResult(daysOfWeek.get(acc), childListening));
                    }



                recyclerView = findViewById(R.id.recyclerinschedule);
                MatchActivityAdapter matchActivityAdapter = new MatchActivityAdapter(parentListing);
                recyclerView.setAdapter(matchActivityAdapter);
                recyclerView.setHasFixedSize(false);
                LinearLayoutManager layoutManager = new LinearLayoutManager(MatchedSchedule.this);
                recyclerView.setLayoutManager(layoutManager);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        mDatabase.addValueEventListener(postListener);
        mDatabaseReciever.addValueEventListener(postListener);

    }

    //function that helps display time in PM
    private int numLogic(int i){
        if (i == 0){
            return 12;
        } else {
            i -= 12;
            return i;
        }
    }


}