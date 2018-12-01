package de.fschillerg.vplan.settings

import android.content.Intent
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import de.fschillerg.vplan.R

class SettingsActivity : AppCompatActivity() {
    var toolbar: Toolbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        toolbar = findViewById(R.id.settings_toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = resources.getString(R.string.settings)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        findViewById<ConstraintLayout>(R.id.settings_credentials).setOnClickListener {
            CredentialsDialog.create(this, true).show()
        }

        findViewById<ConstraintLayout>(R.id.settings_notifications).setOnClickListener {
            startActivity(Intent(this, NotificationsActivity::class.java))
        }
    }
}
