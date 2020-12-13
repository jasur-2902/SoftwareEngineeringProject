package com.example.calendarapp.Adapter;

import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.calendarapp.Model.DayAnswer;
import com.example.calendarapp.R;

import java.util.List;

public class DayListAdapter extends RecyclerView.Adapter<DayListAdapter.ViewHolder> {

    private List<DayAnswer> dayAnswers;

    public DayListAdapter(List<DayAnswer> dayAnswers) {
        this.dayAnswers = dayAnswers;
    }


    @NonNull
    @Override
    public DayListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.match_activity_row_layout, parent, false);
        return new DayListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DayListAdapter.ViewHolder holder, int position) {
        DayAnswer dayAnswer = dayAnswers.get(position);
        holder.daynum.setText(String.valueOf(dayAnswer.getHournum()));
        holder.mornight.setText(dayAnswer.getAmpm());
        boolean statusCheck = dayAnswers.get(position).isAvailiabilitystatus();
        holder.status.setVisibility(statusCheck ? View.VISIBLE : View.GONE);


    }

    @Override
    public int getItemCount() {
        return dayAnswers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView daynum, mornight, status;

        public ViewHolder(@NonNull View itemView){
            super(itemView);
            daynum = itemView.findViewById(R.id.DateNumber);
            mornight = itemView.findViewById(R.id.AMPMDecide);
            status = itemView.findViewById(R.id.AvailablityAnswer);
        }
    }

}