
package com.andrewsummers.otashu.data;

import java.util.ArrayList;
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
            OtashuDatabaseHelper.COLUMN_EMOTION_ID,
            OtashuDatabaseHelper.COLUMN_APPRENTICE_ID,
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
     * KeySignaturesDataSource constructor.
     * 
     * @param context Current state.
     * @param databaseName Database to use.
     */
    public KeySignaturesDataSource(Context context, String databaseName) {
        dbHelper = new OtashuDatabaseHelper(context, databaseName);
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
     * Create keySignature row in database.
     * 
     * @param keySignaturevalues String of keySignature values to insert.
     * @return KeySignature of newly-created keySignature data.
     */
    public KeySignature createKeySignature(KeySignature keySignature) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(OtashuDatabaseHelper.COLUMN_EMOTION_ID, keySignature.getEmotionId());
        contentValues
                .put(OtashuDatabaseHelper.COLUMN_APPRENTICE_ID, keySignature.getApprenticeId());

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
        db.close();

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

        db.close();
    }

    /**
     * Get all keySignatures from database table.
     * 
     * @return List of KeySignatures.
     */
    public List<KeySignature> getAllKeySignatures(long apprenticeId) {
        List<KeySignature> keySignatures = new ArrayList<KeySignature>();

        String query = "SELECT * FROM " + OtashuDatabaseHelper.TABLE_KEY_SIGNATURES
                + " WHERE " + OtashuDatabaseHelper.COLUMN_APPRENTICE_ID + "=?";

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // select all notes from database
        Cursor cursor = db.rawQuery(query, new String[] {
                String.valueOf(apprenticeId)
        });

        KeySignature keySignature = null;
        if (cursor.moveToFirst()) {
            do {
                // create note objects based on note data from database
                keySignature = new KeySignature();
                keySignature.setId(cursor.getLong(0));
                keySignature.setEmotionId(cursor.getLong(1));
                keySignature.setApprenticeId(cursor.getLong(2));

                // add note string to list of strings
                keySignatures.add(keySignature);
            } while (cursor.moveToNext());
        }

        db.close();

        return keySignatures;
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
        keySignature.setApprenticeId(cursor.getLong(2));
        return keySignature;
    }

    public KeySignature getKeySignature(long keySignatureId) {
        KeySignature keySignature = new KeySignature();

        String query = "SELECT * FROM " + OtashuDatabaseHelper.TABLE_KEY_SIGNATURES
                + " WHERE " + OtashuDatabaseHelper.COLUMN_ID + "=?";

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // select all keySignatures from database
        Cursor cursor = db.rawQuery(query, new String[] {
                String.valueOf(keySignatureId)
        });

        if (cursor.moveToFirst()) {
            do {
                // create keySignature objects based on keySignature data from database
                keySignature = new KeySignature();
                keySignature.setId(cursor.getLong(0));
                keySignature.setEmotionId(cursor.getLong(1));
                keySignature.setApprenticeId(cursor.getLong(2));
            } while (cursor.moveToNext());
        }

        db.close();

        return keySignature;
    }

    public KeySignature updateKeySignature(KeySignature keySignature) {

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(OtashuDatabaseHelper.COLUMN_ID, keySignature.getId());
        contentValues.put(OtashuDatabaseHelper.COLUMN_EMOTION_ID, keySignature.getEmotionId());
        contentValues
                .put(OtashuDatabaseHelper.COLUMN_APPRENTICE_ID, keySignature.getApprenticeId());

        db.update(OtashuDatabaseHelper.TABLE_KEY_SIGNATURES, contentValues,
                OtashuDatabaseHelper.COLUMN_ID + "=" + keySignature.getId(), null);

        db.close();

        return keySignature;
    }

    public KeySignature getRandomKeySignature(long apprenticeId) {
        KeySignature keySignature = new KeySignature();

        // get all keySignatures first
        List<KeySignature> allKeySignatures = getAllKeySignatures(apprenticeId);

        if (allKeySignatures.size() > 0) {
            // choose random keySignature
            int chosenIndex = new Random().nextInt(allKeySignatures.size());
            keySignature = allKeySignatures.get(chosenIndex);
        }

        return keySignature;
    }

    public SQLiteDatabase getDatabase() {
        return database;
    }

    public void setDatabase(SQLiteDatabase database) {
        this.database = database;
    }
}
