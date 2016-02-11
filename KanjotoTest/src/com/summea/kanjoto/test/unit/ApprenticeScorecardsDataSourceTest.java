
package com.summea.kanjoto.test.unit;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;

import com.summea.kanjoto.data.ApprenticeScorecardsDataSource;
import com.summea.kanjoto.data.KanjotoDatabaseHelper;
import com.summea.kanjoto.model.ApprenticeScorecard;

public class ApprenticeScorecardsDataSourceTest extends AndroidTestCase {

    private ApprenticeScorecardsDataSource asds;
    private KanjotoDatabaseHelper db;
    private RenamingDelegatingContext context;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        context = new RenamingDelegatingContext(getContext(), "test_");
        db = new KanjotoDatabaseHelper(context);
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
        TimeZone timezone = TimeZone.getTimeZone("UTC");
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'",
                Locale.getDefault());
        dateFormat.setTimeZone(timezone);
        String takenAtISO = dateFormat.format(new Date());
        apprenticeScorecard.setTakenAt(takenAtISO);
        ApprenticeScorecard result = asds.createApprenticeScorecard(apprenticeScorecard);
        assertNotNull("created apprentice scorecard is not null", result);
    }

    public void test_deleteApprenticeScorecard_paramApprenticeScorecard() throws Throwable {
        ApprenticeScorecard apprenticeScorecard = new ApprenticeScorecard();
        apprenticeScorecard.setId(1);
        apprenticeScorecard.setCorrect(1);
        apprenticeScorecard.setTotal(2);
        apprenticeScorecard.setApprenticeId(1);
        TimeZone timezone = TimeZone.getTimeZone("UTC");
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'",
                Locale.getDefault());
        dateFormat.setTimeZone(timezone);
        String takenAtISO = dateFormat.format(new Date());
        apprenticeScorecard.setTakenAt(takenAtISO);
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
        apprenticeScorecards = asds.getAllApprenticeScorecards(1, KanjotoDatabaseHelper.COLUMN_ID);
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
