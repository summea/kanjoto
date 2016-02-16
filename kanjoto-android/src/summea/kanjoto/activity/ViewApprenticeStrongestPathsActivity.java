
package summea.kanjoto.activity;

import java.util.ArrayList;
import java.util.List;

import summea.kanjoto.data.EdgesDataSource;
import summea.kanjoto.model.Edge;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.TextView;

import summea.kanjoto.R;

public class ViewApprenticeStrongestPathsActivity extends Activity {
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
        setContentView(R.layout.activity_view_apprentice_strongest_paths);

        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        apprenticeId = Long.parseLong(sharedPref.getString(
                "pref_selected_apprentice", "1"));

        Intent intent = getIntent();
        
        TextView pathText = (TextView) findViewById(R.id.path_text);
        pathText.setText("Strongest Path\n");

        EdgesDataSource eds = new EdgesDataSource(this);

        // select a given graph
        long graphId = 2;

        // select a given emotion
        long emotionId = intent.getLongExtra("list_id", 1);

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

        List<Edge> bestMatch = new ArrayList<Edge>();
        boolean edge1To2Match = false;
        boolean edge2To3Match = false;
        // check to see if any of the lowest-weight edges are related nodes (i.e. do they connect in
        // the graph?)
        outerloop: for (Edge edge1 : p1Edges) {
            for (Edge edge2 : p2Edges) {
                if (edge1.getToNodeId() == edge2.getFromNodeId()) {
                    // edge1 to edge2 match!
                    edge1To2Match = true;
                }
                for (Edge edge3 : p3Edges) {
                    if (edge2.getToNodeId() == edge3.getFromNodeId()) {
                        // edge2 to edge3 match!
                        edge2To3Match = true;

                        if (edge1To2Match && edge2To3Match) {
                            // cross section match found!
                            bestMatch.add(edge1);
                            bestMatch.add(edge2);
                            bestMatch.add(edge3);
                            break outerloop;
                        }
                    }
                }
            }
        }

        // return results
        pathText.setText("Strongest Path\n" + bestMatch.toString());
    }
}
