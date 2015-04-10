
package com.andrewsummers.otashu.data;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import com.andrewsummers.otashu.model.Emotion;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class EmotionsDataSource {
    private SQLiteDatabase database;
    private OtashuDatabaseHelper dbHelper;

    // database table columns
    private String[] allColumns = {
            OtashuDatabaseHelper.COLUMN_ID,
            OtashuDatabaseHelper.COLUMN_NAME,
            OtashuDatabaseHelper.COLUMN_LABEL_ID,
            OtashuDatabaseHelper.COLUMN_APPRENTICE_ID,
    };

    /**
     * EmotionsDataSource constructor.
     * 
     * @param context Current state.
     */
    public EmotionsDataSource(Context context) {
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
     * Create emotion row in database.
     * 
     * @param emotionvalues String of emotion values to insert.
     * @return Emotion of newly-created emotion data.
     */
    public Emotion createEmotion(Emotion emotion) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(OtashuDatabaseHelper.COLUMN_NAME, emotion.getName());
        contentValues.put(OtashuDatabaseHelper.COLUMN_LABEL_ID, emotion.getLabelId());
        contentValues.put(OtashuDatabaseHelper.COLUMN_APPRENTICE_ID, emotion.getApprenticeId());

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        long insertId = db.insert(OtashuDatabaseHelper.TABLE_EMOTIONS, null,
                contentValues);

        Cursor cursor = db.query(
                OtashuDatabaseHelper.TABLE_EMOTIONS, allColumns,
                OtashuDatabaseHelper.COLUMN_ID + " = " + insertId, null,
                null, null, null);

        cursor.moveToFirst();
        Emotion newEmotion = cursorToEmotion(cursor);
        cursor.close();
        return newEmotion;
    }

    /**
     * Delete emotion row from database.
     * 
     * @param emotion Emotion to delete.
     */
    public void deleteEmotion(Emotion emotion) {
        long id = emotion.getId();

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // delete emotion
        db.delete(OtashuDatabaseHelper.TABLE_EMOTIONS,
                OtashuDatabaseHelper.COLUMN_ID + " = " + id, null);
    }

    /**
     * Get all emotions from database table.
     * 
     * @return List of Emotions.
     */
    public List<Emotion> getAllEmotions() {
        List<Emotion> emotions = new ArrayList<Emotion>();

        String query = "SELECT * FROM " + OtashuDatabaseHelper.TABLE_EMOTIONS;

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // select all notes from database
        Cursor cursor = db.rawQuery(query, null);

        Emotion emotion = null;
        if (cursor.moveToFirst()) {
            do {
                // create note objects based on note data from database
                emotion = new Emotion();
                emotion.setId(cursor.getLong(0));
                emotion.setName(cursor.getString(1));
                emotion.setLabelId(cursor.getLong(2));
                emotion.setApprenticeId(cursor.getLong(3));

                // add note string to list of strings
                emotions.add(emotion);
            } while (cursor.moveToNext());
        }

        return emotions;
    }

    /**
     * Get all emotion ids from database table.
     * 
     * @return List of Emotions ids.
     */
    public List<Integer> getAllEmotionIds() {
        List<Integer> emotion_ids = new ArrayList<Integer>();

        String query = "SELECT " + OtashuDatabaseHelper.COLUMN_ID + " FROM "
                + OtashuDatabaseHelper.TABLE_EMOTIONS;

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // select all notes from database
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                // create note objects based on note data from database
                Emotion emotion = new Emotion();
                emotion.setId(cursor.getLong(0));

                // add note string to list of strings
                emotion_ids.add((int) emotion.getId());
            } while (cursor.moveToNext());
        }

        cursor.close();
        return emotion_ids;
    }

    /**
     * Access column data at current position of result.
     * 
     * @param cursor Current cursor location.
     * @return Emotion
     */
    private Emotion cursorToEmotion(Cursor cursor) {
        Emotion emotion = new Emotion();
        emotion.setId(cursor.getLong(0));
        emotion.setName(cursor.getString(1));
        emotion.setLabelId(cursor.getLong(2));
        emotion.setApprenticeId(cursor.getLong(3));
        return emotion;
    }

    /**
     * getAllEmotions gets a preview list of all emotions.
     * 
     * @return List of Emotion preview strings.
     */
    public List<String> getAllEmotionListPreviews() {
        List<String> emotions = new LinkedList<String>();

        String query = "SELECT * FROM " + OtashuDatabaseHelper.TABLE_EMOTIONS;

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // select all emotions from database
        Cursor cursor = db.rawQuery(query, null);

        Emotion emotion = null;
        if (cursor.moveToFirst()) {
            do {
                // create emotion objects based on emotion data from database
                emotion = new Emotion();
                emotion.setId(cursor.getLong(0));
                emotion.setName(cursor.getString(1));
                emotion.setLabelId(cursor.getLong(2));
                emotion.setApprenticeId(cursor.getLong(3));

                // add emotion string to list of strings
                emotions.add(emotion.toString());
            } while (cursor.moveToNext());
        }

        return emotions;
    }

    /**
     * Get a list of all emotions ids.
     * 
     * @return List of Emotion ids.
     */
    public List<Long> getAllEmotionListDBTableIds() {
        List<Long> emotions = new LinkedList<Long>();

        String query = "SELECT " + OtashuDatabaseHelper.COLUMN_ID + " FROM "
                + OtashuDatabaseHelper.TABLE_EMOTIONS;

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // select all emotions from database
        Cursor cursor = db.rawQuery(query, null);

        Emotion emotion = null;
        if (cursor.moveToFirst()) {
            do {
                // create emotion objects based on emotion data from database
                emotion = new Emotion();
                emotion.setId(cursor.getLong(0));

                // add emotion to emotions list
                emotions.add(emotion.getId());
            } while (cursor.moveToNext());
        }

        return emotions;
    }

    public Emotion getEmotion(long emotionId) {
        Emotion emotion = new Emotion();

        String query = "SELECT * FROM " + OtashuDatabaseHelper.TABLE_EMOTIONS + " WHERE "
                + OtashuDatabaseHelper.COLUMN_ID + "=" + emotionId;

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // select all emotions from database
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                // create emotion objects based on emotion data from database
                emotion = new Emotion();
                emotion.setId(cursor.getLong(0));
                emotion.setName(cursor.getString(1));
                emotion.setLabelId(cursor.getLong(2));
                emotion.setApprenticeId(cursor.getLong(3));
            } while (cursor.moveToNext());
        }

        db.close();

        return emotion;
    }

    public Emotion updateEmotion(Emotion emotion) {

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(OtashuDatabaseHelper.COLUMN_ID, emotion.getId());
        contentValues.put(OtashuDatabaseHelper.COLUMN_NAME, emotion.getName());
        contentValues.put(OtashuDatabaseHelper.COLUMN_LABEL_ID, emotion.getLabelId());
        contentValues.put(OtashuDatabaseHelper.COLUMN_APPRENTICE_ID, emotion.getApprenticeId());

        db.update(OtashuDatabaseHelper.TABLE_EMOTIONS, contentValues,
                OtashuDatabaseHelper.COLUMN_ID + "=" + emotion.getId(), null);

        return emotion;
    }

    public Emotion getRandomEmotion() {
        Emotion emotion = new Emotion();

        // get all emotions first
        List<Emotion> allEmotions = getAllEmotions();

        // choose random emotion
        int chosenIndex = new Random().nextInt(allEmotions.size());

        emotion = allEmotions.get(chosenIndex);

        return emotion;
    }
}
