
package com.andrewsummers.otashu.activity;

import java.util.List;

import com.andrewsummers.otashu.R;
import com.andrewsummers.otashu.data.EdgesDataSource;
import com.andrewsummers.otashu.data.EmotionsDataSource;
import com.andrewsummers.otashu.data.LabelsDataSource;
import com.andrewsummers.otashu.model.Edge;
import com.andrewsummers.otashu.model.Emotion;
import com.andrewsummers.otashu.model.Label;
import com.andrewsummers.otashu.model.Note;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.SparseArray;
import android.widget.TextView;

/**
 * View details of a particular Emotion Fingerprint.
 * <p>
 * This activity allows a user to see more information about a particular Emotion Fingerprint.
 * </p>
 */
public class ViewEmotionFingerprintDetailActivity extends Activity {
    private int emotionId = 0;
    private SharedPreferences sharedPref;
    private long graphId;

    /**
     * onCreate override used to get details view.
     * 
     * @param savedInstanceState Current application state data.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // get specific layout for content view
        setContentView(R.layout.activity_view_emotion_fingerprint_detail);

        emotionId = (int) getIntent().getExtras().getLong("list_id");

        // get emotion graph id for Apprentice's note relationships graph
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        graphId = Long.parseLong(sharedPref.getString(
                "pref_emotion_graph_for_apprentice", "1"));

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

        gatherEmotionPaths(0.5f);
    }

    public SparseArray<List<Note>> gatherEmotionPaths(float maxWeight) {
        SparseArray<List<Note>> foundPaths = new SparseArray<List<Note>>();

        // TODO: add logic
        // 2. Look for all possible Emotion Graph paths that are stronger than X weight
        EdgesDataSource eds = new EdgesDataSource(this);
        SparseArray<List<Edge>> foundEdges = eds.getPathsForEmotion(graphId, emotionId, 0.5f);

        return foundPaths;
    }

}
