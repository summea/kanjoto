
package com.andrewsummers.otashu.activity;

import java.io.File;

import com.andrewsummers.otashu.R;
import com.andrewsummers.otashu.data.EmotionsDataSource;
import com.andrewsummers.otashu.data.LabelsDataSource;
import com.andrewsummers.otashu.model.Emotion;
import com.andrewsummers.otashu.model.Label;
import com.andrewsummers.otashu.task.UploadFileTask;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

/**
 * View details of a particular Emotion.
 * <p>
 * This activity allows a user to see more information about a particular Emotion. Emotions are tags
 * that can be assigned to a Noteset either by the User or by the Apprentice.
 * </p>
 */
public class ViewEmotionDetailActivity extends Activity implements OnClickListener {
    private int emotionId = 0;
    private Button buttonSendEmofing = null;
    private SharedPreferences sharedPref;
    private long apprenticeId = 0;
    private String emofingUploadUrl;
    private File path = Environment.getExternalStorageDirectory();
    private String externalDirectory = path.toString() + "/otashu/";
    private String fullPathString = externalDirectory + "emofing.png";
    private File bitmapSource = new File(externalDirectory + "emofing.png");

    /**
     * onCreate override used to get details view.
     * 
     * @param savedInstanceState Current application state data.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // get specific layout for content view
        setContentView(R.layout.activity_view_emotion_detail);

        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        apprenticeId = Long.parseLong(sharedPref.getString(
                "pref_selected_apprentice", "1"));
        emofingUploadUrl = sharedPref.getString("pref_emofing_upload_url", "");

        emotionId = (int) getIntent().getExtras().getLong("list_id");

        EmotionsDataSource eds = new EmotionsDataSource(this);
        Emotion emotion = new Emotion();
        emotion = eds.getEmotion(apprenticeId, emotionId);
        eds.close();

        LabelsDataSource lds = new LabelsDataSource(this);
        Label label = lds.getLabel(emotion.getLabelId());
        lds.close();

        // fill in form data
        TextView emotionName = (TextView) findViewById(R.id.emotion_detail_name_value);
        emotionName.setText(emotion.getName());

        buttonSendEmofing = (Button) findViewById(R.id.button_send_emofing);

        TextView emotionLabel = (TextView) findViewById(R.id.emotion_detail_label_value);
        emotionLabel.setText(lds.getLabel(emotion.getLabelId()).getName());

        // get label background color, if available
        if (label.getColor() != null) {
            emotionLabel.setBackgroundColor(Color.parseColor(label.getColor()));
        }

        try {
            // add listeners to buttons
            buttonSendEmofing.setOnClickListener(this);
        } catch (Exception e) {
            Log.d("MYLOG", e.getStackTrace().toString());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_send_emofing:
                // disable button to avoid multiple sends for same emotion
                buttonSendEmofing = (Button) findViewById(R.id.button_send_emofing);
                buttonSendEmofing.setClickable(false);

                new UploadFileTask().execute(emofingUploadUrl, fullPathString, "image");
        }
    }
}
