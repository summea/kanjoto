package com.andrewsummers.otashu.activity;

import com.andrewsummers.otashu.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/**
 * MainActivity currently acts as a general menu in order to demo various
 * functionality available in this application.
 */
public class MainActivity extends Activity implements OnClickListener {
    private Button buttonChooseEmotion = null;
    private Button buttonViewAllNotesets = null;    
    private Button buttonViewAllEmotions = null;
    private Button buttonApprentice = null;

    /**
     * onCreate override that provides menu buttons on menu view.
     * 
     * @param savedInstanceState
     *            Current application state data.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // set default preferences
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
        
        // get specific layout for content view
        setContentView(R.layout.activity_main);

        try {
            // add listeners to buttons
            // have to cast to Button in this case    
            buttonViewAllNotesets = (Button) findViewById(R.id.button_view_all_notesets);
            buttonViewAllNotesets.setOnClickListener(this);
    
            buttonChooseEmotion = (Button) findViewById(R.id.button_choose_emotion);
            buttonChooseEmotion.setOnClickListener(this);
            
            buttonViewAllEmotions = (Button) findViewById(R.id.button_view_all_emotions);
            buttonViewAllEmotions.setOnClickListener(this);
            
            buttonApprentice = (Button) findViewById(R.id.button_apprentice);
            buttonApprentice.setOnClickListener(this);
        } catch (Exception e) {
            Log.d("MYLOG", e.getStackTrace().toString());
        }
    }

    /**
     * onClick override that acts as a router to start desired activities.
     * 
     * @param view
     *            Incoming view.
     */
    @Override
    public void onClick(View v) {
        Intent intent = null;

        switch (v.getId()) {
        case R.id.button_view_all_notesets:
            intent = new Intent(this, ViewAllNotesetsActivity.class);
            startActivity(intent);
            break;
        case R.id.button_choose_emotion:
            intent = new Intent(this, ChooseEmotionActivity.class);
            startActivity(intent);
            break;
        case R.id.button_view_all_emotions:
            intent = new Intent(this, ViewAllEmotionsActivity.class);
            startActivity(intent);
            break;
        case R.id.button_apprentice:
            intent = new Intent(this, ApprenticeActivity.class);
            startActivity(intent);
            break;
        }
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = null;
        
        // handle menu item selection
        switch (item.getItemId()) {
        case R.id.view_settings:
            intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        case R.id.export_database:
            intent = new Intent(this, ExportDatabaseActivity.class);
            startActivity(intent);
            return true;
        case R.id.import_database:
            intent = new Intent(this, ImportDatabaseActivity.class);
            startActivity(intent);
            return true;
        case R.id.database_dumper:
            intent = new Intent(this, DatabaseDumperActivity.class);
            startActivity(intent);
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }
}