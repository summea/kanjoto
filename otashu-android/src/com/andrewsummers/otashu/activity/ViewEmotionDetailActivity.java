
package com.andrewsummers.otashu.activity;

import com.andrewsummers.otashu.R;
import com.andrewsummers.otashu.data.EmotionsDataSource;
import com.andrewsummers.otashu.data.LabelsDataSource;
import com.andrewsummers.otashu.model.Emotion;
import com.andrewsummers.otashu.model.Label;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

/**
 * View details of a particular Emotion.
 * <p>
 * This activity allows a user to see more information about a particular Emotion. Emotions are tags
 * that can be assigned to a Noteset either by the User or by the Apprentice.
 * </p>
 */
public class ViewEmotionDetailActivity extends Activity {
    private int emotionId = 0;

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

        emotionId = (int) getIntent().getExtras().getLong("list_id");

        EmotionsDataSource eds = new EmotionsDataSource(this);
        Emotion emotion = new Emotion();
        emotion = eds.getEmotion(emotionId);
        eds.close();

        LabelsDataSource lds = new LabelsDataSource(this);
        Label label = lds.getLabel(emotion.getLabelId());
        lds.close();

        // fill in form data
        TextView emotionName = (TextView) findViewById(R.id.emotion_detail_name_value);
        emotionName.setText(emotion.getName());

        TextView emotionLabel = (TextView) findViewById(R.id.emotion_detail_label_value);
        emotionLabel.setText(lds.getLabel(emotion.getLabelId()).getName());

        // get label background color, if available
        if (label.getColor() != null) {
            emotionLabel.setBackgroundColor(Color.parseColor(label.getColor()));
        }
    }
}
