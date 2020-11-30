package com.example.calendarapp.calendar.interfaces

import com.example.calendarapp.calendar.models.Event

interface WeeklyCalendar {
    fun updateWeeklyCalendar(events: ArrayList<Event>)
}
