package io.mochahub.powermeter

import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.microsoft.appcenter.AppCenter
import com.microsoft.appcenter.analytics.Analytics
import com.microsoft.appcenter.crashes.Crashes
import com.microsoft.appcenter.push.Push
import kotlinx.android.synthetic.main.activity_main.*

private const val NIGHT_MODE = "night_mode"

class MainActivity : AppCompatActivity() {

    private val preferences: SharedPreferences
        get() = PreferenceManager.getDefaultSharedPreferences(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCenter.start(application, BuildConfig.HOCKEY_APP_SECRET,
            Push::class.java, Analytics::class.java, Crashes::class.java)

//        TODO (Zahin): uncomment the following line when prod app is ready
//        Eventually we should do a switch on this so that it only triggers on a prod product flavour
//        Analytics.trackEvent("Hello World")

        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val navController: NavController = Navigation.findNavController(this, R.id.nav_host_fragment)
        NavigationUI.setupWithNavController(bottom_navigation, navController)

        preferences.registerOnSharedPreferenceChangeListener { sharedPreferences, key ->
            when (key) {
                NIGHT_MODE -> {
                    if (sharedPreferences.getBoolean(key, false)) {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    } else {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    }
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu
        menuInflater.inflate(R.menu.menu_toolbar, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.action_settings -> {
                Navigation.findNavController(this, R.id.nav_host_fragment)
                    .navigate(R.id.settingsFragment, null, NavOptions.Builder().setLaunchSingleTop(true).build())
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
