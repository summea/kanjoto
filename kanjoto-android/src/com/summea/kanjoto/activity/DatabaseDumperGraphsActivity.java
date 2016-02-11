
package com.summea.kanjoto.activity;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.summea.kanjoto.R;
import com.summea.kanjoto.data.GraphsDataSource;
import com.summea.kanjoto.data.KanjotoDatabaseHelper;
import com.summea.kanjoto.model.Graph;

public class DatabaseDumperGraphsActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // get specific layout for content view
        setContentView(R.layout.activity_database_dumper);

        GraphsDataSource gds = new GraphsDataSource(this);
        List<Graph> allGraphs = gds.getAllGraphs();
        gds.close();

        TextView debugText = (TextView) findViewById(R.id.debug_text);

        debugText.setText(debugText.getText().toString() + "Table: Graphs\n"
                + KanjotoDatabaseHelper.COLUMN_ID + "|" + KanjotoDatabaseHelper.COLUMN_NAME + "|"
                + KanjotoDatabaseHelper.COLUMN_LABEL_ID + "\n");

        for (Graph graph : allGraphs) {

            String newText = debugText.getText().toString();
            newText += graph.getId() + "|" + graph.getName() + "|" + graph.getLabelId() + "\n";

            debugText.setText(newText);
        }
    }
}
