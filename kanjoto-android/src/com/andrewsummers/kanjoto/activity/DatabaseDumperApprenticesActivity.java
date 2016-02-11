
package com.andrewsummers.kanjoto.activity;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.andrewsummers.kanjoto.R;
import com.andrewsummers.kanjoto.data.ApprenticesDataSource;
import com.andrewsummers.kanjoto.data.KanjotoDatabaseHelper;
import com.andrewsummers.kanjoto.model.Apprentice;

public class DatabaseDumperApprenticesActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // get specific layout for content view
        setContentView(R.layout.activity_database_dumper);

        ApprenticesDataSource gds = new ApprenticesDataSource(this);
        List<Apprentice> allApprentices = gds.getAllApprentices();
        gds.close();

        TextView debugText = (TextView) findViewById(R.id.debug_text);

        debugText.setText(debugText.getText().toString() + "Table: Apprentices\n"
                + KanjotoDatabaseHelper.COLUMN_ID + "|" + KanjotoDatabaseHelper.COLUMN_NAME + "|"
                + KanjotoDatabaseHelper.COLUMN_LEARNING_STYLE_ID + "\n");

        for (Apprentice apprentice : allApprentices) {

            String newText = debugText.getText().toString();
            newText += apprentice.getId() + "|" + apprentice.getName() + "|"
                    + apprentice.getLearningStyleId() + "\n";

            debugText.setText(newText);
        }
    }
}
