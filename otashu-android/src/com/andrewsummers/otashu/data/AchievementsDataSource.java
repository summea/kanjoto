
package com.andrewsummers.otashu.data;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import com.andrewsummers.otashu.model.Achievement;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class AchievementsDataSource {
    private SQLiteDatabase database;
    private OtashuDatabaseHelper dbHelper;

    // database table columns
    private String[] allColumns = {
            OtashuDatabaseHelper.COLUMN_ID,
            OtashuDatabaseHelper.COLUMN_NAME,
            OtashuDatabaseHelper.COLUMN_APPRENTICE_ID,
            OtashuDatabaseHelper.COLUMN_EARNED_ON,
            OtashuDatabaseHelper.COLUMN_KEY,
    };

    /**
     * AchievementsDataSource constructor.
     * 
     * @param context Current state.
     */
    public AchievementsDataSource(Context context) {
        dbHelper = new OtashuDatabaseHelper(context);
    }

    /**
     * Open database.
     * 
     * @throws SQLException
     */
    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    /**
     * Close database.
     */
    public void close() {
        dbHelper.close();
    }

    /**
     * Create achievement row in database.
     * 
     * @param achievementvalues String of achievement values to insert.
     * @return Achievement of newly-created achievement data.
     */
    public Achievement createAchievement(Achievement achievement) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(OtashuDatabaseHelper.COLUMN_NAME, achievement.getName());
        contentValues.put(OtashuDatabaseHelper.COLUMN_APPRENTICE_ID, achievement.getApprenticeId());
        contentValues.put(OtashuDatabaseHelper.COLUMN_EARNED_ON, achievement.getEarnedOn());
        contentValues.put(OtashuDatabaseHelper.COLUMN_KEY, achievement.getKey());

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        long insertId = db.insert(OtashuDatabaseHelper.TABLE_ACHIEVEMENTS, null,
                contentValues);

        Cursor cursor = db.query(
                OtashuDatabaseHelper.TABLE_ACHIEVEMENTS, allColumns,
                OtashuDatabaseHelper.COLUMN_ID + " = " + insertId, null,
                null, null, null);

        cursor.moveToFirst();
        Achievement newAchievement = cursorToAchievement(cursor);
        cursor.close();
        return newAchievement;
    }

    /**
     * Delete achievement row from database.
     * 
     * @param achievement Achievement to delete.
     */
    public void deleteAchievement(Achievement achievement) {
        long id = achievement.getId();

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // delete achievement
        db.delete(OtashuDatabaseHelper.TABLE_ACHIEVEMENTS,
                OtashuDatabaseHelper.COLUMN_ID + " = " + id, null);
    }

    /**
     * Get all achievements from database table.
     * 
     * @return List of Achievements.
     */
    public List<Achievement> getAllAchievements(long apprenticeId) {
        List<Achievement> achievements = new ArrayList<Achievement>();

        String query = "SELECT * FROM " + OtashuDatabaseHelper.TABLE_ACHIEVEMENTS
                + " WHERE " + OtashuDatabaseHelper.COLUMN_APPRENTICE_ID + "=" + apprenticeId;

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // select all notes from database
        Cursor cursor = db.rawQuery(query, null);

        Achievement achievement = null;
        if (cursor.moveToFirst()) {
            do {
                // create note objects based on note data from database
                achievement = new Achievement();
                achievement.setId(cursor.getLong(0));
                achievement.setName(cursor.getString(1));
                achievement.setApprenticeId(cursor.getLong(2));
                achievement.setEarnedOn(cursor.getString(3));
                achievement.setKey(cursor.getString(4));

                // add note string to list of strings
                achievements.add(achievement);
            } while (cursor.moveToNext());
        }

        return achievements;
    }

    /**
     * Get all achievement ids from database table.
     * 
     * @return List of Achievements ids.
     */
    public List<Integer> getAllAchievementIds(long apprenticeId) {
        List<Integer> achievement_ids = new ArrayList<Integer>();

        String query = "SELECT " + OtashuDatabaseHelper.COLUMN_ID + " FROM "
                + OtashuDatabaseHelper.TABLE_ACHIEVEMENTS
                + " WHERE " + OtashuDatabaseHelper.COLUMN_APPRENTICE_ID + "=" + apprenticeId;

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // select all notes from database
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                // create note objects based on note data from database
                Achievement achievement = new Achievement();
                achievement.setId(cursor.getLong(0));

                // add note string to list of strings
                achievement_ids.add((int) achievement.getId());
            } while (cursor.moveToNext());
        }

        cursor.close();
        return achievement_ids;
    }

    /**
     * Access column data at current position of result.
     * 
     * @param cursor Current cursor location.
     * @return Achievement
     */
    private Achievement cursorToAchievement(Cursor cursor) {
        Achievement achievement = new Achievement();
        achievement.setId(cursor.getLong(0));
        achievement.setName(cursor.getString(1));
        achievement.setApprenticeId(cursor.getLong(2));
        achievement.setEarnedOn(cursor.getString(3));
        achievement.setKey(cursor.getString(4));
        return achievement;
    }

    /**
     * getAllAchievements gets a preview list of all achievements.
     * 
     * @return List of Achievement preview strings.
     */
    public List<String> getAllAchievementListPreviews(long apprenticeId) {
        List<String> achievements = new LinkedList<String>();

        String query = "SELECT * FROM " + OtashuDatabaseHelper.TABLE_ACHIEVEMENTS
                + " WHERE " + OtashuDatabaseHelper.COLUMN_APPRENTICE_ID + "=" + apprenticeId;

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // select all achievements from database
        Cursor cursor = db.rawQuery(query, null);

        Achievement achievement = null;
        if (cursor.moveToFirst()) {
            do {
                // create achievement objects based on achievement data from database
                achievement = new Achievement();
                achievement.setId(cursor.getLong(0));
                achievement.setName(cursor.getString(1));
                achievement.setApprenticeId(cursor.getLong(2));
                achievement.setEarnedOn(cursor.getString(3));
                achievement.setKey(cursor.getString(4));

                // add achievement string to list of strings
                achievements.add(achievement.toString());
            } while (cursor.moveToNext());
        }

        return achievements;
    }

    /**
     * Get a list of all achievements ids.
     * 
     * @return List of Achievement ids.
     */
    public List<Long> getAllAchievementListDBTableIds(long apprenticeId) {
        List<Long> achievements = new LinkedList<Long>();

        String query = "SELECT " + OtashuDatabaseHelper.COLUMN_ID + " FROM "
                + OtashuDatabaseHelper.TABLE_ACHIEVEMENTS
                + " WHERE " + OtashuDatabaseHelper.COLUMN_APPRENTICE_ID + "=" + apprenticeId;

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // select all achievements from database
        Cursor cursor = db.rawQuery(query, null);

        Achievement achievement = null;
        if (cursor.moveToFirst()) {
            do {
                // create achievement objects based on achievement data from database
                achievement = new Achievement();
                achievement.setId(cursor.getLong(0));

                // add achievement to achievements list
                achievements.add(achievement.getId());
            } while (cursor.moveToNext());
        }

        return achievements;
    }

    public Achievement getAchievement(long apprenticeId, long achievementId) {
        Achievement achievement = new Achievement();

        String query = "SELECT * FROM " + OtashuDatabaseHelper.TABLE_ACHIEVEMENTS
                + " WHERE " + OtashuDatabaseHelper.COLUMN_APPRENTICE_ID + "=" + apprenticeId
                + " AND " + OtashuDatabaseHelper.COLUMN_ID + "=" + achievementId;

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // select all achievements from database
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                // create achievement objects based on achievement data from database
                achievement = new Achievement();
                achievement.setId(cursor.getLong(0));
                achievement.setName(cursor.getString(1));
                achievement.setApprenticeId(cursor.getLong(2));
                achievement.setEarnedOn(cursor.getString(3));
                achievement.setKey(cursor.getString(4));
            } while (cursor.moveToNext());
        }

        db.close();

        return achievement;
    }

    public Achievement getAchievementByKey(long apprenticeId, String key) {
        Achievement achievement = new Achievement();

        String query = "SELECT * FROM " + OtashuDatabaseHelper.TABLE_ACHIEVEMENTS
                + " WHERE " + OtashuDatabaseHelper.COLUMN_APPRENTICE_ID + "=" + apprenticeId
                + " AND " + OtashuDatabaseHelper.COLUMN_KEY + "=" + key;

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // select all achievements from database
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                // create achievement objects based on achievement data from database
                achievement = new Achievement();
                achievement.setId(cursor.getLong(0));
                achievement.setName(cursor.getString(1));
                achievement.setApprenticeId(cursor.getLong(2));
                achievement.setEarnedOn(cursor.getString(3));
                achievement.setKey(cursor.getString(4));
            } while (cursor.moveToNext());
        }

        db.close();

        return achievement;
    }

    public Achievement updateAchievement(Achievement achievement) {

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(OtashuDatabaseHelper.COLUMN_ID, achievement.getId());
        contentValues.put(OtashuDatabaseHelper.COLUMN_NAME, achievement.getName());
        contentValues.put(OtashuDatabaseHelper.COLUMN_APPRENTICE_ID, achievement.getApprenticeId());
        contentValues.put(OtashuDatabaseHelper.COLUMN_EARNED_ON, achievement.getEarnedOn());
        contentValues.put(OtashuDatabaseHelper.COLUMN_KEY, achievement.getKey());

        db.update(OtashuDatabaseHelper.TABLE_ACHIEVEMENTS, contentValues,
                OtashuDatabaseHelper.COLUMN_ID + "=" + achievement.getId(), null);

        return achievement;
    }

    public Achievement getRandomAchievement(long apprenticeId) {
        Achievement achievement = new Achievement();

        // get all achievements first
        List<Achievement> allAchievements = getAllAchievements(apprenticeId);

        // choose random achievement
        int chosenIndex = new Random().nextInt(allAchievements.size());

        achievement = allAchievements.get(chosenIndex);

        return achievement;
    }
}
