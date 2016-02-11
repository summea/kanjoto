
package com.summea.kanjoto.data;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import com.summea.kanjoto.model.Emotion;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class EmotionsDataSource {
    private SQLiteDatabase database;
    private KanjotoDatabaseHelper dbHelper;

    // database table columns
    private String[] allColumns = {
            KanjotoDatabaseHelper.COLUMN_ID,
            KanjotoDatabaseHelper.COLUMN_NAME,
            KanjotoDatabaseHelper.COLUMN_LABEL_ID,
            KanjotoDatabaseHelper.COLUMN_APPRENTICE_ID,
    };

    /**
     * EmotionsDataSource constructor.
     * 
     * @param context Current state.
     */
    public EmotionsDataSource(Context context) {
        dbHelper = new KanjotoDatabaseHelper(context);
    }

    /**
     * EmotionsDataSource constructor.
     * 
     * @param context Current state.
     * @param databaseName Database to use.
     */
    public EmotionsDataSource(Context context, String databaseName) {
        dbHelper = new KanjotoDatabaseHelper(context, databaseName);
    }

    /**
     * Open database.
     * 
     * @throws SQLException
     */
    public void open() throws SQLException {
        setDatabase(dbHelper.getWritableDatabase());
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
        contentValues.put(KanjotoDatabaseHelper.COLUMN_NAME, emotion.getName());
        contentValues.put(KanjotoDatabaseHelper.COLUMN_LABEL_ID, emotion.getLabelId());
        contentValues.put(KanjotoDatabaseHelper.COLUMN_APPRENTICE_ID, emotion.getApprenticeId());

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        long insertId = db.insert(KanjotoDatabaseHelper.TABLE_EMOTIONS, null,
                contentValues);

        Cursor cursor = db.query(
                KanjotoDatabaseHelper.TABLE_EMOTIONS, allColumns,
                KanjotoDatabaseHelper.COLUMN_ID + " = " + insertId, null,
                null, null, null);

        cursor.moveToFirst();
        Emotion newEmotion = cursorToEmotion(cursor);
        cursor.close();
        db.close();

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
        db.delete(KanjotoDatabaseHelper.TABLE_EMOTIONS,
                KanjotoDatabaseHelper.COLUMN_ID + " = " + id, null);

        db.close();
    }

    /**
     * Get all emotions from database table.
     * 
     * @return List of Emotions.
     */
    public List<Emotion> getAllEmotions(long apprenticeId) {
        List<Emotion> emotions = new ArrayList<Emotion>();

        String query = "SELECT * FROM " + KanjotoDatabaseHelper.TABLE_EMOTIONS
                + " WHERE " + KanjotoDatabaseHelper.COLUMN_APPRENTICE_ID + "=?";

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // select all notes from database
        Cursor cursor = db.rawQuery(query, new String[] {
                String.valueOf(apprenticeId),
        });

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

        db.close();

        return emotions;
    }

    /**
     * Get all emotion ids from database table.
     * 
     * @return List of Emotions ids.
     */
    public List<Integer> getAllEmotionIds(long apprenticeId) {
        List<Integer> emotion_ids = new ArrayList<Integer>();

        String query = "SELECT " + KanjotoDatabaseHelper.COLUMN_ID + " FROM "
                + KanjotoDatabaseHelper.TABLE_EMOTIONS
                + " WHERE " + KanjotoDatabaseHelper.COLUMN_APPRENTICE_ID + "=?";

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // select all notes from database
        Cursor cursor = db.rawQuery(query, new String[] {
                String.valueOf(apprenticeId),
        });

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
        db.close();

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
     * Get a list of all emotions ids.
     * 
     * @return List of Emotion ids.
     */
    public List<Long> getAllEmotionListDBTableIds(long apprenticeId) {
        List<Long> emotions = new LinkedList<Long>();

        String query = "SELECT " + KanjotoDatabaseHelper.COLUMN_ID + " FROM "
                + KanjotoDatabaseHelper.TABLE_EMOTIONS
                + " WHERE " + KanjotoDatabaseHelper.COLUMN_APPRENTICE_ID + "=?";

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // select all emotions from database
        Cursor cursor = db.rawQuery(query, new String[] {
                String.valueOf(apprenticeId),
        });

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

        db.close();

        return emotions;
    }

    public Emotion getEmotion(long emotionId) {
        Emotion emotion = new Emotion();

        String query = "SELECT * FROM " + KanjotoDatabaseHelper.TABLE_EMOTIONS
                + " WHERE " + KanjotoDatabaseHelper.COLUMN_ID + "=?";

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // select all emotions from database
        Cursor cursor = db.rawQuery(query, new String[] {
                String.valueOf(emotionId),
        });

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
        contentValues.put(KanjotoDatabaseHelper.COLUMN_ID, emotion.getId());
        contentValues.put(KanjotoDatabaseHelper.COLUMN_NAME, emotion.getName());
        contentValues.put(KanjotoDatabaseHelper.COLUMN_LABEL_ID, emotion.getLabelId());
        contentValues.put(KanjotoDatabaseHelper.COLUMN_APPRENTICE_ID, emotion.getApprenticeId());

        db.update(KanjotoDatabaseHelper.TABLE_EMOTIONS, contentValues,
                KanjotoDatabaseHelper.COLUMN_ID + "=" + emotion.getId(), null);

        db.close();

        return emotion;
    }

    public Emotion getRandomEmotion(long apprenticeId) {
        Emotion emotion = new Emotion();

        // get all emotions first
        List<Emotion> allEmotions = getAllEmotions(apprenticeId);

        if (allEmotions.size() > 1) {
            // choose random emotion
            int chosenIndex = new Random().nextInt(allEmotions.size());

            emotion = allEmotions.get(chosenIndex);
        } else {
            if (allEmotions.size() > 0) {
                emotion = allEmotions.get(0);
            } else {
                emotion = new Emotion();
            }
        }
        return emotion;
    }

    /**
     * Get count of emotions from database table.
     * 
     * @return <int> count of emotions
     */
    public int getEmotionCount(long apprenticeId) {
        String query = "SELECT * FROM " + KanjotoDatabaseHelper.TABLE_EMOTIONS
                + " WHERE " + KanjotoDatabaseHelper.COLUMN_APPRENTICE_ID + "=?";

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // select all notes from database
        Cursor cursor = db.rawQuery(query, new String[] {
                String.valueOf(apprenticeId),
        });
        int count = cursor.getCount();
        cursor.close();
        db.close();

        return count;
    }

    public SQLiteDatabase getDatabase() {
        return database;
    }

    public void setDatabase(SQLiteDatabase database) {
        this.database = database;
    }
}
