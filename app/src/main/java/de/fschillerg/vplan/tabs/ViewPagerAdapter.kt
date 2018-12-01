package de.fschillerg.vplan.tabs

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.view.ViewGroup
import de.fschillerg.vplan.dialogs.DialogManager
import de.fschillerg.vplan.vplan.Cache
import de.fschillerg.vplan.vplan.Weekday

class ViewPagerAdapter(
    context: Context,
    manager: FragmentManager,
    dialogManager: DialogManager,
    cache: Cache
) : FragmentPagerAdapter(manager) {
    private val tabTitles: Array<String> = arrayOf(
            Weekday.MONDAY.toShortString(context),
            Weekday.TUESDAY.toShortString(context),
            Weekday.WEDNESDAY.toShortString(context),
            Weekday.THURSDAY.toShortString(context),
            Weekday.FRIDAY.toShortString(context)
    )

    private val monday = DayFragment(context, cache, Weekday.MONDAY, dialogManager)
    private val tuesday = DayFragment(context, cache, Weekday.TUESDAY, dialogManager)
    private val wednesday = DayFragment(context, cache, Weekday.WEDNESDAY, dialogManager)
    private val thursday = DayFragment(context, cache, Weekday.THURSDAY, dialogManager)
    private val friday = DayFragment(context, cache, Weekday.FRIDAY, dialogManager)

    lateinit var current: DayFragment

    override fun getItem(position: Int): Fragment {
        return when (Weekday.from(position)) {
            Weekday.MONDAY -> monday
            Weekday.TUESDAY -> tuesday
            Weekday.WEDNESDAY -> wednesday
            Weekday.THURSDAY -> thursday
            Weekday.FRIDAY -> friday
        }
    }

    override fun setPrimaryItem(container: ViewGroup, position: Int, `object`: Any) {
        current = when (Weekday.from(position)) {
            Weekday.MONDAY -> monday
            Weekday.TUESDAY -> tuesday
            Weekday.WEDNESDAY -> wednesday
            Weekday.THURSDAY -> thursday
            Weekday.FRIDAY -> friday
        }

        super.setPrimaryItem(container, position, `object`)
    }

    override fun getCount(): Int {
        return tabTitles.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return tabTitles[position]
    }
}
