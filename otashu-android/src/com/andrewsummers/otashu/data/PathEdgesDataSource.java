
package com.andrewsummers.otashu.data;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.andrewsummers.otashu.model.PathEdge;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class PathEdgesDataSource {
    private OtashuDatabaseHelper dbHelper;

    // database table columns
    private String[] allColumns = {
            OtashuDatabaseHelper.COLUMN_ID,
            OtashuDatabaseHelper.COLUMN_PATH_ID,
            OtashuDatabaseHelper.COLUMN_FROM_NODE_ID,
            OtashuDatabaseHelper.COLUMN_TO_NODE_ID,
            OtashuDatabaseHelper.COLUMN_APPRENTICE_ID,
            OtashuDatabaseHelper.COLUMN_EMOTION_ID,
            OtashuDatabaseHelper.COLUMN_POSITION,
            OtashuDatabaseHelper.COLUMN_RANK,
    };

    /**
     * PathEdgesDataSource constructor.
     * 
     * @param context Current state.
     */
    public PathEdgesDataSource(Context context) {
        dbHelper = new OtashuDatabaseHelper(context);
    }

    /**
     * PathEdgesDataSource constructor.
     * 
     * @param context Current state.
     * @param databaseName Database to use.
     */
    public PathEdgesDataSource(Context context, String databaseName) {
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
     * Create pathEdge row in database.
     * 
     * @param pathEdgevalues String of pathEdge values to insert.
     * @return PathEdge of newly-created pathEdge data.
     */
    public PathEdge createPathEdge(PathEdge pathEdge) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(OtashuDatabaseHelper.COLUMN_PATH_ID, pathEdge.getPathId());
        contentValues.put(OtashuDatabaseHelper.COLUMN_FROM_NODE_ID, pathEdge.getFromNodeId());
        contentValues.put(OtashuDatabaseHelper.COLUMN_TO_NODE_ID, pathEdge.getToNodeId());
        contentValues.put(OtashuDatabaseHelper.COLUMN_APPRENTICE_ID, pathEdge.getApprenticeId());
        contentValues.put(OtashuDatabaseHelper.COLUMN_EMOTION_ID, pathEdge.getEmotionId());
        contentValues.put(OtashuDatabaseHelper.COLUMN_POSITION, pathEdge.getPosition());
        contentValues.put(OtashuDatabaseHelper.COLUMN_RANK, pathEdge.getRank());

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        long insertId = db.insert(OtashuDatabaseHelper.TABLE_PATH_EDGES, null,
                contentValues);

        Cursor cursor = db.query(
                OtashuDatabaseHelper.TABLE_PATH_EDGES, allColumns,
                OtashuDatabaseHelper.COLUMN_ID + " = " + insertId, null,
                null, null, null);

        cursor.moveToFirst();
        PathEdge newPathEdge = cursorToPathEdge(cursor);
        cursor.close();
        db.close();

        return newPathEdge;
    }

    /**
     * Delete pathEdge row from database.
     * 
     * @param pathEdge PathEdge to delete.
     */
    public void deletePathEdge(PathEdge pathEdge) {
        long id = pathEdge.getId();

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // delete pathEdge
        db.delete(OtashuDatabaseHelper.TABLE_PATH_EDGES,
                OtashuDatabaseHelper.COLUMN_ID + " = " + id, null);

        db.close();
    }

    /**
     * Get all pathEdges from database table.
     * 
     * @return List of PathEdges.
     */
    public List<PathEdge> getAllPathEdges() {
        List<PathEdge> pathEdges = new ArrayList<PathEdge>();

        String query = "SELECT * FROM " + OtashuDatabaseHelper.TABLE_PATH_EDGES;

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // select all notes from database
        Cursor cursor = db.rawQuery(query, null);

        PathEdge pathEdge = null;
        if (cursor.moveToFirst()) {
            do {
                // create note objects based on note data from database
                pathEdge = new PathEdge();
                pathEdge.setId(cursor.getLong(0));
                pathEdge.setPathId(cursor.getLong(1));
                pathEdge.setFromNodeId(cursor.getInt(2));
                pathEdge.setToNodeId(cursor.getInt(3));
                pathEdge.setApprenticeId(cursor.getLong(4));
                pathEdge.setEmotionId(cursor.getLong(5));
                pathEdge.setPosition(cursor.getInt(6));
                pathEdge.setRank(cursor.getInt(7));

                // add note string to list of strings
                pathEdges.add(pathEdge);
            } while (cursor.moveToNext());
        }

        db.close();

        return pathEdges;
    }

    /**
     * Get all pathEdges from database table by Emotion.
     * 
     * @return List of PathEdges.
     */
    public List<PathEdge> getAllPathEdgesByEmotion(long emotionId) {
        List<PathEdge> pathEdges = new ArrayList<PathEdge>();

        String query = "SELECT * FROM " + OtashuDatabaseHelper.TABLE_PATH_EDGES
                + " WHERE " + OtashuDatabaseHelper.COLUMN_EMOTION_ID + "=?";

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // select all notes from database
        Cursor cursor = db.rawQuery(query, new String[] {
                String.valueOf(emotionId)
        });

        PathEdge pathEdge = null;
        if (cursor.moveToFirst()) {
            do {
                // create note objects based on note data from database
                pathEdge = new PathEdge();
                pathEdge.setId(cursor.getLong(0));
                pathEdge.setPathId(cursor.getLong(1));
                pathEdge.setFromNodeId(cursor.getInt(2));
                pathEdge.setToNodeId(cursor.getInt(3));
                pathEdge.setApprenticeId(cursor.getLong(4));
                pathEdge.setEmotionId(cursor.getLong(5));
                pathEdge.setPosition(cursor.getInt(6));
                pathEdge.setRank(cursor.getInt(7));

                // add note string to list of strings
                pathEdges.add(pathEdge);
            } while (cursor.moveToNext());
        }

        db.close();

        return pathEdges;
    }

    /**
     * Access column data at current position of result.
     * 
     * @param cursor Current cursor location.
     * @return PathEdge
     */
    private PathEdge cursorToPathEdge(Cursor cursor) {
        PathEdge pathEdge = new PathEdge();
        pathEdge.setId(cursor.getLong(0));
        pathEdge.setPathId(cursor.getLong(1));
        pathEdge.setFromNodeId(cursor.getInt(2));
        pathEdge.setToNodeId(cursor.getInt(3));
        pathEdge.setApprenticeId(cursor.getLong(4));
        pathEdge.setEmotionId(cursor.getLong(5));
        pathEdge.setPosition(cursor.getInt(6));
        pathEdge.setRank(cursor.getInt(7));
        return pathEdge;
    }

    /**
     * getAllPathEdges gets a preview list of all pathEdges.
     * 
     * @return List of PathEdge preview strings.
     */
    public List<String> getAllPathEdgeListPreviews() {
        List<String> pathEdges = new LinkedList<String>();

        String query = "SELECT * FROM " + OtashuDatabaseHelper.TABLE_PATH_EDGES;

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // select all pathEdges from database
        Cursor cursor = db.rawQuery(query, null);

        PathEdge pathEdge = null;
        if (cursor.moveToFirst()) {
            do {
                // create pathEdge objects based on pathEdge data from database
                pathEdge = new PathEdge();
                pathEdge.setId(cursor.getLong(0));
                pathEdge.setPathId(cursor.getLong(1));
                pathEdge.setFromNodeId(cursor.getInt(2));
                pathEdge.setToNodeId(cursor.getInt(3));
                pathEdge.setApprenticeId(cursor.getLong(4));
                pathEdge.setEmotionId(cursor.getLong(5));
                pathEdge.setPosition(cursor.getInt(6));
                pathEdge.setRank(cursor.getInt(7));

                // add pathEdge string to list of strings
                pathEdges.add(pathEdge.toString());
            } while (cursor.moveToNext());
        }

        db.close();

        return pathEdges;
    }

    /**
     * Get a list of all pathEdges ids.
     * 
     * @return List of PathEdge ids.
     */
    public List<Long> getAllPathEdgeListDBTableIds() {
        List<Long> pathEdges = new LinkedList<Long>();

        String query = "SELECT " + OtashuDatabaseHelper.COLUMN_ID + " FROM "
                + OtashuDatabaseHelper.TABLE_PATH_EDGES;

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // select all pathEdges from database
        Cursor cursor = db.rawQuery(query, null);

        PathEdge pathEdge = null;
        if (cursor.moveToFirst()) {
            do {
                // create pathEdge objects based on pathEdge data from database
                pathEdge = new PathEdge();
                pathEdge.setId(cursor.getLong(0));

                // add pathEdge to pathEdges list
                pathEdges.add(pathEdge.getId());
            } while (cursor.moveToNext());
        }

        db.close();

        return pathEdges;
    }

    public PathEdge getPathEdge(long pathEdgeId) {
        PathEdge pathEdge = new PathEdge();

        String query = "SELECT * FROM " + OtashuDatabaseHelper.TABLE_PATH_EDGES + " WHERE "
                + OtashuDatabaseHelper.COLUMN_ID + "=?";

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // select all pathEdges from database
        Cursor cursor = db.rawQuery(query, new String[] {
                String.valueOf(pathEdgeId)
        });

        if (cursor.moveToFirst()) {
            do {
                // create pathEdge objects based on pathEdge data from database
                pathEdge = new PathEdge();
                pathEdge.setId(cursor.getLong(0));
                pathEdge.setPathId(cursor.getLong(1));
                pathEdge.setFromNodeId(cursor.getInt(2));
                pathEdge.setToNodeId(cursor.getInt(3));
                pathEdge.setApprenticeId(cursor.getLong(4));
                pathEdge.setEmotionId(cursor.getLong(5));
                pathEdge.setPosition(cursor.getInt(6));
                pathEdge.setRank(cursor.getInt(7));
            } while (cursor.moveToNext());
        }

        db.close();

        return pathEdge;
    }

    public PathEdge updatePathEdge(PathEdge pathEdge) {

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(OtashuDatabaseHelper.COLUMN_ID, pathEdge.getId());
        contentValues.put(OtashuDatabaseHelper.COLUMN_PATH_ID, pathEdge.getPathId());
        contentValues.put(OtashuDatabaseHelper.COLUMN_FROM_NODE_ID, pathEdge.getFromNodeId());
        contentValues.put(OtashuDatabaseHelper.COLUMN_TO_NODE_ID, pathEdge.getToNodeId());
        contentValues.put(OtashuDatabaseHelper.COLUMN_APPRENTICE_ID, pathEdge.getApprenticeId());
        contentValues.put(OtashuDatabaseHelper.COLUMN_EMOTION_ID, pathEdge.getEmotionId());
        contentValues.put(OtashuDatabaseHelper.COLUMN_POSITION, pathEdge.getPosition());
        contentValues.put(OtashuDatabaseHelper.COLUMN_RANK, pathEdge.getRank());

        db.update(OtashuDatabaseHelper.TABLE_PATH_EDGES, contentValues,
                OtashuDatabaseHelper.COLUMN_ID
                        + "=" + pathEdge.getId(), null);

        db.close();

        return pathEdge;
    }
}
