package de.fschillerg.vplan.background

import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import androidx.work.Worker
import androidx.work.WorkerParameters
import de.fschillerg.vplan.MainActivity
import de.fschillerg.vplan.R
import de.fschillerg.vplan.preferences.Preferences
import de.fschillerg.vplan.vplan.Vplan
import de.fschillerg.vplan.vplan.VplanClient
import de.fschillerg.vplan.vplan.Weekday
import io.karn.notify.Notify
import java.util.Calendar
import java.util.GregorianCalendar
import java.util.TimeZone

class BackgroundWorker(context: Context, params: WorkerParameters) : Worker(context, params) {
    private val dayBegin = Calendar.MONDAY
    private val dayEnd = Calendar.FRIDAY
    @Suppress("MagicNumber")
    private val hourBegin = 10
    @Suppress("MagicNumber")
    private val hourEnd = 15

    @Suppress("ComplexMethod", "ReturnCount")
    override fun doWork(): Result {
        val home = GregorianCalendar(TimeZone.getTimeZone("Europe/Berlin"))
        home.timeInMillis = Calendar.getInstance().timeInMillis

        if (home.get(Calendar.DAY_OF_WEEK) !in dayBegin..dayEnd && home.get(Calendar.HOUR) !in hourBegin..hourEnd) {
            return Result.SUCCESS
        }

        val preferences = Preferences(applicationContext)

        val weekday = Weekday.relevant()

        val client = VplanClient(preferences.username, preferences.password)
        val vplan: Vplan
        try {
            vplan = client.get(weekday)
            client.destroy()
        } catch (error: VplanClient.RequestError) {
            client.destroy()
            notifyError()
            return Result.FAILURE
        }

        val storage = Storage(applicationContext)

        if (storage.contains(weekday)) {
            val old = storage.get(weekday)

            if (old.date.date.time >= vplan.date.date.time) {
                storage.close()
                return Result.SUCCESS
            }
        }

        storage.put(vplan)
        storage.close()

        if (!preferences.filtersEnabled) {
            notifyNew()
        }

        val forms = preferences.forms
        val teachers = preferences.teachers

        vplan.changes.forEach { change ->
            if (forms.contains(change.form)) {
                notifyNew()
                return Result.SUCCESS
            }

            if (teachers.contains(change.teacher)) {
                notifyNew()
                return Result.SUCCESS
            }
        }

        return Result.SUCCESS
    }

    private fun notifyNew() {
        val stackBuilder = TaskStackBuilder.create(applicationContext)
        stackBuilder.addNextIntentWithParentStack(Intent(applicationContext, MainActivity::class.java))

        Notify
                .with(applicationContext)
                .header {
                    icon = R.drawable.ic_new
                }
                .content {
                    title = applicationContext.resources.getString(R.string.notification_title)
                    text = applicationContext.resources.getString(R.string.notification_description)
                }
                .meta {
                    clickIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
                }
                .show()
    }

    private fun notifyError() {
        val stackBuilder = TaskStackBuilder.create(applicationContext)
        stackBuilder.addNextIntentWithParentStack(Intent(applicationContext, MainActivity::class.java))

        Notify
                .with(applicationContext)
                .header {
                    icon = R.drawable.ic_error
                }
                .content {
                    title = applicationContext.resources.getString(R.string.notification_error_title)
                    text = applicationContext.resources.getString(R.string.notification_error_description)
                }
                .meta {
                    clickIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
                }
                .show()
    }
}
