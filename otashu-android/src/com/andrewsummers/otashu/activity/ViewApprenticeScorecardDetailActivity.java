
package com.andrewsummers.otashu.activity;

import java.util.Locale;

import com.andrewsummers.otashu.R;
import com.andrewsummers.otashu.data.ApprenticeScorecardsDataSource;
import com.andrewsummers.otashu.data.ApprenticeScoresDataSource;
import com.andrewsummers.otashu.model.ApprenticeScorecard;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.TextView;

/**
 * View details of a particular ApprenticeScorecard.
 * <p>
 * An ApprenticeScorecard keeps track of individual Apprentice test scores for a particular test
 * session. In a test, the Apprentice gathers an Emotion and related Notes (grouped together in a
 * Noteset) and asks the user if the noteset-emotion combination sounds "correct" for that
 * particular Emotion. The ApprenticeScorecard shows the total percentage correct of how the
 * Apprentice performed during a particular test.
 * </p>
 */
public class ViewApprenticeScorecardDetailActivity extends Activity {
    private int apprenticeScorecardId = 0;
    private SharedPreferences sharedPref;
    private long apprenticeId = 0;

    /**
     * onCreate override used to get details view.
     * 
     * @param savedInstanceState Current application state data.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // get specific layout for content view
        setContentView(R.layout.activity_view_apprentice_scorecard_detail);

        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        apprenticeId = Long.parseLong(sharedPref.getString(
                "pref_selected_apprentice", "1"));

        apprenticeScorecardId = (int) getIntent().getExtras().getLong("list_id");

        ApprenticeScorecard apprenticeScorecard = new ApprenticeScorecard();
        ApprenticeScorecardsDataSource asc = new ApprenticeScorecardsDataSource(this);
        apprenticeScorecard = asc.getApprenticeScorecard(apprenticeId, apprenticeScorecardId);
        asc.close();

        // fill in form data
        TextView apprenticeScorecardTakenAt = (TextView) findViewById(R.id.apprentice_scorecard_taken_at_value);
        apprenticeScorecardTakenAt.setText(apprenticeScorecard.getTakenAt());

        // get number of correct answers
        ApprenticeScoresDataSource asds = new ApprenticeScoresDataSource(this);
        int total = asds.getApprenticeScoresCount(apprenticeId, apprenticeScorecard.getId());
        int totalCorrect = asds.getCorrectApprenticeScoresCount(apprenticeId,
                apprenticeScorecard.getId());
        asds.close();

        double guessesCorrectPercentage = 0.0d;

        if (total > 0) {
            guessesCorrectPercentage = ((double) totalCorrect / (double) total) * 100.0;
        }

        String guessesCorrectPercentageString = String.format(Locale.getDefault(), "%.02f",
                guessesCorrectPercentage);

        // display percent correct for this ApprenticeScorecard
        TextView apprenticeTotalGuesses = (TextView) findViewById(R.id.apprentice_total_guesses);
        apprenticeTotalGuesses.setText(totalCorrect + "/" + total + " ("
                + guessesCorrectPercentageString + "%)");
    }
}
