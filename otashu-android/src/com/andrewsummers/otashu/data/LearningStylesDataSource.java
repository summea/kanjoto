
package com.andrewsummers.otashu.data;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.andrewsummers.otashu.model.LearningStyle;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class LearningStylesDataSource {
    private OtashuDatabaseHelper dbHelper;

    // database table columns
    private String[] allColumns = {
            OtashuDatabaseHelper.COLUMN_ID,
            OtashuDatabaseHelper.COLUMN_NAME,
    };

    /**
     * LearningStylesDataSource constructor.
     * 
     * @param context Current state.
     */
    public LearningStylesDataSource(Context context) {
        dbHelper = new OtashuDatabaseHelper(context);
    }

    /**
     * LearningStylesDataSource constructor.
     * 
     * @param context Current state.
     * @param databaseName Database to use.
     */
    public LearningStylesDataSource(Context context, String databaseName) {
        dbHelper = new OtashuDatabaseHelper(context, databaseName);
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
     * Create learningStyle row in database.
     * 
     * @param learningStylevalues String of learningStyle values to insert.
     * @return LearningStyle of newly-created learningStyle data.
     */
    public LearningStyle createLearningStyle(LearningStyle learningStyle) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(OtashuDatabaseHelper.COLUMN_NAME, learningStyle.getName());

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        long insertId = db
                .insert(OtashuDatabaseHelper.TABLE_LEARNING_STYLES, null,
                        contentValues);

        Cursor cursor = db.query(
                OtashuDatabaseHelper.TABLE_LEARNING_STYLES, allColumns,
                OtashuDatabaseHelper.COLUMN_ID + " = " + insertId, null,
                null, null, null);

        cursor.moveToFirst();

        LearningStyle newLearningStyle = cursorToLearningStyle(cursor);
        cursor.close();
        db.close();

        return newLearningStyle;
    }

    /**
     * Delete learningStyle row from database.
     * 
     * @param learningStyle LearningStyle to delete.
     */
    public void deleteLearningStyle(LearningStyle learningStyle) {
        long id = learningStyle.getId();

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // delete learningStyle
        db.delete(OtashuDatabaseHelper.TABLE_LEARNING_STYLES,
                OtashuDatabaseHelper.COLUMN_ID + " = " + id, null);

        db.close();
    }

    /**
     * Get all learningStyles from database table.
     * 
     * @return List of LearningStyles.
     */
    public List<LearningStyle> getAllLearningStyles() {
        List<LearningStyle> learningStyles = new ArrayList<LearningStyle>();

        String query = "SELECT * FROM " + OtashuDatabaseHelper.TABLE_LEARNING_STYLES;

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // select all notes from database
        Cursor cursor = db.rawQuery(query, null);

        LearningStyle learningStyle = null;
        if (cursor.moveToFirst()) {
            do {
                // create note objects based on note data from database
                learningStyle = new LearningStyle();
                learningStyle.setId(cursor.getLong(0));
                learningStyle.setName(cursor.getString(1));

                // add note string to list of strings
                learningStyles.add(learningStyle);
            } while (cursor.moveToNext());
        }

        db.close();

        return learningStyles;
    }

    /**
     * Access column data at current position of result.
     * 
     * @param cursor Current cursor location.
     * @return LearningStyle
     */
    private LearningStyle cursorToLearningStyle(Cursor cursor) {
        LearningStyle learningStyle = new LearningStyle();
        learningStyle.setId(cursor.getLong(0));
        learningStyle.setName(cursor.getString(1));
        return learningStyle;
    }

    /**
     * Get a list of all learningStyles ids.
     * 
     * @return List of LearningStyle ids.
     */
    public List<Long> getAllLearningStyleListDBTableIds() {
        List<Long> learningStyles = new LinkedList<Long>();

        String query = "SELECT " + OtashuDatabaseHelper.COLUMN_ID + " FROM "
                + OtashuDatabaseHelper.TABLE_LEARNING_STYLES;

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // select all learningStyles from database
        Cursor cursor = db.rawQuery(query, null);

        LearningStyle learningStyle = null;
        if (cursor.moveToFirst()) {
            do {
                // create learningStyle objects based on learningStyle data from database
                learningStyle = new LearningStyle();
                learningStyle.setId(cursor.getLong(0));

                // add learningStyle to learningStyles list
                learningStyles.add(learningStyle.getId());
            } while (cursor.moveToNext());
        }

        db.close();

        return learningStyles;
    }

    public LearningStyle getLearningStyle(long learningStyleId) {
        LearningStyle learningStyle = new LearningStyle();

        String query = "SELECT * FROM " + OtashuDatabaseHelper.TABLE_LEARNING_STYLES + " WHERE "
                + OtashuDatabaseHelper.COLUMN_ID + "=?";

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // select all learningStyles from database
        Cursor cursor = db.rawQuery(query, new String[] {
                String.valueOf(learningStyleId)
        });

        if (cursor.moveToFirst()) {
            do {
                // create learningStyle objects based on learningStyle data from database
                learningStyle = new LearningStyle();
                learningStyle.setId(cursor.getLong(0));
                learningStyle.setName(cursor.getString(1));
            } while (cursor.moveToNext());
        }

        db.close();

        return learningStyle;
    }

    public LearningStyle updateLearningStyle(LearningStyle learningStyle) {

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(OtashuDatabaseHelper.COLUMN_ID, learningStyle.getId());
        contentValues.put(OtashuDatabaseHelper.COLUMN_NAME, learningStyle.getName());

        db.update(OtashuDatabaseHelper.TABLE_LEARNING_STYLES, contentValues,
                OtashuDatabaseHelper.COLUMN_ID + "=" + learningStyle.getId(), null);

        db.close();

        return learningStyle;
    }
}
