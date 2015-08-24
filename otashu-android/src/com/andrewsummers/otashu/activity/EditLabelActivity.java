
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
 * EditLabelActivity is an Activity which provides users the ability to edit labels.
 * <p>
 * This activity provides a form for editing existing Labels. Label to edit is selected either via
 * the "view all labels" activity or by the related "edit" context menu. The edit form fills in data
 * found (from the database) for specified Label to edit and (if successful) any saved updates will
 * then be saved in the database.
 * </p>
 */
public class EditLabelActivity extends Activity implements OnClickListener {
    private Button buttonSave = null;
    private Label editLabel; // keep track of which Label is currently being edited

    /**
     * onCreate override that provides label creation view to user .
     * 
     * @param savedInstanceState Current application state data.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // get specific layout for content view
        setContentView(R.layout.activity_edit_label);

        // add listeners to buttons
        buttonSave = (Button) findViewById(R.id.button_save);
        buttonSave.setOnClickListener(this);

        // open data source handle
        LabelsDataSource lds = new LabelsDataSource(this);
        long labelId = getIntent().getExtras().getLong("list_id");

        editLabel = lds.getLabel(labelId);
        lds.close();

        // fill in form data
        EditText labelNameText = (EditText) findViewById(R.id.edittext_label_name);
        labelNameText.setText(editLabel.getName());

        EditText labelColorText = (EditText) findViewById(R.id.edittext_label_color);
        labelColorText.setText(editLabel.getColor());
    }

    /**
     * onClick override used to save label data once user clicks save button.
     * 
     * @param view Incoming view.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_save:
                // gather label data from form
                Label labelToUpdate = new Label();

                labelToUpdate.setId(editLabel.getId());

                String labelName = ((EditText) findViewById(R.id.edittext_label_name)).getText()
                        .toString();
                String labelColor = ((EditText) findViewById(R.id.edittext_label_color)).getText()
                        .toString();

                labelToUpdate.setName(labelName.toString());
                labelToUpdate.setColor(labelColor.toString());

                // first insert new label (parent of all related notes)
                saveLabelUpdates(v, labelToUpdate);

                // close activity
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
     * @param v Incoming view.
     * @param data Incoming string of data to be saved.
     */
    private void saveLabelUpdates(View v, Label label) {
        // save label in database
        LabelsDataSource lds = new LabelsDataSource(this);
        lds.updateLabel(label);
        lds.close();

        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context,
                context.getResources().getString(R.string.label_saved),
                duration);
        toast.show();
    }
}
