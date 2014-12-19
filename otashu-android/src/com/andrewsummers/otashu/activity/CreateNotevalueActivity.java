
package com.andrewsummers.otashu.activity;

import java.util.ArrayList;
import java.util.List;

import com.andrewsummers.otashu.R;
import com.andrewsummers.otashu.data.NotevaluesDataSource;
import com.andrewsummers.otashu.data.LabelsDataSource;
import com.andrewsummers.otashu.model.Notevalue;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

/**
 * CreateNotevalueActivity is an Activity which provides users the ability to create new notevalues.
 */
public class CreateNotevalueActivity extends Activity implements OnClickListener {
    private Button buttonSave = null;
    private Notevalue newlyInsertedNotevalue = new Notevalue();

    /**
     * onCreate override that provides notevalue creation view to user .
     * 
     * @param savedInstanceState Current application state data.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // get specific layout for content view
        setContentView(R.layout.activity_create_notevalue);

        // add listeners to buttons
        // have to cast to Button in this case
        buttonSave = (Button) findViewById(R.id.button_save);
        buttonSave.setOnClickListener(this);

        LabelsDataSource lds = new LabelsDataSource(this);

        List<String> allLabels = new ArrayList<String>();
        allLabels = lds.getAllLabelListPreviews();
        lds.close();

        Spinner spinner = null;

        ArrayAdapter<CharSequence> adapter = null;

        spinner = (Spinner) findViewById(R.id.spinner_notelabel);
        adapter = ArrayAdapter
                .createFromResource(this, R.array.note_labels_array,
                        android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(adapter.getPosition(String.valueOf("C4"))); // start at note C4

        // locate next spinner in layout
        spinner = (Spinner) findViewById(R.id.spinner_label);

        // create array adapter for list of notevalues
        ArrayAdapter<String> labelsAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item);
        labelsAdapter.addAll(allLabels);

        // specify the default layout when list of choices appears
        labelsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // apply this adapter to the spinner
        spinner.setAdapter(labelsAdapter);
    }

    /**
     * onClick override used to save notevalue data once user clicks save button.
     * 
     * @param view Incoming view.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_save:
                // gather notevalue data from form
                Spinner notevalueNotevalue;
                Spinner notevalueLabel;
                String[] noteValuesArray = getResources().getStringArray(R.array.note_values_array);
                String[] noteLabelsArray = getResources().getStringArray(R.array.note_labels_array);

                LabelsDataSource lds = new LabelsDataSource(this);
                List<Long> allLabelIds = lds.getAllLabelListDBTableIds();
                lds.close();

                Notevalue notevalueToInsert = new Notevalue();

                notevalueNotevalue = (Spinner) findViewById(R.id.spinner_notelabel);
                // notevalueNotelabel = (Spinner) findViewById(R.id.spinner_notelabel);
                notevalueLabel = (Spinner) findViewById(R.id.spinner_label);

                notevalueToInsert.setNotevalue(Integer.parseInt(noteValuesArray[notevalueNotevalue
                        .getSelectedItemPosition()]));
                notevalueToInsert.setNotelabel(noteLabelsArray[notevalueNotevalue
                        .getSelectedItemPosition()]);
                notevalueToInsert.setLabelId(allLabelIds.get(notevalueLabel
                        .getSelectedItemPosition()));

                // first insert new notevalue (parent of all related notes)
                saveNotevalue(v, notevalueToInsert);

                finish();
                break;
        }
    }

    /**
     * onResume override used to open up data source when resuming activity.
     */
    @Override
    protected void onResume() {
        super.onResume();
    }

    /**
     * onPause override used to close data source when activity paused.
     */
    @Override
    protected void onPause() {
        super.onPause();
    }

    /**
     * Save notevalue data.
     * 
     * @param v Incoming view.
     * @param data Incoming string of data to be saved.
     */
    private void saveNotevalue(View v, Notevalue notevalue) {

        // save notevalue in database
        NotevaluesDataSource nvds = new NotevaluesDataSource(this);
        setNewlyInsertedNotevalue(nvds.createNotevalue(notevalue));
        nvds.close();

        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context,
                context.getResources().getString(R.string.notevalue_saved),
                duration);
        toast.show();
    }

    public Notevalue getNewlyInsertedNotevalue() {
        return newlyInsertedNotevalue;
    }

    public void setNewlyInsertedNotevalue(Notevalue newlyInsertedNotevalue) {
        this.newlyInsertedNotevalue = newlyInsertedNotevalue;
    }
}
