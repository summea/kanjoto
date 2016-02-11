
package com.summea.kanjoto.activity;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.summea.kanjoto.R;
import com.summea.kanjoto.data.KanjotoDatabaseHelper;
import com.summea.kanjoto.data.PathEdgesDataSource;
import com.summea.kanjoto.model.PathEdge;

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
                + KanjotoDatabaseHelper.COLUMN_ID + "|"
                + KanjotoDatabaseHelper.COLUMN_PATH_ID + "|"
                + KanjotoDatabaseHelper.COLUMN_FROM_NODE_ID + "|"
                + KanjotoDatabaseHelper.COLUMN_TO_NODE_ID + "|"
                + KanjotoDatabaseHelper.COLUMN_APPRENTICE_ID + "|"
                + KanjotoDatabaseHelper.COLUMN_EMOTION_ID + "|"
                + KanjotoDatabaseHelper.COLUMN_POSITION + "|"
                + KanjotoDatabaseHelper.COLUMN_RANK + "|"
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
