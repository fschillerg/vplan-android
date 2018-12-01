package de.fschillerg.vplan.background

import android.content.Context
import de.fschillerg.vplan.vplan.RealmVplan
import de.fschillerg.vplan.vplan.Vplan
import de.fschillerg.vplan.vplan.Weekday
import io.realm.Realm

class Storage(context: Context) {
    private val realm: Realm

    init {
        Realm.init(context)
        realm = Realm.getDefaultInstance()
    }

    fun contains(weekday: Weekday): Boolean {
        return realm
                .where(RealmVplan::class.java)
                .equalTo("weekday", weekday.value)
                .findFirst() != null
    }

    fun get(weekday: Weekday): Vplan {
        val vplan = realm
                .where(RealmVplan::class.java)
                .equalTo("weekday", weekday.value)
                .findFirst()
                as RealmVplan

        return vplan.toVplan()
    }

    fun put(vplan: Vplan) {
        realm.beginTransaction()

        val realmVplan = RealmVplan.fromVplan(vplan)

        val old = realm
                .where(RealmVplan::class.java)
                .equalTo("weekday", realmVplan.weekday)
                .findAll()

        old.deleteAllFromRealm()

        realm.copyToRealm(realmVplan)

        realm.commitTransaction()
    }

    fun close() {
        realm.close()
    }
}
