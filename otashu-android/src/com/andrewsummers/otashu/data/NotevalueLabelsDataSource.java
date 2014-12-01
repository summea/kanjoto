package com.andrewsummers.otashu.data;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import com.andrewsummers.otashu.model.NotevalueLabel;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class NotevalueLabelsDataSource {
    private SQLiteDatabase database;
    private OtashuDatabaseHelper dbHelper;

    // database table columns
    private String[] allColumns = {
            OtashuDatabaseHelper.COLUMN_ID,
            OtashuDatabaseHelper.COLUMN_NOTEVALUE,
            OtashuDatabaseHelper.COLUMN_LABEL_ID
    };

    /**
     * NoteLabelsDataSource constructor.
     * 
     * @param context
     *            Current state.
     */
    public NotevalueLabelsDataSource(Context context) {
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
     * Create notevalueLabel row in database.
     * 
     * @param notevalueLabelvalues
     *            String of notevalueLabel values to insert.
     * @return NoteLabel of newly-created notevalueLabel data.
     */
    public NotevalueLabel createNoteLabel(NotevalueLabel notevalueLabel) {        
        ContentValues contentValues = new ContentValues();
        contentValues.put(OtashuDatabaseHelper.COLUMN_NOTEVALUE, notevalueLabel.getNotevalue());
        contentValues.put(OtashuDatabaseHelper.COLUMN_LABEL_ID, notevalueLabel.getLabelId());

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        
        long insertId = db.insert(OtashuDatabaseHelper.TABLE_NOTEVALUE_LABELS, null,
                        contentValues);

        Cursor cursor = db.query(
                OtashuDatabaseHelper.TABLE_NOTEVALUE_LABELS, allColumns,
                OtashuDatabaseHelper.COLUMN_ID + " = " + insertId, null,
                null, null, null);

        cursor.moveToFirst();
        NotevalueLabel newNoteLabel = cursorToNoteLabel(cursor);
        cursor.close();
        return newNoteLabel;
    }

    /**
     * Delete notevalueLabel row from database.
     * 
     * @param notevalueLabel
     *            NoteLabel to delete.
     */
    public void deleteNoteLabel(NotevalueLabel notevalueLabel) {
        long id = notevalueLabel.getId();
        
        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        
        // delete notevalueLabel
        db.delete(OtashuDatabaseHelper.TABLE_NOTEVALUE_LABELS,
                OtashuDatabaseHelper.COLUMN_ID + " = " + id, null);
    }

    /**
     * Get all notevalueLabels from database table.
     * 
     * @return List of NoteLabels.
     */
    public List<NotevalueLabel> getAllNoteLabels() {
        List<NotevalueLabel> notevalueLabels = new ArrayList<NotevalueLabel>();

        String query = "SELECT * FROM " + OtashuDatabaseHelper.TABLE_NOTEVALUE_LABELS;

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // select all notes from database
        Cursor cursor = db.rawQuery(query, null);

        NotevalueLabel notevalueLabel = null;
        if (cursor.moveToFirst()) {
            do {
                // create note objects based on note data from database
                notevalueLabel = new NotevalueLabel();
                notevalueLabel.setId(cursor.getLong(0));
                notevalueLabel.setNotevalue(cursor.getInt(1));
                notevalueLabel.setLabelId(cursor.getLong(2));

                // add note string to list of strings
                notevalueLabels.add(notevalueLabel);
            } while (cursor.moveToNext());
        }

        return notevalueLabels;
    }

    /**
     * Access column data at current position of result.
     * 
     * @param cursor
     *            Current cursor location.
     * @return NoteLabel
     */
    private NotevalueLabel cursorToNoteLabel(Cursor cursor) {
        NotevalueLabel notevalueLabel = new NotevalueLabel();
        notevalueLabel.setId(cursor.getLong(0));
        notevalueLabel.setNotevalue(cursor.getInt(1));
        notevalueLabel.setLabelId(cursor.getLong(2));
        return notevalueLabel;
    }
        
    /**
     * getAllNoteLabels gets a preview list of all notevalueLabels.
     * 
     * @return List of NoteLabel preview strings.
     */
    public List<String> getAllNoteLabelListPreviews() {
        List<String> notevalueLabels = new LinkedList<String>();

        String query = "SELECT * FROM " + OtashuDatabaseHelper.TABLE_NOTEVALUE_LABELS;

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // select all notevalueLabels from database
        Cursor cursor = db.rawQuery(query, null);

        NotevalueLabel notevalueLabel = null;
        if (cursor.moveToFirst()) {
            do {
                // create notevalueLabel objects based on notevalueLabel data from database
                notevalueLabel = new NotevalueLabel();
                notevalueLabel.setId(cursor.getLong(0));
                notevalueLabel.setNotevalue(cursor.getInt(1));
                notevalueLabel.setLabelId(cursor.getLong(2));

                // add notevalueLabel string to list of strings
                notevalueLabels.add(notevalueLabel.toString());
            } while (cursor.moveToNext());
        }

        return notevalueLabels;
    }

    public NotevalueLabel getNoteLabel(long notevalueLabelId) {
        NotevalueLabel notevalueLabel = new NotevalueLabel();
        
        String query = "SELECT * FROM " + OtashuDatabaseHelper.TABLE_NOTEVALUE_LABELS + " WHERE " + OtashuDatabaseHelper.COLUMN_ID + "=" + notevalueLabelId;

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // select all notevalueLabels from database
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                // create notevalueLabel objects based on notevalueLabel data from database
                notevalueLabel = new NotevalueLabel();
                notevalueLabel.setId(cursor.getLong(0));
                notevalueLabel.setNotevalue(cursor.getInt(1));
                notevalueLabel.setLabelId(cursor.getLong(2));
            } while (cursor.moveToNext());
        }
        
        return notevalueLabel;
    }
    
    public NotevalueLabel getNoteLabelByNoteValue(int noteValueId) {
        NotevalueLabel notevalueLabel = new NotevalueLabel();
        
        String query = "SELECT * FROM " + OtashuDatabaseHelper.TABLE_NOTEVALUE_LABELS + " WHERE " + OtashuDatabaseHelper.COLUMN_NOTEVALUE + "=" + noteValueId;

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // select all notevalueLabels from database
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                // create notevalueLabel objects based on notevalueLabel data from database
                notevalueLabel = new NotevalueLabel();
                notevalueLabel.setId(cursor.getLong(0));
                notevalueLabel.setNotevalue(cursor.getInt(1));
                notevalueLabel.setLabelId(cursor.getLong(2));
            } while (cursor.moveToNext());
        }
        
        return notevalueLabel;
    }
    
    public NotevalueLabel updateNoteLabel(NotevalueLabel notevalueLabel) {
        
        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        
        ContentValues contentValues = new ContentValues();
        contentValues.put(OtashuDatabaseHelper.COLUMN_ID, notevalueLabel.getId());
        contentValues.put(OtashuDatabaseHelper.COLUMN_NOTEVALUE, notevalueLabel.getNotevalue());
        contentValues.put(OtashuDatabaseHelper.COLUMN_LABEL_ID, notevalueLabel.getLabelId());
        
        db.update(OtashuDatabaseHelper.TABLE_NOTEVALUE_LABELS, contentValues, OtashuDatabaseHelper.COLUMN_ID + "=" + notevalueLabel.getId(), null);

        return notevalueLabel;
    }

    public NotevalueLabel getRandomNoteLabel() {
        NotevalueLabel notevalueLabel = new NotevalueLabel();
        
        // get all notevalueLabels first
        List<NotevalueLabel> allNoteLabels = getAllNoteLabels();

        // choose random notevalueLabel
        int chosenIndex = new Random().nextInt(allNoteLabels.size());        

        notevalueLabel = allNoteLabels.get(chosenIndex);
        
        return notevalueLabel;
    }
}