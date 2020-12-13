package com.example.calendarapp.Model;

import java.util.HashMap;
import java.util.List;

public class ScheduleResult {

    private String dayText;
    private List<DayAnswer> dayAnswers;
    private boolean expandable;

    public boolean isExpandable() {
        return expandable;
    }

    public void setExpandable(boolean expandable) {
        this.expandable = expandable;
    }

    public ScheduleResult(String dayText, List<DayAnswer> dayAnswers) {
        this.dayText = dayText;
        this.dayAnswers = dayAnswers;
        this.expandable = false;
    }

    public String getDayText() {
        return dayText;
    }

    public void setDayText(String dayText) {
        this.dayText = dayText;
    }

    public List<DayAnswer> getDayAnswers() {
        return dayAnswers;
    }

    public void setDayAnswers(List<DayAnswer> dayAnswers) {
        this.dayAnswers = dayAnswers;
    }
}
