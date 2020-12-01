 package com.example.calendarapp;

 import androidx.appcompat.app.AppCompatActivity;

 import android.content.Intent;
 import android.graphics.Color;
 import android.os.Bundle;
 import android.util.Log;
 import android.view.View;
 import android.widget.Button;
 import android.widget.ImageView;
 import android.widget.LinearLayout;
 import android.widget.RelativeLayout;
 import android.widget.TextView;

 import com.example.calendarapp.Model.Event;
 import com.google.firebase.auth.FirebaseAuth;
 import com.google.firebase.auth.FirebaseUser;
 import com.google.firebase.database.DatabaseReference;
 import com.google.firebase.database.FirebaseDatabase;

 import java.text.SimpleDateFormat;
 import java.util.Calendar;
 import java.util.Date;
 import java.util.HashMap;
 import java.util.List;
 import androidx.appcompat.app.AppCompatActivity;
 import android.os.Bundle;
 import java.util.Locale;

        //get the date
        public class ScheduleActivity extends AppCompatActivity {
            private static final String TAG = MainActivity.class.getSimpleName();
            private ImageView previousDay;
            private ImageView nextDay;
            private TextView currentDate;
            private Calendar cal = Calendar.getInstance();
            //private DatabaseQuery mQuery;
            private RelativeLayout mLayout;
            private int eventIndex;
            private DatabaseReference databaseReference;
            private FirebaseUser firebaseUser;
            private Button addEventButton;

            @Override
            protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_schedule);

                //get the date
                //get the references from XML to here
                mLayout = findViewById(R.id.left_event_column);
                eventIndex = mLayout.getChildCount();
                currentDate = findViewById(R.id.display_current_date);
                addEventButton = findViewById(R.id.event_create_button);

                Intent intent = getIntent();

                if (intent != null){
                    int year = intent.getIntExtra("year", 1970);
                    int month = intent.getIntExtra("month", 1);
                    int dayOfMonth = intent.getIntExtra("dayOfMonth", 1);
                }

                final Date d = new Date(getIntent().getLongExtra("DATE", -1));
                //retrieve the current formatted time of weekdaytext/month/day
                currentDate.setText(displayDateInString(d));
                //
                //displayDailyEvents();

                //get references of the two buttons that switch th days and move then to another event sheet
                previousDay = findViewById(R.id.previous_day);
                previousDay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        previousCalendarDate();
                    }
                });

                nextDay = findViewById(R.id.next_day);
                nextDay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        nextCalendarDate();
                    }
                });

                addEventButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        setEvent(displayDateInString(d));
                    }
                });

            }

            //
            private void previousCalendarDate(){
                mLayout.removeViewAt(eventIndex - 1);
                cal.add(Calendar.DAY_OF_MONTH, -1);
                currentDate.setText(displayDateInString(cal.getTime()));
                //displayDailyEvents();
            }
            private void nextCalendarDate(){
                mLayout.removeViewAt(eventIndex - 1);
                cal.add(Calendar.DAY_OF_MONTH, 1);
                currentDate.setText(displayDateInString(cal.getTime()));
                //displayDailyEvents();
            }

            private String displayDateInString(Date date){
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMMM, yyyy", Locale.ENGLISH);
                return simpleDateFormat.format(date);
            }

            private void setEvent(String date) {
                //first create a button that sets the event
                //have that in between the time stamps and the the date display
                //should we make this a new activity or not, still gotta decide
                Intent intentEventDay = new Intent(ScheduleActivity.this, EventCreationActivity.class);
                intentEventDay.putExtra("day", date);
                startActivity(intentEventDay);
            }

            private void editEvent(){
                //have the event already here
                //user clicks on the box to edit event
            }

            private int getEventTimeFrame(Date start, Date end){
                long timeDifference = end.getTime() - start.getTime();
                Calendar mCal = Calendar.getInstance();
                mCal.setTimeInMillis(timeDifference);
                int hours = mCal.get(Calendar.HOUR);
                int minutes = mCal.get(Calendar.MINUTE);
                return (hours * 60) + ((minutes * 60) / 100);
            }

            private void displayEventSection(Date eventDate, int height, String message){
                SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
                String displayValue = timeFormatter.format(eventDate);
                String[]hourMinutes = displayValue.split(":");
                int hours = Integer.parseInt(hourMinutes[0]);
                int minutes = Integer.parseInt(hourMinutes[1]);
                Log.d(TAG, "Hour value " + hours);
                Log.d(TAG, "Minutes value " + minutes);
                int topViewMargin = (hours * 60) + ((minutes * 60) / 100);
                Log.d(TAG, "Margin top " + topViewMargin);
                createEventView(topViewMargin, height, message);
            }

            private void createEventView(int topMargin, int height, String message){
                TextView mEventView = new TextView(ScheduleActivity.this);
                RelativeLayout.LayoutParams lParam = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                lParam.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                lParam.topMargin = topMargin * 2;
                lParam.leftMargin = 24;
                mEventView.setLayoutParams(lParam);
                mEventView.setPadding(24, 0, 24, 0);
                mEventView.setHeight(height * 2);
                mEventView.setGravity(0x11);
                mEventView.setTextColor(Color.parseColor("#ffffff"));
                mEventView.setText(message);
                mEventView.setBackgroundColor(Color.parseColor("#3F51B5"));
                mLayout.addView(mEventView, eventIndex - 1);
            }
        }
