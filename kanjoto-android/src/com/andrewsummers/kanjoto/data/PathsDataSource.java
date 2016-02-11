
package com.andrewsummers.kanjoto.data;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.andrewsummers.kanjoto.model.Path;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class PathsDataSource {
    private KanjotoDatabaseHelper dbHelper;

    // database table columns
    private String[] allColumns = {
            KanjotoDatabaseHelper.COLUMN_ID,
            KanjotoDatabaseHelper.COLUMN_NAME,
    };

    /**
     * PathsDataSource constructor.
     * 
     * @param context Current state.
     */
    public PathsDataSource(Context context) {
        dbHelper = new KanjotoDatabaseHelper(context);
    }

    /**
     * PathsDataSource constructor.
     * 
     * @param context Current state.
     * @param databaseName Database to use.
     */
    public PathsDataSource(Context context, String databaseName) {
        dbHelper = new KanjotoDatabaseHelper(context, databaseName);
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
     * Create path row in database.
     * 
     * @param pathvalues String of path values to insert.
     * @return Path of newly-created path data.
     */
    public Path createPath(Path path) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(KanjotoDatabaseHelper.COLUMN_NAME, path.getName());

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        long insertId = db.insert(KanjotoDatabaseHelper.TABLE_PATHS, null,
                contentValues);

        Cursor cursor = db.query(
                KanjotoDatabaseHelper.TABLE_PATHS, allColumns,
                KanjotoDatabaseHelper.COLUMN_ID + " = " + insertId, null,
                null, null, null);

        cursor.moveToFirst();
        Path newPath = cursorToPath(cursor);
        cursor.close();
        db.close();

        return newPath;
    }

    /**
     * Delete path row from database.
     * 
     * @param path Path to delete.
     */
    public void deletePath(Path path) {
        long id = path.getId();

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // delete path
        db.delete(KanjotoDatabaseHelper.TABLE_PATHS,
                KanjotoDatabaseHelper.COLUMN_ID + " = " + id, null);

        db.close();
    }

    /**
     * Get all paths from database table.
     * 
     * @return List of Paths.
     */
    public List<Path> getAllPaths() {
        List<Path> paths = new ArrayList<Path>();

        String query = "SELECT * FROM " + KanjotoDatabaseHelper.TABLE_PATHS;

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // select all notes from database
        Cursor cursor = db.rawQuery(query, null);

        Path path = null;
        if (cursor.moveToFirst()) {
            do {
                // create note objects based on note data from database
                path = new Path();
                path.setId(cursor.getLong(0));
                path.setName(cursor.getString(1));

                // add note string to list of strings
                paths.add(path);
            } while (cursor.moveToNext());
        }

        db.close();

        return paths;
    }

    /**
     * Access column data at current position of result.
     * 
     * @param cursor Current cursor location.
     * @return Path
     */
    private Path cursorToPath(Cursor cursor) {
        Path path = new Path();
        path.setId(cursor.getLong(0));
        path.setName(cursor.getString(1));
        return path;
    }

    /**
     * getAllPaths gets a preview list of all paths.
     * 
     * @return List of Path preview strings.
     */
    public List<String> getAllPathListPreviews() {
        List<String> paths = new LinkedList<String>();

        String query = "SELECT * FROM " + KanjotoDatabaseHelper.TABLE_PATHS;

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // select all paths from database
        Cursor cursor = db.rawQuery(query, null);

        Path path = null;
        if (cursor.moveToFirst()) {
            do {
                // create path objects based on path data from database
                path = new Path();
                path.setId(cursor.getLong(0));
                path.setName(cursor.getString(1));

                // add path string to list of strings
                paths.add(path.toString());
            } while (cursor.moveToNext());
        }

        db.close();

        return paths;
    }

    /**
     * Get a list of all paths ids.
     * 
     * @return List of Path ids.
     */
    public List<Long> getAllPathListDBTableIds() {
        List<Long> paths = new LinkedList<Long>();

        String query = "SELECT " + KanjotoDatabaseHelper.COLUMN_ID + " FROM "
                + KanjotoDatabaseHelper.TABLE_PATHS;

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // select all paths from database
        Cursor cursor = db.rawQuery(query, null);

        Path path = null;
        if (cursor.moveToFirst()) {
            do {
                // create path objects based on path data from database
                path = new Path();
                path.setId(cursor.getLong(0));
                path.setName(cursor.getString(1));

                // add path to paths list
                paths.add(path.getId());
            } while (cursor.moveToNext());
        }

        db.close();

        return paths;
    }

    public Path getPath(long pathId) {
        Path path = new Path();

        String query = "SELECT * FROM " + KanjotoDatabaseHelper.TABLE_PATHS + " WHERE "
                + KanjotoDatabaseHelper.COLUMN_ID + "=?";

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // select all paths from database
        Cursor cursor = db.rawQuery(query, new String[] {
                String.valueOf(pathId)
        });

        if (cursor.moveToFirst()) {
            do {
                // create path objects based on path data from database
                path = new Path();
                path.setId(cursor.getLong(0));
                path.setName(cursor.getString(1));
            } while (cursor.moveToNext());
        }

        db.close();

        return path;
    }

    public Path updatePath(Path path) {

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(KanjotoDatabaseHelper.COLUMN_ID, path.getId());
        contentValues.put(KanjotoDatabaseHelper.COLUMN_NAME, path.getName());

        db.update(KanjotoDatabaseHelper.TABLE_PATHS, contentValues, KanjotoDatabaseHelper.COLUMN_ID
                + "=" + path.getId(), null);

        db.close();

        return path;
    }

    public Path getLastPath() {
        Path path = new Path();

        String query = "SELECT * FROM " + KanjotoDatabaseHelper.TABLE_PATHS
                + " ORDER BY " + KanjotoDatabaseHelper.COLUMN_ID + " DESC LIMIT 1";

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // select all paths from database
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                // create path objects based on path data from database
                path = new Path();
                path.setId(cursor.getLong(0));
                path.setName(cursor.getString(1));
            } while (cursor.moveToNext());
        }

        db.close();

        return path;
    }

    public void resetAutoIncrement() {
        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String query = "DELETE FROM " + KanjotoDatabaseHelper.TABLE_PATHS;
        db.execSQL(query);

        query = "DELETE FROM SQLITE_SEQUENCE WHERE NAME=?";
        db.execSQL(query, new String[] {
                KanjotoDatabaseHelper.TABLE_PATHS,
        });

        db.close();
    }
}
