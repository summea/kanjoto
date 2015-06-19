
package com.andrewsummers.otashu.data;

import java.util.ArrayList;
import java.util.List;

import com.andrewsummers.otashu.model.Bookmark;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class BookmarksDataSource {
    private SQLiteDatabase database;
    private OtashuDatabaseHelper dbHelper;

    // database table columns
    private String[] allColumns = {
            OtashuDatabaseHelper.COLUMN_ID,
            OtashuDatabaseHelper.COLUMN_NAME,
            OtashuDatabaseHelper.COLUMN_SERIALIZED_VALUE
    };

    /**
     * BookmarksDataSource constructor.
     * 
     * @param context Current state.
     */
    public BookmarksDataSource(Context context) {
        dbHelper = new OtashuDatabaseHelper(context);
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
     * Create bookmark row in database.
     * 
     * @param bookmarkvalues String of bookmark values to insert.
     * @return Bookmark of newly-created bookmark data.
     */
    public Bookmark createBookmark(Bookmark bookmark) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(OtashuDatabaseHelper.COLUMN_NAME, bookmark.getName());
        contentValues.put(OtashuDatabaseHelper.COLUMN_SERIALIZED_VALUE,
                bookmark.getSerializedValue());

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        long insertId = db
                .insert(OtashuDatabaseHelper.TABLE_BOOKMARKS, null,
                        contentValues);

        Cursor cursor = db.query(
                OtashuDatabaseHelper.TABLE_BOOKMARKS, allColumns,
                OtashuDatabaseHelper.COLUMN_ID + " = " + insertId, null,
                null, null, null);

        cursor.moveToFirst();

        Bookmark newBookmark = cursorToBookmark(cursor);
        cursor.close();
        db.close();
        
        return newBookmark;
    }

    /**
     * Delete bookmark row from database.
     * 
     * @param bookmark Bookmark to delete.
     */
    public void deleteBookmark(Bookmark bookmark) {
        long id = bookmark.getId();

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // delete bookmark
        db.delete(OtashuDatabaseHelper.TABLE_BOOKMARKS,
                OtashuDatabaseHelper.COLUMN_ID + " = " + id, null);
        
        db.close();
    }

    /**
     * Get all bookmarks from database table.
     * 
     * @return List of Bookmarks.
     */
    public List<Bookmark> getAllBookmarks() {
        List<Bookmark> bookmarks = new ArrayList<Bookmark>();

        String query = "SELECT * FROM " + OtashuDatabaseHelper.TABLE_BOOKMARKS;

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // select all notes from database
        Cursor cursor = db.rawQuery(query, null);

        Bookmark bookmark = null;
        if (cursor.moveToFirst()) {
            do {
                // create note objects based on note data from database
                bookmark = new Bookmark();
                bookmark.setId(cursor.getLong(0));
                bookmark.setName(cursor.getString(1));
                bookmark.setSerializedValue(cursor.getString(2));

                // add note string to list of strings
                bookmarks.add(bookmark);
            } while (cursor.moveToNext());
        }
        
        db.close();

        return bookmarks;
    }

    /**
     * Access column data at current position of result.
     * 
     * @param cursor Current cursor location.
     * @return Bookmark
     */
    private Bookmark cursorToBookmark(Cursor cursor) {
        Bookmark bookmark = new Bookmark();
        bookmark.setId(cursor.getLong(0));
        bookmark.setName(cursor.getString(1));
        bookmark.setSerializedValue(cursor.getString(2));
        return bookmark;
    }

    public Bookmark getBookmark(long bookmarkId) {
        Bookmark bookmark = new Bookmark();

        String query = "SELECT * FROM " + OtashuDatabaseHelper.TABLE_BOOKMARKS + " WHERE "
                + OtashuDatabaseHelper.COLUMN_ID + "=?";

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // select all bookmarks from database
        Cursor cursor = db.rawQuery(query, new String[] {
            String.valueOf(bookmarkId)
        });

        if (cursor.moveToFirst()) {
            do {
                // create bookmark objects based on bookmark data from database
                bookmark = new Bookmark();
                bookmark.setId(cursor.getLong(0));
                bookmark.setName(cursor.getString(1));
                bookmark.setSerializedValue(cursor.getString(2));
            } while (cursor.moveToNext());
        }
        
        db.close();

        return bookmark;
    }

    public Bookmark updateBookmark(Bookmark bookmark) {

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(OtashuDatabaseHelper.COLUMN_ID, bookmark.getId());
        contentValues.put(OtashuDatabaseHelper.COLUMN_NAME, bookmark.getName());
        contentValues.put(OtashuDatabaseHelper.COLUMN_SERIALIZED_VALUE,
                bookmark.getSerializedValue());

        db.update(OtashuDatabaseHelper.TABLE_BOOKMARKS, contentValues,
                OtashuDatabaseHelper.COLUMN_ID + "=" + bookmark.getId(), null);

        db.close();
        
        return bookmark;
    }

    public SQLiteDatabase getDatabase() {
        return database;
    }

    public void setDatabase(SQLiteDatabase database) {
        this.database = database;
    }
}
