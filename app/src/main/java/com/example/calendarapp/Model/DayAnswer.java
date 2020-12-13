package com.example.calendarapp.Model;

public class DayAnswer {

    private int hournum;
    private boolean availiabilitystatus;
    private String ampm;

    public DayAnswer(int hournum, boolean availiabilitystatus, String ampm) {
        this.hournum = hournum;
        this.availiabilitystatus = availiabilitystatus;
        this.ampm = ampm;
    }

    public int getHournum() {
        return hournum;
    }

    public void setHournum(int hournum) {
        this.hournum = hournum;
    }

    public boolean isAvailiabilitystatus() {
        return availiabilitystatus;
    }

    public void setAvailiabilitystatus(boolean availiabilitystatus) {
        this.availiabilitystatus = availiabilitystatus;
    }

    public String getAmpm() {
        return ampm;
    }

    public void setAmpm(String ampm) {
        this.ampm = ampm;
    }
}
