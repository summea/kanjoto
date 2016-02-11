
package com.andrewsummers.kanjoto.activity;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.andrewsummers.kanjoto.R;
import com.andrewsummers.kanjoto.data.LearningStylesDataSource;
import com.andrewsummers.kanjoto.data.KanjotoDatabaseHelper;
import com.andrewsummers.kanjoto.model.LearningStyle;

public class DatabaseDumperLearningStylesActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // get specific layout for content view
        setContentView(R.layout.activity_database_dumper);

        LearningStylesDataSource eds = new LearningStylesDataSource(this);
        List<LearningStyle> allLearningStyles = eds.getAllLearningStyles();
        eds.close();

        TextView debugText = (TextView) findViewById(R.id.debug_text);

        debugText.setText(debugText.getText().toString() + "Table: LearningStyles\n"
                + KanjotoDatabaseHelper.COLUMN_ID + "|" + KanjotoDatabaseHelper.COLUMN_NAME + "\n");

        for (LearningStyle learningStyle : allLearningStyles) {

            String newText = debugText.getText().toString();
            newText += learningStyle.getId() + "|" + learningStyle.getName() + "\n";

            debugText.setText(newText);
        }
    }
}
