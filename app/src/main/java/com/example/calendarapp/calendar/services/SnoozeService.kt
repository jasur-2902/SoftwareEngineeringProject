package com.example.calendarapp.calendar.services

import android.app.IntentService
import android.content.Intent
import com.example.calendarapp.calendar.extensions.config
import com.example.calendarapp.calendar.extensions.eventsDB
import com.example.calendarapp.calendar.extensions.rescheduleReminder
import com.example.calendarapp.calendar.helpers.EVENT_ID

class SnoozeService : IntentService("Snooze") {
    override fun onHandleIntent(intent: Intent?) {
        if (intent != null) {
            val eventId = intent.getLongExtra(EVENT_ID, 0L)
            val event = eventsDB.getEventWithId(eventId)
            rescheduleReminder(event, config.snoozeTime)
        }
    }
}
