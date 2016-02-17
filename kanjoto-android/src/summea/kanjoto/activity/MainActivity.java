
package summea.kanjoto.activity;

import java.io.File;

import summea.kanjoto.adapter.ImageAdapter;
import summea.kanjoto.data.KanjotoDatabaseHelper;
import summea.kanjoto.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

/**
 * MainActivity is the first screen that appears for the Kanjoto program. This main screen acts as a
 * menu to various areas of the program.
 */
public class MainActivity extends Activity implements OnClickListener {
    private SharedPreferences sharedPref;

    /**
     * onCreate override that provides menu buttons on menu view.
     * 
     * @param savedInstanceState Current application state data.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // make sure destination directory exists
        File path = Environment.getExternalStorageDirectory();
        String newDirectory = path.toString() + "/kanjoto/";
        File newFile = new File(newDirectory);

        if (!newFile.exists()) {
            Log.d("MYLOG", "> creating destination folder");
            newFile.mkdirs();
        }
        
        // check if database exists
        File databaseFile = new File(Environment.getExternalStorageDirectory().toString()
                + "/kanjoto/" + KanjotoDatabaseHelper.DATABASE_NAME);
        if (!databaseFile.exists()) {
            // need to create database file first
            KanjotoDatabaseHelper dbHelper = new KanjotoDatabaseHelper(this);
            dbHelper.getWritableDatabase();
            dbHelper.close();

            // let user name a new apprentice
            Intent intent = new Intent(MainActivity.this, NameApprenticeActivity.class);
            startActivity(intent);
        }

        // set default preferences
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);

        // get specific layout for content view
        setContentView(R.layout.activity_main);

        GridView gridView = (GridView) findViewById(R.id.gridview);
        gridView.setAdapter(new ImageAdapter(this));

        gridView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Intent intent = null;

                switch (position) {
                    case 0:
                        intent = new Intent(MainActivity.this, DataModeMainMenuActivity.class);
                        break;
                    case 1:
                        intent = new Intent(MainActivity.this, PlayModeMainMenuActivity.class);
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
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        boolean developerMode = sharedPref.getBoolean(
                "pref_developer_mode", false);
        if (developerMode) {
            inflater.inflate(R.menu.menu_main_developer, menu);
        } else {
            inflater.inflate(R.menu.menu_main, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = null;

        // handle menu item selection
        switch (item.getItemId()) {
            case R.id.menu_button_settings:
                intent = new Intent(this, SettingsActivity.class);
                break;
            case R.id.menu_button_labels:
                intent = new Intent(this, ViewAllLabelsActivity.class);
                break;
            case R.id.menu_button_export_database:
                intent = new Intent(this, ExportDatabaseActivity.class);
                break;
            case R.id.menu_button_import_database:
                confirmImport();
                break;
            case R.id.menu_button_database_dumper:
                intent = new Intent(this, DatabaseDumperActivity.class);
                break;
            case R.id.menu_button_bookmarks:
                intent = new Intent(this, ViewAllBookmarksActivity.class);
                break;
            case R.id.menu_button_notevalues:
                intent = new Intent(this, ViewAllNotevaluesActivity.class);
                break;
            case R.id.menu_button_graphs:
                intent = new Intent(this, ViewAllGraphsActivity.class);
                break;
            case R.id.menu_button_apprentice_scorecards:
                intent = new Intent(this, ViewAllApprenticeScorecardsActivity.class);
                break;
            case R.id.menu_button_apprentice_strongest_paths:
                intent = new Intent(this, ViewAllApprenticeStrongestPathsByEmotionActivity.class);
                break;
            case R.id.menu_button_emotion_fingerprints:
                intent = new Intent(this, ViewAllEmotionFingerprintsActivity.class);
                break;
            case R.id.menu_button_get_emotion_from_noteset:
                intent = new Intent(this, GetEmotionFromNotesetActivity.class);
                break;
            case R.id.menu_button_apprentices:
                intent = new Intent(this, ViewAllApprenticesActivity.class);
                break;
            case R.id.menu_button_learning_styles:
                intent = new Intent(this, ViewAllLearningStylesActivity.class);
                break;
        }

        if (intent != null) {
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    public void confirmImport() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.dialog_confirm_import_message).setTitle(
                R.string.dialog_confirm_import_title);
        builder.setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // user clicked ok, so go ahead and import database
                Intent intent = new Intent(getBaseContext(), ImportDatabaseActivity.class);
                startActivity(intent);
            }
        });
        builder.setNegativeButton(R.string.button_cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // user clicked cancel
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onResume() {
        super.onResume();

        // make sure menu is up to date (e.g. developer mode changes)
        invalidateOptionsMenu();
    }
}
