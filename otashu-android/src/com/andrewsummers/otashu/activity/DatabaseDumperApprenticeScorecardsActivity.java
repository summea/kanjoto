
package com.andrewsummers.otashu.activity;

import java.util.List;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.TextView;

import com.andrewsummers.otashu.R;
import com.andrewsummers.otashu.data.ApprenticeScorecardsDataSource;
import com.andrewsummers.otashu.data.OtashuDatabaseHelper;
import com.andrewsummers.otashu.model.ApprenticeScorecard;

public class DatabaseDumperApprenticeScorecardsActivity extends Activity {
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

        ApprenticeScorecardsDataSource ascds = new ApprenticeScorecardsDataSource(this);
        List<ApprenticeScorecard> allApprenticeScorecards = ascds.getAllApprenticeScorecards(
                apprenticeId, "");
        ascds.close();

        TextView debugText = (TextView) findViewById(R.id.debug_text);

        debugText.setText(debugText.getText().toString() + "Table: Apprentice Scorecards\n"
                + OtashuDatabaseHelper.COLUMN_ID + "|" + OtashuDatabaseHelper.COLUMN_TAKEN_AT + "|"
                + OtashuDatabaseHelper.COLUMN_CORRECT + "|" + OtashuDatabaseHelper.COLUMN_TOTAL
                + "|"
                + OtashuDatabaseHelper.COLUMN_APPRENTICE_ID + "\n");

        for (ApprenticeScorecard aScorecard : allApprenticeScorecards) {

            String newText = debugText.getText().toString();
            newText += aScorecard.getId() + "|" + aScorecard.getTakenAt()
                    + aScorecard.getCorrect() + "|" + aScorecard.getTotal()
                    + "|" + aScorecard.getApprenticeId() + "\n";

            debugText.setText(newText);
        }
    }
}
