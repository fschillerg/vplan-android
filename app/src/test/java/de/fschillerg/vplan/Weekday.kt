package de.fschillerg.vplan

import de.fschillerg.vplan.vplan.Weekday
import org.junit.Test
import org.junit.Assert.assertEquals

class Weekday {
    @Test
    fun fromInt_monday() {
        assertEquals(Weekday.from(0), Weekday.MONDAY)
        assertEquals(Weekday.from(-1), Weekday.MONDAY)
        assertEquals(Weekday.from(5), Weekday.MONDAY)
    }

    @Test
    fun next_monday() {
        assertEquals(Weekday.MONDAY.next(), Weekday.TUESDAY)
    }

    @Test
    fun fromInt_tuesday() {
        assertEquals(Weekday.from(1), Weekday.TUESDAY)
    }

    @Test
    fun next_tuesday() {
        assertEquals(Weekday.TUESDAY.next(), Weekday.WEDNESDAY)
    }

    @Test
    fun fromInt_wednesday() {
        assertEquals(Weekday.from(2), Weekday.WEDNESDAY)
    }

    @Test
    fun next_wednesday() {
        assertEquals(Weekday.WEDNESDAY.next(), Weekday.THURSDAY)
    }

    @Test
    fun fromInt_thursday() {
        assertEquals(Weekday.from(3), Weekday.THURSDAY)
    }

    @Test
    fun next_thursday() {
        assertEquals(Weekday.THURSDAY.next(), Weekday.FRIDAY)
    }

    @Test
    fun fromInt_friday() {
        assertEquals(Weekday.from(4), Weekday.FRIDAY)
    }

    @Test
    fun next_friday() {
        assertEquals(Weekday.FRIDAY.next(), Weekday.MONDAY)
    }
}
