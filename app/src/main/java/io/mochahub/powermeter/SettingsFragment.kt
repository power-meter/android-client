package io.mochahub.powermeter

import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference

// TODO(Atul): Deduplicate below strings and the ones in MainActivity file
private const val NIGHT_MODE = "night_mode"
private const val SYSTEM_THEME = "match_system_theme"

class SettingsFragment : PreferenceFragmentCompat(),
    SharedPreferences.OnSharedPreferenceChangeListener {

    override fun onResume() {
        super.onResume()
        preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        super.onPause()
        preferenceScreen.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preference, rootKey)
        activity?.title = resources.getString(R.string.settings_screen_label)

        findPreference<SwitchPreference>(NIGHT_MODE)?.isEnabled =
            !(preferenceManager.sharedPreferences.getBoolean(SYSTEM_THEME, false))
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        when (key) {
            NIGHT_MODE, SYSTEM_THEME -> {
                findPreference<SwitchPreference>(NIGHT_MODE)?.isEnabled =
                    !((sharedPreferences ?: preferenceManager.sharedPreferences).getBoolean(
                        SYSTEM_THEME,
                        false
                    ))
                when {
                    preferenceManager.sharedPreferences.getBoolean(SYSTEM_THEME, false) -> {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                    }
                    preferenceManager.sharedPreferences.getBoolean(NIGHT_MODE, false) -> {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    }
                    else -> {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    }
                }
            }
        }
    }
}
