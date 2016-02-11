
package com.andrewsummers.kanjoto.activity;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.andrewsummers.kanjoto.R;
import com.andrewsummers.kanjoto.data.ApprenticeScoresDataSource;
import com.andrewsummers.kanjoto.data.KanjotoDatabaseHelper;
import com.andrewsummers.kanjoto.model.ApprenticeScore;

public class DatabaseDumperApprenticeScoresActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // get specific layout for content view
        setContentView(R.layout.activity_database_dumper);

        ApprenticeScoresDataSource asds = new ApprenticeScoresDataSource(this);
        List<ApprenticeScore> allApprenticeScores = asds.getAllApprenticeScores();
        asds.close();

        TextView debugText = (TextView) findViewById(R.id.debug_text);

        debugText.setText(debugText.getText().toString() + "Table: Apprentice Scores\n"
                + KanjotoDatabaseHelper.COLUMN_ID
                + "|" + KanjotoDatabaseHelper.COLUMN_SCORECARD_ID
                + "|" + KanjotoDatabaseHelper.COLUMN_QUESTION_NUMBER
                + "|" + KanjotoDatabaseHelper.COLUMN_CORRECT
                + "|" + KanjotoDatabaseHelper.COLUMN_NOTEVALUE
                + "\n");

        for (ApprenticeScore aScore : allApprenticeScores) {

            String newText = debugText.getText().toString();
            newText += aScore.getId() + "|" + aScore.getScorecardId()
                    + "|" + aScore.getQuestionNumber()
                    + "|" + aScore.getCorrect()
                    + "|" + aScore.getNotevalue() + "\n";

            debugText.setText(newText);
        }
    }
}
