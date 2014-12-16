package com.andrewsummers.otashu.fragment;

import java.util.Calendar;
import java.util.Date;

import com.andrewsummers.otashu.OtashuReceiver;
import com.andrewsummers.otashu.R;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
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

        int second = 0; // used to set seconds for calendar alarm
        
        // save preferences
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("alarm_hour_of_day", hourOfDay);
        editor.putInt("alarm_minute",  minute);
        editor.commit();
        
        // load preferences
        boolean alarmEnabled = sharedPref.getBoolean("pref_alarm_enabled", false);
        
        // set alarm, if enabled
        if (alarmEnabled) {
            AlarmManager am = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
            
            // get current time
            Date dateNow = new Date();
            Calendar calAlarm = Calendar.getInstance();
            Calendar calNow = Calendar.getInstance();
            
            // init time
            calNow.setTime(dateNow);
            calAlarm.setTime(dateNow);
            
            // set calendar alarm
            calAlarm.set(Calendar.HOUR_OF_DAY, hourOfDay);
            calAlarm.set(Calendar.MINUTE, minute);
            calAlarm.set(Calendar.SECOND, second);
            
            // make sure time starts for following day (if necessary)
            if (calAlarm.before(calNow)) {
                calAlarm.add(Calendar.DATE, 1);
            }
    
            Intent intent = new Intent(getActivity(), OtashuReceiver.class);
            PendingIntent sender = PendingIntent.getBroadcast(getActivity(), 0, intent, PendingIntent.FLAG_ONE_SHOT);
            
            am.set(AlarmManager.RTC_WAKEUP, calAlarm.getTimeInMillis(), sender);
            Log.d("MYLOG", "alarm set: hour: " + hourOfDay + " minute: " + minute);
        }
    }

    public void showTimeDialog() {
        final Calendar cal = Calendar.getInstance();
        
        // load preferences
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());      

        int hourOfDay = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);
        
        try {
            hourOfDay = sharedPref.getInt("alarm_hour_of_day", Calendar.HOUR_OF_DAY);
            minute = sharedPref.getInt("alarm_minute", Calendar.MINUTE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        OnTimeSetListener timeSetListener = (OnTimeSetListener) this;
        
        new TimePickerDialog(getActivity(), 0, timeSetListener, hourOfDay, minute, true).show();
    }
}