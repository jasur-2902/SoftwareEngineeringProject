package com.example.calendarapp.calendar.fragments

import android.os.Bundle
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.calendarapp.R
import com.example.calendarapp.calendar.activities.CalendarActivity
import com.example.calendarapp.calendar.extensions.config
import com.example.calendarapp.calendar.extensions.getViewBitmap
import com.example.calendarapp.calendar.extensions.printBitmap

import com.example.calendarapp.calendar.helpers.YEAR_LABEL
import com.example.calendarapp.calendar.helpers.YearlyCalendarImpl
import com.example.calendarapp.calendar.interfaces.YearlyCalendar
import com.example.calendarapp.calendar.models.DayYearly
import com.example.calendarapp.calendar.views.SmallMonthView
import com.simplemobiletools.commons.extensions.getAdjustedPrimaryColor
import com.simplemobiletools.commons.extensions.updateTextColors

import kotlinx.android.synthetic.main.fragment_year.view.*
import org.joda.time.DateTime
import java.util.*

class YearFragment : Fragment(), YearlyCalendar {
    private var mYear = 0
    private var mSundayFirst = false
    private var isPrintVersion = false
    private var lastHash = 0
    private var mCalendar: YearlyCalendarImpl? = null

    lateinit var mView: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mView = inflater.inflate(R.layout.fragment_year, container, false)
        mYear = arguments!!.getInt(YEAR_LABEL)
        context!!.updateTextColors(mView.calendar_holder)
        setupMonths()

        mCalendar = YearlyCalendarImpl(this, context!!, mYear)
        return mView
    }

    override fun onPause() {
        super.onPause()
        mSundayFirst = context!!.config.isSundayFirst
    }

    override fun onResume() {
        super.onResume()
        val sundayFirst = context!!.config.isSundayFirst
        if (sundayFirst != mSundayFirst) {
            mSundayFirst = sundayFirst
            setupMonths()
        }
        updateCalendar()
    }

    fun updateCalendar() {
        mCalendar?.getEvents(mYear)
    }

    private fun setupMonths() {
        val dateTime = DateTime().withDate(mYear, 2, 1).withHourOfDay(12)
        val days = dateTime.dayOfMonth().maximumValue
        mView.month_2.setDays(days)

        val now = DateTime()

        for (i in 1..12) {
            val monthView = mView.findViewById<SmallMonthView>(resources.getIdentifier("month_$i", "id", context!!.packageName))
            var dayOfWeek = dateTime.withMonthOfYear(i).dayOfWeek().get()
            if (!mSundayFirst) {
                dayOfWeek--
            }

            val monthLabel = mView.findViewById<TextView>(resources.getIdentifier("month_${i}_label", "id", context!!.packageName))
            val curTextColor = when {
                isPrintVersion -> resources.getColor(R.color.theme_light_text_color)
                else -> context!!.config.textColor
            }

            monthLabel.setTextColor(curTextColor)

            monthView.firstDay = dayOfWeek
            monthView.setOnClickListener {
                (activity as CalendarActivity).openMonthFromYearly(DateTime().withDate(mYear, i, 1))
            }
        }

        if (!isPrintVersion) {
            markCurrentMonth(now)
        }
    }

    private fun markCurrentMonth(now: DateTime) {
        if (now.year == mYear) {
            val monthLabel = mView.findViewById<TextView>(resources.getIdentifier("month_${now.monthOfYear}_label", "id", context!!.packageName))
            monthLabel.setTextColor(context!!.getAdjustedPrimaryColor())

            val monthView = mView.findViewById<SmallMonthView>(resources.getIdentifier("month_${now.monthOfYear}", "id", context!!.packageName))
            monthView.todaysId = now.dayOfMonth
        }
    }

    override fun updateYearlyCalendar(events: SparseArray<ArrayList<DayYearly>>, hashCode: Int) {
        if (!isAdded)
            return

        if (hashCode == lastHash) {
            return
        }

        lastHash = hashCode
        for (i in 1..12) {
            val monthView = mView.findViewById<SmallMonthView>(resources.getIdentifier("month_$i", "id", context!!.packageName))
            monthView.setEvents(events.get(i))
        }
    }

    fun printCurrentView() {
        isPrintVersion = true
        setupMonths()
        toggleSmallMonthPrintModes()

        context!!.printBitmap(mView.calendar_holder.getViewBitmap())

        isPrintVersion = false
        setupMonths()
        toggleSmallMonthPrintModes()
    }

    private fun toggleSmallMonthPrintModes() {
        for (i in 1..12) {
            val monthView = mView.findViewById<SmallMonthView>(resources.getIdentifier("month_$i", "id", context!!.packageName))
            monthView.togglePrintMode()
        }
    }
}
