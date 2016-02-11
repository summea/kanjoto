
package com.andrewsummers.otashu.test.unit;

import java.util.ArrayList;
import java.util.List;

import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;

import com.andrewsummers.otashu.data.ApprenticeScorecardsDataSource;
import com.andrewsummers.otashu.data.OtashuDatabaseHelper;
import com.andrewsummers.otashu.model.ApprenticeScorecard;

public class ApprenticeScorecardsDataSourceTest extends AndroidTestCase {

    private ApprenticeScorecardsDataSource asds;
    private OtashuDatabaseHelper db;
    private RenamingDelegatingContext context;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        context = new RenamingDelegatingContext(getContext(), "test_");
        db = new OtashuDatabaseHelper(context);
        asds = new ApprenticeScorecardsDataSource(context);
    }

    @Override
    public void tearDown() throws Exception {
        db.close();
        super.tearDown();
    }

    public void test_createApprenticeScorecard_paramApprenticeScorecard() throws Throwable {
        ApprenticeScorecard apprenticeScorecard = new ApprenticeScorecard();
        apprenticeScorecard.setCorrect(1);
        apprenticeScorecard.setTotal(2);
        apprenticeScorecard.setApprenticeId(1);
        ApprenticeScorecard result = asds.createApprenticeScorecard(apprenticeScorecard);
        assertNotNull("created apprentice scorecard is not null", result);
    }

    public void test_deleteApprenticeScorecard_paramApprenticeScorecard() throws Throwable {
        ApprenticeScorecard apprenticeScorecard = new ApprenticeScorecard();
        apprenticeScorecard.setId(1);
        apprenticeScorecard.setCorrect(1);
        apprenticeScorecard.setTotal(2);
        apprenticeScorecard.setApprenticeId(1);
        ApprenticeScorecard result = asds.createApprenticeScorecard(apprenticeScorecard);
        assertNotNull("created apprentice scorecard is not null", result);
        asds.deleteApprenticeScorecard(apprenticeScorecard);
        apprenticeScorecard = new ApprenticeScorecard();
        apprenticeScorecard = asds.getApprenticeScorecard(1);
        assertTrue(apprenticeScorecard.getId() != 1);
    }

    public void test_getAllApprenticeScorecards_paramApprenticeId_paramOrderBy() throws Throwable {
        test_createApprenticeScorecard_paramApprenticeScorecard();
        List<ApprenticeScorecard> apprenticeScorecards = new ArrayList<ApprenticeScorecard>();
        apprenticeScorecards = asds.getAllApprenticeScorecards(1, OtashuDatabaseHelper.COLUMN_ID);
        assertNotNull("get all apprentice scorecards is not null", apprenticeScorecards);
        assertFalse(apprenticeScorecards.isEmpty());
    }

    public void test_getAllApprenticeScorecardListPreviews_paramApprenticeId() throws Throwable {
        test_createApprenticeScorecard_paramApprenticeScorecard();
        List<String> previews = asds.getAllApprenticeScorecardListPreviews(1);
        assertNotNull("get all apprentice scorecard list previews is not null", previews);
        assertFalse(previews.isEmpty());
    }

    public void test_getApprenticeScorecard_paramApprenticeScorecardId() throws Throwable {
        test_createApprenticeScorecard_paramApprenticeScorecard();
        ApprenticeScorecard apprenticeScorecard = new ApprenticeScorecard();
        apprenticeScorecard = asds.getApprenticeScorecard(1);
        assertNotNull("get apprentice scorecard is not null", apprenticeScorecard);
        assertTrue(apprenticeScorecard.getId() > 0);
    }

    public void test_updateApprenticeScorecard_paramApprenticeScorecard() throws Throwable {
        test_createApprenticeScorecard_paramApprenticeScorecard();
        ApprenticeScorecard apprenticeScorecard = asds.getApprenticeScorecard(1);
        apprenticeScorecard.setCorrect(2);
        asds.updateApprenticeScorecard(apprenticeScorecard);
        ApprenticeScorecard apprenticeScorecard2 = asds.getApprenticeScorecard(1);
        assertNotNull("update apprenticeScorecard is not null", apprenticeScorecard2);
        assertTrue(apprenticeScorecard.getCorrect() == 2);
    }
}
