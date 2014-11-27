package com.andrewsummers.otashu.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import com.andrewsummers.otashu.model.Note;
import com.andrewsummers.otashu.model.Noteset;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.util.SparseArray;

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
        contentValues.put(OtashuDatabaseHelper.COLUMN_EMOTION_ID,
                noteset.getEmotion());

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
        
        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        
        // delete noteset
        db.delete(OtashuDatabaseHelper.TABLE_NOTESETS,
                OtashuDatabaseHelper.COLUMN_ID + " = " + id, null);
        
        // delete related notes
        db.delete(OtashuDatabaseHelper.TABLE_NOTES,
                OtashuDatabaseHelper.COLUMN_NOTESET_ID + " = " + id, null);
    }
    
    /**
     * Get all notesets from database table.
     * 
     * @return List of Notesets.
     */
    public List<Noteset> getAllNotesets() {
        List<Noteset> notesets = new ArrayList<Noteset>();

        String query = "SELECT * FROM " + OtashuDatabaseHelper.TABLE_NOTESETS;

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // select all notes from database
        Cursor cursor = db.rawQuery(query, null);

        Noteset noteset = null;
        if (cursor.moveToFirst()) {
            do {
                // create note objects based on note data from database
                noteset = new Noteset();
                noteset.setId(Long.parseLong(cursor.getString(0)));
                noteset.setName(cursor.getString(1));
                noteset.setEmotion(Integer.parseInt(cursor.getString(2)));

                // add note string to list of strings
                notesets.add(noteset);
            } while (cursor.moveToNext());
        }

        return notesets;
    }
    
    /**
     * Get all notesets from database table.
     * 
     * @return List of Notesets.
     */
    public List<Noteset> getAllNotesets(int limit, int offset) {
        List<Noteset> notesets = new ArrayList<Noteset>();

        String query = "SELECT * FROM " + OtashuDatabaseHelper.TABLE_NOTESETS;
        if (limit > 0)
            query += " LIMIT " + limit;
        if (offset > 0)
            query += " OFFSET " + offset;

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // select all notes from database
        Cursor cursor = db.rawQuery(query, null);

        Noteset noteset = null;
        if (cursor.moveToFirst()) {
            do {
                // create note objects based on note data from database
                noteset = new Noteset();
                noteset.setId(Long.parseLong(cursor.getString(0)));
                noteset.setName(cursor.getString(1));
                noteset.setEmotion(Integer.parseInt(cursor.getString(2)));

                // add note string to list of strings
                notesets.add(noteset);
            } while (cursor.moveToNext());
        }

        return notesets;
    }

    /**
     * Get all notesets bundles from database table.
     * 
     * @return List of noteset bundles.
     */
    public HashMap<Integer, List<Note>> getAllNotesetBundles() {        
        HashMap<Integer, List<Note>> notesetBundles = new HashMap<Integer, List<Note>>();
        
        String query = "SELECT * FROM " + OtashuDatabaseHelper.TABLE_NOTESETS;

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // select all notesets from database
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                Integer notesetId = Integer.parseInt(cursor.getString(0));
                
                // get all related notes inside this noteset
                // TODO: make this query approach more efficient at some point, if necessary
                String queryForRelatedNotes = "SELECT * FROM " + OtashuDatabaseHelper.TABLE_NOTES + " WHERE " + OtashuDatabaseHelper.COLUMN_NOTESET_ID + " = " + notesetId;
                Cursor cursorForRelatedNotes = db.rawQuery(queryForRelatedNotes, null);
                
                
                List<Note> notes = new LinkedList<Note>();
                
                if (cursorForRelatedNotes.moveToFirst()) {
                    do {
                        Note note = null;
                        note = new Note();
                        note.setNotesetId(cursorForRelatedNotes.getLong(1));
                        note.setNotevalue(cursorForRelatedNotes.getInt(2));
                        note.setVelocity(cursorForRelatedNotes.getInt(3));
                        note.setLength(cursorForRelatedNotes.getFloat(4));
                        note.setPosition(cursorForRelatedNotes.getInt(5));
                        notes.add(note);
                    } while (cursorForRelatedNotes.moveToNext());
                }
                
                notesetBundles.put(notesetId, notes);
                
            } while (cursor.moveToNext());
        }

        return notesetBundles;
    }
    
    /**
     * Get all notesets bundles from database table.
     * 
     * @return List of noteset bundles.
     */
    public HashMap<Integer, List<Note>> getAllNotesetBundles(int emotion_id) {
        HashMap<Integer, List<Note>> notesetBundles = new HashMap<Integer, List<Note>>();
        
        String query = "SELECT * FROM " + OtashuDatabaseHelper.TABLE_NOTESETS + " WHERE " + OtashuDatabaseHelper.COLUMN_EMOTION_ID + "=" + emotion_id;

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // select all notesets from database
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                Integer notesetId = Integer.parseInt(cursor.getString(0));
                
                // get all related notes inside this noteset
                // TODO: make this query approach more efficient at some point, if necessary
                String queryForRelatedNotes = "SELECT * FROM " + OtashuDatabaseHelper.TABLE_NOTES + " WHERE " + OtashuDatabaseHelper.COLUMN_NOTESET_ID + " = " + notesetId;
                Cursor cursorForRelatedNotes = db.rawQuery(queryForRelatedNotes, null);
                
                
                List<Note> notes = new LinkedList<Note>();
                
                if (cursorForRelatedNotes.moveToFirst()) {
                    do {
                        Note note = null;
                        note = new Note();
                        note.setNotesetId(cursorForRelatedNotes.getLong(1));
                        note.setNotevalue(cursorForRelatedNotes.getInt(2));
                        note.setVelocity(cursorForRelatedNotes.getInt(3));
                        note.setLength(cursorForRelatedNotes.getFloat(4));
                        note.setPosition(cursorForRelatedNotes.getInt(5));
                        notes.add(note);
                    } while (cursorForRelatedNotes.moveToNext());
                }
                
                notesetBundles.put(notesetId, notes);
                
            } while (cursor.moveToNext());
        }

        return notesetBundles;
    }

    public SparseArray<List<Note>> getNotesetBundle(long id) {
        SparseArray<List<Note>> notesetBundle = new SparseArray<List<Note>>();
        
        String query = "SELECT * FROM " + OtashuDatabaseHelper.TABLE_NOTESETS + " WHERE " + OtashuDatabaseHelper.COLUMN_ID + "=" + id;

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // select all notesets from database
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                Integer notesetId = Integer.parseInt(cursor.getString(0));
                
                // get all related notes inside this noteset
                // TODO: make this query approach more efficient at some point, if necessary
                String queryForRelatedNotes = "SELECT * FROM " + OtashuDatabaseHelper.TABLE_NOTES + " WHERE " + OtashuDatabaseHelper.COLUMN_NOTESET_ID + " = " + id;
                Cursor cursorForRelatedNotes = db.rawQuery(queryForRelatedNotes, null);
                
                List<Note> notes = new LinkedList<Note>();
                
                if (cursorForRelatedNotes.moveToFirst()) {
                    do {
                        Note note = null;
                        note = new Note();
                        note.setNotesetId(cursorForRelatedNotes.getLong(1));
                        note.setNotevalue(cursorForRelatedNotes.getInt(2));
                        note.setVelocity(cursorForRelatedNotes.getInt(3));
                        note.setLength(cursorForRelatedNotes.getFloat(4));
                        note.setPosition(cursorForRelatedNotes.getInt(5));
                        notes.add(note);
                    } while (cursorForRelatedNotes.moveToNext());
                }
                
                notesetBundle.put(notesetId, notes);
                
            } while (cursor.moveToNext());
        }

        return notesetBundle;
    }

    
    public HashMap<String, List<Object>> getNotesetBundleDetail(long id) {
        HashMap<String, List<Object>> notesetBundle = new HashMap<String, List<Object>>();
        
        String query = "SELECT * FROM " + OtashuDatabaseHelper.TABLE_NOTESETS + " WHERE " + OtashuDatabaseHelper.COLUMN_ID + "=" + id;

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // select all notesets from database
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                Noteset noteset = new Noteset();
                noteset.setId(Integer.parseInt(cursor.getString(0)));
                noteset.setName(cursor.getString(1));
                noteset.setEmotion(Integer.parseInt(cursor.getString(2)));
                
                List<Object> notesets = new LinkedList<Object>();
                notesets.add(noteset);
                
                notesetBundle.put("noteset", notesets);
                
                // get all related notes inside this noteset
                // TODO: make this query approach more efficient at some point, if necessary
                String queryForRelatedNotes = "SELECT * FROM " + OtashuDatabaseHelper.TABLE_NOTES + " WHERE " + OtashuDatabaseHelper.COLUMN_NOTESET_ID + " = " + id;
                Cursor cursorForRelatedNotes = db.rawQuery(queryForRelatedNotes, null);
                
                List<Object> notes = new LinkedList<Object>();
                
                if (cursorForRelatedNotes.moveToFirst()) {
                    do {                        
                        Long nId = 0L;
                        Long nNotesetId = 0L;
                        int nNotevalue = 0;
                        int nVelocity = 0;
                        float nLength = 0.0f;
                        
                        try {
                            nId = cursorForRelatedNotes.getLong(0);
                            nNotesetId = cursorForRelatedNotes.getLong(1);
                            nNotevalue = cursorForRelatedNotes.getInt(2);
                            nVelocity = cursorForRelatedNotes.getInt(3);
                            nLength = cursorForRelatedNotes.getFloat(4);
                        } catch (Exception e) {
                            Log.d("MYLOG", e.getStackTrace().toString());
                        }
                        
                        Note note = new Note();
                        note.setId(nId);
                        note.setNotesetId(nNotesetId);
                        note.setNotevalue(nNotevalue);
                        note.setVelocity(nVelocity);
                        note.setLength(nLength);
                        
                        notes.add(note);
                    } while (cursorForRelatedNotes.moveToNext());
                }
                
                notesetBundle.put("notes", notes);
                
            } while (cursor.moveToNext());
        }

        return notesetBundle;
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

        //String[] noteValuesArray = getResources().getStringArray(R.array.note_values_array);
        
        String query = "SELECT * FROM " + OtashuDatabaseHelper.TABLE_NOTESETS;

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // select all notesets from database
        Cursor cursor = db.rawQuery(query, null);

        Noteset noteset = null;
        if (cursor.moveToFirst()) {
            do {
                String itemForList = "";
                
                // create noteset objects based on noteset data from database
                noteset = new Noteset();
                noteset.setId(Integer.parseInt(cursor.getString(0)));
                noteset.setName(cursor.getString(1));
                noteset.setEmotion((cursor.getInt(2)));

                // get all related notes inside this noteset
                // TODO: make this query approach more efficient at some point, if necessary
                String queryForRelatedNotes = "SELECT * FROM " + OtashuDatabaseHelper.TABLE_NOTES + " WHERE " + OtashuDatabaseHelper.COLUMN_NOTESET_ID + " = " + noteset.getId();
                Cursor cursorForRelatedNotes = db.rawQuery(queryForRelatedNotes, null);
                
                Note note = null;
                if (cursorForRelatedNotes.moveToFirst()) {
                    do {
                        note = new Note();
                        note.setNotesetId(cursorForRelatedNotes.getLong(1));
                        note.setNotevalue(cursorForRelatedNotes.getInt(2));
                        note.setVelocity(cursorForRelatedNotes.getInt(3));
                        note.setLength(cursorForRelatedNotes.getFloat(4));
                        note.setPosition(cursorForRelatedNotes.getInt(5));
                        
                        itemForList += note.getNotevalue(); 
                    } while (cursorForRelatedNotes.moveToNext());
                }
                
                // add noteset string to list of strings
                //notesets.add(noteset.toString() + " " + itemForList);
                notesets.add(itemForList);
            } while (cursor.moveToNext());
        }

        return notesets;
    }
    
    /**
     * getAllNotesets gets a preview list of all notesets.
     * 
     * @return List of Noteset preview strings.
     */
    public List<String> getAllNotesetListPreviews(String[] noteLabelsArray, String[] noteValuesArray) {
        List<String> notesets = new LinkedList<String>();
        
        String query = "SELECT * FROM " + OtashuDatabaseHelper.TABLE_NOTESETS;

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // select all notesets from database
        Cursor cursor = db.rawQuery(query, null);

        Noteset noteset = null;
        if (cursor.moveToFirst()) {
            do {
                String itemForList = "";
                
                // create noteset objects based on noteset data from database
                noteset = new Noteset();
                noteset.setId(Integer.parseInt(cursor.getString(0)));
                noteset.setName(cursor.getString(1));
                noteset.setEmotion((cursor.getInt(2)));

                // get all related notes inside this noteset
                // TODO: make this query approach more efficient at some point, if necessary
                String queryForRelatedNotes = "SELECT * FROM " + OtashuDatabaseHelper.TABLE_NOTES + " WHERE " + OtashuDatabaseHelper.COLUMN_NOTESET_ID + " = " + noteset.getId();
                Cursor cursorForRelatedNotes = db.rawQuery(queryForRelatedNotes, null);
                
                Note note = null;
                if (cursorForRelatedNotes.moveToFirst()) {
                    do {
                        note = new Note();
                        note.setNotesetId(cursorForRelatedNotes.getLong(1));
                        note.setNotevalue(cursorForRelatedNotes.getInt(2));
                        note.setVelocity(cursorForRelatedNotes.getInt(3));
                        note.setLength(cursorForRelatedNotes.getFloat(4));
                        note.setPosition(cursorForRelatedNotes.getInt(5));
                        for (int i = 0; i < noteValuesArray.length; i++) {
                            // get actual note name (C3, D3, E3, etc.)
                            if (note.getNotevalue() == Integer.valueOf(noteValuesArray[i])) {
                                itemForList += noteLabelsArray[i] + " ";
                            }
                        }
                    } while (cursorForRelatedNotes.moveToNext());
                }
                
                // add noteset string to list of strings
                //notesets.add(noteset.toString() + " " + itemForList);
                notesets.add(itemForList);
            } while (cursor.moveToNext());
        }

        return notesets;
    }
    
    /**
     * Get a list of all notesets ids.
     * 
     * @return List of Noteset ids.
     */
    public List<Long> getAllNotesetListDBTableIds() {
        List<Long> notesets = new LinkedList<Long>();
        
        String query = "SELECT " + OtashuDatabaseHelper.COLUMN_ID + " FROM " + OtashuDatabaseHelper.TABLE_NOTESETS;

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // select all notesets from database
        Cursor cursor = db.rawQuery(query, null);

        Noteset noteset = null;
        if (cursor.moveToFirst()) {
            do {                
                // create noteset objects based on noteset data from database
                noteset = new Noteset();
                noteset.setId(Long.parseLong(cursor.getString(0)));
                
                // add noteset to notesets list
                notesets.add(noteset.getId());
            } while (cursor.moveToNext());
        }

        return notesets;
    }
    
    public Noteset getNoteset(long id) {
        Noteset noteset = new Noteset();
        
        String query = "SELECT * FROM " + OtashuDatabaseHelper.TABLE_NOTESETS + " WHERE " + OtashuDatabaseHelper.COLUMN_ID + "=" + id;

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // select all notesets from database
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {                
                // create noteset objects based on noteset data from database
                noteset = new Noteset();
                noteset.setId(Integer.parseInt(cursor.getString(0)));
                noteset.setName(cursor.getString(1));
                noteset.setEmotion((cursor.getInt(2)));
            } while (cursor.moveToNext());
        }

        return noteset;
    }

    public Noteset updateNoteset(Noteset noteset) {
        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        
        ContentValues contentValues = new ContentValues();
        contentValues.put(OtashuDatabaseHelper.COLUMN_NAME,
                noteset.getName());
        contentValues.put(OtashuDatabaseHelper.COLUMN_EMOTION_ID,
                noteset.getEmotion());
        
        db.update(OtashuDatabaseHelper.TABLE_NOTESETS, contentValues, OtashuDatabaseHelper.COLUMN_ID + "=" + noteset.getId(), null);
        
        return noteset;
    }

    public int getCount() {
        int count = 0;
        
        String query = "SELECT " + OtashuDatabaseHelper.COLUMN_ID + " FROM " + OtashuDatabaseHelper.TABLE_NOTESETS;

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // select all notesets from database
        Cursor cursor = db.rawQuery(query, null);

        count = cursor.getCount();
        
        return count;
    }
}