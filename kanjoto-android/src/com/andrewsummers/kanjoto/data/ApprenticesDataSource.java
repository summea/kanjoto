
package com.andrewsummers.kanjoto.data;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.andrewsummers.kanjoto.model.Apprentice;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class ApprenticesDataSource {
    private SQLiteDatabase database;
    private KanjotoDatabaseHelper dbHelper;

    // database table columns
    private String[] allColumns = {
            KanjotoDatabaseHelper.COLUMN_ID,
            KanjotoDatabaseHelper.COLUMN_NAME,
            KanjotoDatabaseHelper.COLUMN_LEARNING_STYLE_ID,
    };

    /**
     * ApprenticesDataSource constructor.
     * 
     * @param context Current state.
     */
    public ApprenticesDataSource(Context context) {
        dbHelper = new KanjotoDatabaseHelper(context);
    }

    /**
     * ApprenticesDataSource constructor.
     * 
     * @param context Current state.
     * @param databaseName Database to use.
     */
    public ApprenticesDataSource(Context context, String databaseName) {
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
     * Create apprentice row in database.
     * 
     * @param apprenticevalues String of apprentice values to insert.
     * @return Apprentice of newly-created apprentice data.
     */
    public Apprentice createApprentice(Apprentice apprentice) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(KanjotoDatabaseHelper.COLUMN_NAME, apprentice.getName());
        contentValues.put(KanjotoDatabaseHelper.COLUMN_LEARNING_STYLE_ID,
                apprentice.getLearningStyleId());

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        long insertId = db.insert(KanjotoDatabaseHelper.TABLE_APPRENTICES, null,
                contentValues);

        Cursor cursor = db.query(
                KanjotoDatabaseHelper.TABLE_APPRENTICES, allColumns,
                KanjotoDatabaseHelper.COLUMN_ID + " = " + insertId, null,
                null, null, null);

        cursor.moveToFirst();
        Apprentice newApprentice = cursorToApprentice(cursor);
        cursor.close();
        db.close();

        return newApprentice;
    }

    /**
     * Delete apprentice row from database.
     * 
     * @param apprentice Apprentice to delete.
     */
    public void deleteApprentice(Apprentice apprentice) {
        long id = apprentice.getId();

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // delete apprentice
        db.delete(KanjotoDatabaseHelper.TABLE_APPRENTICES,
                KanjotoDatabaseHelper.COLUMN_ID + " = " + id, null);

        db.close();
    }

    /**
     * Get all apprentices from database table.
     * 
     * @return List of Apprentices.
     */
    public List<Apprentice> getAllApprentices() {
        List<Apprentice> apprentices = new ArrayList<Apprentice>();

        String query = "SELECT * FROM " + KanjotoDatabaseHelper.TABLE_APPRENTICES;

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // select all notes from database
        Cursor cursor = db.rawQuery(query, null);

        Apprentice apprentice = null;
        if (cursor.moveToFirst()) {
            do {
                // create note objects based on note data from database
                apprentice = new Apprentice();
                apprentice.setId(cursor.getLong(0));
                apprentice.setName(cursor.getString(1));
                apprentice.setLearningStyleId(cursor.getLong(2));

                // add note string to list of strings
                apprentices.add(apprentice);
            } while (cursor.moveToNext());
        }

        db.close();

        return apprentices;
    }

    /**
     * Access column data at current position of result.
     * 
     * @param cursor Current cursor location.
     * @return Apprentice
     */
    private Apprentice cursorToApprentice(Cursor cursor) {
        Apprentice apprentice = new Apprentice();
        apprentice.setId(cursor.getLong(0));
        apprentice.setName(cursor.getString(1));
        apprentice.setLearningStyleId(cursor.getLong(2));
        return apprentice;
    }

    /**
     * Get a list of all apprentices ids.
     * 
     * @return List of Apprentice ids.
     */
    public List<Long> getAllApprenticeListDBTableIds() {
        List<Long> apprentices = new LinkedList<Long>();

        String query = "SELECT " + KanjotoDatabaseHelper.COLUMN_ID + " FROM "
                + KanjotoDatabaseHelper.TABLE_APPRENTICES;

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // select all apprentices from database
        Cursor cursor = db.rawQuery(query, null);

        Apprentice apprentice = null;
        if (cursor.moveToFirst()) {
            do {
                // create apprentice objects based on apprentice data from database
                apprentice = new Apprentice();
                apprentice.setId(Long.parseLong(cursor.getString(0)));

                // add apprentice to apprentices list
                apprentices.add(apprentice.getId());
            } while (cursor.moveToNext());
        }

        db.close();

        return apprentices;
    }

    public Apprentice getApprentice(long apprenticeId) {
        Apprentice apprentice = new Apprentice();

        String query = "SELECT * FROM " + KanjotoDatabaseHelper.TABLE_APPRENTICES + " WHERE "
                + KanjotoDatabaseHelper.COLUMN_ID + "=?";

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // select all apprentices from database
        Cursor cursor = db.rawQuery(query, new String[] {
                String.valueOf(apprenticeId)
        });

        if (cursor.moveToFirst()) {
            do {
                // create apprentice objects based on apprentice data from database
                apprentice = new Apprentice();
                apprentice.setId(cursor.getLong(0));
                apprentice.setName(cursor.getString(1));
                apprentice.setLearningStyleId(cursor.getLong(2));
            } while (cursor.moveToNext());
        }

        db.close();

        return apprentice;
    }

    public Apprentice updateApprentice(Apprentice apprentice) {

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(KanjotoDatabaseHelper.COLUMN_ID, apprentice.getId());
        contentValues.put(KanjotoDatabaseHelper.COLUMN_NAME, apprentice.getName());
        contentValues.put(KanjotoDatabaseHelper.COLUMN_LEARNING_STYLE_ID,
                apprentice.getLearningStyleId());

        db.update(KanjotoDatabaseHelper.TABLE_APPRENTICES, contentValues,
                KanjotoDatabaseHelper.COLUMN_ID
                        + "=" + apprentice.getId(), null);

        db.close();

        return apprentice;
    }

    public SQLiteDatabase getDatabase() {
        return database;
    }

    public void setDatabase(SQLiteDatabase database) {
        this.database = database;
    }
}
