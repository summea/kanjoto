
package com.andrewsummers.otashu.data;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import com.andrewsummers.otashu.model.KeySignature;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class KeySignaturesDataSource {
    private SQLiteDatabase database;
    private OtashuDatabaseHelper dbHelper;

    // database table columns
    private String[] allColumns = {
            OtashuDatabaseHelper.COLUMN_ID,
            OtashuDatabaseHelper.COLUMN_EMOTION_ID
    };

    /**
     * KeySignaturesDataSource constructor.
     * 
     * @param context Current state.
     */
    public KeySignaturesDataSource(Context context) {
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
     * Create keySignature row in database.
     * 
     * @param keySignaturevalues String of keySignature values to insert.
     * @return KeySignature of newly-created keySignature data.
     */
    public KeySignature createKeySignature(KeySignature keySignature) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(OtashuDatabaseHelper.COLUMN_EMOTION_ID, keySignature.getEmotionId());

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        long insertId = db
                .insert(OtashuDatabaseHelper.TABLE_KEY_SIGNATURES, null,
                        contentValues);

        Cursor cursor = db.query(
                OtashuDatabaseHelper.TABLE_KEY_SIGNATURES, allColumns,
                OtashuDatabaseHelper.COLUMN_ID + " = " + insertId, null,
                null, null, null);

        cursor.moveToFirst();

        KeySignature newKeySignature = cursorToKeySignature(cursor);
        cursor.close();
        return newKeySignature;
    }

    /**
     * Delete keySignature row from database.
     * 
     * @param keySignature KeySignature to delete.
     */
    public void deleteKeySignature(KeySignature keySignature) {
        long id = keySignature.getId();

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // delete keySignature
        db.delete(OtashuDatabaseHelper.TABLE_KEY_SIGNATURES,
                OtashuDatabaseHelper.COLUMN_ID + " = " + id, null);
    }

    /**
     * Get all keySignatures from database table.
     * 
     * @return List of KeySignatures.
     */
    public List<KeySignature> getAllKeySignatures() {
        List<KeySignature> keySignatures = new ArrayList<KeySignature>();

        String query = "SELECT * FROM " + OtashuDatabaseHelper.TABLE_KEY_SIGNATURES;

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // select all notes from database
        Cursor cursor = db.rawQuery(query, null);

        KeySignature keySignature = null;
        if (cursor.moveToFirst()) {
            do {
                // create note objects based on note data from database
                keySignature = new KeySignature();
                keySignature.setId(cursor.getLong(0));
                keySignature.setEmotionId(cursor.getLong(1));

                // add note string to list of strings
                keySignatures.add(keySignature);
            } while (cursor.moveToNext());
        }

        return keySignatures;
    }

    /**
     * Get all keySignature ids from database table.
     * 
     * @return List of KeySignatures ids.
     */
    public List<Integer> getAllKeySignatureIds() {
        List<Integer> keySignature_ids = new ArrayList<Integer>();

        Cursor cursor = database.query(
                OtashuDatabaseHelper.TABLE_KEY_SIGNATURES, allColumns, null,
                null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            KeySignature keySignature = cursorToKeySignature(cursor);
            keySignature_ids.add((int) keySignature.getId());
            cursor.moveToNext();
        }

        cursor.close();
        return keySignature_ids;
    }

    /**
     * Access column data at current position of result.
     * 
     * @param cursor Current cursor location.
     * @return KeySignature
     */
    private KeySignature cursorToKeySignature(Cursor cursor) {
        KeySignature keySignature = new KeySignature();
        keySignature.setId(cursor.getLong(0));
        keySignature.setEmotionId(cursor.getLong(1));
        return keySignature;
    }

    /**
     * getAllKeySignatures gets a preview list of all keySignatures.
     * 
     * @return List of KeySignature preview strings.
     */
    public List<String> getAllKeySignatureListPreviews() {
        List<String> keySignatures = new LinkedList<String>();

        String query = "SELECT * FROM " + OtashuDatabaseHelper.TABLE_KEY_SIGNATURES;

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // select all keySignatures from database
        Cursor cursor = db.rawQuery(query, null);

        KeySignature keySignature = null;
        if (cursor.moveToFirst()) {
            do {
                // create keySignature objects based on keySignature data from database
                keySignature = new KeySignature();
                keySignature.setId(cursor.getLong(0));
                keySignature.setEmotionId(cursor.getLong(1));

                // add keySignature string to list of strings
                keySignatures.add(keySignature.toString());
            } while (cursor.moveToNext());
        }

        return keySignatures;
    }

    /**
     * Get a list of all keySignatures ids.
     * 
     * @return List of KeySignature ids.
     */
    public List<Long> getAllKeySignatureListDBTableIds() {
        List<Long> keySignatures = new LinkedList<Long>();

        String query = "SELECT " + OtashuDatabaseHelper.COLUMN_ID + " FROM "
                + OtashuDatabaseHelper.TABLE_KEY_SIGNATURES;

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // select all keySignatures from database
        Cursor cursor = db.rawQuery(query, null);

        KeySignature keySignature = null;
        if (cursor.moveToFirst()) {
            do {
                // create keySignature objects based on keySignature data from database
                keySignature = new KeySignature();
                keySignature.setId(cursor.getLong(0));
                keySignature.setEmotionId(cursor.getLong(1));

                // add keySignature to keySignatures list
                keySignatures.add(keySignature.getId());
            } while (cursor.moveToNext());
        }

        return keySignatures;
    }

    public KeySignature getKeySignature(long keySignatureId) {
        KeySignature keySignature = new KeySignature();

        String query = "SELECT * FROM " + OtashuDatabaseHelper.TABLE_KEY_SIGNATURES + " WHERE "
                + OtashuDatabaseHelper.COLUMN_ID + "=" + keySignatureId;

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // select all keySignatures from database
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                // create keySignature objects based on keySignature data from database
                keySignature = new KeySignature();
                keySignature.setId(cursor.getLong(0));
                keySignature.setEmotionId(cursor.getLong(1));
            } while (cursor.moveToNext());
        }

        return keySignature;
    }

    public KeySignature updateKeySignature(KeySignature keySignature) {

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(OtashuDatabaseHelper.COLUMN_ID, keySignature.getId());
        contentValues.put(OtashuDatabaseHelper.COLUMN_EMOTION_ID, keySignature.getEmotionId());

        db.update(OtashuDatabaseHelper.TABLE_KEY_SIGNATURES, contentValues,
                OtashuDatabaseHelper.COLUMN_ID + "=" + keySignature.getId(), null);

        return keySignature;
    }

    public KeySignature getRandomKeySignature() {
        KeySignature keySignature = new KeySignature();

        // get all keySignatures first
        List<KeySignature> allKeySignatures = getAllKeySignatures();

        if (allKeySignatures.size() > 0) {
            // choose random keySignature
            int chosenIndex = new Random().nextInt(allKeySignatures.size());
            keySignature = allKeySignatures.get(chosenIndex);
        }

        return keySignature;
    }
}
