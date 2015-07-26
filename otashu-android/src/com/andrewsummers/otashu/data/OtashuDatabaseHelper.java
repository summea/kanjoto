
package com.andrewsummers.otashu.data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;

/**
 * NotesetCollectionOpenHelper is an SQLiteOpenHelper that simplifies connection access to
 * application database.
 */
public class OtashuDatabaseHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 57;
    private static Context mContext;

    public static final String PRODUCTION_DATABASE_NAME = "kanjoto.db";
    public static final String TEST_DATABASE_NAME = "kanjoto_test.db";

    public static final String DATABASE_NAME = "kanjoto.db";
    public static final String DATABASE_PATH = Environment.getExternalStorageDirectory().toString()
            + "/otashu/";
    public static final String LABELS_IMPORT_FILE = "labels.csv";
    public static final String NOTEVALUES_IMPORT_FILE = "notevalues.csv";

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
    public static final String COLUMN_SCALE_ID = "scale_id";

    public static final String TABLE_KEY_SIGNATURES = "key_signatures";

    public static final String TABLE_KEY_NOTES = "key_notes";
    public static final String COLUMN_KEY_SIGNATURE_ID = "key_signature_id";

    public static final String TABLE_APPRENTICES = "apprentices";
    public static final String COLUMN_APPRENTICE_ID = "apprentice_id";
    public static final String COLUMN_LEARNING_STYLE_ID = "learning_style_id";

    public static final String TABLE_LEARNING_STYLES = "learning_styles";

    public static final String TABLE_ACHIEVEMENTS = "achievements";
    public static final String COLUMN_EARNED_ON = "earned_on";
    public static final String COLUMN_KEY = "key";

    public static final String TABLE_PATHS = "paths";
    public static final String COLUMN_RANK = "rank";

    public static final String TABLE_PATH_EDGES = "path_edges";
    public static final String COLUMN_PATH_ID = "path_id";

    private static final String CREATE_TABLE_NOTESETS = "CREATE TABLE " + TABLE_NOTESETS
            + " (" + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_NAME + " text,"
            + COLUMN_EMOTION_ID + " integer,"
            + COLUMN_ENABLED + " integer,"
            + COLUMN_APPRENTICE_ID + " integer);";

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
            + COLUMN_LABEL_ID + " integer);";

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
            + COLUMN_NOTEVALUE + " integer,"
            + COLUMN_GRAPH_ID + " integer,"
            + COLUMN_APPRENTICE_ID + " integer,"
            + COLUMN_SCALE_ID + " integer);";

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
            + COLUMN_NAME + " text,"
            + COLUMN_LEARNING_STYLE_ID + " integer);";

    private static final String CREATE_TABLE_LEARNING_STYLES = "CREATE TABLE "
            + TABLE_LEARNING_STYLES
            + " (" + COLUMN_ID + " integer primary key autoincrement,"
            + COLUMN_NAME + " text);";

    private static final String CREATE_TABLE_ACHIEVEMENTS = "CREATE TABLE " + TABLE_ACHIEVEMENTS
            + " (" + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_NAME + " text,"
            + COLUMN_APPRENTICE_ID + " integer,"
            + COLUMN_EARNED_ON + " text,"
            + COLUMN_KEY + " text);";

    private static final String CREATE_TABLE_PATHS = "CREATE TABLE " + TABLE_PATHS
            + " (" + COLUMN_ID + " integer primary key autoincrement"
            + COLUMN_NAME + " text);";

    private static final String CREATE_TABLE_PATH_EDGES = "CREATE TABLE " + TABLE_PATH_EDGES
            + " (" + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_PATH_ID + " integer, "
            + COLUMN_FROM_NODE_ID + " integer, "
            + COLUMN_TO_NODE_ID + " integer, "
            + COLUMN_APPRENTICE_ID + " integer,"
            + COLUMN_EMOTION_ID + " integer,"
            + COLUMN_POSITION + " integer,"
            + COLUMN_RANK + " integer);";

    /**
     * NotesetCollectionOpenHelper constructor.
     * 
     * @param context Current state.
     */
    public OtashuDatabaseHelper(Context context) {
        super(context, DATABASE_PATH + OtashuDatabaseHelper.DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;
        Log.d("MYLOG", "database helper regular");
    }

    public OtashuDatabaseHelper(Context context, String databaseName) {
        super(context, DATABASE_PATH + databaseName, null, DATABASE_VERSION);
        mContext = context;
        Log.d("MYLOG", "database helper with database: " + DATABASE_PATH + databaseName);
    }

    /**
     * onCreate override that creates application database.
     * 
     * @param db <code>SQLiteDatabase</code> database instance.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("MYLOG", "database: " + db.toString());
        ContentValues contentValues = new ContentValues();

        // set default preferences
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(mContext);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("pref_selected_apprentice", Long.toString(1));
        editor.apply();

        Log.d("MYLOG", "db: creating tables...");
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
        db.execSQL(CREATE_TABLE_LEARNING_STYLES);
        db.execSQL(CREATE_TABLE_ACHIEVEMENTS);
        db.execSQL(CREATE_TABLE_PATHS);
        db.execSQL(CREATE_TABLE_PATH_EDGES);

        // add default apprentice
        Log.d("MYLOG", "db: adding apprentices...");
        contentValues.put(OtashuDatabaseHelper.COLUMN_ID, 1);
        contentValues.put(OtashuDatabaseHelper.COLUMN_NAME, "Early");
        contentValues.put(OtashuDatabaseHelper.COLUMN_LEARNING_STYLE_ID, 1);
        db.insert(OtashuDatabaseHelper.TABLE_APPRENTICES, null, contentValues);

        // add default emotions
        Log.d("MYLOG", "db: adding emotions...");
        contentValues = new ContentValues();
        contentValues.put(OtashuDatabaseHelper.COLUMN_ID, 1);
        contentValues.put(OtashuDatabaseHelper.COLUMN_NAME, "Happy");
        contentValues.put(OtashuDatabaseHelper.COLUMN_LABEL_ID, 3);
        contentValues.put(OtashuDatabaseHelper.COLUMN_APPRENTICE_ID, 1);
        db.insert(OtashuDatabaseHelper.TABLE_EMOTIONS, null, contentValues);

        contentValues = new ContentValues();
        contentValues.put(OtashuDatabaseHelper.COLUMN_ID, 2);
        contentValues.put(OtashuDatabaseHelper.COLUMN_NAME, "Sad");
        contentValues.put(OtashuDatabaseHelper.COLUMN_LABEL_ID, 5);
        contentValues.put(OtashuDatabaseHelper.COLUMN_APPRENTICE_ID, 1);
        db.insert(OtashuDatabaseHelper.TABLE_EMOTIONS, null, contentValues);

        // add default graphs
        Log.d("MYLOG", "db: adding graphs...");
        contentValues = new ContentValues();
        contentValues.put(OtashuDatabaseHelper.COLUMN_ID, 1);
        contentValues.put(OtashuDatabaseHelper.COLUMN_NAME, "Emotion Graph");
        contentValues.put(OtashuDatabaseHelper.COLUMN_LABEL_ID, 1);
        db.insert(OtashuDatabaseHelper.TABLE_GRAPHS, null, contentValues);

        contentValues = new ContentValues();
        contentValues.put(OtashuDatabaseHelper.COLUMN_ID, 2);
        contentValues.put(OtashuDatabaseHelper.COLUMN_NAME, "Transition Graph");
        contentValues.put(OtashuDatabaseHelper.COLUMN_LABEL_ID, 2);
        db.insert(OtashuDatabaseHelper.TABLE_GRAPHS, null, contentValues);

        contentValues = new ContentValues();
        contentValues.put(OtashuDatabaseHelper.COLUMN_ID, 3);
        contentValues.put(OtashuDatabaseHelper.COLUMN_NAME, "Scale Graph");
        contentValues.put(OtashuDatabaseHelper.COLUMN_LABEL_ID, 3);
        db.insert(OtashuDatabaseHelper.TABLE_GRAPHS, null, contentValues);

        // add default labels
        AssetManager am = mContext.getAssets();
        InputStream LABELS_CSV = null;
        try {
            LABELS_CSV = am.open(OtashuDatabaseHelper.LABELS_IMPORT_FILE);
        } catch (IOException e) {
            Log.d("MYLOG", e.getMessage());
        }

        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(LABELS_CSV, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            Log.d("MYLOG", e.getMessage());
        }

        // parse and edit approach based on example code from this source:
        // http://stackoverflow.com/q/16672074/1167750
        String line = "";
        if (LABELS_CSV != null && br != null) {
            try {
                db.beginTransaction();
                // read in CSV file and split into content value items
                while ((line = br.readLine()) != null) {
                    String[] csvColumns = line.split(",");
                    contentValues = new ContentValues();
                    contentValues.put(OtashuDatabaseHelper.COLUMN_ID, csvColumns[0]);
                    contentValues.put(OtashuDatabaseHelper.COLUMN_NAME, csvColumns[1]);
                    contentValues.put(OtashuDatabaseHelper.COLUMN_COLOR, csvColumns[2]);
                    db.insert(TABLE_LABELS, null, contentValues);
                }
                db.setTransactionSuccessful();
            } catch (IOException e) {
                Log.d("MYLOG", e.getMessage());
            } finally {
                db.endTransaction();
            }
        }

        // add default learning styles
        Log.d("MYLOG", "db: adding learning styles...");
        contentValues = new ContentValues();
        contentValues.put(OtashuDatabaseHelper.COLUMN_ID, 1);
        contentValues.put(OtashuDatabaseHelper.COLUMN_NAME, "Logic A");
        db.insert(OtashuDatabaseHelper.TABLE_LEARNING_STYLES, null, contentValues);

        contentValues = new ContentValues();
        contentValues.put(OtashuDatabaseHelper.COLUMN_ID, 2);
        contentValues.put(OtashuDatabaseHelper.COLUMN_NAME, "Logic B");
        db.insert(OtashuDatabaseHelper.TABLE_LEARNING_STYLES, null, contentValues);

        contentValues = new ContentValues();
        contentValues.put(OtashuDatabaseHelper.COLUMN_ID, 3);
        contentValues.put(OtashuDatabaseHelper.COLUMN_NAME, "Logic C");
        db.insert(OtashuDatabaseHelper.TABLE_LEARNING_STYLES, null, contentValues);

        contentValues = new ContentValues();
        contentValues.put(OtashuDatabaseHelper.COLUMN_ID, 4);
        contentValues.put(OtashuDatabaseHelper.COLUMN_NAME, "Logic D");
        db.insert(OtashuDatabaseHelper.TABLE_LEARNING_STYLES, null, contentValues);

        // add default notevalues
        InputStream NOTEVALUES_CSV = null;
        try {
            NOTEVALUES_CSV = am.open(OtashuDatabaseHelper.NOTEVALUES_IMPORT_FILE);
        } catch (IOException e) {
            Log.d("MYLOG", e.getMessage());
        }

        try {
            br = new BufferedReader(new InputStreamReader(NOTEVALUES_CSV, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            Log.d("MYLOG", e.getMessage());
        }

        // parse and edit approach based on example code from this source:
        // http://stackoverflow.com/q/16672074/1167750
        line = "";
        if (NOTEVALUES_CSV != null && br != null) {
            try {
                db.beginTransaction();
                // read in CSV file and split into content value items
                while ((line = br.readLine()) != null) {
                    String[] csvColumns = line.split(",");
                    contentValues = new ContentValues();
                    contentValues.put(OtashuDatabaseHelper.COLUMN_ID, csvColumns[0]);
                    contentValues.put(OtashuDatabaseHelper.COLUMN_NOTEVALUE, csvColumns[1]);
                    contentValues.put(OtashuDatabaseHelper.COLUMN_NOTELABEL, csvColumns[2]);
                    contentValues.put(OtashuDatabaseHelper.COLUMN_LABEL_ID, csvColumns[3]);
                    db.insert(TABLE_NOTEVALUES, null, contentValues);
                }
                db.setTransactionSuccessful();
            } catch (IOException e) {
                Log.d("MYLOG", e.getMessage());
            } finally {
                db.endTransaction();
            }
        }

        // add strong paths
        // TODO: these are used for general testing (take out or relocate these in the future)
        Log.d("MYLOG", "db: adding strong paths...");
        contentValues = new ContentValues();
        contentValues.put(OtashuDatabaseHelper.COLUMN_ID, 1);
        contentValues.put(OtashuDatabaseHelper.COLUMN_GRAPH_ID, 1);
        contentValues.put(OtashuDatabaseHelper.COLUMN_EMOTION_ID, 1);
        contentValues.put(OtashuDatabaseHelper.COLUMN_FROM_NODE_ID, 60);
        contentValues.put(OtashuDatabaseHelper.COLUMN_TO_NODE_ID, 61);
        contentValues.put(OtashuDatabaseHelper.COLUMN_WEIGHT, 0.1f);
        contentValues.put(OtashuDatabaseHelper.COLUMN_POSITION, 1);
        contentValues.put(OtashuDatabaseHelper.COLUMN_APPRENTICE_ID, 1);
        db.insert(OtashuDatabaseHelper.TABLE_EDGES, null, contentValues);

        contentValues = new ContentValues();
        contentValues.put(OtashuDatabaseHelper.COLUMN_ID, 2);
        contentValues.put(OtashuDatabaseHelper.COLUMN_GRAPH_ID, 1);
        contentValues.put(OtashuDatabaseHelper.COLUMN_EMOTION_ID, 1);
        contentValues.put(OtashuDatabaseHelper.COLUMN_FROM_NODE_ID, 61);
        contentValues.put(OtashuDatabaseHelper.COLUMN_TO_NODE_ID, 62);
        contentValues.put(OtashuDatabaseHelper.COLUMN_WEIGHT, 0.1f);
        contentValues.put(OtashuDatabaseHelper.COLUMN_POSITION, 2);
        contentValues.put(OtashuDatabaseHelper.COLUMN_APPRENTICE_ID, 1);
        db.insert(OtashuDatabaseHelper.TABLE_EDGES, null, contentValues);

        contentValues = new ContentValues();
        contentValues.put(OtashuDatabaseHelper.COLUMN_ID, 3);
        contentValues.put(OtashuDatabaseHelper.COLUMN_GRAPH_ID, 1);
        contentValues.put(OtashuDatabaseHelper.COLUMN_EMOTION_ID, 1);
        contentValues.put(OtashuDatabaseHelper.COLUMN_FROM_NODE_ID, 62);
        contentValues.put(OtashuDatabaseHelper.COLUMN_TO_NODE_ID, 63);
        contentValues.put(OtashuDatabaseHelper.COLUMN_WEIGHT, 0.1f);
        contentValues.put(OtashuDatabaseHelper.COLUMN_POSITION, 3);
        contentValues.put(OtashuDatabaseHelper.COLUMN_APPRENTICE_ID, 1);
        db.insert(OtashuDatabaseHelper.TABLE_EDGES, null, contentValues);

        contentValues = new ContentValues();
        contentValues.put(OtashuDatabaseHelper.COLUMN_ID, 4);
        contentValues.put(OtashuDatabaseHelper.COLUMN_GRAPH_ID, 1);
        contentValues.put(OtashuDatabaseHelper.COLUMN_EMOTION_ID, 2);
        contentValues.put(OtashuDatabaseHelper.COLUMN_FROM_NODE_ID, 61);
        contentValues.put(OtashuDatabaseHelper.COLUMN_TO_NODE_ID, 62);
        contentValues.put(OtashuDatabaseHelper.COLUMN_WEIGHT, 0.1f);
        contentValues.put(OtashuDatabaseHelper.COLUMN_POSITION, 1);
        contentValues.put(OtashuDatabaseHelper.COLUMN_APPRENTICE_ID, 1);
        db.insert(OtashuDatabaseHelper.TABLE_EDGES, null, contentValues);

        contentValues = new ContentValues();
        contentValues.put(OtashuDatabaseHelper.COLUMN_ID, 5);
        contentValues.put(OtashuDatabaseHelper.COLUMN_GRAPH_ID, 1);
        contentValues.put(OtashuDatabaseHelper.COLUMN_EMOTION_ID, 2);
        contentValues.put(OtashuDatabaseHelper.COLUMN_FROM_NODE_ID, 62);
        contentValues.put(OtashuDatabaseHelper.COLUMN_TO_NODE_ID, 63);
        contentValues.put(OtashuDatabaseHelper.COLUMN_WEIGHT, 0.1f);
        contentValues.put(OtashuDatabaseHelper.COLUMN_POSITION, 2);
        contentValues.put(OtashuDatabaseHelper.COLUMN_APPRENTICE_ID, 1);
        db.insert(OtashuDatabaseHelper.TABLE_EDGES, null, contentValues);

        contentValues = new ContentValues();
        contentValues.put(OtashuDatabaseHelper.COLUMN_ID, 6);
        contentValues.put(OtashuDatabaseHelper.COLUMN_GRAPH_ID, 1);
        contentValues.put(OtashuDatabaseHelper.COLUMN_EMOTION_ID, 2);
        contentValues.put(OtashuDatabaseHelper.COLUMN_FROM_NODE_ID, 63);
        contentValues.put(OtashuDatabaseHelper.COLUMN_TO_NODE_ID, 64);
        contentValues.put(OtashuDatabaseHelper.COLUMN_WEIGHT, 0.1f);
        contentValues.put(OtashuDatabaseHelper.COLUMN_POSITION, 3);
        contentValues.put(OtashuDatabaseHelper.COLUMN_APPRENTICE_ID, 1);
        db.insert(OtashuDatabaseHelper.TABLE_EDGES, null, contentValues);
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
        // db.execSQL("ALTER TABLE " + TABLE_APPRENTICE_SCORES + " ADD COLUMN " + COLUMN_SCALE_ID +
        // " integer");
    }
}
