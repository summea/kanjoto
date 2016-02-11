package com.summea.kanjoto.activity;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.summea.kanjoto.R;
import com.summea.kanjoto.data.KanjotoDatabaseHelper;
import com.summea.kanjoto.data.VerticesDataSource;
import com.summea.kanjoto.model.Vertex;

public class DatabaseDumperVerticesActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // get specific layout for content view
        setContentView(R.layout.activity_database_dumper);

        VerticesDataSource vds = new VerticesDataSource(this);
        List<Vertex> allVertices = vds.getAllVertices();
        vds.close();

        TextView debugText = (TextView) findViewById(R.id.debug_text);

        debugText.setText(debugText.getText().toString() + "Table: Vertices\n"
                + KanjotoDatabaseHelper.COLUMN_ID
                + "|" + KanjotoDatabaseHelper.COLUMN_NODE + "\n");

        for (Vertex vertex : allVertices) {

            String newText = debugText.getText().toString();
            newText += vertex.getId()
                    + "|" + vertex.getNode() + "\n";

            debugText.setText(newText);
        }
    }
}
