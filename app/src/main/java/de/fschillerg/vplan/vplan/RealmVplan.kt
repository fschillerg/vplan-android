package de.fschillerg.vplan.vplan

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.util.Date

open class RealmVplanDate(
    var date: Long,
    var weekType: Int
) : RealmObject() {
    companion object {
        fun fromVplanDate(date: VplanDate): RealmVplanDate {
            return RealmVplanDate(date.date.time, date.weekType.value)
        }
    }

    constructor() : this(0, 0)

    fun toVplanDate(): VplanDate {
        return VplanDate(Date(date), WeekType.from(weekType))
    }
}

open class RealmChange(
    var form: String,
    var lesson: String,
    var subject: String,
    var teacher: String,
    var room: String,
    var info: String
) : RealmObject() {
    companion object {
        fun fromVplanChange(change: Change): RealmChange {
            return RealmChange(
                    change.form,
                    change.lesson,
                    change.subject,
                    change.teacher,
                    change.room,
                    change.info
            )
        }
    }

    constructor() : this("", "", "", "", "", "")

    fun toVplanChange(): Change {
        return Change(
                form,
                lesson,
                subject,
                teacher,
                room,
                info
        )
    }
}

open class RealmVplan(
    @PrimaryKey var weekday: Int,
    var date: RealmVplanDate?,
    var changed: Long,
    var daysOff: RealmList<Long>,
    var changes: RealmList<RealmChange>,
    var info: RealmList<String>
) : RealmObject() {
    companion object {
        fun fromVplan(vplan: Vplan): RealmVplan {
            val daysOffRealm = RealmList<Long>()
            vplan.daysOff.forEach {
                daysOffRealm.add(it.time)
            }

            val changesRealm = RealmList<RealmChange>()
            vplan.changes.forEach {
                changesRealm.add(RealmChange.fromVplanChange(it))
            }

            val infoRealm = RealmList<String>()
            vplan.info.forEach {
                infoRealm.add(it)
            }

            return RealmVplan(
                    vplan.weekday.value,
                    RealmVplanDate.fromVplanDate(vplan.date),
                    vplan.changed.time,
                    daysOffRealm,
                    changesRealm,
                    infoRealm
            )
        }
    }

    constructor() : this(
            0,
            RealmVplanDate(0, 0),
            0L,
            RealmList<Long>(0),
            RealmList<RealmChange>(RealmChange("", "", "", "", "", "")),
            RealmList<String>("")
    )

    fun toVplan(): Vplan {
        return Vplan(
                Weekday.from(weekday),
                date!!.toVplanDate(),
                Date(changed),
                daysOff.map {
                    Date(it)
                }.toTypedArray(),
                changes.map {
                    it.toVplanChange()
                }.toTypedArray(),
                info.map {
                    it
                }.toTypedArray()
        )
    }
}
