package io.github.dkocian.vtd_xml_example.activities;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceFragment;

import io.github.dkocian.vtd_xml_example.R;

/**
 * This preference activity has in its manifest declaration an intent filter for
 * the ACTION_MANAGE_NETWORK_USAGE action. This activity provides a settings UI
 * for users to specify network settings to control data usage.
 */
public class SettingsActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Display the fragment as the main content.
        getFragmentManager().beginTransaction().replace(android.R.id.content, new SettingsFragment()).commit();
    }

    public static class SettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            // Loads the XML preferences file.
            addPreferencesFromResource(R.xml.preferences);
        }

        @Override
        public void onResume() {
            super.onResume();
            // Registers a callback to be invoked whenever a user changes a preference.
            getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
        }

        @Override
        public void onPause() {
            super.onPause();
            // Unregisters the listener set in onResume().
            // It's best practice to unregister listeners when your app isn't using them to cut down on
            // unnecessary system overhead. You do this in onPause().
            getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
        }

        // Fires when the user changes a preference.
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            // Sets refreshDisplay to true so that when the user returns to the network_activity_ui
            // activity, the display refreshes to reflect the new settings.
            NetworkActivity.refreshDisplay = true;
        }
    }
}