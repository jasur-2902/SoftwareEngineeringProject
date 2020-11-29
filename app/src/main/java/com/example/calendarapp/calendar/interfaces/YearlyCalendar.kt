package com.example.calendarapp.calendar.interfaces

import android.util.SparseArray
import com.example.calendarapp.calendar.models.DayYearly
import java.util.*

interface YearlyCalendar {
    fun updateYearlyCalendar(events: SparseArray<ArrayList<DayYearly>>, hashCode: Int)
}
