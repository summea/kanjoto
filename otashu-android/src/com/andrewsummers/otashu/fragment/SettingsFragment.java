package com.andrewsummers.otashu.fragment;

import com.andrewsummers.otashu.R;

import android.os.Bundle;
import android.preference.PreferenceFragment;

public class SettingsFragment extends PreferenceFragment {
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // load preferences from XML resource
        addPreferencesFromResource(R.xml.preferences);
    }

}