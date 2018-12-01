package de.fschillerg.vplan.vplan

import java.util.Date

class NativeVplan(var handle: Long) {
    fun toVplan(day: Weekday): Vplan {
        val date = VplanDate(Date(getDateNative()), getWeekTypeNative())
        val changed = Date(getChangedNative())
        val daysOffNative = getDaysOffNative()
        val daysOff = daysOffNative.map {
            Date(it)
        }
        val changes = getChangesNative()
        val info = getInfoNative()

        return Vplan(day, date, changed, daysOff.toTypedArray(), changes, info)
    }

    fun destroy() {
        destroyNative()
    }

    private external fun getDateNative(): Long
    private external fun getWeekTypeNative(): WeekType
    private external fun getChangedNative(): Long
    private external fun getDaysOffNative(): LongArray
    private external fun getChangesNative(): Array<Change>
    private external fun getInfoNative(): Array<String>

    private external fun destroyNative()
}
