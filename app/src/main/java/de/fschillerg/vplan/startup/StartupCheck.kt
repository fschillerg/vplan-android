package de.fschillerg.vplan.startup

import android.content.Context
import android.net.ConnectivityManager
import de.fschillerg.vplan.preferences.Preferences

class StartupCheck(context: Context, preferences: Preferences) {
    val setup = preferences.setup
    val connection: Boolean

    init {
        val connectionManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        connection = connectionManager.activeNetworkInfo.isConnected
    }
}
