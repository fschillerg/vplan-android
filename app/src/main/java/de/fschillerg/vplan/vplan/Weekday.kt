package de.fschillerg.vplan.vplan

import android.content.Context
import de.fschillerg.vplan.R
import java.util.Calendar
import java.util.GregorianCalendar
import java.util.TimeZone

@Suppress("MagicNumber")
enum class Weekday(val value: Int) {
    MONDAY(0),
    TUESDAY(1),
    WEDNESDAY(2),
    THURSDAY(3),
    FRIDAY(4);

    fun next(): Weekday {
        return Weekday.from(value + 1)
    }

    fun toString(context: Context): String {
        return when (this) {
            MONDAY -> context.getString(R.string.monday)
            TUESDAY -> context.getString(R.string.tuesday)
            WEDNESDAY -> context.getString(R.string.wednesday)
            THURSDAY -> context.getString(R.string.thursday)
            FRIDAY -> context.getString(R.string.friday)
        }
    }

    fun toShortString(context: Context): String {
        return when (this) {
            MONDAY -> context.getString(R.string.monday_short)
            TUESDAY -> context.getString(R.string.tuesday_short)
            WEDNESDAY -> context.getString(R.string.wednesday_short)
            THURSDAY -> context.getString(R.string.thursday_short)
            FRIDAY -> context.getString(R.string.friday_short)
        }
    }

    companion object {
        fun from(from: Int): Weekday {
            var search = from

            if (search < MONDAY.value || search > FRIDAY.value) {
                search = MONDAY.value
            }

            return Weekday.values().first {
                it.value == search
            }
        }

        fun current(): Weekday {
            val home = GregorianCalendar(TimeZone.getTimeZone("Europe/Berlin"))
            home.timeInMillis = Calendar.getInstance().timeInMillis
            return from(home.get(Calendar.DAY_OF_WEEK) - 2)
        }

        fun relevant(): Weekday {
            val home = GregorianCalendar(TimeZone.getTimeZone("Europe/Berlin"))
            home.timeInMillis = Calendar.getInstance().timeInMillis

            return if (home.get(Calendar.HOUR_OF_DAY) < 10) {
                current()
            } else {
                current().next()
            }
        }
    }
}
