package com.example.calendarapp.calendar.extensions

import com.example.calendarapp.calendar.helpers.MONTH
import com.example.calendarapp.calendar.helpers.WEEK
import com.example.calendarapp.calendar.helpers.YEAR

fun Int.isXWeeklyRepetition() = this != 0 && this % WEEK == 0

fun Int.isXMonthlyRepetition() = this != 0 && this % MONTH == 0

fun Int.isXYearlyRepetition() = this != 0 && this % YEAR == 0
