
package com.andrewsummers.otashu.test;

import java.util.ArrayList;
import java.util.List;

import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;

import com.andrewsummers.otashu.data.ApprenticeScoresDataSource;
import com.andrewsummers.otashu.data.OtashuDatabaseHelper;
import com.andrewsummers.otashu.model.ApprenticeScore;

public class ApprenticeScoresDataSourceTest extends AndroidTestCase {

    private ApprenticeScoresDataSource asds;
    private OtashuDatabaseHelper db;
    private RenamingDelegatingContext context;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        context = new RenamingDelegatingContext(getContext(), "test_");
        db = new OtashuDatabaseHelper(context);
        asds = new ApprenticeScoresDataSource(context);
    }

    @Override
    public void tearDown() throws Exception {
        db.close();
        super.tearDown();
    }

    public void test_createApprenticeScore_paramApprenticeScore() throws Throwable {
        ApprenticeScore apprenticeScore = new ApprenticeScore();
        apprenticeScore.setCorrect(1);
        apprenticeScore.setScorecardId(1);
        apprenticeScore.setQuestionNumber(1);
        apprenticeScore.setApprenticeId(1);
        ApprenticeScore result = asds.createApprenticeScore(apprenticeScore);
        assertNotNull("created apprentice score is not null", result);
    }

    public void test_deleteApprenticeScore_paramApprenticeScore() throws Throwable {
        ApprenticeScore apprenticeScore = new ApprenticeScore();
        apprenticeScore.setId(1);
        apprenticeScore.setCorrect(1);
        apprenticeScore.setScorecardId(1);
        apprenticeScore.setQuestionNumber(1);
        apprenticeScore.setApprenticeId(1);
        ApprenticeScore result = asds.createApprenticeScore(apprenticeScore);
        assertNotNull("created apprentice score is not null", result);
        asds.deleteApprenticeScore(apprenticeScore);
        apprenticeScore = new ApprenticeScore();
        apprenticeScore = asds.getApprenticeScore(1);
        assertTrue(apprenticeScore.getId() != 1);
    }

    public void test_getAllApprenticeScoresByApprentice_paramApprenticeId() throws Throwable {
        test_createApprenticeScore_paramApprenticeScore();
        List<ApprenticeScore> apprenticeScores = new ArrayList<ApprenticeScore>();
        apprenticeScores = asds.getAllApprenticeScoresByApprentice(1);
        assertNotNull("get all apprentice scores is not null", apprenticeScores);
        assertFalse(apprenticeScores.isEmpty());
    }

    public void test_getAllApprenticeScores_paramScoreId() throws Throwable {
        test_createApprenticeScore_paramApprenticeScore();
        List<ApprenticeScore> apprenticeScores = new ArrayList<ApprenticeScore>();
        apprenticeScores = asds.getAllApprenticeScores(1);
        assertNotNull("get all apprentice scores is not null", apprenticeScores);
        assertFalse(apprenticeScores.isEmpty());
    }

    public void test_updateApprenticeScore_paramApprenticeScore() throws Throwable {
        test_createApprenticeScore_paramApprenticeScore();
        ApprenticeScore apprenticeScore = asds.getApprenticeScore(1);
        apprenticeScore.setCorrect(2);
        asds.updateApprenticeScore(apprenticeScore);
        ApprenticeScore apprenticeScore2 = asds.getApprenticeScore(1);
        assertNotNull("update apprenticeScore is not null", apprenticeScore2);
        assertTrue(apprenticeScore.getCorrect() == 2);
    }

    public void test_getCorrectApprenticeScoresCount_paramScorecardId() throws Throwable {
        test_createApprenticeScore_paramApprenticeScore();
        long correctApprenticeScores = asds.getCorrectApprenticeScoresCount(1);
        assertNotNull("get all correct apprentice scores is not null", correctApprenticeScores);
        assertTrue(correctApprenticeScores > 0);
    }

    public void test_getApprenticeScoresCount_paramScorecardId() throws Throwable {
        test_createApprenticeScore_paramApprenticeScore();
        long apprenticeScores = asds.getApprenticeScoresCount(1);
        assertNotNull("get all apprentice scores is not null", apprenticeScores);
        assertTrue(apprenticeScores > 0);
    }
}
