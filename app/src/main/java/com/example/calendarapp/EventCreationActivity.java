package com.example.calendarapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EventCreationActivity extends AppCompatActivity {

    private TextView eventDateDisplay;
    private TimePicker timeStart, timeEnd;
    private EditText title, description;
    private RadioGroup priorityRadioGroup;
    private RadioButton priorityButton;
    private Button setEventButton;
    private int answerPriority;

    private FirebaseUser firebaseUser;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_creation);

        String date = getIntent().getStringExtra("day");

        //fetch reference from database and continue from there by setting everything up in the UI
        //to match everything and build on from there

        eventDateDisplay = findViewById(R.id.event_date_card);
        title = findViewById(R.id.event_title);
        description = findViewById(R.id.event_description);
        //yes = findViewById(R.id.available_button);
        //maybe = findViewById(R.id.chance_button);
        //no = findViewById(R.id.not_available_button);
        priorityRadioGroup = findViewById(R.id.radio_group_priority);

        timeStart = findViewById(R.id.time_start);
        timeStart.setIs24HourView(false);
        timeEnd = findViewById(R.id.time_finish);
        timeEnd.setIs24HourView(false);

        setEventButton = findViewById(R.id.publish_event_button);

        eventDateDisplay.setText(date);

        // TODO: create radio input functionality
        //  title must be inputted
        //  description is optional
        //  the continue button must check all of these before its a go

        //create other info of data references
        //send the answerpriority through the intent, it'll change the color of the sqaure
        //and be used as reference for other friend-end users

        //firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        //databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid()).child("Events");



        //this has to be a method, too much code here
        setEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submit();
            }
        });
    }


    //checks to see which button is pressed and saves it in a variable
    public void checkButton(View v) {
        int radioId = priorityRadioGroup.getCheckedRadioButtonId();
        priorityButton = findViewById(radioId);

        switch (radioId){
            case R.id.available_button:
                answerPriority = 1;
                break;
            case R.id.chance_button:
                answerPriority = 2;
                break;
            case R.id.not_available_button:
                answerPriority = 3;
                break;
            default:
                break;
        }

    }

    public void submit(){

        //Setup the intents
        Intent intent = new Intent(EventCreationActivity.this, ScheduleActivity.class);
        /*
        //part 1 - make sure time is set
        int hourStart = 0;
        int hourEnd = 0;
        int minuteStart, minuteEnd;
        String am_pm_start, am_pm_end;

        if (Build.VERSION.SDK_INT >= 23){
            hourStart = timeStart.getHour();
            hourEnd = timeEnd.getHour();
            minuteStart = timeStart.getMinute();
            minuteEnd = timeEnd.getMinute();
        }

        if (hourStart > 12){
            am_pm_start = "PM";
            hourStart -= 12;
        } else {
            am_pm_start = "AM";
        }

        if (hourEnd > 12) {
            am_pm_end = "PM";
            hourEnd -= 12;
        } else {
            am_pm_end = "AM";
        }*/

        //TODO: parse through the data to see if the times can potentially overlap other events, because
        //that would be bad
        String eventTitle = title.getText().toString();
        //part 2 - make sure that we send out the priorityAnswer, either 1 2 or 3
        if (answerPriority == 1 || answerPriority == 2 || answerPriority ==3){
            //push to firebase database
            if (!TextUtils.isEmpty(eventTitle)){
                //push to firebase database
                startActivity(intent);
            } else {
                Toast.makeText(EventCreationActivity.this, "Please fill in the requirements", Toast.LENGTH_SHORT).show();
            }
        } else {
            //output error
            Toast.makeText(EventCreationActivity.this, "Please fill in the requirements", Toast.LENGTH_SHORT).show();
        }
        //part 3 - make sure the title is at least filled out , out of the two

        //use if else clause to check all of this, then output to database when all done

    }
}