package de.fschillerg.vplan.vplan

import de.fschillerg.vplan.background.Storage
import de.fschillerg.vplan.preferences.Preferences

class Cache(preferences: Preferences, private val storage: Storage) {
    private var client = VplanClient(preferences.username, preferences.password)

    private var cache: MutableMap<Weekday, Vplan> = mutableMapOf()

    @Throws(VplanClient.RequestError::class)
    fun get(day: Weekday): Vplan {
        return when {
            cache.contains(day) -> cache[day]!!

            storage.contains(day) -> {
                val vplan = storage.get(day)
                cache[day] = vplan
                vplan
            }

            else -> {
                val vplan = try {
                    client.get(day)
                } catch (error: VplanClient.RequestError) {
                    throw error
                }

                storage.put(vplan)
                cache[day] = vplan
                vplan
            }
        }
    }

    @Throws(VplanClient.RequestError::class)
    fun update(day: Weekday): Vplan {
        val vplan = try {
            client.get(day)
        } catch (error: VplanClient.RequestError) {
            throw error
        }

        if (cache.contains(day)) {
            cache.remove(day)
        }

        storage.put(vplan)
        cache[day] = vplan
        return vplan
    }

    @Throws(VplanClient.RequestError::class)
    fun preload() {
        try {
            for (i in 0..Weekday.values().size) {
                get(Weekday.from(i))
            }
        } catch (error: VplanClient.RequestError) {
            throw error
        }
    }

    fun destroy() {
        client.destroy()
    }
}
