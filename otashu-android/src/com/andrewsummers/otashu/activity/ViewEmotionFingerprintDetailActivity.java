
package com.andrewsummers.otashu.activity;

import java.util.List;

import com.andrewsummers.otashu.data.EdgesDataSource;
import com.andrewsummers.otashu.model.Edge;
import com.andrewsummers.otashu.model.Note;
import com.andrewsummers.otashu.view.DrawView;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.SparseArray;

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
    DrawView drawView;

    /**
     * onCreate override used to get details view.
     * 
     * @param savedInstanceState Current application state data.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // get specific layout for content view
        // setContentView(R.layout.activity_view_emotion_fingerprint_detail);

        drawView = new DrawView(this);
        drawView.setBackgroundColor(Color.BLACK);
        setContentView(drawView);

        emotionId = (int) getIntent().getExtras().getLong("list_id");

        // get emotion graph id for Apprentice's note relationships graph
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        graphId = Long.parseLong(sharedPref.getString(
                "pref_emotion_graph_for_apprentice", "1"));

        gatherEmotionPaths(0.5f);
    }

    public SparseArray<List<Note>> gatherEmotionPaths(float maxWeight) {
        SparseArray<List<Note>> foundPaths = new SparseArray<List<Note>>();

        // TODO: add logic
        // 2. Look for all possible Emotion Graph paths that are stronger than X weight
        EdgesDataSource eds = new EdgesDataSource(this);
        SparseArray<List<Edge>> foundEdges = eds.getPathsForEmotion(graphId, emotionId, 0.5f);

        // TODO: add logic
        // 5. Convert noteset paths into root numbers +/- {1, ..., 12}
        for (int i = 0; i < foundEdges.size(); i++) {
            
        }

        // TODO: add logic
        // 6. Store noteset path root number reductions into data structure

        return foundPaths;
    }
}
