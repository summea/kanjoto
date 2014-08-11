package com.andrewsummers.otashu;

import com.andrewsummers.otashu.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/**
 * MainActivity currently acts as a general menu in order to demo
 * various functionality available in this application.
 */
public class MainActivity extends Activity implements OnClickListener {
	
	private Button buttonCreateNoteset = null;
	private Button buttonViewAllNotesets = null;
	private Button buttonViewSequence = null;
	private Button buttonGetRemoteNoteset = null;
	private Button buttonSettings = null;
	
	/**
	 * onCreate override that provides menu buttons on menu view.
	 * 
	 * @param savedInstanceState	Current application state data.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// get specific layout for content view
		setContentView(R.layout.activity_main);
		
		// add listeners to buttons
		// have to cast to Button in this case
		buttonCreateNoteset = (Button)findViewById(R.id.button_create_noteset);
		buttonCreateNoteset.setOnClickListener(this);
		
		buttonViewAllNotesets = (Button)findViewById(R.id.button_view_all_notesets);
		buttonViewAllNotesets.setOnClickListener(this);
		
		buttonViewSequence = (Button)findViewById(R.id.button_view_sequence);
		buttonViewSequence.setOnClickListener(this);
		
		buttonGetRemoteNoteset = (Button)findViewById(R.id.button_get_remote_noteset);
		buttonGetRemoteNoteset.setOnClickListener(this);
		
		buttonSettings = (Button)findViewById(R.id.button_settings);
		buttonSettings.setOnClickListener(this);
	}

	/**
	 * onClick override that acts as a router to start desired activities.
	 * 
	 * @param view		Incoming view.
	 */
	@Override
	public void onClick(View v) {
		Intent intent = null;
		
		switch (v.getId()) {
			case R.id.button_create_noteset:
				intent = new Intent(this, CreateNotesetActivity.class);
				startActivity(intent);
				break;
			case R.id.button_view_all_notesets:
				intent = new Intent(this, ViewAllNotesetsActivity.class);
				startActivity(intent);
				break;
			case R.id.button_view_sequence:
				intent = new Intent(this, ViewNotesetSequenceActivity.class);
				startActivity(intent);
				break;
			case R.id.button_get_remote_noteset:
				intent = new Intent(this, GetRemoteNotesetActivity.class);
				startActivity(intent);
				break;
			case R.id.button_settings:
				intent = new Intent(this, SettingsActivity.class);
				startActivity(intent);
				break;
		}
	}
}