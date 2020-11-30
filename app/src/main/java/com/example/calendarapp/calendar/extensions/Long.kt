package com.example.calendarapp.calendar.extensions

import com.example.calendarapp.calendar.helpers.Formatter
import com.example.calendarapp.calendar.models.Event

fun Long.isTsOnProperDay(event: Event): Boolean {
    val dateTime = Formatter.getDateTimeFromTS(this)
    val power = Math.pow(2.0, (dateTime.dayOfWeek - 1).toDouble()).toInt()
    return event.repeatRule and power != 0
}
