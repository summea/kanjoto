
package com.andrewsummers.otashu.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.TextView;

import com.andrewsummers.otashu.R;
import com.andrewsummers.otashu.data.EdgesDataSource;
import com.andrewsummers.otashu.model.Edge;
import com.andrewsummers.otashu.model.Path;

/**
 * View details of a particular Apprentice strongest path.
 * <p>
 * As the Apprentice learns more about which notesets fit well with certain emotions, this
 * information is saved in a graph in the database. As this data is "learned" by the Apprentice, the
 * Apprentice becomes more confident in making an educated guess towards certain noteset-emotion
 * combinations. This confidence comes from lower weights for particular edges in the graph (the
 * lower the weight, the less effort it takes to follow that path for the Apprentice when making
 * decisions). This activity displays the strongest paths (i.e. the paths with the lowest weights)
 * learned up to this point by the Apprentice.
 * </p>
 */
public class ViewApprenticeStrongestPathDetailActivity extends Activity {
    private SharedPreferences sharedPref;
    private long apprenticeId = 0;

    /**
     * onCreate override that provides emotion-choose view to user.
     * 
     * @param savedInstanceState Current application state data.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // get specific layout for content view
        setContentView(R.layout.activity_view_apprentice_strongest_path_detail);

        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        apprenticeId = Long.parseLong(sharedPref.getString(
                "pref_selected_apprentice", "1"));

        TextView pathText = (TextView) findViewById(R.id.path_text);
        pathText.setText("Strongest Path\n");

        List<Path> topPaths = new ArrayList<Path>();
        EdgesDataSource eds = new EdgesDataSource(this);

        // select a given graph
        long graphId = 2;

        // select a given emotion
        long emotionId = 1;
        emotionId = getIntent().getExtras().getLong("emotion_id");
        Log.d("MYLOG", "emotion id: " + emotionId);

        // select a given weight limit
        float weightLimit = 0.5f;

        // select an edge position
        int position = 1;

        // select all position one edges for given emotion with given threshold (e.g. all rows that
        // have a weight less than 0.5)
        List<Edge> p1Edges = eds.getAllEdges(apprenticeId, graphId, emotionId, weightLimit,
                position);

        position = 2;
        // select all position two edges for given emotion with given threshold (e.g. all rows that
        // have a weight less than 0.5)
        List<Edge> p2Edges = eds.getAllEdges(apprenticeId, graphId, emotionId, weightLimit,
                position);

        position = 3;
        // select all position three edges for given emotion with given threshold (e.g. all rows
        // that have a weight less than 0.5)
        List<Edge> p3Edges = eds.getAllEdges(apprenticeId, graphId, emotionId, weightLimit,
                position);
        List<Long> usedOnce = new ArrayList<Long>();

        // get top 3
        for (int i = 0; i < 3; i++) {
            List<Edge> bestMatch = new ArrayList<Edge>();
            boolean edge1To2Match = false;
            boolean edge2To3Match = false;

            // check to see if any of the lowest-weight edges are related nodes (i.e. do they
            // connect in the graph?)
            outerloop: for (Edge edge1 : p1Edges) {
                if (!usedOnce.contains(edge1.getId())) {
                    for (Edge edge2 : p2Edges) {
                        if (!usedOnce.contains(edge2.getId())) {
                            if (edge1.getToNodeId() == edge2.getFromNodeId()) {
                                // edge1 to edge2 match!
                                edge1To2Match = true;
                            }
                            for (Edge edge3 : p3Edges) {
                                if (!usedOnce.contains(edge3.getId())) {
                                    if (edge2.getToNodeId() == edge3.getFromNodeId()) {
                                        // edge2 to edge3 match!
                                        edge2To3Match = true;

                                        if (edge1To2Match && edge2To3Match) {
                                            Log.d("MYLOG", "cross section match found!");
                                            bestMatch.add(edge1);
                                            bestMatch.add(edge2);
                                            bestMatch.add(edge3);
                                            break outerloop;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            try {
                // return results
                Log.d("MYLOG", "best match results: " + bestMatch.toString());

                // keep track of what edges have been used already
                for (int j = 0; j < 3; j++) {
                    if (!usedOnce.contains(bestMatch.get(j).getId())) {
                        usedOnce.add(bestMatch.get(j).getId());
                    }
                }

                Log.d("MYLOG", usedOnce.toString());
            } catch (Exception e) {
                Log.d("MYLOG", e.getStackTrace().toString());
            }

            if (!bestMatch.isEmpty()) {
                // add path for list
                Path path = new Path();
                path.setPath(bestMatch);
                Log.d("MYLOG", "adding best match: " + bestMatch.toString());
                topPaths.add(path);
                Log.d("MYLOG", "current state of topPaths: " + topPaths.toString());
            }
        }

        // get correct path detail for clicked list item
        try {
            long pathListPosition = getIntent().getExtras().getLong("list_id");
            pathText.setText(pathText.getText() + "\n" + topPaths.get((int) pathListPosition));
        } catch (Exception e) {
            Log.d("MYLOG", e.getStackTrace().toString());
        }
    }
}
