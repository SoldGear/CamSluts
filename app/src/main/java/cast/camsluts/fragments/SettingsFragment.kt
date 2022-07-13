package cast.camsluts.fragments

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.BuildCompat
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import cast.camsluts.R

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
        val preference = findPreference<ListPreference>(getString(R.string.pref_key_night))
        preference?.onPreferenceChangeListener = darkModeChangeListener

    }
    private val darkModeChangeListener =
    Preference.OnPreferenceChangeListener { _, newValue ->
        Log.i("newValue", newValue.toString())
        newValue as? String
        when (newValue) {
            getString(R.string.pref_night_on) -> {
                updateTheme(AppCompatDelegate.MODE_NIGHT_YES)
            }
            getString(R.string.pref_night_off) -> {
                updateTheme(AppCompatDelegate.MODE_NIGHT_NO)
            }
            else -> {
                if (BuildCompat.isAtLeastQ()) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY);
                }
            }
        }
        true
    }

    private fun updateTheme(nightMode: Int): Boolean {
        AppCompatDelegate.setDefaultNightMode(nightMode)
        requireActivity().recreate()
        return true
    }


}