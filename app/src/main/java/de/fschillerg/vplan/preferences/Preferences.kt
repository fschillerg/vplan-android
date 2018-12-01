package de.fschillerg.vplan.preferences

import android.content.Context
import de.adorsys.android.securestoragelibrary.SecurePreferences

class Preferences(context: Context) {
    private val tag = "de.fschillerg.de"

    private val preferences = context.getSharedPreferences(tag, Context.MODE_PRIVATE)

    var setup: Boolean
        get() = preferences.getBoolean("setup", false)
        set(value) {
            preferences.edit().putBoolean("setup", value).apply()
        }

    var preload: Boolean
        get() = preferences.getBoolean("preload", false)
        set(value) {
            preferences.edit().putBoolean("preload", value).apply()
        }

    var username: String
        get() = SecurePreferences.getStringValue("username", "").orEmpty()
        set(value) {
            SecurePreferences.setValue("username", value)
        }

    var password: String
        get() = SecurePreferences.getStringValue("password", "").orEmpty()
        set(value) {
            SecurePreferences.setValue("password", value)
        }

    var notifications: Boolean
        get() = preferences.getBoolean("notifications", false)
        set(value) {
            preferences.edit().putBoolean("notifications", value).apply()
        }

    var filtersEnabled: Boolean
        get() = preferences.getBoolean("filtersEnabled", false)
        set(value) {
            preferences.edit().putBoolean("filtersEnabled", value).apply()
        }

    var forms: Set<String>
        get() = preferences.getStringSet("forms", setOf())!!
        set(value) {
            preferences.edit().putStringSet("forms", value).apply()
            checkFilters()
        }

    var teachers: Set<String>
        get() = preferences.getStringSet("teachers", setOf())!!
        set(value) {
            preferences.edit().putStringSet("teachers", value).apply()
            checkFilters()
        }

    private fun checkFilters() {
        filtersEnabled = !(forms.isEmpty() && teachers.isEmpty())
    }
}
