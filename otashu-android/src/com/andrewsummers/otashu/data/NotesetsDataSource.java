
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

public class NotesetsDataSource {
    private OtashuDatabaseHelper dbHelper;

    // database table columns
    private String[] allColumns = {
            OtashuDatabaseHelper.COLUMN_ID,
            OtashuDatabaseHelper.COLUMN_NAME,
            OtashuDatabaseHelper.COLUMN_EMOTION_ID,
            OtashuDatabaseHelper.COLUMN_ENABLED,
            OtashuDatabaseHelper.COLUMN_APPRENTICE_ID,
    };

    /**
     * NotesetsDataSource constructor.
     * 
     * @param context Current state.
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
        dbHelper.getWritableDatabase();
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
     * @param notevalues String of note values to insert.
     * @return Noteset of newly-created noteset data.
     */
    public Noteset createNoteset(Noteset noteset) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(OtashuDatabaseHelper.COLUMN_NAME,
                noteset.getName());
        contentValues.put(OtashuDatabaseHelper.COLUMN_EMOTION_ID,
                noteset.getEmotion());
        contentValues.put(OtashuDatabaseHelper.COLUMN_ENABLED,
                noteset.getEnabled());
        contentValues.put(OtashuDatabaseHelper.COLUMN_APPRENTICE_ID,
                noteset.getApprenticeId());

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        long insertId = db.insert(OtashuDatabaseHelper.TABLE_NOTESETS, null,
                contentValues);

        Cursor cursor = db.query(
                OtashuDatabaseHelper.TABLE_NOTESETS, allColumns,
                OtashuDatabaseHelper.COLUMN_ID + " = " + insertId, null,
                null, null, null);

        cursor.moveToFirst();
        Noteset newNoteset = cursorToNoteset(cursor);
        cursor.close();
        db.close();
        
        return newNoteset;
    }

    /**
     * Delete noteset row from database.
     * 
     * @param noteset Noteset to delete.
     */
    public void deleteNoteset(Noteset noteset) {
        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // delete noteset
        db.delete(OtashuDatabaseHelper.TABLE_NOTESETS,
                OtashuDatabaseHelper.COLUMN_ID + " = " + noteset.getId(), null);

        // delete related notes
        db.delete(OtashuDatabaseHelper.TABLE_NOTES,
                OtashuDatabaseHelper.COLUMN_NOTESET_ID + " = " + noteset.getId(), null);
        
        db.close();
    }

    /**
     * Get all notesets from database table.
     * 
     * @return List of Notesets.
     */
    public List<Noteset> getAllNotesets(long apprenticeId) {
        List<Noteset> notesets = new ArrayList<Noteset>();

        String query = "SELECT * FROM " + OtashuDatabaseHelper.TABLE_NOTESETS
                + " WHERE " + OtashuDatabaseHelper.COLUMN_APPRENTICE_ID + "=?";

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // select all notes from database
        Cursor cursor = db.rawQuery(query, new String[] {
                String.valueOf(apprenticeId)
        });

        Noteset noteset = null;
        if (cursor.moveToFirst()) {
            do {
                // create note objects based on note data from database
                noteset = new Noteset();
                noteset.setId(Long.parseLong(cursor.getString(0)));
                noteset.setName(cursor.getString(1));
                noteset.setEmotion(cursor.getInt(2));
                noteset.setEnabled(cursor.getInt(3));
                noteset.setApprenticeId(cursor.getLong(4));

                // add note string to list of strings
                notesets.add(noteset);
            } while (cursor.moveToNext());
        }
        
        db.close();

        return notesets;
    }

    /**
     * Get all notesets from database table.
     * 
     * @return List of Notesets.
     */
    public List<Noteset> getAllNotesets(long apprenticeId, int limit, int offset) {
        List<Noteset> notesets = new ArrayList<Noteset>();

        String query = "SELECT * FROM " + OtashuDatabaseHelper.TABLE_NOTESETS
                + " WHERE " + OtashuDatabaseHelper.COLUMN_APPRENTICE_ID + "=?";
        if (limit > 0)
            query += " LIMIT " + limit;
        if (offset > 0)
            query += " OFFSET " + offset;

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // select all notes from database
        Cursor cursor = db.rawQuery(query, new String[] {
                String.valueOf(apprenticeId)
        });

        Noteset noteset = null;
        if (cursor.moveToFirst()) {
            do {
                // create note objects based on note data from database
                noteset = new Noteset();
                noteset.setId(Long.parseLong(cursor.getString(0)));
                noteset.setName(cursor.getString(1));
                noteset.setEmotion(cursor.getInt(2));
                noteset.setEnabled(cursor.getInt(3));
                noteset.setApprenticeId(cursor.getLong(4));

                // add note string to list of strings
                notesets.add(noteset);
            } while (cursor.moveToNext());
        }
        
        db.close();

        return notesets;
    }

    /**
     * Get all notesets bundles from database table.
     * 
     * @return List of noteset bundles.
     */
    public SparseArray<List<Note>> getAllNotesetBundles(long apprenticeId) {
        SparseArray<List<Note>> notesetBundles = new SparseArray<List<Note>>();

        String query = "SELECT * FROM " + OtashuDatabaseHelper.TABLE_NOTESETS
                + " WHERE " + OtashuDatabaseHelper.COLUMN_APPRENTICE_ID + "=?";

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // select all notesets from database
        Cursor cursor = db.rawQuery(query, new String[] {
                String.valueOf(apprenticeId)
        });

        if (cursor.moveToFirst()) {
            do {
                Integer notesetId = Integer.parseInt(cursor.getString(0));

                // get all related notes inside this noteset
                // TODO: make this query approach more efficient at some point, if necessary
                String queryForRelatedNotes = "SELECT * FROM " + OtashuDatabaseHelper.TABLE_NOTES
                        + " WHERE " + OtashuDatabaseHelper.COLUMN_NOTESET_ID + "=?";
                
                Cursor cursorForRelatedNotes = db.rawQuery(queryForRelatedNotes, new String[] {
                        String.valueOf(notesetId)
                });

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
        
        db.close();

        return notesetBundles;
    }

    /**
     * Get all notesets bundles from database table.
     * 
     * @return List of noteset bundles.
     */
    public SparseArray<List<Note>> getAllNotesetBundlesByEmotion(long apprenticeId, long emotion_id) {
        SparseArray<List<Note>> notesetBundles = new SparseArray<List<Note>>();

        String query = "SELECT * FROM " + OtashuDatabaseHelper.TABLE_NOTESETS
                + " WHERE " + OtashuDatabaseHelper.COLUMN_APPRENTICE_ID + "=?"
                + " AND " + OtashuDatabaseHelper.COLUMN_EMOTION_ID + "=?"
                + " AND " + OtashuDatabaseHelper.COLUMN_ENABLED + "=?";

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // select all notesets from database
        Cursor cursor = db.rawQuery(query, new String[] {
                String.valueOf(apprenticeId),
                String.valueOf(emotion_id),
                String.valueOf(1),
        });

        if (cursor.moveToFirst()) {
            do {
                Integer notesetId = Integer.parseInt(cursor.getString(0));

                // get all related notes inside this noteset
                // TODO: make this query approach more efficient at some point, if necessary
                String queryForRelatedNotes = "SELECT * FROM " + OtashuDatabaseHelper.TABLE_NOTES
                        + " WHERE " + OtashuDatabaseHelper.COLUMN_NOTESET_ID + "=?";
                
                Cursor cursorForRelatedNotes = db.rawQuery(queryForRelatedNotes, new String[] {
                        String.valueOf(notesetId)
                });

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
        
        db.close();

        return notesetBundles;
    }

    public SparseArray<List<Note>> getNotesetBundleByNotesetId(long notesetId) {
        SparseArray<List<Note>> notesetBundle = new SparseArray<List<Note>>();

        String query = "SELECT * FROM " + OtashuDatabaseHelper.TABLE_NOTESETS
                + " WHERE " + OtashuDatabaseHelper.COLUMN_ID + "=?";

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // select all notesets from database
        Cursor cursor = db.rawQuery(query, new String[] {
                String.valueOf(notesetId)
        });

        if (cursor.moveToFirst()) {
            do {
                Integer nsId = Integer.parseInt(cursor.getString(0));

                // get all related notes inside this noteset
                // TODO: make this query approach more efficient at some point, if necessary
                String queryForRelatedNotes = "SELECT * FROM " + OtashuDatabaseHelper.TABLE_NOTES
                        + " WHERE " + OtashuDatabaseHelper.COLUMN_NOTESET_ID + "=?";
                
                Cursor cursorForRelatedNotes = db.rawQuery(queryForRelatedNotes, new String[] {
                        String.valueOf(notesetId)
                });

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

                notesetBundle.put(nsId, notes);

            } while (cursor.moveToNext());
        }
        
        db.close();

        return notesetBundle;
    }

    public HashMap<String, List<Object>> getNotesetBundleDetailById(long id) {
        HashMap<String, List<Object>> notesetBundle = new HashMap<String, List<Object>>();

        String query = "SELECT * FROM " + OtashuDatabaseHelper.TABLE_NOTESETS
                + " WHERE " + OtashuDatabaseHelper.COLUMN_ID + "=?";

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // select all notesets from database
        Cursor cursor = db.rawQuery(query, new String[] {
                String.valueOf(id)
        });

        if (cursor.moveToFirst()) {
            do {
                Noteset noteset = new Noteset();
                noteset.setId(cursor.getInt(0));
                noteset.setName(cursor.getString(1));
                noteset.setEmotion(cursor.getInt(2));
                noteset.setEnabled(cursor.getInt(3));
                noteset.setApprenticeId(cursor.getLong(4));

                List<Object> notesets = new LinkedList<Object>();
                notesets.add(noteset);

                notesetBundle.put("noteset", notesets);

                // get all related notes inside this noteset
                // TODO: make this query approach more efficient at some point, if necessary
                String queryForRelatedNotes = "SELECT * FROM " + OtashuDatabaseHelper.TABLE_NOTES
                        + " WHERE " + OtashuDatabaseHelper.COLUMN_NOTESET_ID + "=?";
                
                Cursor cursorForRelatedNotes = db.rawQuery(queryForRelatedNotes, new String[] {
                        String.valueOf(id)
                });

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
        
        db.close();

        return notesetBundle;
    }

    /**
     * Access column data at current position of result.
     * 
     * @param cursor Current cursor location.
     * @return Noteset
     */
    private Noteset cursorToNoteset(Cursor cursor) {
        Noteset noteset = new Noteset();
        noteset.setId(cursor.getLong(0));
        noteset.setName(cursor.getString(1));
        noteset.setEmotion(cursor.getInt(2));
        noteset.setEnabled(cursor.getInt(3));
        noteset.setApprenticeId(cursor.getLong(4));
        return noteset;
    }

    /**
     * getAllNotesets gets a preview list of all notesets.
     * 
     * @return List of Noteset preview strings.
     */
    public List<String> getAllNotesetListPreviews(long apprenticeId) {
        List<String> notesets = new LinkedList<String>();

        String query = "SELECT * FROM " + OtashuDatabaseHelper.TABLE_NOTESETS
                + " WHERE " + OtashuDatabaseHelper.COLUMN_APPRENTICE_ID + "=?";

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // select all notesets from database
        Cursor cursor = db.rawQuery(query, new String[] {
                String.valueOf(apprenticeId)
        });

        Noteset noteset = null;
        if (cursor.moveToFirst()) {
            do {
                String itemForList = "";

                // create noteset objects based on noteset data from database
                noteset = new Noteset();
                noteset.setId(Integer.parseInt(cursor.getString(0)));
                noteset.setName(cursor.getString(1));
                noteset.setEmotion((cursor.getInt(2)));
                noteset.setEnabled(cursor.getInt(3));
                noteset.setApprenticeId(cursor.getLong(4));

                // get all related notes inside this noteset
                // TODO: make this query approach more efficient at some point, if necessary
                String queryForRelatedNotes = "SELECT * FROM " + OtashuDatabaseHelper.TABLE_NOTES
                        + " WHERE " + OtashuDatabaseHelper.COLUMN_NOTESET_ID + "=?";
                
                Cursor cursorForRelatedNotes = db.rawQuery(queryForRelatedNotes, new String[] {
                        String.valueOf(noteset.getId())
                });

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
                // notesets.add(noteset.toString() + " " + itemForList);
                notesets.add(itemForList);
            } while (cursor.moveToNext());
        }
        
        db.close();

        return notesets;
    }

    public Noteset getNoteset(long id) {
        Noteset noteset = new Noteset();

        String query = "SELECT * FROM " + OtashuDatabaseHelper.TABLE_NOTESETS
                + " WHERE " + OtashuDatabaseHelper.COLUMN_ID + "=?";

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // select all notesets from database
        Cursor cursor = db.rawQuery(query, new String[] {
                String.valueOf(id)
        });

        if (cursor.moveToFirst()) {
            do {
                // create noteset objects based on noteset data from database
                noteset = new Noteset();
                noteset.setId(cursor.getInt(0));
                noteset.setName(cursor.getString(1));
                noteset.setEmotion((cursor.getInt(2)));
                noteset.setEnabled(cursor.getInt(3));
                noteset.setApprenticeId(cursor.getLong(4));
            } while (cursor.moveToNext());
        }
        
        db.close();

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
        contentValues.put(OtashuDatabaseHelper.COLUMN_ENABLED,
                noteset.getEnabled());
        contentValues.put(OtashuDatabaseHelper.COLUMN_APPRENTICE_ID,
                noteset.getApprenticeId());

        db.update(OtashuDatabaseHelper.TABLE_NOTESETS, contentValues,
                OtashuDatabaseHelper.COLUMN_ID + "=" + noteset.getId(), null);

        db.close();
        
        return noteset;
    }

    public int getCount(long apprenticeId) {
        int count = 0;

        String query = "SELECT " + OtashuDatabaseHelper.COLUMN_ID + " FROM "
                + OtashuDatabaseHelper.TABLE_NOTESETS
                + " WHERE " + OtashuDatabaseHelper.COLUMN_APPRENTICE_ID + "=?";

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // select all notesets from database
        Cursor cursor = db.rawQuery(query, new String[] {
                String.valueOf(apprenticeId)
        });

        count = cursor.getCount();
        
        db.close();

        return count;
    }

    /**
     * Get all notesets from database table by emotion.
     * 
     * @return List of notesets.
     */
    public List<Noteset> getAllNotesetsByEmotion(long emotionId) {
        List<Noteset> notesets = new ArrayList<Noteset>();

        String query = "SELECT * FROM " + OtashuDatabaseHelper.TABLE_NOTESETS
                + " WHERE " + OtashuDatabaseHelper.COLUMN_EMOTION_ID + "=?";

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // select all notesets from database
        Cursor cursor = db.rawQuery(query, new String[] {
                String.valueOf(emotionId)
        });

        if (cursor.moveToFirst()) {
            do {
                // create noteset objects based on noteset data from database
                Noteset noteset = new Noteset();
                noteset.setId(cursor.getInt(0));
                noteset.setName(cursor.getString(1));
                noteset.setEmotion((cursor.getInt(2)));
                noteset.setEnabled(cursor.getInt(3));
                noteset.setApprenticeId(cursor.getLong(4));
                notesets.add(noteset);
            } while (cursor.moveToNext());
        }
        
        db.close();

        return notesets;
    }
}
