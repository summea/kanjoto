
package com.summea.kanjoto.activity;

import com.summea.kanjoto.R;
import com.summea.kanjoto.data.LearningStylesDataSource;
import com.summea.kanjoto.model.LearningStyle;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * EditLearningStyleActivity is an Activity which provides users the ability to edit learningStyles.
 * <p>
 * This activity provides a form for editing existing LearningStyles. LearningStyle to edit is
 * selected either via the "view all learningStyles" activity or by the related "edit" context menu.
 * The edit form fills in data found (from the database) for specified LearningStyle to edit and (if
 * successful) any saved updates will then be saved in the database.
 * </p>
 */
public class EditLearningStyleActivity extends Activity implements OnClickListener {
    private Button buttonSave = null;
    private LearningStyle editLearningStyle; // keep track of which LearningStyle is currently being
                                             // edited

    /**
     * onCreate override that provides learningStyle creation view to user .
     * 
     * @param savedInstanceState Current application state data.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // get specific layout for content view
        setContentView(R.layout.activity_edit_learning_style);

        // add listeners to buttons
        buttonSave = (Button) findViewById(R.id.button_save);
        buttonSave.setOnClickListener(this);

        // open data source handle
        LearningStylesDataSource lsds = new LearningStylesDataSource(this);
        long learningStyleId = getIntent().getExtras().getLong("list_id");

        editLearningStyle = lsds.getLearningStyle(learningStyleId);
        lsds.close();

        // fill in existing form data
        EditText learningStyleNameText = (EditText) findViewById(R.id.edittext_learning_style_name);
        learningStyleNameText.setText(editLearningStyle.getName());
    }

    /**
     * onClick override used to save learningStyle data once user clicks save button.
     * 
     * @param view Incoming view.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_save:
                // gather learningStyle data from form
                LearningStyle learningStyleToUpdate = new LearningStyle();
                learningStyleToUpdate.setId(editLearningStyle.getId());

                String learningStyleName = ((EditText) findViewById(R.id.edittext_learning_style_name))
                        .getText()
                        .toString();

                learningStyleToUpdate.setName(learningStyleName.toString());

                // first insert new learningStyle (parent of all related notes)
                saveLearningStyleUpdates(v, learningStyleToUpdate);

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
     * Save learningStyle data.
     * 
     * @param v Incoming view.
     * @param data Incoming string of data to be saved.
     */
    private void saveLearningStyleUpdates(View v, LearningStyle learningStyle) {

        // save learningStyle in database
        LearningStylesDataSource lsds = new LearningStylesDataSource(this);
        lsds.updateLearningStyle(learningStyle);
        lsds.close();

        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context,
                context.getResources().getString(R.string.learning_style_saved),
                duration);
        toast.show();
    }
}
