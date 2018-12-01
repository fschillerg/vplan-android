package de.fschillerg.vplan.background

import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

class Scheduler {
    companion object {
        fun schedule(interval: Long) {
            val constraints = Constraints
                    .Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build()

            val work = PeriodicWorkRequest
                    .Builder(BackgroundWorker::class.java, interval, TimeUnit.MINUTES)
                    .setConstraints(constraints)
                    .build()

            WorkManager.getInstance().cancelAllWork()
            WorkManager.getInstance().enqueue(work)
        }

        fun cancel() {
            WorkManager.getInstance().cancelAllWork()
        }
    }
}
