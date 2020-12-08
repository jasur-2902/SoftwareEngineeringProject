package com.example.calendarapp.calendar.activities


import android.annotation.SuppressLint
import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.content.pm.ShortcutInfo
import android.content.pm.ShortcutManager
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Icon
import android.graphics.drawable.LayerDrawable
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuItemCompat
import com.example.calendarapp.BuildConfig
import com.example.calendarapp.FindFriendsActivity
import com.example.calendarapp.R
import com.example.calendarapp.StartActivity
import com.example.calendarapp.calendar.adapters.EventListAdapter
import com.example.calendarapp.calendar.databases.EventsDatabase
import com.example.calendarapp.calendar.extensions.*
import com.example.calendarapp.calendar.fragments.*
import com.example.calendarapp.calendar.helpers.*
import com.example.calendarapp.calendar.helpers.Formatter
import com.example.calendarapp.calendar.models.ListEvent
import com.google.firebase.auth.FirebaseAuth
import com.simplemobiletools.commons.dialogs.RadioGroupDialog
import com.simplemobiletools.commons.extensions.*
import com.simplemobiletools.commons.helpers.*
import com.simplemobiletools.commons.interfaces.RefreshRecyclerViewListener
import com.simplemobiletools.commons.models.RadioItem
import kotlinx.android.synthetic.main.activity_calendar.*
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import java.util.*
import kotlin.collections.ArrayList

class CalendarActivity : SimpleActivity(), RefreshRecyclerViewListener {
    private val PICK_IMPORT_SOURCE_INTENT = 1
    private val PICK_EXPORT_FILE_INTENT = 2

    private var showCalDAVRefreshToast = false
    private var mShouldFilterBeVisible = false
    private var mIsSearchOpen = false
    private var mLatestSearchQuery = ""
    private var mSearchMenuItem: MenuItem? = null
    private var shouldGoToTodayBeVisible = false
    private var goToTodayButton: MenuItem? = null
    private var currentFragments = ArrayList<MyFragmentHolder>()
    private var eventTypesToExport = ArrayList<Long>()

    private var mStoredTextColor = 0
    private var mStoredBackgroundColor = 0
    private var mStoredPrimaryColor = 0
    private var mStoredDayCode = ""
    private var mStoredIsSundayFirst = false
    private var mStoredUse24HourFormat = false
    private var mStoredDimPastEvents = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar)
        appLaunched(BuildConfig.APPLICATION_ID)

        calendar_fab.beVisibleIf(config.storedView != YEARLY_VIEW && config.storedView != WEEKLY_VIEW)
        calendar_fab.setOnClickListener {
            launchNewEventIntent(currentFragments.last().getNewEventDayCode())
        }

        storeStateVariables()

        if (!hasPermission(PERMISSION_WRITE_CALENDAR) || !hasPermission(PERMISSION_READ_CALENDAR)) {
            config.caldavSync = false
        }

        if (config.caldavSync) {
            refreshCalDAVCalendars(false)
        }

        swipe_refresh_layout.setOnRefreshListener {
            refreshCalDAVCalendars(false)
        }

        checkIsViewIntent()

        if (!checkIsOpenIntent()) {
            updateViewPager()
        }

        checkAppOnSDCard()


//        if (!config.wasUpgradedFromFreeShown && isPackageInstalled("com.simplemobiletools.calendar")) {
//            ConfirmationDialog(this, "", R.string.upgraded_from_free, R.string.ok, 0) {}
//            config.wasUpgradedFromFreeShown = true
//        }
    }

    override fun onResume() {
        super.onResume()
        if (mStoredTextColor != config.textColor || mStoredBackgroundColor != config.backgroundColor || mStoredPrimaryColor != config.primaryColor
            || mStoredDayCode != Formatter.getTodayCode() || mStoredDimPastEvents != config.dimPastEvents) {
            updateViewPager()
        }

        eventsHelper.getEventTypes(this, false) {
            val newShouldFilterBeVisible = it.size > 1 || config.displayEventTypes.isEmpty()
            if (newShouldFilterBeVisible != mShouldFilterBeVisible) {
                mShouldFilterBeVisible = newShouldFilterBeVisible
            }
        }

        if (config.storedView == WEEKLY_VIEW) {
            if (mStoredIsSundayFirst != config.isSundayFirst || mStoredUse24HourFormat != config.use24HourFormat) {
                updateViewPager()
            }
        }

        storeStateVariables()
        //updateTextColors(calendar_coordinator)

        search_holder.background = ColorDrawable(config.backgroundColor)
        checkSwipeRefreshAvailability()
        checkShortcuts()

        if (!mIsSearchOpen) {
            invalidateOptionsMenu()
        }
    }

    override fun onPause() {
        super.onPause()
        storeStateVariables()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (!isChangingConfigurations) {
            EventsDatabase.destroyInstance()
            //stopCalDAVUpdateListener()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        shouldGoToTodayBeVisible = currentFragments.last().shouldGoToTodayBeVisible()
        menu.apply {
            goToTodayButton = findItem(R.id.go_to_today)

            findItem(R.id.go_to_today).isVisible = shouldGoToTodayBeVisible && !mIsSearchOpen
            findItem(R.id.go_to_date).isVisible = config.storedView != EVENTS_LIST_VIEW
        }

        setupSearch(menu)
        updateMenuItemColors(menu)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        menu.apply {
            findItem(R.id.refresh_caldav_calendars).isVisible = config.caldavSync
        }

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.change_view -> showViewDialog()
            R.id.go_to_today -> goToToday()
            R.id.go_to_date -> showGoToDateDialog()
            R.id.logout -> logout()
            R.id.findfriends -> sendUserToFindFriendsActivity()
            R.id.refresh_caldav_calendars -> refreshCalDAVCalendars(true)
            android.R.id.home -> onBackPressed()
            else -> return super.onOptionsItemSelected(item)
        }
        return true
    }

    private fun logout(){
        FirebaseAuth.getInstance().signOut()
        // change this code beacuse your app will crash
        // change this code beacuse your app will crash
        startActivity(Intent(this@CalendarActivity, StartActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
    }


    private fun sendUserToFindFriendsActivity() {
        val findFriendsIntent = Intent(this@CalendarActivity, FindFriendsActivity::class.java)
        startActivity(findFriendsIntent)
    }


    override fun onBackPressed() {
        swipe_refresh_layout.isRefreshing = false
        checkSwipeRefreshAvailability()
        if (currentFragments.size > 1) {
            removeTopFragment()
        } else {
            super.onBackPressed()
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        setIntent(intent)
        checkIsOpenIntent()
        checkIsViewIntent()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, resultData: Intent?) {
        super.onActivityResult(requestCode, resultCode, resultData)
        if (requestCode == PICK_IMPORT_SOURCE_INTENT && resultCode == AppCompatActivity.RESULT_OK && resultData != null && resultData.data != null) {
            //tryImportEventsFromFile(resultData.data!!)
        } else if (requestCode == PICK_EXPORT_FILE_INTENT && resultCode == AppCompatActivity.RESULT_OK && resultData != null && resultData.data != null) {
            val outputStream = contentResolver.openOutputStream(resultData.data!!)
            //exportEventsTo(eventTypesToExport, outputStream)
        }
    }

    private fun storeStateVariables() {
        config.apply {
            mStoredIsSundayFirst = isSundayFirst
            mStoredTextColor = textColor
            mStoredPrimaryColor = primaryColor
            mStoredBackgroundColor = backgroundColor
            mStoredUse24HourFormat = use24HourFormat
            mStoredDimPastEvents = dimPastEvents
        }
        mStoredDayCode = Formatter.getTodayCode()
    }

    private fun setupSearch(menu: Menu) {
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        mSearchMenuItem = menu.findItem(R.id.search)
        (mSearchMenuItem!!.actionView as SearchView).apply {
            setSearchableInfo(searchManager.getSearchableInfo(componentName))
            isSubmitButtonEnabled = false
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String) = false

                override fun onQueryTextChange(newText: String): Boolean {
                    if (mIsSearchOpen) {
                        searchQueryChanged(newText)
                    }
                    return true
                }
            })
        }

        MenuItemCompat.setOnActionExpandListener(mSearchMenuItem, object : MenuItemCompat.OnActionExpandListener {
            override fun onMenuItemActionExpand(item: MenuItem?): Boolean {
                mIsSearchOpen = true
                search_holder.beVisible()
                calendar_fab.beGone()
                searchQueryChanged("")
                invalidateOptionsMenu()
                return true
            }

            override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
                mIsSearchOpen = false
                search_holder.beGone()
                calendar_fab.beVisibleIf(currentFragments.last() !is YearFragmentsHolder && currentFragments.last() !is WeekFragmentsHolder)
                invalidateOptionsMenu()
                return true
            }
        })
    }

    private fun closeSearch() {
        mSearchMenuItem?.collapseActionView()
    }


    @SuppressLint("NewApi")
    private fun checkShortcuts() {
        val appIconColor = config.appIconColor
        if (isNougatMR1Plus() && config.lastHandledShortcutColor != appIconColor) {
            val newEvent = getString(R.string.new_event)
            val manager = getSystemService(ShortcutManager::class.java)
            val drawable = resources.getDrawable(R.drawable.shortcut_plus)
            (drawable as LayerDrawable).findDrawableByLayerId(R.id.shortcut_plus_background).applyColorFilter(appIconColor)
            val bmp = drawable.convertToBitmap()

            intent.action = SHORTCUT_NEW_EVENT
            val shortcut = ShortcutInfo.Builder(this, "new_event")
                .setShortLabel(newEvent)
                .setLongLabel(newEvent)
                .setIcon(Icon.createWithBitmap(bmp))
                .setIntent(intent)
                .build()

            try {
                manager.dynamicShortcuts = Arrays.asList(shortcut)
                config.lastHandledShortcutColor = appIconColor
            } catch (ignored: Exception) {
            }
        }
    }

    private fun checkIsOpenIntent(): Boolean {
        val dayCodeToOpen = intent.getStringExtra(DAY_CODE) ?: ""
        val viewToOpen = intent.getIntExtra(VIEW_TO_OPEN, DAILY_VIEW)
        intent.removeExtra(VIEW_TO_OPEN)
        intent.removeExtra(DAY_CODE)
        if (dayCodeToOpen.isNotEmpty()) {
            calendar_fab.beVisible()
            if (viewToOpen != LAST_VIEW) {
                config.storedView = viewToOpen
            }
            updateViewPager(dayCodeToOpen)
            return true
        }

        val eventIdToOpen = intent.getLongExtra(EVENT_ID, 0L)
        val eventOccurrenceToOpen = intent.getLongExtra(EVENT_OCCURRENCE_TS, 0L)
        intent.removeExtra(EVENT_ID)
        intent.removeExtra(EVENT_OCCURRENCE_TS)
        if (eventIdToOpen != 0L && eventOccurrenceToOpen != 0L) {
            Intent(this, EventActivity::class.java).apply {
                putExtra(EVENT_ID, eventIdToOpen)
                putExtra(EVENT_OCCURRENCE_TS, eventOccurrenceToOpen)
                startActivity(this)
            }
        }

        return false
    }

    private fun checkIsViewIntent() {
        if (intent?.action == Intent.ACTION_VIEW && intent.data != null) {
            val uri = intent.data
            if (uri?.authority?.equals("com.android.calendar") == true || uri?.authority?.substringAfter("@") == "com.android.calendar") {
                if (uri.path!!.startsWith("/events")) {
                    ensureBackgroundThread {
                        // intents like content://com.android.calendar/events/1756
                        val eventId = uri.lastPathSegment
                        val id = eventsDB.getEventIdWithLastImportId("%-$eventId")
                        if (id != null) {
                            Intent(this, EventActivity::class.java).apply {
                                putExtra(EVENT_ID, id)
                                startActivity(this)
                            }
                        } else {
                            toast(R.string.caldav_event_not_found, Toast.LENGTH_LONG)
                        }
                    }
                } else if (uri.path!!.startsWith("/time") || intent?.extras?.getBoolean("DETAIL_VIEW", false) == true) {
                    // clicking date on a third party widget: content://com.android.calendar/time/1507309245683
                    // or content://0@com.android.calendar/time/1584958526435
                    val timestamp = uri.pathSegments.last()
                    if (timestamp.areDigitsOnly()) {
                        openDayAt(timestamp.toLong())
                        return
                    }
                }
            } else {
                //tryImportEventsFromFile(uri!!)
            }
        }
    }

    private fun showViewDialog() {
        val items = arrayListOf(
            RadioItem(DAILY_VIEW, getString(R.string.daily_view)),
            RadioItem(WEEKLY_VIEW, getString(R.string.weekly_view)),
            RadioItem(MONTHLY_VIEW, getString(R.string.monthly_view)),
            RadioItem(YEARLY_VIEW, getString(R.string.yearly_view)),
            RadioItem(EVENTS_LIST_VIEW, getString(R.string.simple_event_list)))

        RadioGroupDialog(this, items, config.storedView) {
            resetActionBarTitle()
            closeSearch()
            updateView(it as Int)
            shouldGoToTodayBeVisible = false
            invalidateOptionsMenu()
        }
    }

    private fun goToToday() {
        currentFragments.last().goToToday()
    }

    fun showGoToDateDialog() {
        currentFragments.last().showGoToDateDialog()
    }

    private fun printView() {
        currentFragments.last().printView()
    }

    private fun resetActionBarTitle() {
        updateActionBarTitle(getString(R.string.app_launcher_name))
        updateActionBarSubtitle("")
    }

//    private fun showFilterDialog() {
//       // FilterEventTypesDialog(this) {
//            refreshViewPager()
//        }
//    }

    fun toggleGoToTodayVisibility(beVisible: Boolean) {
        shouldGoToTodayBeVisible = beVisible
        if (goToTodayButton?.isVisible != beVisible) {
            invalidateOptionsMenu()
        }
    }

    private fun refreshCalDAVCalendars(showRefreshToast: Boolean) {
        showCalDAVRefreshToast = showRefreshToast
        if (showRefreshToast) {
            toast(R.string.refreshing)
        }

        syncCalDAVCalendars {
            calDAVHelper.refreshCalendars(true) {
                calDAVChanged()
            }
        }
    }

    private fun calDAVChanged() {
        refreshViewPager()
        if (showCalDAVRefreshToast) {
            toast(R.string.refreshing_complete)
        }
        runOnUiThread {
            swipe_refresh_layout.isRefreshing = false
        }
    }

    private fun updateView(view: Int) {
        calendar_fab.beVisibleIf(view != YEARLY_VIEW && view != WEEKLY_VIEW)
        config.storedView = view
        checkSwipeRefreshAvailability()
        updateViewPager()
        if (goToTodayButton?.isVisible == true) {
            shouldGoToTodayBeVisible = false
            invalidateOptionsMenu()
        }
    }

    private fun updateViewPager(dayCode: String? = Formatter.getTodayCode()) {
        val fragment = getFragmentsHolder()
        currentFragments.forEach {
            supportFragmentManager.beginTransaction().remove(it).commitNow()
        }
        currentFragments.clear()
        currentFragments.add(fragment)
        val bundle = Bundle()

        when (config.storedView) {
            DAILY_VIEW, MONTHLY_VIEW -> bundle.putString(DAY_CODE, dayCode)
            WEEKLY_VIEW -> bundle.putString(WEEK_START_DATE_TIME, getThisWeekDateTime())
        }

        fragment.arguments = bundle
        supportFragmentManager.beginTransaction().add(R.id.fragments_holder, fragment).commitNow()
    }

    fun openMonthFromYearly(dateTime: DateTime) {
        if (currentFragments.last() is MonthFragmentsHolder) {
            return
        }

        val fragment = MonthFragmentsHolder()
        currentFragments.add(fragment)
        val bundle = Bundle()
        bundle.putString(DAY_CODE, Formatter.getDayCodeFromDateTime(dateTime))
        fragment.arguments = bundle
        supportFragmentManager.beginTransaction().add(R.id.fragments_holder, fragment).commitNow()
        resetActionBarTitle()
        calendar_fab.beVisible()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    fun openDayFromMonthly(dateTime: DateTime) {
        if (currentFragments.last() is DayFragmentsHolder) {
            return
        }

        val fragment = DayFragmentsHolder()
        currentFragments.add(fragment)
        val bundle = Bundle()
        bundle.putString(DAY_CODE, Formatter.getDayCodeFromDateTime(dateTime))
        fragment.arguments = bundle
        try {
            supportFragmentManager.beginTransaction().add(R.id.fragments_holder, fragment).commitNow()
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        } catch (e: Exception) {
        }
    }

    private fun getThisWeekDateTime(): String {
        var thisweek = DateTime().withZone(DateTimeZone.UTC).withDayOfWeek(1).withHourOfDay(12).minusDays(if (config.isSundayFirst) 1 else 0)
        if (DateTime().minusDays(7).seconds() > thisweek.seconds()) {
            thisweek = thisweek.plusDays(7)
        }
        return thisweek.toString()
    }

    private fun getFragmentsHolder() = when (config.storedView) {
        DAILY_VIEW -> DayFragmentsHolder()
        MONTHLY_VIEW -> MonthFragmentsHolder()
        YEARLY_VIEW -> YearFragmentsHolder()
        EVENTS_LIST_VIEW -> EventListFragment()
        else -> WeekFragmentsHolder()
    }

    private fun removeTopFragment() {
        supportFragmentManager.beginTransaction().remove(currentFragments.last()).commit()
        currentFragments.removeAt(currentFragments.size - 1)
        toggleGoToTodayVisibility(currentFragments.last().shouldGoToTodayBeVisible())
        currentFragments.last().apply {
            refreshEvents()
            updateActionBarTitle()
        }
        calendar_fab.beGoneIf(currentFragments.size == 1 && config.storedView == YEARLY_VIEW)
        supportActionBar?.setDisplayHomeAsUpEnabled(currentFragments.size > 1)
    }

    private fun refreshViewPager() {
        runOnUiThread {
            if (!isDestroyed) {
                currentFragments.last().refreshEvents()
            }
        }
    }

    private fun tryImportEvents() {
        if (isQPlus()) {
            Intent(Intent.ACTION_GET_CONTENT).apply {
                addCategory(Intent.CATEGORY_OPENABLE)
                type = "text/calendar"
                startActivityForResult(this, PICK_IMPORT_SOURCE_INTENT)
            }
        } else {
            handlePermission(PERMISSION_READ_STORAGE) {
                if (it) {
                    //importEvents()
                }
            }
        }
    }

    private fun searchQueryChanged(text: String) {
        mLatestSearchQuery = text
        search_placeholder_2.beGoneIf(text.length >= 2)
        if (text.length >= 2) {
            eventsHelper.getEventsWithSearchQuery(text, this) { searchedText, events ->
                if (searchedText == mLatestSearchQuery) {
                    search_results_list.beVisibleIf(events.isNotEmpty())
                    search_placeholder.beVisibleIf(events.isEmpty())
                    val listItems = getEventListItems(events)
                    val eventsAdapter = EventListAdapter(this, listItems, true, this, search_results_list) {
                        if (it is ListEvent) {
                            Intent(applicationContext, EventActivity::class.java).apply {
                                putExtra(EVENT_ID, it.id)
                                startActivity(this)
                            }
                        }
                    }

                    search_results_list.adapter = eventsAdapter
                }
            }
        } else {
            search_placeholder.beVisible()
            search_results_list.beGone()
        }
    }

    private fun checkSwipeRefreshAvailability() {
        swipe_refresh_layout.isEnabled = config.caldavSync && config.pullToRefresh && config.storedView != WEEKLY_VIEW
        if (!swipe_refresh_layout.isEnabled) {
            swipe_refresh_layout.isRefreshing = false
        }
    }

    // only used at active search
    override fun refreshItems() {
        searchQueryChanged(mLatestSearchQuery)
        refreshViewPager()
    }

    private fun openDayAt(timestamp: Long) {
        val dayCode = Formatter.getDayCodeFromTS(timestamp / 1000L)
        calendar_fab.beVisible()
        config.storedView = DAILY_VIEW
        updateViewPager(dayCode)
    }


}
