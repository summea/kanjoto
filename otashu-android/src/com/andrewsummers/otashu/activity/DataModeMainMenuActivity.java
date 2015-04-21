
package com.andrewsummers.otashu.activity;

import com.andrewsummers.otashu.R;
import com.andrewsummers.otashu.adapter.DataModeMenuImageAdapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

public class DataModeMainMenuActivity extends Activity implements OnClickListener {

    /**
     * onCreate override that provides menu buttons on menu view.
     * 
     * @param savedInstanceState Current application state data.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // set default preferences
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);

        // get specific layout for content view
        setContentView(R.layout.activity_data_mode_main_menu);

        GridView gridView = (GridView) findViewById(R.id.gridview);
        gridView.setAdapter(new DataModeMenuImageAdapter(this));

        gridView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Intent intent = null;

                switch (position) {
                    case 0:
                        intent = new Intent(DataModeMainMenuActivity.this,
                                ViewAllNotesetsActivity.class);
                        break;
                    case 1:
                        intent = new Intent(DataModeMainMenuActivity.this,
                                ChooseEmotionActivity.class);
                        break;
                    case 2:
                        intent = new Intent(DataModeMainMenuActivity.this,
                                ViewAllEmotionsActivity.class);
                        break;
                    case 3:
                        intent = new Intent(DataModeMainMenuActivity.this, ApprenticeActivity.class);
                        break;
                }

                if (intent != null) {
                    startActivity(intent);
                }
            }
        });
    }

    /**
     * onClick override that acts as a router to start desired activities.
     * 
     * @param view Incoming view.
     */
    @Override
    public void onClick(View v) {
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
                break;
            case R.id.view_labels:
                intent = new Intent(this, ViewAllLabelsActivity.class);
                break;
            case R.id.export_database:
                intent = new Intent(this, ExportDatabaseActivity.class);
                break;
            case R.id.import_database:
                intent = new Intent(this, ImportDatabaseActivity.class);
                break;
            case R.id.database_dumper:
                intent = new Intent(this, DatabaseDumperActivity.class);
                break;
            case R.id.view_bookmarks:
                intent = new Intent(this, ViewAllBookmarksActivity.class);
                break;
            case R.id.view_notevalues:
                intent = new Intent(this, ViewAllNotevaluesActivity.class);
                break;
            case R.id.view_graphs:
                intent = new Intent(this, ViewAllGraphsActivity.class);
                break;
            case R.id.view_apprentice_scorecards:
                intent = new Intent(this, ViewAllApprenticeScorecardsActivity.class);
                break;
            case R.id.view_apprentice_strongest_paths:
                intent = new Intent(this, ViewAllApprenticeStrongestPathsByEmotionActivity.class);
                break;
            case R.id.view_emotion_fingerprints:
                intent = new Intent(this, ViewAllEmotionFingerprintsActivity.class);
                break;
            case R.id.get_emotion_from_noteset:
                intent = new Intent(this, GetEmotionFromNotesetActivity.class);
                break;
            case R.id.view_apprentices:
                intent = new Intent(this, ViewAllApprenticesActivity.class);
                break;
            case R.id.view_learning_styles:
                intent = new Intent(this, ViewAllLearningStylesActivity.class);
                break;
        }

        startActivity(intent);
        return super.onOptionsItemSelected(item);
    }
}
