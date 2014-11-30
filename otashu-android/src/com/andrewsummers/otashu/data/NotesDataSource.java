package com.andrewsummers.otashu.data;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.andrewsummers.otashu.model.Note;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

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
     * @param context
     *            Current state.
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
     * @param notevalues
     *            String of note values to insert.
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
     * @param note
     *            Note to delete.
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

        String query = "SELECT * FROM " + OtashuDatabaseHelper.TABLE_NOTES + " WHERE " + OtashuDatabaseHelper.COLUMN_NOTESET_ID + "=" + notesetId;

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
     * Access column data at current position of result.
     * 
     * @param cursor
     *            Current cursor location.
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

        db.update(OtashuDatabaseHelper.TABLE_NOTES, contentValues, OtashuDatabaseHelper.COLUMN_ID + "=" + note.getId(), null);

        return note;
    }
}