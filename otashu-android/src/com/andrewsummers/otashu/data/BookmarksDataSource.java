package com.andrewsummers.otashu.data;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import com.andrewsummers.otashu.model.Bookmark;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * BookmarksDataSource is a data source that provides database functionality for
 * bookmark-related data (e.g. CRUD) actions.
 * 
 * Bookmark: Data source based on tutorial by vogella
 * http://www.vogella.com/tutorials/AndroidSQLite/article.html
 * Licensed under: CC BY-NC-SA 3.0 DE:
 * http://creativecommons.org/licenses/by-nc-sa/3.0/de/deed.en
 * Eclipse Public License: https://www.eclipse.org/legal/epl-v10.html
 */
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
     * @param context
     *            Current state.
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
        database = dbHelper.getWritableDatabase();
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
     * @param bookmarkvalues
     *            String of bookmark values to insert.
     * @return Bookmark of newly-created bookmark data.
     */
    public Bookmark createBookmark(Bookmark bookmark) {        
        ContentValues contentValues = new ContentValues();
        contentValues.put(OtashuDatabaseHelper.COLUMN_NAME, bookmark.getName());
        contentValues.put(OtashuDatabaseHelper.COLUMN_SERIALIZED_VALUE, bookmark.getSerializedValue());

        Log.d("MYLOG", contentValues.toString());
        
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
        return newBookmark;
    }

    /**
     * Delete bookmark row from database.
     * 
     * @param bookmark
     *            Bookmark to delete.
     */
    public void deleteBookmark(Bookmark bookmark) {
        long id = bookmark.getId();
        
        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        
        // delete bookmark
        db.delete(OtashuDatabaseHelper.TABLE_BOOKMARKS,
                OtashuDatabaseHelper.COLUMN_ID + " = " + id, null);
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

        return bookmarks;
    }
    
    /**
     * Get all bookmark ids from database table.
     * 
     * @return List of Bookmarks ids.
     */
    public List<Integer> getAllBookmarkIds() {
        List<Integer> bookmark_ids = new ArrayList<Integer>();

        Cursor cursor = database.query(
                OtashuDatabaseHelper.TABLE_BOOKMARKS, allColumns, null,
                null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Bookmark bookmark = cursorToBookmark(cursor);
            bookmark_ids.add((int) bookmark.getId());
            cursor.moveToNext();
        }

        cursor.close();
        return bookmark_ids;
    }

    /**
     * Access column data at current position of result.
     * 
     * @param cursor
     *            Current cursor location.
     * @return Bookmark
     */
    private Bookmark cursorToBookmark(Cursor cursor) {
        Bookmark bookmark = new Bookmark();
        bookmark.setId(cursor.getLong(0));
        bookmark.setName(cursor.getString(1));
        bookmark.setSerializedValue(cursor.getString(2));
        return bookmark;
    }
        
    /**
     * getAllBookmarks gets a preview list of all bookmarks.
     * 
     * @return List of Bookmark preview strings.
     */
    public List<String> getAllBookmarkListPreviews() {
        List<String> bookmarks = new LinkedList<String>();

        String query = "SELECT * FROM " + OtashuDatabaseHelper.TABLE_BOOKMARKS;

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // select all bookmarks from database
        Cursor cursor = db.rawQuery(query, null);

        Bookmark bookmark = null;
        if (cursor.moveToFirst()) {
            do {
                // create bookmark objects based on bookmark data from database
                bookmark = new Bookmark();
                bookmark.setId(cursor.getLong(0));
                bookmark.setName(cursor.getString(1));
                bookmark.setSerializedValue(cursor.getString(2));

                // add bookmark string to list of strings
                bookmarks.add(bookmark.toString());
            } while (cursor.moveToNext());
        }

        return bookmarks;
    }

    /**
     * Get a list of all bookmarks ids.
     * 
     * @return List of Bookmark ids.
     */
    public List<Long> getAllBookmarkListDBTableIds() {
        List<Long> bookmarks = new LinkedList<Long>();
        
        String query = "SELECT " + OtashuDatabaseHelper.COLUMN_ID + " FROM " + OtashuDatabaseHelper.TABLE_BOOKMARKS;

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // select all bookmarks from database
        Cursor cursor = db.rawQuery(query, null);

        Bookmark bookmark = null;
        if (cursor.moveToFirst()) {
            do {                
                // create bookmark objects based on bookmark data from database
                bookmark = new Bookmark();
                bookmark.setId(cursor.getLong(0));
                
                // add bookmark to bookmarks list
                bookmarks.add(bookmark.getId());
            } while (cursor.moveToNext());
        }

        return bookmarks;
    }
    
    public Bookmark getBookmark(long bookmarkId) {
        Bookmark bookmark = new Bookmark();
        
        String query = "SELECT * FROM " + OtashuDatabaseHelper.TABLE_BOOKMARKS + " WHERE " + OtashuDatabaseHelper.COLUMN_ID + "=" + bookmarkId;

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // select all bookmarks from database
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                // create bookmark objects based on bookmark data from database
                bookmark = new Bookmark();
                bookmark.setId(cursor.getLong(0));
                bookmark.setName(cursor.getString(1));
                bookmark.setSerializedValue(cursor.getString(2));
            } while (cursor.moveToNext());
        }
        
        return bookmark;
    }
    
    public Bookmark updateBookmark(Bookmark bookmark) {
        
        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        
        ContentValues contentValues = new ContentValues();
        contentValues.put(OtashuDatabaseHelper.COLUMN_ID, bookmark.getId());
        contentValues.put(OtashuDatabaseHelper.COLUMN_NAME, bookmark.getName());
        contentValues.put(OtashuDatabaseHelper.COLUMN_SERIALIZED_VALUE, bookmark.getSerializedValue());
        
        db.update(OtashuDatabaseHelper.TABLE_BOOKMARKS, contentValues, OtashuDatabaseHelper.COLUMN_ID + "=" + bookmark.getId(), null);

        return bookmark;
    }

    public Bookmark getRandomBookmark() {
        Bookmark bookmark = new Bookmark();
        
        // get all bookmarks first
        List<Bookmark> allBookmarks = getAllBookmarks();

        // choose random bookmark
        int chosenIndex = new Random().nextInt(allBookmarks.size());        

        bookmark = allBookmarks.get(chosenIndex);
        
        return bookmark;
    }
}