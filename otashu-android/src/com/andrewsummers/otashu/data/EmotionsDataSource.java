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
import android.util.Log;

/**
 * EmotionsDataSource is a data source that provides database functionality for
 * emotion-related data (e.g. CRUD) actions.
 * 
 * Emotion: Data source based on tutorial by vogella
 * http://www.vogella.com/tutorials/AndroidSQLite/article.html
 * Licensed under: CC BY-NC-SA 3.0 DE:
 * http://creativecommons.org/licenses/by-nc-sa/3.0/de/deed.en
 * Eclipse Public License: https://www.eclipse.org/legal/epl-v10.html
 */
public class EmotionsDataSource {
    private SQLiteDatabase database;
    private OtashuDatabaseHelper dbHelper;

    // database table columns
    private String[] allColumns = {
            OtashuDatabaseHelper.COLUMN_ID,
            OtashuDatabaseHelper.COLUMN_NAME
    };

    /**
     * EmotionsDataSource constructor.
     * 
     * @param context
     *            Current state.
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
     * @param emotionvalues
     *            String of emotion values to insert.
     * @return Emotion of newly-created emotion data.
     */
    public Emotion createEmotion(Emotion emotion) {        
        ContentValues contentValues = new ContentValues();
        contentValues.put(OtashuDatabaseHelper.COLUMN_NAME, emotion.getName());

        long insertId = database
                .insert(OtashuDatabaseHelper.TABLE_EMOTIONS, null,
                        contentValues);

        Cursor cursor = database.query(
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
     * @param emotion
     *            Emotion to delete.
     */
    public void deleteEmotion(Emotion emotion) {
        long id = emotion.getId();
        
        Log.d("MYLOG", "now really deleting id: " + id);
        
        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        
        // delete emotion
        Log.d("OTASHULOG", "deleting emotion with id: " + id);
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
                emotion.setId(Integer.parseInt(cursor.getString(0)));
                emotion.setName(cursor.getString(1));

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

        Cursor cursor = database.query(
                OtashuDatabaseHelper.TABLE_EMOTIONS, allColumns, null,
                null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Emotion emotion = cursorToEmotion(cursor);
            emotion_ids.add((int) emotion.getId());
            cursor.moveToNext();
        }

        cursor.close();
        return emotion_ids;
    }

    /**
     * Access column data at current position of result.
     * 
     * @param cursor
     *            Current cursor location.
     * @return Emotion
     */
    private Emotion cursorToEmotion(Cursor cursor) {
        Emotion emotion = new Emotion();
        emotion.setId(cursor.getLong(0));
        emotion.setName(cursor.getString(1));        
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
                emotion.setId(Integer.parseInt(cursor.getString(0)));
                emotion.setName(cursor.getString(1));

                // add emotion string to list of strings
                emotions.add(emotion.toString());
            } while (cursor.moveToNext());
        }

        Log.d("MYLOG", emotions.toString());

        return emotions;
    }

    /**
     * Get a list of all emotions ids.
     * 
     * @return List of Emotion ids.
     */
    public List<Long> getAllEmotionListDBTableIds() {
        List<Long> emotions = new LinkedList<Long>();
        
        String query = "SELECT " + OtashuDatabaseHelper.COLUMN_ID + " FROM " + OtashuDatabaseHelper.TABLE_EMOTIONS;

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // select all emotions from database
        Cursor cursor = db.rawQuery(query, null);

        Emotion emotion = null;
        if (cursor.moveToFirst()) {
            do {                
                // create emotion objects based on emotion data from database
                emotion = new Emotion();
                emotion.setId(Long.parseLong(cursor.getString(0)));
                
                // add emotion to emotions list
                emotions.add(emotion.getId());
            } while (cursor.moveToNext());
        }

        Log.d("MYLOG", emotions.toString());

        return emotions;
    }
    
    public Emotion getEmotion(long emotionId) {
        Emotion emotion = new Emotion();
        
        String query = "SELECT * FROM " + OtashuDatabaseHelper.TABLE_EMOTIONS + " WHERE " + OtashuDatabaseHelper.COLUMN_ID + "=" + emotionId;

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // select all emotions from database
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                // create emotion objects based on emotion data from database
                emotion = new Emotion();
                emotion.setId(Integer.parseInt(cursor.getString(0)));
                emotion.setName(cursor.getString(1));
            } while (cursor.moveToNext());
        }
        
        return emotion;
    }
    
    public Emotion updateEmotion(Emotion emotion) {
        
        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        
        Log.d("MYLOG", Long.toString(emotion.getId()));
        Log.d("MYLOG", emotion.getName());
        
        ContentValues contentValues = new ContentValues();
        contentValues.put(OtashuDatabaseHelper.COLUMN_ID, emotion.getId());
        contentValues.put(OtashuDatabaseHelper.COLUMN_NAME, emotion.getName());

        //db.update(OtashuDatabaseHelper.TABLE_NOTES, contentValues, OtashuDatabaseHelper.COLUMN_ID + "=" + emotion.getId(), null);
        
        Log.d("MYLOG", "sql update: " + OtashuDatabaseHelper.COLUMN_ID + "=" + emotion.getId());
        
        db.update(OtashuDatabaseHelper.TABLE_EMOTIONS, contentValues, OtashuDatabaseHelper.COLUMN_ID + "=" + emotion.getId(), null);

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