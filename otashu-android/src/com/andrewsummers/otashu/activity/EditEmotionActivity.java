
package com.andrewsummers.otashu.activity;

import java.util.ArrayList;
import java.util.List;

import com.andrewsummers.otashu.R;
import com.andrewsummers.otashu.data.EmotionsDataSource;
import com.andrewsummers.otashu.data.LabelsDataSource;
import com.andrewsummers.otashu.model.Emotion;
import com.andrewsummers.otashu.model.Label;

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
 * CreateEmotionActivity is an Activity which provides users the ability to create new emotions.
 */
public class EditEmotionActivity extends Activity implements OnClickListener {
    private Button buttonSave = null;
    private Emotion editEmotion;

    /**
     * onCreate override that provides emotion creation view to user .
     * 
     * @param savedInstanceState Current application state data.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // get specific layout for content view
        setContentView(R.layout.activity_edit_emotion);

        // add listeners to buttons
        // have to cast to Button in this case
        buttonSave = (Button) findViewById(R.id.button_save);
        buttonSave.setOnClickListener(this);

        // open data source handle
        EmotionsDataSource eds = new EmotionsDataSource(this);
        eds.open();

        int emotionId = (int) getIntent().getExtras().getLong("list_id");

        eds.close();

        editEmotion = eds.getEmotion(emotionId);

        EditText emotionNameText = (EditText) findViewById(R.id.edittext_emotion_name);
        // emotionNameText.setText(allEmotions.get(emotionId).getName());
        emotionNameText.setText(editEmotion.getName());

        LabelsDataSource lds = new LabelsDataSource(this);

        List<String> allLabels = new ArrayList<String>();
        allLabels = lds.getAllLabelListPreviews();

        Label selectedLabel = lds.getLabel(editEmotion.getLabelId());
        lds.close();

        Spinner spinner = null;

        // locate next spinner in layout
        spinner = (Spinner) findViewById(R.id.spinner_emotion_label);

        // create array adapter for list of emotions
        ArrayAdapter<String> labelsAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item);
        labelsAdapter.addAll(allLabels);

        // specify the default layout when list of choices appears
        labelsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // apply this adapter to the spinner
        spinner.setAdapter(labelsAdapter);
        spinner.setSelection(labelsAdapter.getPosition(selectedLabel.getName()));
    }

    /**
     * onClick override used to save emotion data once user clicks save button.
     * 
     * @param view Incoming view.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_save:
                // gather emotion data from form
                String emotionName;
                Spinner emotionLabel;

                LabelsDataSource lds = new LabelsDataSource(this);
                List<Long> allLabelIds = lds.getAllLabelListDBTableIds();
                lds.close();

                Emotion emotionToUpdate = new Emotion();
                emotionToUpdate.setId(editEmotion.getId());

                emotionName = ((EditText) findViewById(R.id.edittext_emotion_name)).getText()
                        .toString();
                emotionLabel = (Spinner) findViewById(R.id.spinner_emotion_label);

                emotionToUpdate.setName(emotionName.toString());
                emotionToUpdate.setLabelId(allLabelIds.get(emotionLabel.getSelectedItemPosition()));

                // first insert new emotion (parent of all related notes)
                saveEmotionUpdates(v, emotionToUpdate);

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
     * Save emotion data.
     * 
     * @param v Incoming view.
     * @param data Incoming string of data to be saved.
     */
    private void saveEmotionUpdates(View v, Emotion emotion) {

        // save emotion in database
        EmotionsDataSource eds = new EmotionsDataSource(this);
        eds.updateEmotion(emotion);
        eds.close();

        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context,
                context.getResources().getString(R.string.emotion_saved),
                duration);
        toast.show();
    }
}
