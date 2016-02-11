
package com.andrewsummers.kanjoto.activity;

import com.andrewsummers.kanjoto.R;
import com.andrewsummers.kanjoto.data.LearningStylesDataSource;
import com.andrewsummers.kanjoto.model.LearningStyle;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * CreateLearningStyleActivity is an Activity which provides users the ability to create new
 * learningStyles.
 * <p>
 * This activity provides a form for creating a new LearningStyle. LearningStyles are used for a
 * variety of different purposes, for example: assigning colors to notes for lists and visual
 * playback, and providing background colors for emotions.
 * </p>
 */
public class CreateLearningStyleActivity extends Activity implements OnClickListener {
    private Button buttonSave = null;
    private LearningStyle newlyInsertedLearningStyle = new LearningStyle();

    /**
     * onCreate override that provides learningStyle creation view to user .
     * 
     * @param savedInstanceState Current application state data.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // get specific layout for content view
        setContentView(R.layout.activity_create_learning_style);

        // add listeners to buttons
        buttonSave = (Button) findViewById(R.id.button_save);
        buttonSave.setOnClickListener(this);
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
                LearningStyle learningStyleToInsert = new LearningStyle();

                String learningStyleName = ((EditText) findViewById(R.id.edittext_learning_style_name))
                        .getText()
                        .toString();

                learningStyleToInsert.setName(learningStyleName.toString());

                // first insert new learningStyle (parent of all related notes)
                saveLearningStyle(v, learningStyleToInsert);

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
    private void saveLearningStyle(View v, LearningStyle learningStyle) {
        // save learningStyle in database
        LearningStylesDataSource lsds = new LearningStylesDataSource(this);
        setNewlyInsertedLearningStyle(lsds.createLearningStyle(learningStyle));
        lsds.close();

        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context,
                context.getResources().getString(R.string.learning_style_saved),
                duration);
        toast.show();
    }

    public LearningStyle getNewlyInsertedLearningStyle() {
        return newlyInsertedLearningStyle;
    }

    public void setNewlyInsertedLearningStyle(LearningStyle newlyInsertedLearningStyle) {
        this.newlyInsertedLearningStyle = newlyInsertedLearningStyle;
    }
}
