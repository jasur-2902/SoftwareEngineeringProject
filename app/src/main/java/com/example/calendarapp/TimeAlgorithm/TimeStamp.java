package com.example.calendarapp.TimeAlgorithm;

public class TimeStamp{
    private int weekday;
    private int hour;
    private int minute;
    private int second;
    private String[] weekdays = {"Monday","Tuesday","Wednesday","Thursday","Friday","Saturday","Sunday"};

    // default constructor
    public TimeStamp (){
        weekday = 0;
        hour = 0;
        minute = 0;
        second = 0;
    }

    // constructor weekday is input as a number 1-7, 1 is Sunday
    public TimeStamp(String weekDay, int hour, int minute, int second){
        int weekday=0;
        for(int i = 0; i< weekdays.length; i++){
            if (weekDay.equals(weekdays[i])){
                weekday = i;
                break;
            }
        }
        this.weekday = weekday;
        this.hour = hour;
        this.minute = minute;
        this.second = second;
    }

    // check to see if this timestamp is before the other
    public boolean isBefore(TimeStamp stamp2){
        // comparing hour
        if (this.hour < stamp2.hour){
            return true;
        }
        else if (this.hour > stamp2.hour){
            return false;
        }
        else{
            // comparing minute
            if(this.minute < stamp2.minute){
                return true;
            }
            else if (this.minute > stamp2.minute){
                return false;
            }
            else{
                // comparing second
                if(this.second<stamp2.second){
                    return true;
                }
                else if (this.second > stamp2.second){
                    return false;
                }
                else return true;
            }
        }
    }

    public boolean isAfter(TimeStamp stamp2){
        return !isBefore(stamp2);
    }

    public String toString(){
        return weekday + " " + hour + " " + minute + " " + second;
    }
    // getters and setters
    public int getWeekDay(){
        return weekday;
    }
    public int getHour(){
        return hour;
    }
    public int getMinute(){
        return minute;
    }
    public int getSecond(){
        return second;
    }

    public void setWeekDay(int weekday){
        this.weekday = weekday;
    }
    public void setHour(int hour){
        this.hour = hour;
    }
    public void setMinute(int minute){
        this.minute =minute;
    }
    public void setSecond(int second){
        this.second = second;
    }

}
