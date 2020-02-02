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
import com.google.firebase.analytics.FirebaseAnalytics
import com.microsoft.appcenter.AppCenter
import com.microsoft.appcenter.analytics.Analytics
import com.microsoft.appcenter.crashes.Crashes
import com.microsoft.appcenter.push.Push
import kotlinx.android.synthetic.main.activity_main.bottom_navigation
import kotlinx.android.synthetic.main.activity_main.toolbar

private const val NIGHT_MODE = "night_mode"
private const val SYSTEM_THEME = "match_system_theme"

class MainActivity : AppCompatActivity() {

    private lateinit var firebaseAnalytics: FirebaseAnalytics

    private val preferences: SharedPreferences
        get() = PreferenceManager.getDefaultSharedPreferences(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCenter.start(
            application, BuildConfig.HOCKEY_APP_SECRET,
            Push::class.java, Analytics::class.java, Crashes::class.java
        )
        firebaseAnalytics = FirebaseAnalytics.getInstance(this)

        if (!BuildConfig.DEBUG) {
            Analytics.trackEvent("Hello World")
        }
        // Firebase will point to debug / prod automatically
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.APP_OPEN, Bundle.EMPTY)

        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val navController: NavController =
            Navigation.findNavController(this, R.id.nav_host_fragment)
        NavigationUI.setupWithNavController(bottom_navigation, navController)

        when {
            preferences.getBoolean(SYSTEM_THEME, false) -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            }
            preferences.getBoolean(NIGHT_MODE, false) -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
            else -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
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
                    .navigate(
                        R.id.settingsFragment,
                        null,
                        NavOptions.Builder().setLaunchSingleTop(true).build()
                    )
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
