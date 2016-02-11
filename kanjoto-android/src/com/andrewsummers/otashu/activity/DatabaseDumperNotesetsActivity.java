
package com.andrewsummers.otashu.activity;

import java.util.List;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.TextView;

import com.andrewsummers.otashu.R;
import com.andrewsummers.otashu.data.NotesetsDataSource;
import com.andrewsummers.otashu.data.OtashuDatabaseHelper;
import com.andrewsummers.otashu.model.Noteset;

public class DatabaseDumperNotesetsActivity extends Activity {
    private SharedPreferences sharedPref;
    private long apprenticeId = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // get specific layout for content view
        setContentView(R.layout.activity_database_dumper);

        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        apprenticeId = Long.parseLong(sharedPref.getString(
                "pref_selected_apprentice", "1"));

        NotesetsDataSource nsds = new NotesetsDataSource(this);
        List<Noteset> allNotesets = nsds.getAllNotesets(apprenticeId);
        nsds.close();

        TextView debugText = (TextView) findViewById(R.id.debug_text);

        debugText.setText(debugText.getText().toString() + "Table: Notesets\n"
                + OtashuDatabaseHelper.COLUMN_ID + "|" + OtashuDatabaseHelper.COLUMN_NAME + "|"
                + OtashuDatabaseHelper.COLUMN_EMOTION_ID + "|"
                + OtashuDatabaseHelper.COLUMN_ENABLED + "|"
                + OtashuDatabaseHelper.COLUMN_APPRENTICE_ID + "\n");

        for (Noteset noteset : allNotesets) {

            String newText = debugText.getText().toString();
            newText += noteset.getId() + "|" + noteset.getName() + "|" + noteset.getEmotion() + "|"
                    + noteset.getEnabled() + "|" + noteset.getApprenticeId()
                    + "\n";

            debugText.setText(newText);
        }
    }
}
