
package com.andrewsummers.otashu.test.unit;

import java.util.ArrayList;
import java.util.List;

import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;

import com.andrewsummers.otashu.data.AchievementsDataSource;
import com.andrewsummers.otashu.data.OtashuDatabaseHelper;
import com.andrewsummers.otashu.model.Achievement;

public class AchievementsDataSourceTest extends AndroidTestCase {

    private AchievementsDataSource ads;
    private OtashuDatabaseHelper db;
    private RenamingDelegatingContext context;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        context = new RenamingDelegatingContext(getContext(), "test_");
        db = new OtashuDatabaseHelper(context);
        ads = new AchievementsDataSource(context);
    }

    @Override
    public void tearDown() throws Exception {
        db.close();
        super.tearDown();
    }

    public void test_createAchievement_paramAchievement() throws Throwable {
        Achievement achievement = new Achievement();
        achievement.setName("found_strong_path");
        achievement.setKey("60_61_62_63");
        achievement.setApprenticeId(1);
        Achievement result = ads.createAchievement(achievement);
        assertNotNull("created achievement is not null", result);
    }

    public void test_deleteAchievement_paramAchievement() throws Throwable {
        Achievement achievement = new Achievement();
        achievement.setId(1);
        achievement.setName("found_strong_path");
        achievement.setKey("60_61_62_63");
        achievement.setApprenticeId(1);
        Achievement result = ads.createAchievement(achievement);
        assertNotNull("created achievement is not null", result);
        ads.deleteAchievement(achievement);
        achievement = new Achievement();
        achievement = ads.getAchievement(1);
        assertTrue(achievement.getId() != 1);
    }

    public void test_getAllAchievements_paramApprenticeId() throws Throwable {
        test_createAchievement_paramAchievement();
        List<Achievement> achievements = new ArrayList<Achievement>();
        achievements = ads.getAllAchievements(1);
        assertNotNull("get all achievements is not null", achievements);
        assertFalse(achievements.isEmpty());
    }

    public void test_getAchievement_paramAchievementId() throws Throwable {
        test_createAchievement_paramAchievement();
        Achievement achievement = new Achievement();
        achievement = ads.getAchievement(1);
        assertNotNull("get achievement is not null", achievement);
        assertTrue(achievement.getId() > 0);
    }

    public void test_getAchievementByKey_paramAchievementId() throws Throwable {
        test_createAchievement_paramAchievement();
        Achievement achievement = new Achievement();
        achievement = ads.getAchievementByKey("60_61_62_63");
        assertNotNull("get achievement is not null", achievement);
        assertTrue(achievement.getId() > 0);
    }

    public void test_getAchievementCount_paramAchievementId_paramAchievementName() throws Throwable {
        test_createAchievement_paramAchievement();
        int achievementCount = ads.getAchievementCount(1, "found_strong_path");
        assertNotNull("get achievement is not null", achievementCount);
        assertTrue(achievementCount > 0);
    }

    public void test_updateAchievement_paramAchievement() throws Throwable {
        test_createAchievement_paramAchievement();
        Achievement achievement = ads.getAchievement(1);
        achievement.setKey("found_strong_transition");
        ads.updateAchievement(achievement);
        Achievement achievement2 = ads.getAchievement(1);
        assertNotNull("update achievement is not null", achievement2);
        assertTrue(achievement2.getKey().equals("found_strong_transition"));
    }
}
