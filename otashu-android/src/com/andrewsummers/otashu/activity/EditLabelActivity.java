package com.andrewsummers.otashu.activity;

import java.util.ArrayList;
import java.util.List;

import com.andrewsummers.otashu.R;
import com.andrewsummers.otashu.data.LabelsDataSource;
import com.andrewsummers.otashu.model.Label;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * CreateLabelActivity is an Activity which provides users the ability to
 * create new labels.
 */
public class EditLabelActivity extends Activity implements OnClickListener {
    private Button buttonSave = null;
    private LabelsDataSource labelsDataSource;
    private Label editLabel;

    /**
     * onCreate override that provides label creation view to user .
     * 
     * @param savedInstanceState
     *            Current application state data.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // get specific layout for content view
        setContentView(R.layout.activity_edit_label);

        // add listeners to buttons
        // have to cast to Button in this case
        buttonSave = (Button) findViewById(R.id.button_save);
        buttonSave.setOnClickListener(this);

        // open data source handle
        labelsDataSource = new LabelsDataSource(this);
        labelsDataSource.open();
        
        Log.d("MYLOG", "got list item id: " + getIntent().getExtras().getLong("list_id"));
        int labelId = (int) getIntent().getExtras().getLong("list_id");
        
        Log.d("MYLOG", "label id: " + labelId);
        
        List<Label> allLabels = new ArrayList<Label>();
        allLabels = labelsDataSource.getAllLabels();
        
        labelsDataSource.close();

        editLabel = allLabels.get(labelId);
        
        EditText labelNameText = (EditText) findViewById(R.id.edittext_label_name);
        labelNameText.setText(allLabels.get(labelId).getName());
        
        EditText labelColorText = (EditText) findViewById(R.id.edittext_label_color);
        labelColorText.setText(allLabels.get(labelId).getColor());
    }

    /**
     * onClick override used to save label data once user clicks save button.
     * 
     * @param view
     *            Incoming view.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.button_save:
            // gather label data from form
            String labelColor;
            String labelName;
            
            Label labelToUpdate = new Label();
            
            labelToUpdate.setId(editLabel.getId());
            
            labelName = ((EditText) findViewById(R.id.edittext_label_name)).getText().toString();            
            labelColor = ((EditText) findViewById(R.id.edittext_label_color)).getText().toString();
            
            labelToUpdate.setName(labelName.toString());
            labelToUpdate.setColor(labelColor.toString());
            
            Log.d("MYLOG", "new label: " + labelName);
            
            // first insert new label (parent of all related notes)
            saveLabelUpdates(v, labelToUpdate);
            
            finish();
            break;
        }
    }

    /**
     * onResume override used to open up data source when resuming activity.
     */
    @Override
    protected void onResume() {
        labelsDataSource.open();
        super.onResume();
    }

    /**
     * onPause override used to close data source when activity paused.
     */
    @Override
    protected void onPause() {
        labelsDataSource.close();
        super.onPause();
    }

    /**
     * Save label data.
     * 
     * @param v
     *            Incoming view.
     * @param data
     *            Incoming string of data to be saved.
     */
    private void saveLabelUpdates(View v, Label label) {

        // save label in database
        labelsDataSource.updateLabel(label);
        
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context,
                context.getResources().getString(R.string.label_saved),
                duration);
        toast.show();
    }
}