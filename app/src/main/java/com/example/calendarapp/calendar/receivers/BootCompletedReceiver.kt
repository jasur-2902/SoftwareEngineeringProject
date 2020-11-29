package com.example.calendarapp.calendar.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.calendarapp.calendar.extensions.recheckCalDAVCalendars
import com.example.calendarapp.calendar.extensions.scheduleAllEvents
import com.simplemobiletools.commons.helpers.ensureBackgroundThread


class BootCompletedReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        ensureBackgroundThread {
            context.apply {
                scheduleAllEvents()
                //notifyRunningEvents()
                recheckCalDAVCalendars {}
            }
        }
    }
}
