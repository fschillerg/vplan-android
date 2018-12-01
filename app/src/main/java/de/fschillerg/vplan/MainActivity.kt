package de.fschillerg.vplan

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.afollestad.materialdialogs.callbacks.onDismiss
import de.fschillerg.vplan.about.AboutActivity
import de.fschillerg.vplan.background.Storage
import de.fschillerg.vplan.preferences.Preferences
import de.fschillerg.vplan.dialogs.DialogManager
import de.fschillerg.vplan.dialogs.NoConnectionDialog
import de.fschillerg.vplan.settings.CredentialsDialog
import de.fschillerg.vplan.settings.SettingsActivity
import de.fschillerg.vplan.startup.StartupCheck
import de.fschillerg.vplan.tabs.OnTabSelectedListener
import de.fschillerg.vplan.tabs.ViewPagerAdapter
import de.fschillerg.vplan.vplan.Cache
import de.fschillerg.vplan.vplan.VplanClient
import de.fschillerg.vplan.vplan.Weekday
import java.util.Calendar
import java.util.GregorianCalendar
import java.util.TimeZone

class MainActivity : AppCompatActivity() {
    private val tag = "MainActivity"

    lateinit var toolbar: Toolbar
    private var adapter: ViewPagerAdapter? = null

    private lateinit var preferences: Preferences
    private var storage: Storage? = null
    private var cache: Cache? = null

    companion object {
        init {
            System.loadLibrary("vplan")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = resources.getString(R.string.vplan)

        val dialogManager = DialogManager()

        preferences = Preferences(this)
        val check = StartupCheck(this, preferences)

        if (!check.setup) {
            CredentialsDialog.create(this, false).onDismiss {
                recreate()
            }.show()
        } else if (!check.connection) {
            NoConnectionDialog.create(this).show()
        } else {
            storage = Storage(applicationContext)
            cache = Cache(preferences, storage!!)

            if (preferences.preload) {
                cache!!.preload()
            }

            val viewPager: ViewPager = findViewById(R.id.viewpager)
            adapter = ViewPagerAdapter(this, supportFragmentManager, dialogManager, cache!!)
            viewPager.adapter = adapter

            val tabLayout: TabLayout = findViewById(R.id.tablayout)
            tabLayout.tabGravity = TabLayout.GRAVITY_FILL
            tabLayout.setupWithViewPager(viewPager)
            tabLayout.addOnTabSelectedListener(OnTabSelectedListener(viewPager) { position ->
                updateToolbar(Weekday.from(position))
            })

            viewPager.currentItem = Weekday.relevant().value
        }
    }

    fun updateToolbar(weekday: Weekday) {
        Log.d(tag, "updating toolbar")

        supportActionBar?.title = weekday.toString(this)

        try {
            val vplan = cache?.get(weekday)
            val date = GregorianCalendar(TimeZone.getTimeZone("Europe/Berlin"))
            date.timeInMillis = vplan?.date?.date?.time!!
            val day = date.get(Calendar.DAY_OF_MONTH)
            val month = date.get(Calendar.MONTH) + 1
            val year = date.get(Calendar.YEAR)
            val weektype = vplan.date.weekType.toString()
            supportActionBar?.title = "${weekday.toString(this)}, $day.$month.$year ($weektype)"
        } catch (error: VplanClient.RequestError) {
            Log.e(tag, "error updating toolbar")
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.action_reload -> {
                adapter!!.current.update(true)
                updateToolbar(adapter!!.current.weekday)
            }
            R.id.action_settings -> startActivity(Intent(this, SettingsActivity::class.java))
            R.id.action_about -> startActivity(Intent(this, AboutActivity::class.java))
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        cache?.destroy()
        storage?.close()
        super.onDestroy()
    }
}
