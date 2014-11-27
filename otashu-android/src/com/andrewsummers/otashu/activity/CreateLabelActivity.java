package com.andrewsummers.otashu.activity;

import com.andrewsummers.otashu.R;
import com.andrewsummers.otashu.data.LabelsDataSource;
import com.andrewsummers.otashu.model.Label;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * CreateLabelActivity is an Activity which provides users the ability to
 * create new labels.
 */
public class CreateLabelActivity extends Activity implements OnClickListener {
    private Button buttonSave = null;
    private Label newlyInsertedLabel = new Label();

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
        setContentView(R.layout.activity_create_label);

        // add listeners to buttons
        // have to cast to Button in this case
        buttonSave = (Button) findViewById(R.id.button_save);
        buttonSave.setOnClickListener(this);
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
            String labelName;
            
            Label labelToInsert = new Label();
            
            labelName = ((EditText) findViewById(R.id.edittext_label_name)).getText().toString();
            
            labelToInsert.setName(labelName.toString());
            
            // first insert new label (parent of all related notes)
            saveLabel(v, labelToInsert);
            
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
     * Save label data.
     * 
     * @param v
     *            Incoming view.
     * @param data
     *            Incoming string of data to be saved.
     */
    private void saveLabel(View v, Label label) {

        // save label in database
        LabelsDataSource lds = new LabelsDataSource(this);
        setNewlyInsertedLabel(lds.createLabel(label));
        lds.close();
        
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context,
                context.getResources().getString(R.string.label_saved),
                duration);
        toast.show();
    }

    public Label getNewlyInsertedLabel() {
        return newlyInsertedLabel;
    }

    public void setNewlyInsertedLabel(Label newlyInsertedLabel) {
        this.newlyInsertedLabel = newlyInsertedLabel;
    }
}