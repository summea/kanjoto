package com.andrewsummers.otashu;

import com.andrewsummers.otashu.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity implements OnClickListener {
	
	private Button buttonGenerate = null;
	private Button buttonPlay = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// make sure to call super when needed
		super.onCreate(savedInstanceState);
		
		// pull specific layout for content view
		setContentView(R.layout.activity_main);
		
		// have to cast to Button in this case
		buttonGenerate = (Button)findViewById(R.id.button_generate);
		buttonGenerate.setOnClickListener(this);
		
		buttonPlay = (Button)findViewById(R.id.button_play);
		buttonPlay.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// change text of button clicked for now
		((Button)v).setText("I was clicked");
	}
}