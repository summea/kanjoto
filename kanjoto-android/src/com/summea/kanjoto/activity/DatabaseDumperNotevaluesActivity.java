
package com.summea.kanjoto.activity;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.summea.kanjoto.R;
import com.summea.kanjoto.data.KanjotoDatabaseHelper;
import com.summea.kanjoto.data.NotevaluesDataSource;
import com.summea.kanjoto.model.Notevalue;

public class DatabaseDumperNotevaluesActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // get specific layout for content view
        setContentView(R.layout.activity_database_dumper);

        NotevaluesDataSource nvds = new NotevaluesDataSource(this);
        List<Notevalue> allNotevalues = nvds.getAllNotevalues();
        nvds.close();

        TextView debugText = (TextView) findViewById(R.id.debug_text);

        debugText.setText(debugText.getText().toString() + "Table: Notevalues\n"
                + KanjotoDatabaseHelper.COLUMN_ID + "|" + KanjotoDatabaseHelper.COLUMN_NOTEVALUE
                + "|" + KanjotoDatabaseHelper.COLUMN_NOTELABEL + "|"
                + KanjotoDatabaseHelper.COLUMN_LABEL_ID + "\n");

        for (Notevalue notevalue : allNotevalues) {

            String newText = debugText.getText().toString();
            newText += notevalue.getId() + "|" + notevalue.getNotevalue() + "|"
                    + notevalue.getNotelabel() + "|" + notevalue.getLabelId() + "\n";

            debugText.setText(newText);
        }
    }
}
