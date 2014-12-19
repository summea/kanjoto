
package com.andrewsummers.otashu.activity;

import com.andrewsummers.otashu.R;
import com.andrewsummers.otashu.data.LabelsDataSource;
import com.andrewsummers.otashu.data.NotevaluesDataSource;
import com.andrewsummers.otashu.model.Label;
import com.andrewsummers.otashu.model.Notevalue;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

/**
 * View details of a particular notevalue.
 */
public class ViewNotevalueDetailActivity extends Activity {

    private int notevalueId = 0;

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

        Log.d("MYLOG", "got list item id: " + getIntent().getExtras().getLong("list_id"));
        notevalueId = (int) getIntent().getExtras().getLong("list_id");

        /*
         * // prevent crashes due to lack of database data if (allNotevaluesData.isEmpty())
         * allNotevaluesData.add((long) 0);
         */

        Notevalue notevalue = new Notevalue();
        NotevaluesDataSource nvds = new NotevaluesDataSource(this);
        notevalue = nvds.getNotevalue(notevalueId);
        nvds.close();

        LabelsDataSource lds = new LabelsDataSource(this);
        Label label = lds.getLabel(notevalue.getLabelId());
        lds.close();

        TextView notevalueNotevalue = (TextView) findViewById(R.id.notevalue_notevalue_value);
        notevalueNotevalue.setText(String.valueOf(notevalue.getNotevalue()));

        TextView notevalueLabel = (TextView) findViewById(R.id.notevalue_label_value);
        notevalueLabel.setText(notevalue.getNotelabel());

        if (label.getColor() != null)
            notevalueLabel.setBackgroundColor(Color.parseColor(label.getColor()));
    }
}
