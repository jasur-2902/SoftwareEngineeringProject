package com.example.calendarapp.Model;

import java.util.Date;

public class Event {

    private int id;
    private int status; // 1 for no // 2 for maybe // 3 for yes but we need it so its for everything
    private String message;
    private Date date;
    private Date end;

    public Event(){

    }

    /*
    public Event(String message, Date date, Date end) {
        this.message = message;
        this.date = date;
        this.end = end;
    }*/

    public Event(int id, int status, String message, Date date, Date end) {
        this.id = id;
        this.status = status;
        this.message = message;
        this.date = date;
        this.end = end;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }
}

