package com.andrewsummers.otashu;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * NotesetCollectionOpenHelper is an SQLiteOpenHelper that simplifies connection
 * access to application database.
 * 
 * Note: Data source based on tutorial by vogella
 * http://www.vogella.com/tutorials/AndroidSQLite/article.html
 * Licensed under: CC BY-NC-SA 3.0 DE:
 * http://creativecommons.org/licenses/by-nc-sa/3.0/de/deed.en
 * Eclipse Public License: https://www.eclipse.org/legal/epl-v10.html
 */
public class OtashuDatabaseHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "otashu_collection.db";

    public static final String COLUMN_ID = "_id";
    
    public static final String TABLE_NOTESETS = "notesets";
    public static final String COLUMN_NAME = "name";
    
    public static final String TABLE_NOTES = "notes";
    public static final String COLUMN_NOTESET_ID = "noteset_id";
    public static final String COLUMN_NOTEVALUE = "notevalue";
    public static final String COLUMN_VELOCITY = "velocity";
    public static final String COLUMN_LENGTH = "length";

    private static final String CREATE_TABLE_NOTESETS = "CREATE TABLE " + TABLE_NOTESETS
            + " (" + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_NAME + " text);";
    
    private static final String CREATE_TABLE_NOTES = "CREATE TABLE " + TABLE_NOTES
            + " (" + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_NOTESET_ID + " integer,"
            + COLUMN_NOTEVALUE + " integer,"
            + COLUMN_VELOCITY + " integer,"
            + COLUMN_LENGTH + " integer);";

    /**
     * NotesetCollectionOpenHelper constructor.
     * 
     * @param context
     *            Current state.
     */
    OtashuDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * onCreate override that creates application database.
     * 
     * @param db
     *            <code>SQLiteDatabase</code> database instance.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_NOTESETS);
        db.execSQL(CREATE_TABLE_NOTES);
    }

    /**
     * onUpgrade override that handles database changes.
     * 
     * @param db
     *            <code>SQLiteDatabase</code> database instance.
     * @param oldVersion
     *            <code>int</code> value of old version number
     * @param newVersion
     *            <code>int</code> value of new version number
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
    }
}