package com.example.calendarapp.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.Toast;

import com.example.calendarapp.R;
import com.example.calendarapp.ScheduleActivity;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CalendarFragment# newInstance} factory method to
 * create an instance of this fragment.
 */
public class CalendarFragment extends Fragment {

    private CalendarView calendarView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);


        // Inflate the layout for this fragment
        return view;
    }



    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState){

        calendarView = getView().findViewById(R.id.calender);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
                //Toast.makeText(getActivity().getApplicationContext(), ""+i2, Toast.LENGTH_SHORT).show();
                Date date = new GregorianCalendar(i, i1, i2).getTime();
                Intent intent = new Intent(CalendarFragment.this.getActivity(), ScheduleActivity.class);
                intent.putExtra("DATE", date.getTime());
                Objects.requireNonNull(getContext()).startActivity(intent);
            }
        });
    }
}