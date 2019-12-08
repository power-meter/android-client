package io.mochahub.powermeter

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat

private const val TITLE = "Settings"

class SettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preference, rootKey)
        activity?.title = TITLE
    }
}
