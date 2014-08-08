package com.andrewsummers.otashu;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.CheckBox;

public class SettingsActivity extends Activity {
	
	//public static final String PREFS_NAME = "OtashuSharedPrefs";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		
		// restore settings
		SharedPreferences settings = getSharedPreferences(getResources().getString(R.string.settings_file_name), 0);
		
		CheckBox checkboxTouchFeedback = (CheckBox)findViewById(R.id.touch_feedback);
		boolean touchFeedbackEnabled = settings.getBoolean("touchFeedbackEnabled", true);
		
		if (touchFeedbackEnabled) {
			checkboxTouchFeedback.setChecked(true);
		}
	}
	
	@Override
	public void onStop() {
		super.onStop();
		
		// save current settings
		SharedPreferences settings = getSharedPreferences(getResources().getString(R.string.settings_file_name), 0);
		SharedPreferences.Editor editor = settings.edit();
		
		CheckBox checkboxTouchFeedback = (CheckBox)findViewById(R.id.touch_feedback);
		editor.putBoolean("touchFeedbackEnabled", checkboxTouchFeedback.isChecked());
		
		editor.commit();
	}
}
