
package com.andrewsummers.otashu.activity;

import java.util.ArrayList;
import java.util.List;

import com.andrewsummers.otashu.R;
import com.andrewsummers.otashu.data.ApprenticeScorecardsDataSource;
import com.andrewsummers.otashu.data.ApprenticeScoresDataSource;
import com.andrewsummers.otashu.model.ApprenticeScore;
import com.andrewsummers.otashu.model.ApprenticeScorecard;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

/**
 * View details of a particular apprenticeScorecard.
 */
public class ViewApprenticeScorecardDetailActivity extends Activity {

    private int apprenticeScorecardId = 0;

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

        apprenticeScorecardId = (int) getIntent().getExtras().getLong("list_id");

        /*
         * // prevent crashes due to lack of database data if (allApprenticeScorecardsData.isEmpty())
         * allApprenticeScorecardsData.add((long) 0);
         */

        ApprenticeScorecard apprenticeScorecard = new ApprenticeScorecard();
        ApprenticeScorecardsDataSource asc = new ApprenticeScorecardsDataSource(this);
        apprenticeScorecard = asc.getApprenticeScorecard(apprenticeScorecardId);
        asc.close();

        // fill in form data
        TextView apprenticeScorecardTakenAt = (TextView) findViewById(R.id.apprentice_scorecard_taken_at_value);
        apprenticeScorecardTakenAt.setText(apprenticeScorecard.getTakenAt());
        
        // get number of correct answers
        ApprenticeScoresDataSource asds = new ApprenticeScoresDataSource(this);
        int total = asds.getApprenticeScoresCount(apprenticeScorecard.getId());
        int totalCorrect = asds.getCorrectApprenticeScoresCount(apprenticeScorecard.getId());
        asds.close();
        
        Log.d("MYLOG", "total: " + total + " correct: " + totalCorrect);
    }
}
