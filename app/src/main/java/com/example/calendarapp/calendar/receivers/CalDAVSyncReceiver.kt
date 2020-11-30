package com.example.calendarapp.calendar.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.calendarapp.calendar.extensions.config
import com.example.calendarapp.calendar.extensions.recheckCalDAVCalendars
import com.example.calendarapp.calendar.extensions.refreshCalDAVCalendars

class CalDAVSyncReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (context.config.caldavSync) {
            context.refreshCalDAVCalendars(context.config.caldavSyncedCalendarIds, false)
        }

        context.recheckCalDAVCalendars {
        }
    }
}
