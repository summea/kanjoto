
package com.andrewsummers.otashu.activity;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.andrewsummers.otashu.R;
import com.andrewsummers.otashu.data.KeyNotesDataSource;
import com.andrewsummers.otashu.data.OtashuDatabaseHelper;
import com.andrewsummers.otashu.model.KeyNote;

public class DatabaseDumperKeyNotesActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // get specific layout for content view
        setContentView(R.layout.activity_database_dumper);

        KeyNotesDataSource edds = new KeyNotesDataSource(this);
        List<KeyNote> allKeyNotes = edds.getAllKeyNotes();
        edds.close();

        TextView debugText = (TextView) findViewById(R.id.debug_text);

        debugText
                .setText(debugText.getText().toString() + "Table: Key Notes\n"
                        + OtashuDatabaseHelper.COLUMN_ID + "|"
                        + OtashuDatabaseHelper.COLUMN_KEY_SIGNATURE_ID + "|"
                        + OtashuDatabaseHelper.COLUMN_NOTEVALUE + "|"
                        + OtashuDatabaseHelper.COLUMN_WEIGHT + "|"
                        + OtashuDatabaseHelper.COLUMN_APPRENTICE_ID + "\n");

        for (KeyNote keyNote : allKeyNotes) {

            String newText = debugText.getText().toString();
            newText += keyNote.getId() + "|"
                    + keyNote.getKeySignatureId() + "|"
                    + keyNote.getNotevalue() + "|"
                    + keyNote.getWeight()
                    + keyNote.getApprenticeId() + "\n";

            debugText.setText(newText);
        }
    }
}
