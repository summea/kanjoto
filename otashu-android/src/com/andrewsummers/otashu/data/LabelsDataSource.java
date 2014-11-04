package com.andrewsummers.otashu.data;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import com.andrewsummers.otashu.model.Label;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * LabelsDataSource is a data source that provides database functionality for
 * label-related data (e.g. CRUD) actions.
 * 
 * Label: Data source based on tutorial by vogella
 * http://www.vogella.com/tutorials/AndroidSQLite/article.html
 * Licensed under: CC BY-NC-SA 3.0 DE:
 * http://creativecommons.org/licenses/by-nc-sa/3.0/de/deed.en
 * Eclipse Public License: https://www.eclipse.org/legal/epl-v10.html
 */
public class LabelsDataSource {
    private SQLiteDatabase database;
    private OtashuDatabaseHelper dbHelper;

    // database table columns
    private String[] allColumns = {
            OtashuDatabaseHelper.COLUMN_ID,
            OtashuDatabaseHelper.COLUMN_NAME,
            OtashuDatabaseHelper.COLUMN_COLOR
    };

    /**
     * LabelsDataSource constructor.
     * 
     * @param context
     *            Current state.
     */
    public LabelsDataSource(Context context) {
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
     * Create label row in database.
     * 
     * @param labelvalues
     *            String of label values to insert.
     * @return Label of newly-created label data.
     */
    public Label createLabel(Label label) {        
        ContentValues contentValues = new ContentValues();
        contentValues.put(OtashuDatabaseHelper.COLUMN_NAME, label.getName());
        contentValues.put(OtashuDatabaseHelper.COLUMN_COLOR, label.getColor());

        long insertId = database
                .insert(OtashuDatabaseHelper.TABLE_LABELS, null,
                        contentValues);

        Cursor cursor = database.query(
                OtashuDatabaseHelper.TABLE_LABELS, allColumns,
                OtashuDatabaseHelper.COLUMN_ID + " = " + insertId, null,
                null, null, null);

        cursor.moveToFirst();
        Label newLabel = cursorToLabel(cursor);
        cursor.close();
        return newLabel;
    }

    /**
     * Delete label row from database.
     * 
     * @param label
     *            Label to delete.
     */
    public void deleteLabel(Label label) {
        long id = label.getId();
        
        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        
        // delete emotion
        Log.d("OTASHULOG", "deleting emotion with id: " + id);
        db.delete(OtashuDatabaseHelper.TABLE_LABELS,
                OtashuDatabaseHelper.COLUMN_ID + " = " + id, null);
    }

    /**
     * Get all labels from database table.
     * 
     * @return List of Labels.
     */
    public List<Label> getAllLabels() {
        List<Label> labels = new ArrayList<Label>();

        String query = "SELECT * FROM " + OtashuDatabaseHelper.TABLE_LABELS;

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // select all notes from database
        Cursor cursor = db.rawQuery(query, null);

        Label label = null;
        if (cursor.moveToFirst()) {
            do {
                // create note objects based on note data from database
                label = new Label();
                label.setId(Integer.parseInt(cursor.getString(0)));
                label.setName(cursor.getString(1));
                label.setColor(cursor.getString(2));

                // add note string to list of strings
                labels.add(label);
            } while (cursor.moveToNext());
        }

        return labels;
    }
    
    /**
     * Get all label ids from database table.
     * 
     * @return List of Labels ids.
     */
    public List<Integer> getAllLabelIds() {
        List<Integer> label_ids = new ArrayList<Integer>();

        Cursor cursor = database.query(
                OtashuDatabaseHelper.TABLE_LABELS, allColumns, null,
                null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Label label = cursorToLabel(cursor);
            label_ids.add((int) label.getId());
            cursor.moveToNext();
        }

        cursor.close();
        return label_ids;
    }

    /**
     * Access column data at current position of result.
     * 
     * @param cursor
     *            Current cursor location.
     * @return Label
     */
    private Label cursorToLabel(Cursor cursor) {
        Label label = new Label();
        label.setId(cursor.getLong(0));
        label.setName(cursor.getString(1));
        label.setColor(cursor.getString(2));
        return label;
    }
        
    /**
     * getAllLabels gets a preview list of all labels.
     * 
     * @return List of Label preview strings.
     */
    public List<String> getAllLabelListPreviews() {
        List<String> labels = new LinkedList<String>();

        String query = "SELECT * FROM " + OtashuDatabaseHelper.TABLE_LABELS;

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // select all labels from database
        Cursor cursor = db.rawQuery(query, null);

        Label label = null;
        if (cursor.moveToFirst()) {
            do {
                // create label objects based on label data from database
                label = new Label();
                label.setId(Integer.parseInt(cursor.getString(0)));
                label.setName(cursor.getString(1));
                label.setColor(cursor.getString(2));

                // add label string to list of strings
                labels.add(label.toString());
            } while (cursor.moveToNext());
        }

        Log.d("MYLOG", labels.toString());

        return labels;
    }

    /**
     * Get a list of all labels ids.
     * 
     * @return List of Label ids.
     */
    public List<Long> getAllLabelListDBTableIds() {
        List<Long> labels = new LinkedList<Long>();
        
        String query = "SELECT " + OtashuDatabaseHelper.COLUMN_ID + " FROM " + OtashuDatabaseHelper.TABLE_LABELS;

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // select all emotions from database
        Cursor cursor = db.rawQuery(query, null);

        Label label = null;
        if (cursor.moveToFirst()) {
            do {                
                // create emotion objects based on emotion data from database
                label = new Label();
                label.setId(Long.parseLong(cursor.getString(0)));
                
                // add emotion to emotions list
                labels.add(label.getId());
            } while (cursor.moveToNext());
        }

        Log.d("MYLOG", labels.toString());

        return labels;
    }
    
    public Label getLabel(long labelId) {
        Label label = new Label();
        
        String query = "SELECT * FROM " + OtashuDatabaseHelper.TABLE_LABELS + " WHERE " + OtashuDatabaseHelper.COLUMN_ID + "=" + labelId;

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // select all labels from database
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                // create label objects based on label data from database
                label = new Label();
                label.setId(Integer.parseInt(cursor.getString(0)));
                label.setName(cursor.getString(1));
                label.setColor(cursor.getString(2));
            } while (cursor.moveToNext());
        }
        
        return label;
    }
    
    public Label updateLabel(Label label) {
        
        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        
        ContentValues contentValues = new ContentValues();
        contentValues.put(OtashuDatabaseHelper.COLUMN_ID, label.getId());
        contentValues.put(OtashuDatabaseHelper.COLUMN_NAME, label.getName());
        contentValues.put(OtashuDatabaseHelper.COLUMN_COLOR, label.getColor());
        
        Log.d("MYLOG", "sql update: " + OtashuDatabaseHelper.COLUMN_ID + "=" + label.getId());
        
        db.update(OtashuDatabaseHelper.TABLE_LABELS, contentValues, OtashuDatabaseHelper.COLUMN_ID + "=" + label.getId(), null);

        return label;
    }

    public Label getRandomLabel() {
        Label label = new Label();
        
        // get all labels first
        List<Label> allLabels = getAllLabels();

        // choose random label
        int chosenIndex = new Random().nextInt(allLabels.size());        

        label = allLabels.get(chosenIndex);
        
        return label;
    }
}