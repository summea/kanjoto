package com.andrewsummers.otashu.activity;

import java.util.ArrayList;
import java.util.List;

import com.andrewsummers.otashu.R;
import com.andrewsummers.otashu.data.NotevalueLabelsDataSource;
import com.andrewsummers.otashu.data.LabelsDataSource;
import com.andrewsummers.otashu.model.NotevalueLabel;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

/**
 * CreateNoteLabelActivity is an Activity which provides users the ability to
 * create new noteLabels.
 */
public class CreateNotevalueLabelActivity extends Activity implements OnClickListener {
    private Button buttonSave = null;    
    private NotevalueLabel newlyInsertedNoteLabel = new NotevalueLabel();

    /**
     * onCreate override that provides noteLabel creation view to user .
     * 
     * @param savedInstanceState
     *            Current application state data.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // get specific layout for content view
        setContentView(R.layout.activity_create_notevalue_label);

        // add listeners to buttons
        // have to cast to Button in this case
        buttonSave = (Button) findViewById(R.id.button_save);
        buttonSave.setOnClickListener(this);

        LabelsDataSource lds = new LabelsDataSource(this);
        
        List<String> allLabels = new ArrayList<String>();
        allLabels = lds.getAllLabelListPreviews();
        lds.close();
        
        Spinner spinner = null;

        // locate next spinner in layout
        //spinner = (Spinner) findViewById(R.id.spinner_noteLabel_label);
        
        // create array adapter for list of noteLabels
        ArrayAdapter<String> labelsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
        labelsAdapter.addAll(allLabels);
        
        // specify the default layout when list of choices appears
        labelsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        
        // apply this adapter to the spinner
        //spinner.setAdapter(labelsAdapter);
    }

    /**
     * onClick override used to save noteLabel data once user clicks save button.
     * 
     * @param view
     *            Incoming view.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.button_save:
            // gather noteLabel data from form
            String noteLabelName;
            Spinner noteLabelLabel;
            
            LabelsDataSource lds = new LabelsDataSource(this);
            List<Long> allLabelIds = lds.getAllLabelListDBTableIds();
            lds.close();
            /*
            NotevalueLabel noteLabelToInsert = new NotevalueLabel();
            
            noteLabelName = ((EditText) findViewById(R.id.edittext_noteLabel_name)).getText().toString();
            noteLabelLabel = (Spinner) findViewById(R.id.spinner_noteLabel_label);
            
            noteLabelToInsert.setName(noteLabelName.toString());
            noteLabelToInsert.setLabelId(allLabelIds.get(noteLabelLabel.getSelectedItemPosition()));
            
            // first insert new noteLabel (parent of all related notes)
            saveNoteLabel(v, noteLabelToInsert);
            */
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
     * Save noteLabel data.
     * 
     * @param v
     *            Incoming view.
     * @param data
     *            Incoming string of data to be saved.
     */
    private void saveNoteLabel(View v, NotevalueLabel noteLabel) {

        // save noteLabel in database
        NotevalueLabelsDataSource eds = new NotevalueLabelsDataSource(this);
        setNewlyInsertedNoteLabel(eds.createNoteLabel(noteLabel));
        eds.close();
        
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;

        /*
        Toast toast = Toast.makeText(context,
                context.getResources().getString(R.string.notevalue_label_saved),
                duration);
        toast.show();
        */
    }

    public NotevalueLabel getNewlyInsertedNoteLabel() {
        return newlyInsertedNoteLabel;
    }

    public void setNewlyInsertedNoteLabel(NotevalueLabel newlyInsertedNoteLabel) {
        this.newlyInsertedNoteLabel = newlyInsertedNoteLabel;
    }
}