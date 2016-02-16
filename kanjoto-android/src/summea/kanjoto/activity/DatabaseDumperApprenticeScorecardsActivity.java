
package summea.kanjoto.activity;

import java.util.List;

import summea.kanjoto.data.ApprenticeScorecardsDataSource;
import summea.kanjoto.data.KanjotoDatabaseHelper;
import summea.kanjoto.model.ApprenticeScorecard;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.TextView;

import summea.kanjoto.R;

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
                + KanjotoDatabaseHelper.COLUMN_ID
                + "|" + KanjotoDatabaseHelper.COLUMN_TAKEN_AT
                + "|" + KanjotoDatabaseHelper.COLUMN_CORRECT
                + "|" + KanjotoDatabaseHelper.COLUMN_TOTAL
                + "|" + KanjotoDatabaseHelper.COLUMN_APPRENTICE_ID
                + "|" + KanjotoDatabaseHelper.COLUMN_GRAPH_ID
                + "|" + KanjotoDatabaseHelper.COLUMN_SCALE_ID
                + "\n");

        for (ApprenticeScorecard aScorecard : allApprenticeScorecards) {

            String newText = debugText.getText().toString();
            newText += aScorecard.getId()
                    + "|" + aScorecard.getTakenAt()
                    + "|" + aScorecard.getCorrect()
                    + "|" + aScorecard.getTotal()
                    + "|" + aScorecard.getApprenticeId()
                    + "|" + aScorecard.getGraphId()
                    + "|" + aScorecard.getScaleId()+ "\n";

            debugText.setText(newText);
        }
    }
}
