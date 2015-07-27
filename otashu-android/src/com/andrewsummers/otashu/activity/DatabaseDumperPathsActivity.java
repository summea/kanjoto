
package com.andrewsummers.otashu.activity;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.andrewsummers.otashu.R;
import com.andrewsummers.otashu.data.PathsDataSource;
import com.andrewsummers.otashu.data.OtashuDatabaseHelper;
import com.andrewsummers.otashu.model.Path;

public class DatabaseDumperPathsActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // get specific layout for content view
        setContentView(R.layout.activity_database_dumper);

        PathsDataSource pds = new PathsDataSource(this);
        List<Path> allPaths = pds.getAllPaths();
        pds.close();

        TextView debugText = (TextView) findViewById(R.id.debug_text);

        debugText.setText(debugText.getText().toString() + "Table: Paths\n"
                + OtashuDatabaseHelper.COLUMN_ID + "|"
                + OtashuDatabaseHelper.COLUMN_NAME + "|"
                + "\n");

        for (Path path : allPaths) {

            String newText = debugText.getText().toString();
            newText += path.getId() + "|"
                    + path.getName() + "|"
                    + "\n";

            debugText.setText(newText);
        }
    }
}
