
package com.andrewsummers.kanjoto.activity;

import java.util.List;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.TextView;

import com.andrewsummers.kanjoto.R;
import com.andrewsummers.kanjoto.data.NotesetsDataSource;
import com.andrewsummers.kanjoto.data.KanjotoDatabaseHelper;
import com.andrewsummers.kanjoto.model.Noteset;

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
                + KanjotoDatabaseHelper.COLUMN_ID + "|" + KanjotoDatabaseHelper.COLUMN_NAME + "|"
                + KanjotoDatabaseHelper.COLUMN_EMOTION_ID + "|"
                + KanjotoDatabaseHelper.COLUMN_ENABLED + "|"
                + KanjotoDatabaseHelper.COLUMN_APPRENTICE_ID + "\n");

        for (Noteset noteset : allNotesets) {

            String newText = debugText.getText().toString();
            newText += noteset.getId() + "|" + noteset.getName() + "|" + noteset.getEmotion() + "|"
                    + noteset.getEnabled() + "|" + noteset.getApprenticeId()
                    + "\n";

            debugText.setText(newText);
        }
    }
}
