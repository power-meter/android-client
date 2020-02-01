package io.mochahub.powermeter

import android.content.SharedPreferences
import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference

// TODO(atul): Deduplicate below strings and the ones in MainActivity file
private const val NIGHT_MODE = "night_mode"
private const val SYSTEM_THEME = "match_system_theme"

class SettingsFragment : PreferenceFragmentCompat(), SharedPreferences.OnSharedPreferenceChangeListener {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preference, rootKey)
        activity?.title = resources.getString(R.string.settings_screen_label)

        findPreference<SwitchPreference>(NIGHT_MODE)?.isEnabled = !(preferenceManager.sharedPreferences.getBoolean(SYSTEM_THEME, false))
    }

    // TODO(atul): Figure out bug where NIGHT_MODE toggle doesn't get disabled when it is checked and SYSTEM_THEME is also checked

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        when (key) {
            SYSTEM_THEME -> {
                findPreference<SwitchPreference>(NIGHT_MODE)?.isEnabled = !((sharedPreferences ?: preferenceManager.sharedPreferences).getBoolean(SYSTEM_THEME, false))
            }
        }
    }
}
