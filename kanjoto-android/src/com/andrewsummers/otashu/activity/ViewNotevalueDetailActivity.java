
package com.andrewsummers.otashu.activity;

import com.andrewsummers.otashu.R;
import com.andrewsummers.otashu.data.LabelsDataSource;
import com.andrewsummers.otashu.data.NotevaluesDataSource;
import com.andrewsummers.otashu.model.Label;
import com.andrewsummers.otashu.model.Notevalue;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

/**
 * View details of a particular Notevalue.
 * <p>
 * This activity allows a user to see more information about a particular Notevalue. Notevalues are
 * conversion objects between MIDI notevalues and their respective formatted note strings (e.g.
 * A0:21, C8:108). Notevalues are used to provide an easier-to-read format for musical notevalues.
 * </p>
 */
public class ViewNotevalueDetailActivity extends Activity {
    private long notevalueId = 0;

    /**
     * onCreate override used to get details view.
     * 
     * @param savedInstanceState Current application state data.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // get specific layout for content view
        setContentView(R.layout.activity_view_notevalue_detail);

        notevalueId = getIntent().getExtras().getLong("list_id");

        Notevalue notevalue = new Notevalue();
        NotevaluesDataSource nvds = new NotevaluesDataSource(this);
        notevalue = nvds.getNotevalue(notevalueId);
        nvds.close();

        LabelsDataSource lds = new LabelsDataSource(this);
        Label label = lds.getLabel(notevalue.getLabelId());
        lds.close();

        // load form data
        TextView notevalueNotevalue = (TextView) findViewById(R.id.notevalue_notevalue_value);
        notevalueNotevalue.setText(String.valueOf(notevalue.getNotevalue()));

        TextView notevalueLabel = (TextView) findViewById(R.id.notevalue_label_value);
        notevalueLabel.setText(notevalue.getNotelabel());

        // most labels have a background color
        if (label.getColor() != null)
            notevalueLabel.setBackgroundColor(Color.parseColor(label.getColor()));
    }
}
