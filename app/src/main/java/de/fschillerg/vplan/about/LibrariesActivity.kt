package de.fschillerg.vplan.about

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import com.mikepenz.aboutlibraries.LibsBuilder
import de.fschillerg.vplan.R

class LibrariesActivity : AppCompatActivity() {
    var toolbar: Toolbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_libraries)

        toolbar = findViewById(R.id.libraries_toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = resources.getString(R.string.libraries)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        val fragment = LibsBuilder().supportFragment()

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction().replace(R.id.libs_root, fragment).commit()
        }
    }
}
