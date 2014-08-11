package com.andrewsummers.otashu;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.CheckBox;

/**
 * Settings are general settings for the overall application.
 */
public class SettingsActivity extends Activity {
	
	/**
	 * onCreate override used to restore existing settings when viewing
	 * Settings activity.
	 * 
	 * @param savedInstanceState	Current application state data.
	 */
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
	
	/**
	 * onStop override used to save settings when user is done viewing
	 * Settings activity.
	 */
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