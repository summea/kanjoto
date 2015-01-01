
package com.andrewsummers.otashu.data;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.andrewsummers.otashu.model.Note;
import com.andrewsummers.otashu.model.NotesetAndRelated;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.util.SparseArray;

public class NotesDataSource {
    private SQLiteDatabase database;
    private OtashuDatabaseHelper dbHelper;

    // database table columns
    private String[] allColumns = {
            OtashuDatabaseHelper.COLUMN_ID,
            OtashuDatabaseHelper.COLUMN_NOTESET_ID,
            OtashuDatabaseHelper.COLUMN_NOTEVALUE,
            OtashuDatabaseHelper.COLUMN_VELOCITY,
            OtashuDatabaseHelper.COLUMN_LENGTH,
            OtashuDatabaseHelper.COLUMN_POSITION
    };

    /**
     * NotesDataSource constructor.
     * 
     * @param context Current state.
     */
    public NotesDataSource(Context context) {
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
     * Create note row in database.
     * 
     * @param notevalues String of note values to insert.
     * @return Note of newly-created note data.
     */
    public Note createNote(Note note) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(OtashuDatabaseHelper.COLUMN_NOTESET_ID, note.getNotesetId());
        contentValues.put(OtashuDatabaseHelper.COLUMN_NOTEVALUE, note.getNotevalue());
        contentValues.put(OtashuDatabaseHelper.COLUMN_VELOCITY, note.getVelocity());
        contentValues.put(OtashuDatabaseHelper.COLUMN_LENGTH, note.getLength());
        contentValues.put(OtashuDatabaseHelper.COLUMN_POSITION, note.getPosition());

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        long insertId = db.insert(OtashuDatabaseHelper.TABLE_NOTES, null,
                contentValues);

        Cursor cursor = db.query(
                OtashuDatabaseHelper.TABLE_NOTES, allColumns,
                OtashuDatabaseHelper.COLUMN_ID + " = " + insertId, null,
                null, null, null);

        cursor.moveToFirst();
        Note newNote = cursorToNote(cursor);
        cursor.close();
        return newNote;
    }

    /**
     * Delete note row from database.
     * 
     * @param note Note to delete.
     */
    public void deleteNote(Note note) {
        long id = note.getId();
        database.delete(OtashuDatabaseHelper.TABLE_NOTESETS,
                OtashuDatabaseHelper.COLUMN_ID + " = " + id, null);
    }

    /**
     * Get all notes from database table.
     * 
     * @return List of Notes.
     */
    public List<Note> getAllNotes() {
        List<Note> notes = new ArrayList<Note>();

        String query = "SELECT * FROM " + OtashuDatabaseHelper.TABLE_NOTES;

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // select all notes from database
        Cursor cursor = db.rawQuery(query, null);

        Note note = null;
        if (cursor.moveToFirst()) {
            do {
                // create note objects based on note data from database
                note = new Note();
                note.setId(Integer.parseInt(cursor.getString(0)));
                note.setNotesetId(cursor.getLong(1));
                note.setNotevalue(cursor.getInt(2));
                note.setVelocity(cursor.getInt(3));
                note.setLength(cursor.getFloat(4));
                note.setPosition(cursor.getInt(5));

                // add note string to list of strings
                notes.add(note);
            } while (cursor.moveToNext());
        }

        return notes;
    }

    /**
     * Get all notes with specific noteset_id from database table.
     * 
     * @return List of Notes.
     */
    public List<Note> getAllNotes(long notesetId) {
        List<Note> notes = new ArrayList<Note>();

        String query = "SELECT * FROM " + OtashuDatabaseHelper.TABLE_NOTES + " WHERE "
                + OtashuDatabaseHelper.COLUMN_NOTESET_ID + "=" + notesetId;

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // select all notes from database
        Cursor cursor = db.rawQuery(query, null);

        Note note = null;
        if (cursor.moveToFirst()) {
            do {
                // create note objects based on note data from database
                note = new Note();
                note.setId(Integer.parseInt(cursor.getString(0)));
                note.setNotesetId(cursor.getLong(1));
                note.setNotevalue(cursor.getInt(2));
                note.setVelocity(cursor.getInt(3));
                note.setLength(cursor.getFloat(4));
                note.setPosition(cursor.getInt(5));

                // add note string to list of strings
                notes.add(note);
            } while (cursor.moveToNext());
        }

        return notes;
    }

    /**
     * Does a noteset with the given list of notes already exist?
     * 
     * @param notesetAndRelatedToCheck Noteset information to check
     * @return boolean of noteset existence status
     */
    public boolean doesNotesetExist(NotesetAndRelated notesetAndRelatedToCheck) {
        boolean notesetExists = false;
        SparseArray<Set<Long>> foundNotes = new SparseArray<Set<Long>>();

        long parentId = 0;

        if (notesetAndRelatedToCheck.getNotes().get(0) != null) {
            parentId = notesetAndRelatedToCheck.getNotes().get(0).getNotesetId();
        }

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        for (int i = 0; i < notesetAndRelatedToCheck.getNotes().size(); i++) {
            // TODO: check position with i

            // TODO: check all possible note sequences
            // and narrow down as we move through the note positions
            Log.d("MYLOG", "checking note " + i + " in list...");
            String query = "SELECT " + OtashuDatabaseHelper.COLUMN_NOTESET_ID + ", "
                    + OtashuDatabaseHelper.COLUMN_NOTEVALUE + " FROM "
                    + OtashuDatabaseHelper.TABLE_NOTES + " WHERE "
                    + OtashuDatabaseHelper.COLUMN_NOTEVALUE + "="
                    + notesetAndRelatedToCheck.getNotes().get(i) + " AND "
                    + OtashuDatabaseHelper.COLUMN_POSITION + "=" + (i + 1);

            Log.d("MYLOG", "query: " + query);

            // select all notes from database
            Cursor cursor = db.rawQuery(query, null);

            if (cursor.moveToFirst()) {
                do {
                    if (cursor.getLong(0) > 0) {
                        Set<Long> notesetIds = new HashSet<Long>();
                        if (foundNotes.get(i + 1) != null) {
                            notesetIds = foundNotes.get(i + 1);
                        }

                        notesetIds.add(cursor.getLong(0));

                        foundNotes.put(i + 1, notesetIds);
                    }
                } while (cursor.moveToNext());
            } else {
                foundNotes.put(i + 1, new HashSet<Long>());
            }
        }

        Set<Long> foundSet = new HashSet<Long>();
        foundSet.addAll(foundNotes.get(1));
        
        for (int i = 0; i < foundNotes.size(); i++) {
            int key = foundNotes.keyAt(i);
            foundSet.retainAll(foundNotes.get(key));
            Log.d("MYLOG", "checking " + key + ": " + foundNotes.get(key));
        }

        Log.d("MYLOG", "noteset_ids across the table: " + foundSet.toString());

        foundSet.remove(parentId);

        Log.d("MYLOG", "actual noteset_ids across the table: " + foundSet.toString());
        Log.d("MYLOG", "notes we are checking: " + notesetAndRelatedToCheck.getNotes().toString());

        if (foundSet.size() > 0) {

            for (Long id : foundSet) {
                String query = "SELECT * FROM "
                        + OtashuDatabaseHelper.TABLE_NOTESETS + " WHERE "
                        + OtashuDatabaseHelper.COLUMN_ID + "=" + id;

                Log.d("MYLOG", "query: " + query);

                // select all notes from database
                Cursor cursor = db.rawQuery(query, null);

                if (cursor.moveToFirst()) {
                    do {
                        if (cursor.getLong(0) > 0) {
                            if (cursor.getLong(2) == notesetAndRelatedToCheck.getNoteset()
                                    .getEmotion()) {
                                notesetExists = true;
                                break;
                            }
                        }
                    } while (cursor.moveToNext());
                }
            }

        }

        Log.d("MYLOG", "found noteset? " + notesetExists);

        return notesetExists;
    }

    /**
     * Access column data at current position of result.
     * 
     * @param cursor Current cursor location.
     * @return Note
     */
    private Note cursorToNote(Cursor cursor) {
        Note note = new Note();
        note.setId(cursor.getLong(0));
        note.setNotevalue(cursor.getInt(1));
        note.setVelocity(cursor.getInt(2));
        note.setLength(cursor.getFloat(3));
        note.setPosition(cursor.getInt(4));
        return note;
    }

    /**
     * getAllNotes gets a preview list of all notes.
     * 
     * @return List of Note preview strings.
     */
    public List<String> getAllNoteListPreviews() {
        List<String> notes = new LinkedList<String>();

        String query = "SELECT * FROM " + OtashuDatabaseHelper.TABLE_NOTES;

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // select all notes from database
        Cursor cursor = db.rawQuery(query, null);

        Note note = null;
        if (cursor.moveToFirst()) {
            do {
                // create note objects based on note data from database
                note = new Note();
                note.setId(Integer.parseInt(cursor.getString(0)));
                note.setNotevalue(cursor.getInt(1));
                note.setVelocity(cursor.getInt(2));
                note.setLength(cursor.getFloat(3));
                note.setPosition(cursor.getInt(4));

                // add note string to list of strings
                notes.add(note.toString());
            } while (cursor.moveToNext());
        }

        return notes;
    }

    public Note updateNote(Note note) {

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(OtashuDatabaseHelper.COLUMN_NOTESET_ID, note.getNotesetId());
        contentValues.put(OtashuDatabaseHelper.COLUMN_NOTEVALUE, note.getNotevalue());
        contentValues.put(OtashuDatabaseHelper.COLUMN_VELOCITY, note.getVelocity());
        contentValues.put(OtashuDatabaseHelper.COLUMN_LENGTH, note.getLength());
        contentValues.put(OtashuDatabaseHelper.COLUMN_POSITION, note.getPosition());

        db.update(OtashuDatabaseHelper.TABLE_NOTES, contentValues, OtashuDatabaseHelper.COLUMN_ID
                + "=" + note.getId(), null);

        return note;
    }
}
