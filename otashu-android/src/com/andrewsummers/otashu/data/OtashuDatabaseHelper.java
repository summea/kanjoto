package com.andrewsummers.otashu.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * NotesetCollectionOpenHelper is an SQLiteOpenHelper that simplifies connection
 * access to application database.
 */
public class OtashuDatabaseHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 5;
    private static final String DATABASE_NAME = "otashu_collection.db";

    public static final String COLUMN_ID = "_id";
    
    public static final String TABLE_NOTESETS = "notesets";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_EMOTION_ID = "emotion_id";
    
    public static final String TABLE_NOTES = "notes";
    public static final String COLUMN_NOTESET_ID = "noteset_id";
    public static final String COLUMN_NOTEVALUE = "notevalue";
    public static final String COLUMN_VELOCITY = "velocity";
    public static final String COLUMN_LENGTH = "length";
    public static final String COLUMN_POSITION = "position";
    
    public static final String TABLE_EMOTIONS = "emotions";
    public static final String COLUMN_LABEL_ID = "label_id";
    
    public static final String TABLE_LABELS = "labels";
    public static final String COLUMN_COLOR = "color";
    
    public static final String TABLE_BOOKMARKS = "bookmarks";
    public static final String COLUMN_SERIALIZED_VALUE = "serialized_value";

    private static final String CREATE_TABLE_NOTESETS = "CREATE TABLE " + TABLE_NOTESETS
            + " (" + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_NAME + " text, "
            + COLUMN_EMOTION_ID + " text);";
    
    private static final String CREATE_TABLE_NOTES = "CREATE TABLE " + TABLE_NOTES
            + " (" + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_NOTESET_ID + " integer,"
            + COLUMN_NOTEVALUE + " integer,"
            + COLUMN_VELOCITY + " integer,"
            + COLUMN_LENGTH + " real,"
            + COLUMN_POSITION + " integer);";
    
    private static final String CREATE_TABLE_EMOTIONS = "CREATE TABLE " + TABLE_EMOTIONS
            + " (" + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_NAME + " text,"
            + COLUMN_LABEL_ID + " integer);";
    
    private static final String CREATE_TABLE_LABELS = "CREATE TABLE " + TABLE_LABELS
            + " (" + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_NAME + " text, "
            + COLUMN_COLOR + " text);";
    
    private static final String CREATE_TABLE_BOOKMARKS = "CREATE TABLE " + TABLE_BOOKMARKS
            + " (" + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_NAME + " text,"
            + COLUMN_SERIALIZED_VALUE + " text);";

    /**
     * NotesetCollectionOpenHelper constructor.
     * 
     * @param context
     *            Current state.
     */
    public OtashuDatabaseHelper(Context context) {
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
        db.execSQL(CREATE_TABLE_EMOTIONS);
        db.execSQL(CREATE_TABLE_LABELS);
        db.execSQL(CREATE_TABLE_BOOKMARKS);
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
        Log.d("MYLOG", ">>> new database version: " + newVersion);
        Log.d("MYLOG", "updating database...");
        
        // v3
        // db.execSQL(CREATE_TABLE_LABELS);
        
        // v4
        // db.execSQL("ALTER TABLE " + TABLE_EMOTIONS + " ADD COLUMN " + COLUMN_LABEL_ID + " integer;");
        
        // v5
        // db.execSQL(CREATE_TABLE_BOOKMARKS);
    }
}