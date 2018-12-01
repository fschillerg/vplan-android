package de.fschillerg.vplan.vplan

import de.fschillerg.vplan.table.Cell
import java.util.Date

enum class WeekType(val value: Int) {
    A(0),
    B(1);

    override fun toString(): String {
        return when (this) {
            A -> "A"
            B -> "B"
        }
    }

    companion object {
        fun from(from: Int): WeekType {
            var search = from

            if (search < 0 || search > 1) {
                search = 0
            }

            return WeekType.values().first {
                it.value == search
            }
        }
    }
}

class VplanDate(
    val date: Date,
    val weekType: WeekType
)

class Change(
    val form: String,
    val lesson: String,
    val subject: String,
    val teacher: String,
    val room: String,
    val info: String
)

class VplanTable(
    val rowHeaders: List<Cell>,
    val rows: List<List<Cell>>
)

class Vplan(
    val weekday: Weekday,
    val date: VplanDate,
    val changed: Date,
    val daysOff: Array<Date>,
    val changes: Array<Change>,
    val info: Array<String>
) {
    fun toTable(): VplanTable {
        @Suppress("MagicNumber")
        var i = 4

        val rowHeaders: MutableList<Cell> = mutableListOf()

        val rows: MutableList<List<Cell>> = mutableListOf()

        changes.forEach {
            rowHeaders.add(Cell(++i, it.form))

            val row: List<Cell> = listOf(
                    Cell(++i, it.lesson),
                    Cell(++i, it.subject),
                    Cell(++i, it.teacher),
                    Cell(++i, it.room),
                    Cell(++i, it.info)
            )

            rows.add(row)
        }

        return VplanTable(rowHeaders as List<Cell>, rows as List<List<Cell>>)
    }
}
