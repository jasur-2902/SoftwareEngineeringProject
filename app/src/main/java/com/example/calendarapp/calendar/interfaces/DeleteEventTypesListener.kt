package com.example.calendarapp.calendar.interfaces

import com.example.calendarapp.calendar.models.EventType
import java.util.*

interface DeleteEventTypesListener {
    fun deleteEventTypes(eventTypes: ArrayList<EventType>, deleteEvents: Boolean): Boolean
}
