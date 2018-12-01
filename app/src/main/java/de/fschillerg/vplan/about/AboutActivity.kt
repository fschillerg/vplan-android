package de.fschillerg.vplan.about

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.text.method.LinkMovementMethod
import android.widget.Button
import android.widget.TextView
import de.fschillerg.vplan.BuildConfig
import de.fschillerg.vplan.R

class AboutActivity : AppCompatActivity() {
    var toolbar: Toolbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        toolbar = findViewById(R.id.about_toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = resources.getString(R.string.about)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        findViewById<TextView>(R.id.about_version).text = BuildConfig.VERSION_NAME
        findViewById<TextView>(R.id.about_license).movementMethod = LinkMovementMethod.getInstance()
        findViewById<TextView>(R.id.about_source_code).movementMethod = LinkMovementMethod.getInstance()

        findViewById<Button>(R.id.about_libraries).setOnClickListener {
            startActivity(Intent(this, LibrariesActivity::class.java))
        }
    }
}
