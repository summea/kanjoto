
package com.andrewsummers.otashu.activity;

import java.util.ArrayList;
import java.util.List;

import com.andrewsummers.otashu.R;
import com.andrewsummers.otashu.data.ApprenticesDataSource;
import com.andrewsummers.otashu.data.LearningStylesDataSource;
import com.andrewsummers.otashu.model.Apprentice;
import com.andrewsummers.otashu.model.LearningStyle;

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
 * NameApprenticeActivity is an Activity which provides users the ability to edit apprentices.
 */
public class NameApprenticeActivity extends Activity implements OnClickListener {
    private Button buttonSave = null;
    private Apprentice editApprentice; // keep track of which Apprentice is currently being edited

    /**
     * onCreate override that provides apprentice creation view to user .
     * 
     * @param savedInstanceState Current application state data.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // get specific layout for content view
        setContentView(R.layout.activity_name_apprentice);

        // add listeners to buttons
        buttonSave = (Button) findViewById(R.id.button_save);
        buttonSave.setOnClickListener(this);

        int apprenticeId = 1;

        // open data source handle
        ApprenticesDataSource lds = new ApprenticesDataSource(this);
        editApprentice = lds.getApprentice(apprenticeId);
        lds.close();

        // fill in existing form data
        EditText apprenticeNameText = (EditText) findViewById(R.id.edittext_apprentice_name);
        apprenticeNameText.setText(editApprentice.getName());

        // get all learning styles for spinner
        LearningStylesDataSource lsds = new LearningStylesDataSource(this);
        List<LearningStyle> allLearningStyles = new ArrayList<LearningStyle>();
        allLearningStyles = lsds.getAllLearningStyles();
        lsds.close();

        // locate next spinner in layout
        Spinner spinner = (Spinner) findViewById(R.id.spinner_apprentice_learning_style);

        // create array adapter for list of learning styles
        ArrayAdapter<LearningStyle> learningStylesAdapter = new ArrayAdapter<LearningStyle>(this,
                android.R.layout.simple_spinner_item);
        learningStylesAdapter.addAll(allLearningStyles);

        // specify the default layout when list of choices appears
        learningStylesAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // apply this adapter to the spinner
        spinner.setAdapter(learningStylesAdapter);

        // set current selection for spinner
        for (int i = 0; i < allLearningStyles.size(); i++) {
            if (allLearningStyles.get(i).getId() == editApprentice.getLearningStyleId()) {
                spinner.setSelection(i);
                break;
            }
        }
    }

    /**
     * onClick override used to save apprentice data once user clicks save button.
     * 
     * @param view Incoming view.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_save:
                // gather apprentice data from form
                Apprentice apprenticeToUpdate = new Apprentice();
                apprenticeToUpdate.setId(editApprentice.getId());

                String apprenticeName = ((EditText) findViewById(R.id.edittext_apprentice_name))
                        .getText()
                        .toString();

                // get selected apprentice learning style from spinner
                Spinner apprenticeLearningStyleSpinner = (Spinner) findViewById(R.id.spinner_apprentice_learning_style);
                LearningStyle apprenticeLearningStyle = (LearningStyle) apprenticeLearningStyleSpinner
                        .getSelectedItem();

                apprenticeToUpdate.setName(apprenticeName.toString());
                apprenticeToUpdate.setLearningStyleId(apprenticeLearningStyle.getId());

                // first insert new apprentice (parent of all related notes)
                saveApprenticeUpdates(v, apprenticeToUpdate);

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
     * Save apprentice data.
     * 
     * @param v Incoming view.
     * @param data Incoming string of data to be saved.
     */
    private void saveApprenticeUpdates(View v, Apprentice apprentice) {
        // save apprentice in database
        ApprenticesDataSource lds = new ApprenticesDataSource(this);
        lds.updateApprentice(apprentice);
        lds.close();

        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context,
                context.getResources().getString(R.string.apprentice_named),
                duration);
        toast.show();
    }
}
