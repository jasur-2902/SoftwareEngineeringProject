package com.example.calendarapp.Adapter;

import android.telephony.DataFailCause;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.calendarapp.Model.ScheduleResult;
import com.example.calendarapp.R;

import java.util.HashMap;
import java.util.List;

public class MatchActivityAdapter extends RecyclerView.Adapter<MatchActivityAdapter.ViewHolder> {

    List<ScheduleResult> scheduleResults;
    private RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();

    public MatchActivityAdapter(List<ScheduleResult> scheduleResults) {
        this.scheduleResults = scheduleResults;
    }

    @NonNull
    @Override
    public MatchActivityAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_section_header, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MatchActivityAdapter.ViewHolder holder, int position) {
        ScheduleResult scheduleResult = scheduleResults.get(position);
        holder.dateName.setText(scheduleResult.getDayText());
        LinearLayoutManager layoutManager = new LinearLayoutManager(holder.ChildRecyclerView.getContext(), LinearLayoutManager.VERTICAL, false);
        layoutManager.setInitialPrefetchItemCount(scheduleResult.getDayAnswers().size());
        DayListAdapter dayListAdapter = new DayListAdapter(scheduleResult.getDayAnswers());
        holder.ChildRecyclerView.setLayoutManager(layoutManager);
        holder.ChildRecyclerView.setAdapter(dayListAdapter);
        holder.ChildRecyclerView.setRecycledViewPool(viewPool);
        boolean isExpandable = scheduleResults.get(position).isExpandable();
        holder.expandableLayout.setVisibility(isExpandable ? View.VISIBLE : View.GONE);

    }

    @Override
    public int getItemCount() {
        return scheduleResults.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView dateName;
        RecyclerView ChildRecyclerView;
        LinearLayout linearLayout;
        RelativeLayout expandableLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            dateName = itemView.findViewById(R.id.date_name);
            ChildRecyclerView = itemView.findViewById(R.id.child_recyclerview);
            linearLayout = itemView.findViewById(R.id.linearlayoutheader);
            expandableLayout = itemView.findViewById(R.id.layoutexpandable);


            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ScheduleResult scheduleResult = scheduleResults.get(getAdapterPosition());
                    scheduleResult.setExpandable(!scheduleResult.isExpandable());
                    notifyItemChanged(getAdapterPosition());
                }
            });
        }
    }
}
