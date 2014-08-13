package com.andrewsummers.otashu;

import android.app.Activity;
import android.os.Bundle;

/**
 * View details of a particular noteset.
 */
public class ViewNotesetDetailActivity extends Activity {
	/**
	 * onCreate override used to get details view.
	 * 
	 * @param savedInstanceState	Current application state data.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// get specific layout for content view
		setContentView(R.layout.activity_view_noteset_detail);
	}
}