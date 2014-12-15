package com.andrewsummers.otashu.fragment;

import java.util.Calendar;

import com.andrewsummers.otashu.R;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.util.Log;
import android.widget.TimePicker;

public class SettingsFragment extends PreferenceFragment implements TimePickerDialog.OnTimeSetListener {
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // load preferences from XML resource
        addPreferencesFromResource(R.xml.preferences);
        
        Preference button = (Preference)this.findPreference("button");
        button.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            
            @Override
            public boolean onPreferenceClick(Preference preference) {
                // TODO Auto-generated method stub
                showTimeDialog();
                return false;
            }
        });
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        // TODO Auto-generated method stub
        Log.d("MYLOG", "time set... hourOfDay: " + hourOfDay + " minute: " + minute);
    }

    public void showTimeDialog() {
        final Calendar cal = Calendar.getInstance();
        int hourOfDay = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);
        new TimePickerDialog(getActivity(), 0, null, hourOfDay, minute, true).show();
    }
}