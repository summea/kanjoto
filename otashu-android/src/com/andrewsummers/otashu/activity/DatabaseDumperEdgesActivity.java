
package com.andrewsummers.otashu.activity;

import java.util.List;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.TextView;

import com.andrewsummers.otashu.R;
import com.andrewsummers.otashu.data.EdgesDataSource;
import com.andrewsummers.otashu.data.OtashuDatabaseHelper;
import com.andrewsummers.otashu.model.Edge;

public class DatabaseDumperEdgesActivity extends Activity {
    private SharedPreferences sharedPref;
    private long apprenticeId = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // get specific layout for content view
        setContentView(R.layout.activity_database_dumper);

        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        apprenticeId = Long.parseLong(sharedPref.getString(
                "pref_selected_apprentice", "1"));

        EdgesDataSource edds = new EdgesDataSource(this);
        List<Edge> allEdges = edds.getAllEdgesByApprentice(apprenticeId);
        edds.close();

        TextView debugText = (TextView) findViewById(R.id.debug_text);

        debugText
                .setText(debugText.getText().toString() + "Table: Edges\n"
                        + OtashuDatabaseHelper.COLUMN_ID + "|"
                        + OtashuDatabaseHelper.COLUMN_GRAPH_ID + "|"
                        + OtashuDatabaseHelper.COLUMN_EMOTION_ID + "|"
                        + OtashuDatabaseHelper.COLUMN_FROM_NODE_ID + "|"
                        + OtashuDatabaseHelper.COLUMN_TO_NODE_ID + "|"
                        + OtashuDatabaseHelper.COLUMN_WEIGHT + "|"
                        + OtashuDatabaseHelper.COLUMN_POSITION + "|"
                        + OtashuDatabaseHelper.COLUMN_APPRENTICE_ID + "\n");

        for (Edge edge : allEdges) {

            String newText = debugText.getText().toString();
            newText += edge.getId() + "|"
                    + edge.getGraphId() + "|"
                    + edge.getEmotionId() + "|"
                    + edge.getFromNodeId() + "|"
                    + edge.getToNodeId() + "|"
                    + edge.getWeight() + "|"
                    + edge.getPosition() + "|"
                    + edge.getApprenticeId() + "\n";

            debugText.setText(newText);
        }
    }
}
