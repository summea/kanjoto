
package com.andrewsummers.otashu.activity;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.andrewsummers.otashu.R;
import com.andrewsummers.otashu.data.PathEdgesDataSource;
import com.andrewsummers.otashu.data.OtashuDatabaseHelper;
import com.andrewsummers.otashu.model.PathEdge;

public class DatabaseDumperPathEdgesActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // get specific layout for content view
        setContentView(R.layout.activity_database_dumper);

        PathEdgesDataSource pds = new PathEdgesDataSource(this);
        List<PathEdge> allPathEdges = pds.getAllPathEdges();
        pds.close();

        TextView debugText = (TextView) findViewById(R.id.debug_text);

        debugText.setText(debugText.getText().toString() + "Table: PathEdges\n"
                + OtashuDatabaseHelper.COLUMN_ID + "|"
                + OtashuDatabaseHelper.COLUMN_PATH_ID + "|"
                + OtashuDatabaseHelper.COLUMN_FROM_NODE_ID + "|"
                + OtashuDatabaseHelper.COLUMN_TO_NODE_ID + "|"
                + OtashuDatabaseHelper.COLUMN_APPRENTICE_ID + "|"
                + OtashuDatabaseHelper.COLUMN_EMOTION_ID + "|"
                + OtashuDatabaseHelper.COLUMN_POSITION + "|"
                + OtashuDatabaseHelper.COLUMN_RANK + "|"
                + "\n");

        for (PathEdge pathEdge : allPathEdges) {

            String newText = debugText.getText().toString();
            newText += pathEdge.getId() + "|"
                    + pathEdge.getPathId() + "|"
                    + pathEdge.getFromNodeId() + "|"
                    + pathEdge.getToNodeId() + "|"
                    + pathEdge.getApprenticeId() + "|"
                    + pathEdge.getEmotionId() + "|"
                    + pathEdge.getPosition() + "|"
                    + pathEdge.getRank() + "|"
                    + "\n";

            debugText.setText(newText);
        }
    }
}
