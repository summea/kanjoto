package com.andrewsummers.otashu;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * NotesetsDataSource is a data source that provides database functionality for
 * noteset-related data (e.g. CRUD) actions.
 * 
 * Note: Data source based on tutorial by vogella
 * http://www.vogella.com/tutorials/AndroidSQLite/article.html
 * Licensed under: CC BY-NC-SA 3.0 DE:
 * http://creativecommons.org/licenses/by-nc-sa/3.0/de/deed.en
 * Eclipse Public License: https://www.eclipse.org/legal/epl-v10.html
 */
public class NotesetsDataSource {
    private SQLiteDatabase database;
    private OtashuDatabaseHelper dbHelper;

    // database table columns
    private String[] allColumns = {
            OtashuDatabaseHelper.COLUMN_ID,
            OtashuDatabaseHelper.COLUMN_NAME
    };

    /**
     * NotesetsDataSource constructor.
     * 
     * @param context
     *            Current state.
     */
    public NotesetsDataSource(Context context) {
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
     * Create noteset row in database.
     * 
     * @param notevalues
     *            String of note values to insert.
     * @return Noteset of newly-created noteset data.
     */
    public Noteset createNoteset(Noteset noteset) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(OtashuDatabaseHelper.COLUMN_NAME,
                noteset.getName());

        long insertId = database
                .insert(OtashuDatabaseHelper.TABLE_NOTESETS, null,
                        contentValues);

        Cursor cursor = database.query(
                OtashuDatabaseHelper.TABLE_NOTESETS, allColumns,
                OtashuDatabaseHelper.COLUMN_ID + " = " + insertId, null,
                null, null, null);

        cursor.moveToFirst();
        Noteset newNoteset = cursorToNoteset(cursor);
        cursor.close();
        return newNoteset;
    }

    /**
     * Delete noteset row from database.
     * 
     * @param noteset
     *            Noteset to delete.
     */
    public void deleteNoteset(Noteset noteset) {
        long id = noteset.getId();
        Log.d("OTASHULOG", "deleting noteset with id: " + id);
        database.delete(OtashuDatabaseHelper.TABLE_NOTESETS,
                OtashuDatabaseHelper.COLUMN_ID + " = " + id, null);
    }

    /**
     * Get all notesets from database table.
     * 
     * @return List of Notesets.
     */
    public List<Noteset> getAllNotesets() {
        List<Noteset> notesets = new ArrayList<Noteset>();

        Cursor cursor = database.query(
                OtashuDatabaseHelper.TABLE_NOTESETS, allColumns, null,
                null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Noteset noteset = cursorToNoteset(cursor);
            notesets.add(noteset);
            cursor.moveToNext();
        }

        cursor.close();
        return notesets;
    }

    /**
     * Access column data at current position of result.
     * 
     * @param cursor
     *            Current cursor location.
     * @return Noteset
     */
    private Noteset cursorToNoteset(Cursor cursor) {
        Noteset noteset = new Noteset();
        noteset.setId(cursor.getLong(0));
        noteset.setName(cursor.getString(1));
        return noteset;
    }
        
    /**
     * getAllNotesets gets a preview list of all notesets.
     * 
     * @return List of Noteset preview strings.
     */
    public List<String> getAllNotesetListPreviews() {
        List<String> notesets = new LinkedList<String>();
        
        String itemForList = ""; 
        
        String query = "SELECT * FROM " + OtashuDatabaseHelper.TABLE_NOTESETS;

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // select all notesets from database
        Cursor cursor = db.rawQuery(query, null);

        Noteset noteset = null;
        if (cursor.moveToFirst()) {
            do {
                // create noteset objects based on noteset data from database
                noteset = new Noteset();
                noteset.setId(Integer.parseInt(cursor.getString(0)));
                noteset.setName(cursor.getString(1));

                // get all related notes inside this noteset
                // TODO: make this query approach more efficient at some point, if necessary
                String queryForRelatedNotes = "SELECT * FROM " + OtashuDatabaseHelper.TABLE_NOTES + " WHERE " + OtashuDatabaseHelper.COLUMN_NOTESET_ID + " = " + noteset.getId();
                Cursor cursorForRelatedNotes = db.rawQuery(queryForRelatedNotes, null);
                
                Note note = null;
                if (cursorForRelatedNotes.moveToFirst()) {
                    do {
                        note = new Note();
                        note.setNotevalue(Integer.parseInt(cursorForRelatedNotes.getString(2)));
                        itemForList += note.getNotevalue(); 
                    } while (cursorForRelatedNotes.moveToNext());
                }
                
                // add noteset string to list of strings
                notesets.add(noteset.toString() + " " + itemForList);
            } while (cursor.moveToNext());
        }

        Log.d("MYLOG", notesets.toString());

        return notesets;
    }
}