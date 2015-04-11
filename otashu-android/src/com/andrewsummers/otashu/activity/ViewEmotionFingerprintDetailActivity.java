
package com.andrewsummers.otashu.activity;

import java.util.List;

import com.andrewsummers.otashu.data.EdgesDataSource;
import com.andrewsummers.otashu.model.Edge;
import com.andrewsummers.otashu.view.DrawView;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.util.SparseArray;
import android.util.SparseIntArray;

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
    private long apprenticeId = 0;

    /**
     * onCreate override used to get details view.
     * 
     * @param savedInstanceState Current application state data.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 1. Choose an emotion
        emotionId = (int) getIntent().getExtras().getLong("list_id");

        // get emotion graph id for Apprentice's note relationships graph
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        graphId = Long.parseLong(sharedPref.getString(
                "pref_emotion_graph_for_apprentice", "1"));
        apprenticeId = Long.parseLong(sharedPref.getString(
                "pref_selected_apprentice", "1"));

        // 2. Gather all found paths
        // List<Integer> foundPathNotes = gatherEmotionPaths(0.5f);
        SparseArray<SparseIntArray> emofingData = gatherEmotionPaths(0.5f);

        Log.d("MYLOG", ">> found paths: " + emofingData);

        // 7. Plot root number reductions (the emofing)
        drawView = new DrawView(this, emofingData);
        drawView.setBackgroundColor(Color.BLACK);
        setContentView(drawView);
    }

    public SparseArray<SparseIntArray> gatherEmotionPaths(float maxWeight) {
        SparseArray<SparseIntArray> foundPaths = new SparseArray<SparseIntArray>();

        // initialize
        for (int i = 1; i <= 4; i++) {
            SparseIntArray values = new SparseIntArray();
            for (int j = 1; j <= 12; j++) {
                values.put(j, 0);
            }
            foundPaths.put(i, values);
        }

        Log.d("MYLOG", ">> init found paths: " + foundPaths);

        // 3. Look for all possible Emotion Graph paths that are stronger (lower than) X weight
        EdgesDataSource eds = new EdgesDataSource(this);
        SparseArray<List<Edge>> foundEdges = eds.getPathsForEmotion(apprenticeId, graphId,
                emotionId, 0.5f);

        // 5. Store noteset path root number reductions into data structure
        SparseArray<Integer> rootNumbersMap = new SparseArray<Integer>();
        boolean rootNumbersMapCreated = false;

        Log.d("MYLOG", "edges: " + foundEdges.toString());

        try {
            for (int i = 1; i < foundEdges.size() + 1; i++) {
                for (Edge edge : foundEdges.get(i)) {

                    if (!rootNumbersMapCreated) {
                        // firstNote is what is used for the root number start (note "I")
                        int firstNote = 0;

                        if (edge.getPosition() == 1) {
                            firstNote = edge.getFromNodeId();
                            rootNumbersMap.put(firstNote, 1);
                            Log.d("MYLOG", "root numbers map... key: " + firstNote + " j: " + 1);
                        }

                        // 4. Convert noteset paths into root numbers +/- {1, ..., 12} (ex: I, II,
                        // III, IV ...)
                        // QUESTION: do this for each path?
                        // range of notevalues between C4...B4 (60...71)

                        int key = firstNote;
                        for (int j = 2; j < 13; j++) {
                            if (key > 70) {
                                key = 60;
                            } else {
                                key++;
                            }
                            rootNumbersMap.put(key, j);
                            Log.d("MYLOG", "root numbers map... key: " + key + " j: " + j);
                        }
                        rootNumbersMapCreated = true;
                    }

                    Log.d("MYLOG", "current edge: " + edge.toString());

                    SparseIntArray values = foundPaths.get(edge.getPosition());
                    int newValue = values.get(rootNumbersMap.get(edge.getFromNodeId())) + 1;
                    Log.d("MYLOG", "new value: " + newValue);
                    values.put(rootNumbersMap.get(edge.getFromNodeId()), newValue);

                    // add found mapped notevalues to our result
                    foundPaths.put(edge.getPosition(), values);

                    if (edge.getPosition() == 3) {
                        values = foundPaths.get(edge.getPosition() + 1);
                        Log.d("MYLOG", "to node: " + edge.getToNodeId());
                        newValue = values.get(rootNumbersMap.get(edge.getToNodeId())) + 1;
                        Log.d("MYLOG", "new value: " + newValue);
                        values.put(rootNumbersMap.get(edge.getToNodeId()), newValue);

                        // add found mapped notevalues to our result
                        foundPaths.put(edge.getPosition() + 1, values);
                    }
                }
            }

            Log.d("MYLOG", ">> found paths: " + foundPaths);
        } catch (Exception e) {
            Log.d("MYLOG", e.getStackTrace().toString());
        }
        return foundPaths;
    }
}
