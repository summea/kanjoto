
package com.andrewsummers.otashu.activity;

import java.util.ArrayList;
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

        // 1. Choose an emotion
        emotionId = (int) getIntent().getExtras().getLong("list_id");

        // get emotion graph id for Apprentice's note relationships graph
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        graphId = Long.parseLong(sharedPref.getString(
                "pref_emotion_graph_for_apprentice", "1"));

        // 2. Gather all found paths
        List<Integer> foundPathNotes = gatherEmotionPaths(0.5f);

        // 6. Loop through all found paths
        // TODO: add logic

        // 7. Plot root number reductions (the emofing)
        // TODO: add logic

    }

    public List<Integer> gatherEmotionPaths(float maxWeight) {
        // SparseArray<List<Note>> foundPaths = new SparseArray<List<Note>>();
        List<Integer> foundPaths = new ArrayList<Integer>();

        // 3. Look for all possible Emotion Graph paths that are stronger (lower than) X weight
        EdgesDataSource eds = new EdgesDataSource(this);
        SparseArray<List<Edge>> foundEdges = eds.getPathsForEmotion(graphId, emotionId, 0.5f);

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

                    // add found mapped notevalues to our result
                    foundPaths.add(rootNumbersMap.get(edge.getFromNodeId()));

                    if (edge.getPosition() == 3) {
                        foundPaths.add(rootNumbersMap.get(edge.getToNodeId()));
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
