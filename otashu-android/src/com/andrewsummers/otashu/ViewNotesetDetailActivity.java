package com.andrewsummers.otashu;

import android.app.Activity;
import android.os.Bundle;

public class ViewNotesetDetailActivity extends Activity {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// get specific layout for content view
		setContentView(R.layout.activity_view_noteset_detail);
	}
	
}
