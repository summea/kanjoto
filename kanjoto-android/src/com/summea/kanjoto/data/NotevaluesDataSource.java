
package com.summea.kanjoto.data;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import com.summea.kanjoto.model.Notevalue;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class NotevaluesDataSource {
    private SQLiteDatabase database;
    private KanjotoDatabaseHelper dbHelper;

    // database table columns
    private String[] allColumns = {
            KanjotoDatabaseHelper.COLUMN_ID,
            KanjotoDatabaseHelper.COLUMN_NOTEVALUE,
            KanjotoDatabaseHelper.COLUMN_NOTELABEL,
            KanjotoDatabaseHelper.COLUMN_LABEL_ID
    };

    /**
     * NotevaluesDataSource constructor.
     * 
     * @param context Current state.
     */
    public NotevaluesDataSource(Context context) {
        dbHelper = new KanjotoDatabaseHelper(context);
    }

    /**
     * NotevaluesDataSource constructor.
     * 
     * @param context Current state.
     * @param databaseName Database to use.
     */
    public NotevaluesDataSource(Context context, String databaseName) {
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
     * Create notevalue row in database.
     * 
     * @param notevaluevalues String of notevalue values to insert.
     * @return Notevalue of newly-created notevalue data.
     */
    public Notevalue createNotevalue(Notevalue notevalue) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(KanjotoDatabaseHelper.COLUMN_NOTEVALUE, notevalue.getNotevalue());
        contentValues.put(KanjotoDatabaseHelper.COLUMN_NOTELABEL, notevalue.getNotelabel());
        contentValues.put(KanjotoDatabaseHelper.COLUMN_LABEL_ID, notevalue.getLabelId());

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        long insertId = db.insert(KanjotoDatabaseHelper.TABLE_NOTEVALUES, null,
                contentValues);

        Cursor cursor = db.query(
                KanjotoDatabaseHelper.TABLE_NOTEVALUES, allColumns,
                KanjotoDatabaseHelper.COLUMN_ID + " = " + insertId, null,
                null, null, null);

        cursor.moveToFirst();
        Notevalue newNotevalue = cursorToNotevalue(cursor);
        cursor.close();
        db.close();

        return newNotevalue;
    }

    /**
     * Delete notevalue row from database.
     * 
     * @param notevalue Notevalue to delete.
     */
    public void deleteNotevalue(Notevalue notevalue) {
        long id = notevalue.getId();

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // delete notevalue
        db.delete(KanjotoDatabaseHelper.TABLE_NOTEVALUES,
                KanjotoDatabaseHelper.COLUMN_ID + " = " + id, null);

        db.close();
    }

    /**
     * Get all notevalues from database table.
     * 
     * @return List of Notevalues.
     */
    public List<Notevalue> getAllNotevalues() {
        List<Notevalue> notevalues = new ArrayList<Notevalue>();

        String query = "SELECT * FROM " + KanjotoDatabaseHelper.TABLE_NOTEVALUES;

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // select all notes from database
        Cursor cursor = db.rawQuery(query, null);

        Notevalue notevalue = null;
        if (cursor.moveToFirst()) {
            do {
                // create note objects based on note data from database
                notevalue = new Notevalue();
                notevalue.setId(cursor.getLong(0));
                notevalue.setNotevalue(cursor.getInt(1));
                notevalue.setNotelabel(cursor.getString(2));
                notevalue.setLabelId(cursor.getLong(3));

                // add note string to list of strings
                notevalues.add(notevalue);
            } while (cursor.moveToNext());
        }

        db.close();

        return notevalues;
    }

    /**
     * Access column data at current position of result.
     * 
     * @param cursor Current cursor location.
     * @return Notevalue
     */
    private Notevalue cursorToNotevalue(Cursor cursor) {
        Notevalue notevalue = new Notevalue();
        notevalue.setId(cursor.getLong(0));
        notevalue.setNotevalue(cursor.getInt(1));
        notevalue.setNotelabel(cursor.getString(2));
        notevalue.setLabelId(cursor.getLong(3));
        return notevalue;
    }

    /**
     * getAllNotevalues gets a preview list of all notevalues.
     * 
     * @return List of Notevalue preview strings.
     */
    public List<String> getAllNotevalueListPreviews() {
        List<String> notevalues = new LinkedList<String>();

        String query = "SELECT * FROM " + KanjotoDatabaseHelper.TABLE_NOTEVALUES;

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // select all notevalues from database
        Cursor cursor = db.rawQuery(query, null);

        Notevalue notevalue = null;
        if (cursor.moveToFirst()) {
            do {
                // create notevalue objects based on notevalue data from database
                notevalue = new Notevalue();
                notevalue.setId(cursor.getLong(0));
                notevalue.setNotevalue(cursor.getInt(1));
                notevalue.setNotelabel(cursor.getString(2));
                notevalue.setLabelId(cursor.getLong(3));

                // add notevalue string to list of strings
                notevalues.add(notevalue.toString());
            } while (cursor.moveToNext());
        }

        db.close();

        return notevalues;
    }

    /**
     * Get a list of all notevalue ids.
     * 
     * @return List of Notevalue ids.
     */
    public List<Long> getAllNotevalueListDBTableIds() {
        List<Long> notevalues = new LinkedList<Long>();

        String query = "SELECT " + KanjotoDatabaseHelper.COLUMN_ID + " FROM "
                + KanjotoDatabaseHelper.TABLE_NOTEVALUES;

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // select all notevalues from database
        Cursor cursor = db.rawQuery(query, null);

        Notevalue notevalue = null;
        if (cursor.moveToFirst()) {
            do {
                // create notevalue objects based on notevalue data from database
                notevalue = new Notevalue();
                notevalue.setId(Long.parseLong(cursor.getString(0)));

                // add notevalue to notevalues list
                notevalues.add(notevalue.getId());
            } while (cursor.moveToNext());
        }

        db.close();

        return notevalues;
    }

    public Notevalue getNotevalue(long notevalueId) {
        Notevalue notevalue = new Notevalue();

        String query = "SELECT * FROM " + KanjotoDatabaseHelper.TABLE_NOTEVALUES
                + " WHERE " + KanjotoDatabaseHelper.COLUMN_ID + "=?";

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // select all notevalues from database
        Cursor cursor = db.rawQuery(query, new String[] {
                String.valueOf(notevalueId)
        });

        if (cursor.moveToFirst()) {
            do {
                // create notevalue objects based on notevalue data from database
                notevalue = new Notevalue();
                notevalue.setId(cursor.getLong(0));
                notevalue.setNotevalue(cursor.getInt(1));
                notevalue.setNotelabel(cursor.getString(2));
                notevalue.setLabelId(cursor.getLong(3));
            } while (cursor.moveToNext());
        }

        db.close();

        return notevalue;
    }

    public Notevalue getNotevalueByNoteValue(int notevalueId) {
        Notevalue notevalue = new Notevalue();

        String query = "SELECT * FROM " + KanjotoDatabaseHelper.TABLE_NOTEVALUES + " WHERE "
                + KanjotoDatabaseHelper.COLUMN_NOTEVALUE + "=?";

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // select all notevalues from database
        Cursor cursor = db.rawQuery(query, new String[] {
                String.valueOf(notevalueId)
        });

        if (cursor.moveToFirst()) {
            do {
                // create notevalue objects based on notevalue data from database
                notevalue = new Notevalue();
                notevalue.setId(cursor.getLong(0));
                notevalue.setNotevalue(cursor.getInt(1));
                notevalue.setNotelabel(cursor.getString(2));
                notevalue.setLabelId(cursor.getLong(3));
            } while (cursor.moveToNext());
        }

        db.close();

        return notevalue;
    }

    public Notevalue updateNotevalue(Notevalue notevalue) {

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(KanjotoDatabaseHelper.COLUMN_ID, notevalue.getId());
        contentValues.put(KanjotoDatabaseHelper.COLUMN_NOTEVALUE, notevalue.getNotevalue());
        contentValues.put(KanjotoDatabaseHelper.COLUMN_NOTELABEL, notevalue.getNotelabel());
        contentValues.put(KanjotoDatabaseHelper.COLUMN_LABEL_ID, notevalue.getLabelId());

        db.update(KanjotoDatabaseHelper.TABLE_NOTEVALUES, contentValues,
                KanjotoDatabaseHelper.COLUMN_ID + "=" + notevalue.getId(), null);

        db.close();

        return notevalue;
    }

    public Notevalue getRandomNotevalue() {
        Notevalue notevalue = new Notevalue();

        // get all notevalues first
        List<Notevalue> allNotevalues = getAllNotevalues();

        // choose random notevalue
        int chosenIndex = new Random().nextInt(allNotevalues.size());

        notevalue = allNotevalues.get(chosenIndex);

        return notevalue;
    }

    public SQLiteDatabase getDatabase() {
        return database;
    }

    public void setDatabase(SQLiteDatabase database) {
        this.database = database;
    }
}
