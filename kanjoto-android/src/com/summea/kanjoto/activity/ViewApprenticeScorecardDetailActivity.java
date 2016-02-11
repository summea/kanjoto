
package com.summea.kanjoto.activity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import com.summea.kanjoto.R;
import com.summea.kanjoto.data.ApprenticeScorecardsDataSource;
import com.summea.kanjoto.data.ApprenticeScoresDataSource;
import com.summea.kanjoto.model.ApprenticeScorecard;

import android.app.Activity;
import android.os.Bundle;
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
    private long apprenticeScorecardId = 0;

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

        apprenticeScorecardId = getIntent().getExtras().getLong("list_id");

        ApprenticeScorecard apprenticeScorecard = new ApprenticeScorecard();
        ApprenticeScorecardsDataSource asc = new ApprenticeScorecardsDataSource(this);
        apprenticeScorecard = asc.getApprenticeScorecard(apprenticeScorecardId);
        asc.close();

        // fill in form data

        // get formatted string of test "taken at" datestamp
        // based on examples from:
        // http://stackoverflow.com/questions/17692863/converting-string-in-t-z-format-to-date
        TimeZone timezone = TimeZone.getTimeZone("UTC");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'", Locale.getDefault());
        sdf.setTimeZone(timezone);
        Date date = new Date();
        try {
            date = sdf.parse(apprenticeScorecard.getTakenAt());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        TextView apprenticeScorecardTakenAt = (TextView) findViewById(R.id.apprentice_scorecard_taken_at_value);
        apprenticeScorecardTakenAt.setText(date.toString());

        // get number of correct answers
        ApprenticeScoresDataSource asds = new ApprenticeScoresDataSource(this);
        int total = asds.getApprenticeScoresCount(apprenticeScorecard.getId());
        int totalCorrect = asds.getCorrectApprenticeScoresCount(apprenticeScorecard.getId());
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
