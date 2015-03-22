
package com.andrewsummers.otashu.data;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import com.andrewsummers.otashu.model.KeyNote;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class KeyNotesDataSource {
    private SQLiteDatabase database;
    private OtashuDatabaseHelper dbHelper;

    // database table columns
    private String[] allColumns = {
            OtashuDatabaseHelper.COLUMN_ID,
            OtashuDatabaseHelper.COLUMN_KEY_SIGNATURE_ID,
            OtashuDatabaseHelper.COLUMN_NOTEVALUE,
            OtashuDatabaseHelper.COLUMN_WEIGHT,
    };

    /**
     * KeyNotesDataSource constructor.
     * 
     * @param context Current state.
     */
    public KeyNotesDataSource(Context context) {
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
     * Create keyNote row in database.
     * 
     * @param keyNotevalues String of keyNote values to insert.
     * @return KeyNote of newly-created keyNote data.
     */
    public KeyNote createKeyNote(KeyNote keyNote) {
        ContentValues contentValues = new ContentValues();
        contentValues
                .put(OtashuDatabaseHelper.COLUMN_KEY_SIGNATURE_ID, keyNote.getKeySignatureId());
        contentValues.put(OtashuDatabaseHelper.COLUMN_NOTEVALUE, keyNote.getNotevalue());
        contentValues.put(OtashuDatabaseHelper.COLUMN_WEIGHT, keyNote.getWeight());

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        long insertId = db
                .insert(OtashuDatabaseHelper.TABLE_KEY_NOTES, null,
                        contentValues);

        Cursor cursor = db.query(
                OtashuDatabaseHelper.TABLE_KEY_NOTES, allColumns,
                OtashuDatabaseHelper.COLUMN_ID + " = " + insertId, null,
                null, null, null);

        cursor.moveToFirst();

        KeyNote newKeyNote = cursorToKeyNote(cursor);
        cursor.close();
        return newKeyNote;
    }

    /**
     * Delete keyNote row from database.
     * 
     * @param keyNote KeyNote to delete.
     */
    public void deleteKeyNote(KeyNote keyNote) {
        long id = keyNote.getId();

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // delete keyNote
        db.delete(OtashuDatabaseHelper.TABLE_KEY_NOTES,
                OtashuDatabaseHelper.COLUMN_ID + " = " + id, null);
    }

    /**
     * Get all keyNotes from database table.
     * 
     * @return List of KeyNotes.
     */
    public List<KeyNote> getAllKeyNotes() {
        List<KeyNote> keyNotes = new ArrayList<KeyNote>();

        String query = "SELECT * FROM " + OtashuDatabaseHelper.TABLE_KEY_NOTES;

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // select all notes from database
        Cursor cursor = db.rawQuery(query, null);

        KeyNote keyNote = null;
        if (cursor.moveToFirst()) {
            do {
                // create note objects based on note data from database
                keyNote = new KeyNote();
                keyNote.setId(cursor.getLong(0));
                keyNote.setKeySignatureId(cursor.getLong(1));
                keyNote.setNotevalue(cursor.getInt(2));
                keyNote.setWeight(cursor.getFloat(3));

                // add note string to list of strings
                keyNotes.add(keyNote);
            } while (cursor.moveToNext());
        }

        return keyNotes;
    }

    /**
     * Get all keyNote ids from database table.
     * 
     * @return List of KeyNotes ids.
     */
    public List<Integer> getAllKeyNoteIds() {
        List<Integer> keyNote_ids = new ArrayList<Integer>();

        Cursor cursor = database.query(
                OtashuDatabaseHelper.TABLE_KEY_NOTES, allColumns, null,
                null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            KeyNote keyNote = cursorToKeyNote(cursor);
            keyNote_ids.add((int) keyNote.getId());
            cursor.moveToNext();
        }

        cursor.close();
        return keyNote_ids;
    }

    /**
     * Access column data at current position of result.
     * 
     * @param cursor Current cursor location.
     * @return KeyNote
     */
    private KeyNote cursorToKeyNote(Cursor cursor) {
        KeyNote keyNote = new KeyNote();
        keyNote.setId(cursor.getLong(0));
        keyNote.setKeySignatureId(cursor.getLong(1));
        keyNote.setNotevalue(cursor.getInt(2));
        keyNote.setWeight(cursor.getFloat(3));
        return keyNote;
    }

    /**
     * getAllKeyNotes gets a preview list of all keyNotes.
     * 
     * @return List of KeyNote preview strings.
     */
    public List<String> getAllKeyNoteListPreviews() {
        List<String> keyNotes = new LinkedList<String>();

        String query = "SELECT * FROM " + OtashuDatabaseHelper.TABLE_KEY_NOTES;

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // select all keyNotes from database
        Cursor cursor = db.rawQuery(query, null);

        KeyNote keyNote = null;
        if (cursor.moveToFirst()) {
            do {
                // create keyNote objects based on keyNote data from database
                keyNote = new KeyNote();
                keyNote.setId(cursor.getLong(0));
                keyNote.setKeySignatureId(cursor.getLong(1));
                keyNote.setNotevalue(cursor.getInt(2));
                keyNote.setWeight(cursor.getFloat(3));

                // add keyNote string to list of strings
                keyNotes.add(keyNote.toString());
            } while (cursor.moveToNext());
        }

        return keyNotes;
    }

    /**
     * Get a list of all keyNotes ids.
     * 
     * @return List of KeyNote ids.
     */
    public List<Long> getAllKeyNoteListDBTableIds() {
        List<Long> keyNotes = new LinkedList<Long>();

        String query = "SELECT " + OtashuDatabaseHelper.COLUMN_ID + " FROM "
                + OtashuDatabaseHelper.TABLE_KEY_NOTES;

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // select all keyNotes from database
        Cursor cursor = db.rawQuery(query, null);

        KeyNote keyNote = null;
        if (cursor.moveToFirst()) {
            do {
                // create keyNote objects based on keyNote data from database
                keyNote = new KeyNote();
                keyNote.setId(cursor.getLong(0));

                // add keyNote to keyNotes list
                keyNotes.add(keyNote.getId());
            } while (cursor.moveToNext());
        }

        return keyNotes;
    }

    public KeyNote getKeyNote(long keyNoteId) {
        KeyNote keyNote = new KeyNote();

        String query = "SELECT * FROM " + OtashuDatabaseHelper.TABLE_KEY_NOTES + " WHERE "
                + OtashuDatabaseHelper.COLUMN_ID + "=" + keyNoteId;

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // select all keyNotes from database
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                // create keyNote objects based on keyNote data from database
                keyNote = new KeyNote();
                keyNote.setId(cursor.getLong(0));
                keyNote.setKeySignatureId(cursor.getLong(1));
                keyNote.setNotevalue(cursor.getInt(2));
                keyNote.setWeight(cursor.getFloat(3));
            } while (cursor.moveToNext());
        }

        return keyNote;
    }

    public List<KeyNote> getKeyNoteByKeySignature(long keySignatureId) {
        List<KeyNote> keyNotes = new ArrayList<KeyNote>();

        String query = "SELECT * FROM " + OtashuDatabaseHelper.TABLE_KEY_NOTES + " WHERE "
                + OtashuDatabaseHelper.COLUMN_KEY_SIGNATURE_ID + "=" + keySignatureId;

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // select all keyNotes from database
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                // create keyNote objects based on keyNote data from database
                KeyNote keyNote = new KeyNote();
                keyNote.setId(cursor.getLong(0));
                keyNote.setKeySignatureId(cursor.getLong(1));
                keyNote.setNotevalue(cursor.getInt(2));
                keyNote.setWeight(cursor.getFloat(3));
                keyNotes.add(keyNote);
            } while (cursor.moveToNext());
        }

        return keyNotes;
    }

    public KeyNote updateKeyNote(KeyNote keyNote) {

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(OtashuDatabaseHelper.COLUMN_ID, keyNote.getId());
        contentValues
                .put(OtashuDatabaseHelper.COLUMN_KEY_SIGNATURE_ID, keyNote.getKeySignatureId());
        contentValues.put(OtashuDatabaseHelper.COLUMN_NOTEVALUE, keyNote.getNotevalue());
        contentValues.put(OtashuDatabaseHelper.COLUMN_WEIGHT, keyNote.getWeight());

        db.update(OtashuDatabaseHelper.TABLE_KEY_NOTES, contentValues,
                OtashuDatabaseHelper.COLUMN_ID + "=" + keyNote.getId(), null);

        return keyNote;
    }

    public KeyNote getRandomKeyNote() {
        KeyNote keyNote = new KeyNote();

        // get all keyNotes first
        List<KeyNote> allKeyNotes = getAllKeyNotes();

        if (allKeyNotes.size() > 0) {
            // choose random keyNote
            int chosenIndex = new Random().nextInt(allKeyNotes.size());
            keyNote = allKeyNotes.get(chosenIndex);
        }

        return keyNote;
    }
}
