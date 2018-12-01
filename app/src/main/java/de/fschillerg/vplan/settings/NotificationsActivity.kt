package de.fschillerg.vplan.settings

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.widget.Button
import android.widget.Switch
import de.fschillerg.vplan.R
import de.fschillerg.vplan.background.Scheduler
import de.fschillerg.vplan.preferences.Preferences

class NotificationsActivity : AppCompatActivity() {
    companion object {
        private const val updateInterval: Long = 15
    }

    var toolbar: Toolbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notifications)

        toolbar = findViewById(R.id.notifications_toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = resources.getString(R.string.notifications)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        val preferences = Preferences(this)

        val notifications = findViewById<Switch>(R.id.switch_notifications)

        if (preferences.notifications) {
            notifications.isChecked = true
        }

        notifications.setOnCheckedChangeListener { _, isChecked ->
            preferences.notifications = isChecked
            if (isChecked) {
                Scheduler.schedule(updateInterval)
            } else {
                Scheduler.cancel()
            }
        }

        val recyclerView = findViewById<RecyclerView>(R.id.forms)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val adapter = FilterAdapter(Preferences(this))
        recyclerView.adapter = adapter

        findViewById<Button>(R.id.add_form).setOnClickListener {
            AddFormDialog.create(this, adapter).show()
        }

        findViewById<Button>(R.id.add_teacher).setOnClickListener {
            AddTeacherDialog.create(this, adapter).show()
        }
    }
}
