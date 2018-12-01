package de.fschillerg.vplan.vplan

import android.util.Log

class VplanClient(username: String, password: String) {
    private val tag = "VplanClient"

    @Suppress("unused")
    private var handle: Long = 0

    init {
        initNative(username, password)
    }

    inner class RequestError : Exception()

    @Throws(RequestError::class)
    fun get(day: Weekday): Vplan {
        Log.d(tag, """Requesting vplan for $day""")

        val handle = getNative(day)
        if (handle != 0L) {
            val native = NativeVplan(handle)
            val vplan = native.toVplan(day)
            native.destroy()
            return vplan
        } else {
            throw RequestError()
        }
    }

    fun destroy() {
        destroyNative()
    }

    private external fun initNative(username: String, password: String)
    private external fun getNative(day: Weekday): Long

    private external fun destroyNative()
}
