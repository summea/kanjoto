
package com.andrewsummers.otashu.data;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import com.andrewsummers.otashu.model.Edge;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class EdgesDataSource {
    private SQLiteDatabase database;
    private OtashuDatabaseHelper dbHelper;

    // database table columns
    private String[] allColumns = {
            OtashuDatabaseHelper.COLUMN_ID,
            OtashuDatabaseHelper.COLUMN_GRAPH_ID,
            OtashuDatabaseHelper.COLUMN_EMOTION_ID,
            OtashuDatabaseHelper.COLUMN_FROM_NODE_ID,
            OtashuDatabaseHelper.COLUMN_TO_NODE_ID,
            OtashuDatabaseHelper.COLUMN_WEIGHT,
            OtashuDatabaseHelper.COLUMN_POSITION,
    };

    /**
     * EdgesDataSource constructor.
     * 
     * @param context Current state.
     */
    public EdgesDataSource(Context context) {
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
     * Create edge row in database.
     * 
     * @param edgevalues String of edge values to insert.
     * @return Edge of newly-created edge data.
     */
    public Edge createEdge(Edge edge) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(OtashuDatabaseHelper.COLUMN_GRAPH_ID, edge.getGraphId());
        contentValues.put(OtashuDatabaseHelper.COLUMN_EMOTION_ID, edge.getEmotionId());
        contentValues.put(OtashuDatabaseHelper.COLUMN_FROM_NODE_ID, edge.getFromNodeId());
        contentValues.put(OtashuDatabaseHelper.COLUMN_TO_NODE_ID, edge.getToNodeId());
        contentValues.put(OtashuDatabaseHelper.COLUMN_WEIGHT, edge.getWeight());
        contentValues.put(OtashuDatabaseHelper.COLUMN_POSITION, edge.getPosition());

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        long insertId = db.insert(OtashuDatabaseHelper.TABLE_EDGES, null,
                contentValues);

        Cursor cursor = db.query(
                OtashuDatabaseHelper.TABLE_EDGES, allColumns,
                OtashuDatabaseHelper.COLUMN_ID + " = " + insertId, null,
                null, null, null);

        cursor.moveToFirst();
        Edge newEdge = cursorToEdge(cursor);
        cursor.close();
        return newEdge;
    }

    /**
     * Delete edge row from database.
     * 
     * @param edge Edge to delete.
     */
    public void deleteEdge(Edge edge) {
        long id = edge.getId();

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // delete edge
        db.delete(OtashuDatabaseHelper.TABLE_EDGES,
                OtashuDatabaseHelper.COLUMN_ID + " = " + id, null);
    }

    /**
     * Get all edges from database table.
     * 
     * @return List of Edges.
     */
    public List<Edge> getAllEdges() {
        List<Edge> edges = new ArrayList<Edge>();

        String query = "SELECT * FROM " + OtashuDatabaseHelper.TABLE_EDGES;

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // select all notes from database
        Cursor cursor = db.rawQuery(query, null);

        Edge edge = null;
        if (cursor.moveToFirst()) {
            do {
                // create note objects based on note data from database
                edge = new Edge();
                edge.setId(cursor.getLong(0));
                edge.setGraphId(cursor.getLong(1));
                edge.setEmotionId(cursor.getLong(2));
                edge.setFromNodeId(cursor.getInt(3));
                edge.setToNodeId(cursor.getInt(4));
                edge.setWeight(cursor.getFloat(5));
                edge.setPosition(cursor.getInt(6));

                // add note string to list of strings
                edges.add(edge);
            } while (cursor.moveToNext());
        }

        return edges;
    }

    /**
     * Get all edge ids from database table.
     * 
     * @return List of Edges ids.
     */
    public List<Integer> getAllEdgeIds() {
        List<Integer> edge_ids = new ArrayList<Integer>();

        Cursor cursor = database.query(
                OtashuDatabaseHelper.TABLE_EDGES, allColumns, null,
                null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Edge edge = cursorToEdge(cursor);
            edge_ids.add((int) edge.getId());
            cursor.moveToNext();
        }

        cursor.close();
        return edge_ids;
    }

    /**
     * Access column data at current position of result.
     * 
     * @param cursor Current cursor location.
     * @return Edge
     */
    private Edge cursorToEdge(Cursor cursor) {
        Edge edge = new Edge();
        edge.setId(cursor.getLong(0));
        edge.setGraphId(cursor.getLong(1));
        edge.setEmotionId(cursor.getLong(2));
        edge.setFromNodeId(cursor.getInt(3));
        edge.setToNodeId(cursor.getInt(4));
        edge.setWeight(cursor.getFloat(5));
        edge.setPosition(cursor.getInt(6));
        return edge;
    }

    /**
     * getAllEdges gets a preview list of all edges.
     * 
     * @return List of Edge preview strings.
     */
    public List<String> getAllEdgeListPreviews() {
        List<String> edges = new LinkedList<String>();

        String query = "SELECT * FROM " + OtashuDatabaseHelper.TABLE_EDGES;

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // select all edges from database
        Cursor cursor = db.rawQuery(query, null);

        Edge edge = null;
        if (cursor.moveToFirst()) {
            do {
                // create edge objects based on edge data from database
                edge = new Edge();
                edge.setId(cursor.getLong(0));
                edge.setGraphId(cursor.getLong(1));
                edge.setEmotionId(cursor.getLong(2));
                edge.setFromNodeId(cursor.getInt(3));
                edge.setToNodeId(cursor.getInt(4));
                edge.setWeight(cursor.getFloat(5));
                edge.setPosition(cursor.getInt(6));

                // add edge string to list of strings
                edges.add(edge.toString());
            } while (cursor.moveToNext());
        }

        return edges;
    }

    /**
     * Get a list of all edges ids.
     * 
     * @return List of Edge ids.
     */
    public List<Long> getAllEdgeListDBTableIds() {
        List<Long> edges = new LinkedList<Long>();

        String query = "SELECT " + OtashuDatabaseHelper.COLUMN_ID + " FROM "
                + OtashuDatabaseHelper.TABLE_EDGES;

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // select all edges from database
        Cursor cursor = db.rawQuery(query, null);

        Edge edge = null;
        if (cursor.moveToFirst()) {
            do {
                // create edge objects based on edge data from database
                edge = new Edge();
                edge.setId(Long.parseLong(cursor.getString(0)));

                // add edge to edges list
                edges.add(edge.getId());
            } while (cursor.moveToNext());
        }

        return edges;
    }

    public Edge getEdge(long edgeId) {
        Edge edge = new Edge();

        String query = "SELECT * FROM " + OtashuDatabaseHelper.TABLE_EDGES + " WHERE "
                + OtashuDatabaseHelper.COLUMN_ID + "=" + edgeId;

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // select all edges from database
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                // create edge objects based on edge data from database
                edge = new Edge();
                edge.setId(cursor.getLong(0));
                edge.setGraphId(cursor.getLong(1));
                edge.setEmotionId(cursor.getLong(2));
                edge.setFromNodeId(cursor.getInt(3));
                edge.setToNodeId(cursor.getInt(4));
                edge.setWeight(cursor.getFloat(5));
                edge.setPosition(cursor.getInt(6));
            } while (cursor.moveToNext());
        }

        return edge;
    }

    public Edge getEdge(long graphId, int fromNodeId, int toNodeId) {
        Edge edge = new Edge();
        edge.setWeight(-1.0f);

        String query = "SELECT * FROM " + OtashuDatabaseHelper.TABLE_EDGES + " WHERE "
                + OtashuDatabaseHelper.COLUMN_GRAPH_ID + "=" + graphId + " AND "
                + OtashuDatabaseHelper.COLUMN_FROM_NODE_ID + "=" + fromNodeId + " AND "
                + OtashuDatabaseHelper.COLUMN_TO_NODE_ID + "=" + toNodeId;

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // select all edges from database
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                // create edge objects based on edge data from database
                edge = new Edge();
                edge.setId(cursor.getLong(0));
                edge.setGraphId(cursor.getLong(1));
                edge.setEmotionId(cursor.getLong(2));
                edge.setFromNodeId(cursor.getInt(3));
                edge.setToNodeId(cursor.getInt(4));
                edge.setWeight(cursor.getFloat(5));
                edge.setPosition(cursor.getInt(6));
            } while (cursor.moveToNext());
        }

        return edge;
    }

    public Edge getEdge(long graphId, long emotionId, int fromNodeId, int toNodeId) {
        Edge edge = new Edge();
        edge.setWeight(-1.0f);

        String query = "SELECT * FROM " + OtashuDatabaseHelper.TABLE_EDGES + " WHERE "
                + OtashuDatabaseHelper.COLUMN_GRAPH_ID + "=" + graphId + " AND "
                + OtashuDatabaseHelper.COLUMN_EMOTION_ID + "=" + emotionId + " AND "
                + OtashuDatabaseHelper.COLUMN_FROM_NODE_ID + "=" + fromNodeId + " AND "
                + OtashuDatabaseHelper.COLUMN_TO_NODE_ID + "=" + toNodeId;

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // select all edges from database
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                // create edge objects based on edge data from database
                edge = new Edge();
                edge.setId(cursor.getLong(0));
                edge.setGraphId(cursor.getLong(1));
                edge.setEmotionId(cursor.getLong(2));
                edge.setFromNodeId(cursor.getInt(3));
                edge.setToNodeId(cursor.getInt(4));
                edge.setWeight(cursor.getFloat(5));
                edge.setPosition(cursor.getInt(6));
            } while (cursor.moveToNext());
        }

        return edge;
    }

    public Edge updateEdge(Edge edge) {

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(OtashuDatabaseHelper.COLUMN_ID, edge.getId());
        contentValues.put(OtashuDatabaseHelper.COLUMN_GRAPH_ID, edge.getGraphId());
        contentValues.put(OtashuDatabaseHelper.COLUMN_EMOTION_ID, edge.getEmotionId());
        contentValues.put(OtashuDatabaseHelper.COLUMN_FROM_NODE_ID, edge.getFromNodeId());
        contentValues.put(OtashuDatabaseHelper.COLUMN_TO_NODE_ID, edge.getToNodeId());
        contentValues.put(OtashuDatabaseHelper.COLUMN_WEIGHT, edge.getWeight());
        contentValues.put(OtashuDatabaseHelper.COLUMN_POSITION, edge.getPosition());

        db.update(OtashuDatabaseHelper.TABLE_EDGES, contentValues, OtashuDatabaseHelper.COLUMN_ID
                + "=" + edge.getId(), null);

        return edge;
    }

    public Edge getRandomEdge() {
        Edge edge = new Edge();

        // get all edges first
        List<Edge> allEdges = getAllEdges();

        // choose random edge
        int chosenIndex = new Random().nextInt(allEdges.size());

        edge = allEdges.get(chosenIndex);

        return edge;
    }
}
