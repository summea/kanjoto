
package com.summea.kanjoto.activity;

import java.util.List;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.TextView;

import com.summea.kanjoto.R;
import com.summea.kanjoto.data.KanjotoDatabaseHelper;
import com.summea.kanjoto.data.KeyNotesDataSource;
import com.summea.kanjoto.model.KeyNote;

public class DatabaseDumperKeyNotesActivity extends Activity {
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

        KeyNotesDataSource edds = new KeyNotesDataSource(this);
        List<KeyNote> allKeyNotes = edds.getAllKeyNotes(apprenticeId);
        edds.close();

        TextView debugText = (TextView) findViewById(R.id.debug_text);

        debugText
                .setText(debugText.getText().toString() + "Table: Key Notes\n"
                        + KanjotoDatabaseHelper.COLUMN_ID + "|"
                        + KanjotoDatabaseHelper.COLUMN_KEY_SIGNATURE_ID + "|"
                        + KanjotoDatabaseHelper.COLUMN_NOTEVALUE + "|"
                        + KanjotoDatabaseHelper.COLUMN_WEIGHT + "|"
                        + KanjotoDatabaseHelper.COLUMN_APPRENTICE_ID + "\n");

        for (KeyNote keyNote : allKeyNotes) {

            String newText = debugText.getText().toString();
            newText += keyNote.getId() + "|"
                    + keyNote.getKeySignatureId() + "|"
                    + keyNote.getNotevalue() + "|"
                    + keyNote.getWeight() + "|"
                    + keyNote.getApprenticeId() + "\n";

            debugText.setText(newText);
        }
    }
}
