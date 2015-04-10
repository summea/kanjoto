
package com.andrewsummers.otashu.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * NotesetCollectionOpenHelper is an SQLiteOpenHelper that simplifies connection access to
 * application database.
 */
public class OtashuDatabaseHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 45;
    private static final String DATABASE_NAME = "otashu_collection.db";

    public static final String COLUMN_ID = "_id";

    public static final String TABLE_NOTESETS = "notesets";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_EMOTION_ID = "emotion_id";
    public static final String COLUMN_ENABLED = "enabled";

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

    public static final String TABLE_NOTEVALUES = "notevalues";
    public static final String COLUMN_NOTELABEL = "notelabel";

    public static final String TABLE_GRAPHS = "graphs";

    public static final String TABLE_VERTICES = "vertices";
    public static final String COLUMN_GRAPH_ID = "graph_id";
    public static final String COLUMN_NODE = "node";

    public static final String TABLE_EDGES = "edges";
    public static final String COLUMN_FROM_NODE_ID = "from_node_id";
    public static final String COLUMN_TO_NODE_ID = "to_node_id";
    public static final String COLUMN_WEIGHT = "weight";

    public static final String TABLE_APPRENTICE_SCORECARDS = "apprentice_scorecards";
    public static final String COLUMN_TAKEN_AT = "taken_at";
    public static final String COLUMN_TOTAL = "total";

    public static final String TABLE_APPRENTICE_SCORES = "apprentice_scores";
    public static final String COLUMN_SCORECARD_ID = "scorecard_id";
    public static final String COLUMN_QUESTION_NUMBER = "question_number";
    public static final String COLUMN_CORRECT = "correct";
    public static final String COLUMN_EDGE_ID = "edge_id";

    public static final String TABLE_KEY_SIGNATURES = "key_signatures";

    public static final String TABLE_KEY_NOTES = "key_notes";
    public static final String COLUMN_KEY_SIGNATURE_ID = "key_signature_id";

    public static final String TABLE_APPRENTICES = "apprentices";
    public static final String COLUMN_APPRENTICE_ID = "apprentice_id";

    private static final String CREATE_TABLE_NOTESETS = "CREATE TABLE " + TABLE_NOTESETS
            + " (" + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_NAME + " text,"
            + COLUMN_ENABLED + " integer);";

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
            + COLUMN_LABEL_ID + " integer,"
            + COLUMN_APPRENTICE_ID + " integer);";

    private static final String CREATE_TABLE_LABELS = "CREATE TABLE " + TABLE_LABELS
            + " (" + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_NAME + " text, "
            + COLUMN_COLOR + " text);";

    private static final String CREATE_TABLE_BOOKMARKS = "CREATE TABLE " + TABLE_BOOKMARKS
            + " (" + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_NAME + " text,"
            + COLUMN_SERIALIZED_VALUE + " text);";

    private static final String CREATE_TABLE_NOTEVALUES = "CREATE TABLE " + TABLE_NOTEVALUES
            + " (" + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_NOTEVALUE + " integer, "
            + COLUMN_NOTELABEL + " text, "
            + COLUMN_LABEL_ID + " integer);";

    private static final String CREATE_TABLE_GRAPHS = "CREATE TABLE " + TABLE_GRAPHS
            + " (" + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_NAME + " text,"
            + COLUMN_LABEL_ID + ");";

    private static final String CREATE_TABLE_VERTICES = "CREATE TABLE " + TABLE_VERTICES
            + " (" + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_GRAPH_ID + " integer,"
            + COLUMN_NODE + " integer);";

    private static final String CREATE_TABLE_EDGES = "CREATE TABLE " + TABLE_EDGES
            + " (" + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_GRAPH_ID + " integer,"
            + COLUMN_EMOTION_ID + " integer,"
            + COLUMN_FROM_NODE_ID + " integer, "
            + COLUMN_TO_NODE_ID + " integer, "
            + COLUMN_WEIGHT + " real,"
            + COLUMN_POSITION + " integer,"
            + COLUMN_APPRENTICE_ID + " integer);";

    private static final String CREATE_TABLE_APPRENTICE_SCORECARDS = "CREATE TABLE "
            + TABLE_APPRENTICE_SCORECARDS
            + " (" + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_TAKEN_AT + " text, "
            + COLUMN_TOTAL + " integer,"
            + COLUMN_CORRECT + " integer,"
            + COLUMN_APPRENTICE_ID + " integer);";

    private static final String CREATE_TABLE_APPRENTICE_SCORES = "CREATE TABLE "
            + TABLE_APPRENTICE_SCORES
            + " (" + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_SCORECARD_ID + " integer,"
            + COLUMN_QUESTION_NUMBER + " integer,"
            + COLUMN_CORRECT + " integer,"
            + COLUMN_EDGE_ID + " integer,"
            + COLUMN_GRAPH_ID + " integer,"
            + COLUMN_APPRENTICE_ID + " integer);";

    private static final String CREATE_TABLE_KEY_SIGNATURES = "CREATE TABLE "
            + TABLE_KEY_SIGNATURES
            + " (" + COLUMN_ID + " integer primary key autoincrement,"
            + COLUMN_EMOTION_ID + " integer,"
            + COLUMN_APPRENTICE_ID + " integer);";

    private static final String CREATE_TABLE_KEY_NOTES = "CREATE TABLE " + TABLE_KEY_NOTES
            + " (" + COLUMN_ID + " integer primary key autoincrement,"
            + COLUMN_KEY_SIGNATURE_ID + " integer,"
            + COLUMN_NOTEVALUE + " integer,"
            + COLUMN_WEIGHT + " real,"
            + COLUMN_APPRENTICE_ID + " integer);";

    private static final String CREATE_TABLE_APPRENTICES = "CREATE TABLE "
            + TABLE_APPRENTICES
            + " (" + COLUMN_ID + " integer primary key autoincrement,"
            + COLUMN_NAME + " text);";

    /**
     * NotesetCollectionOpenHelper constructor.
     * 
     * @param context Current state.
     */
    public OtashuDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * onCreate override that creates application database.
     * 
     * @param db <code>SQLiteDatabase</code> database instance.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_NOTESETS);
        db.execSQL(CREATE_TABLE_NOTES);
        db.execSQL(CREATE_TABLE_EMOTIONS);
        db.execSQL(CREATE_TABLE_LABELS);
        db.execSQL(CREATE_TABLE_BOOKMARKS);
        db.execSQL(CREATE_TABLE_NOTEVALUES);
        db.execSQL(CREATE_TABLE_GRAPHS);
        db.execSQL(CREATE_TABLE_VERTICES);
        db.execSQL(CREATE_TABLE_EDGES);
        db.execSQL(CREATE_TABLE_APPRENTICE_SCORECARDS);
        db.execSQL(CREATE_TABLE_APPRENTICE_SCORES);
        db.execSQL(CREATE_TABLE_KEY_SIGNATURES);
        db.execSQL(CREATE_TABLE_KEY_NOTES);
        db.execSQL(CREATE_TABLE_APPRENTICES);
    }

    /**
     * onUpgrade override that handles database changes.
     * 
     * @param db <code>SQLiteDatabase</code> database instance.
     * @param oldVersion <code>int</code> value of old version number
     * @param newVersion <code>int</code> value of new version number
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d("MYLOG", ">>> new database version: " + newVersion);
        Log.d("MYLOG", "updating database...");

        // v32
        // db.execSQL("DROP TABLE " + TABLE_APPRENTICE_SCORECARDS);
        // db.execSQL("DROP TABLE " + TABLE_APPRENTICE_SCORES);
        // db.execSQL(CREATE_TABLE_APPRENTICE_SCORECARDS);
        // db.execSQL(CREATE_TABLE_APPRENTICE_SCORES);

        // v33 (change required for emotion graph id)
        // db.execSQL("UPDATE " + TABLE_VERTICES + " SET " + COLUMN_GRAPH_ID + "=1");
        // db.execSQL("UPDATE " + TABLE_EDGES + " SET " + COLUMN_GRAPH_ID + "=1");

        // v34
        // db.execSQL("DROP TABLE " + TABLE_GRAPHS);
        // db.execSQL(CREATE_TABLE_GRAPHS);

        // v36
        // db.execSQL("DELETE FROM " + TABLE_EDGES + " WHERE " + COLUMN_GRAPH_ID + "=2");

        // v38
        // db.execSQL("DELETE FROM " + TABLE_EDGES + " WHERE " + COLUMN_GRAPH_ID + "=3");

        // 39
        // db.execSQL(CREATE_TABLE_KEY_SIGNATURES);
        // db.execSQL(CREATE_TABLE_KEY_NOTES);

        // 40
        // db.execSQL("DROP TABLE " + TABLE_KEY_NOTES);
        // db.execSQL(CREATE_TABLE_KEY_NOTES);
        // db.execSQL("DELETE FROM " + TABLE_EDGES + " WHERE " + COLUMN_GRAPH_ID + "=3");

        // 41
        // db.execSQL(CREATE_TABLE_KEY_SIGNATURES);
        // db.execSQL(CREATE_TABLE_KEY_NOTES);

        // 42 (change required for emotion graph id)
        // db.execSQL("UPDATE " + TABLE_VERTICES + " SET " + COLUMN_GRAPH_ID + "=1");
        // db.execSQL("UPDATE " + TABLE_EDGES + " SET " + COLUMN_GRAPH_ID + "=1");

        // 44
        // db.execSQL(CREATE_TABLE_APPRENTICES);

        // 45
        db.execSQL("DROP TABLE " + TABLE_APPRENTICES);
        db.execSQL(CREATE_TABLE_APPRENTICES);
        db.execSQL("ALTER TABLE " + TABLE_APPRENTICE_SCORECARDS + " ADD COLUMN "
                + COLUMN_APPRENTICE_ID + " integer;");
        db.execSQL("ALTER TABLE " + TABLE_APPRENTICE_SCORES + " ADD COLUMN " + COLUMN_APPRENTICE_ID
                + " integer;");
        db.execSQL("ALTER TABLE " + TABLE_EDGES + " ADD COLUMN " + COLUMN_APPRENTICE_ID
                + " integer;");
        db.execSQL("ALTER TABLE " + TABLE_EMOTIONS + " ADD COLUMN " + COLUMN_APPRENTICE_ID
                + " integer;");
        db.execSQL("ALTER TABLE " + TABLE_KEY_SIGNATURES + " ADD COLUMN " + COLUMN_APPRENTICE_ID
                + " integer;");
        db.execSQL("ALTER TABLE " + TABLE_KEY_NOTES + " ADD COLUMN " + COLUMN_APPRENTICE_ID
                + " integer;");
    }
}
