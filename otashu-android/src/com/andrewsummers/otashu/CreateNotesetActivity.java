package com.andrewsummers.otashu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class CreateNotesetActivity extends Activity implements OnClickListener {

	private Button buttonSave = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// get specific layout for content view
		setContentView(R.layout.activity_create_noteset);
		
		// add listeners to buttons
		// have to cast to Button in this case
		buttonSave = (Button)findViewById(R.id.button_save);
		buttonSave.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		// change text of button clicked for now
		//((Button)v).setText("I was clicked");
		switch (v.getId()) {
			case R.id.button_save:
				Intent intent = new Intent(this, MainActivity.class);
				startActivity(intent);
				break;
		}
	}
}
