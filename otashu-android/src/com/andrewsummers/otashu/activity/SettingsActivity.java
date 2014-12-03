package com.andrewsummers.otashu.activity;

import com.andrewsummers.otashu.fragment.SettingsFragment;


import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;

/**
 * Settings are general settings for the overall application.
 */
public class SettingsActivity extends Activity {
    /**
     * onCreate override used to restore existing settings when viewing Settings
     * activity.
     * 
     * @param savedInstanceState
     *            Current application state data.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // display fragment as main content
        getFragmentManager().beginTransaction().replace(android.R.id.content, new SettingsFragment()).commit();        
    }
}