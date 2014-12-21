
package com.andrewsummers.otashu.data;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import com.andrewsummers.otashu.model.Vertex;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class VerticesDataSource {
    private SQLiteDatabase database;
    private OtashuDatabaseHelper dbHelper;

    // database table columns
    private String[] allColumns = {
            OtashuDatabaseHelper.COLUMN_ID,
            OtashuDatabaseHelper.COLUMN_NODE,
    };

    /**
     * VerticesDataSource constructor.
     * 
     * @param context Current state.
     */
    public VerticesDataSource(Context context) {
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
     * Create vertex row in database.
     * 
     * @param vertexvalues String of vertex values to insert.
     * @return Vertex of newly-created vertex data.
     */
    public Vertex createVertex(Vertex vertex) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(OtashuDatabaseHelper.COLUMN_NODE, vertex.getNode());

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        long insertId = db.insert(OtashuDatabaseHelper.TABLE_VERTICES, null,
                contentValues);

        Cursor cursor = db.query(
                OtashuDatabaseHelper.TABLE_VERTICES, allColumns,
                OtashuDatabaseHelper.COLUMN_ID + " = " + insertId, null,
                null, null, null);

        cursor.moveToFirst();
        Vertex newVertex = cursorToVertex(cursor);
        cursor.close();
        return newVertex;
    }

    /**
     * Delete vertex row from database.
     * 
     * @param vertex Vertex to delete.
     */
    public void deleteVertex(Vertex vertex) {
        long id = vertex.getId();

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // delete vertex
        db.delete(OtashuDatabaseHelper.TABLE_VERTICES,
                OtashuDatabaseHelper.COLUMN_ID + " = " + id, null);
    }

    /**
     * Get all vertices from database table.
     * 
     * @return List of Vertices.
     */
    public List<Vertex> getAllVertices() {
        List<Vertex> vertices = new ArrayList<Vertex>();

        String query = "SELECT * FROM " + OtashuDatabaseHelper.TABLE_VERTICES;

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // select all notes from database
        Cursor cursor = db.rawQuery(query, null);

        Vertex vertex = null;
        if (cursor.moveToFirst()) {
            do {
                // create note objects based on note data from database
                vertex = new Vertex();
                vertex.setId(Integer.parseInt(cursor.getString(0)));
                vertex.setNode(cursor.getInt(1));

                // add note string to list of strings
                vertices.add(vertex);
            } while (cursor.moveToNext());
        }

        return vertices;
    }

    /**
     * Get all vertex ids from database table.
     * 
     * @return List of Vertices ids.
     */
    public List<Integer> getAllVertexIds() {
        List<Integer> vertex_ids = new ArrayList<Integer>();

        Cursor cursor = database.query(
                OtashuDatabaseHelper.TABLE_VERTICES, allColumns, null,
                null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Vertex vertex = cursorToVertex(cursor);
            vertex_ids.add((int) vertex.getId());
            cursor.moveToNext();
        }

        cursor.close();
        return vertex_ids;
    }

    /**
     * Access column data at current position of result.
     * 
     * @param cursor Current cursor location.
     * @return Vertex
     */
    private Vertex cursorToVertex(Cursor cursor) {
        Vertex vertex = new Vertex();
        vertex.setId(cursor.getLong(0));
        vertex.setNode(cursor.getInt(1));
        return vertex;
    }

    /**
     * getAllVertices gets a preview list of all vertices.
     * 
     * @return List of Vertex preview strings.
     */
    public List<String> getAllVertexListPreviews() {
        List<String> vertices = new LinkedList<String>();

        String query = "SELECT * FROM " + OtashuDatabaseHelper.TABLE_VERTICES;

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // select all vertices from database
        Cursor cursor = db.rawQuery(query, null);

        Vertex vertex = null;
        if (cursor.moveToFirst()) {
            do {
                // create vertex objects based on vertex data from database
                vertex = new Vertex();
                vertex.setId(Integer.parseInt(cursor.getString(0)));
                vertex.setNode(cursor.getInt(1));

                // add vertex string to list of strings
                vertices.add(vertex.toString());
            } while (cursor.moveToNext());
        }

        return vertices;
    }

    /**
     * Get a list of all vertices ids.
     * 
     * @return List of Vertex ids.
     */
    public List<Long> getAllVertexListDBTableIds() {
        List<Long> vertices = new LinkedList<Long>();

        String query = "SELECT " + OtashuDatabaseHelper.COLUMN_ID + " FROM "
                + OtashuDatabaseHelper.TABLE_VERTICES;

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // select all vertices from database
        Cursor cursor = db.rawQuery(query, null);

        Vertex vertex = null;
        if (cursor.moveToFirst()) {
            do {
                // create vertex objects based on vertex data from database
                vertex = new Vertex();
                vertex.setId(Long.parseLong(cursor.getString(0)));

                // add vertex to vertices list
                vertices.add(vertex.getId());
            } while (cursor.moveToNext());
        }

        return vertices;
    }

    public Vertex getVertex(long vertexId) {
        Vertex vertex = new Vertex();

        String query = "SELECT * FROM " + OtashuDatabaseHelper.TABLE_VERTICES + " WHERE "
                + OtashuDatabaseHelper.COLUMN_ID + "=" + vertexId;

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // select all vertices from database
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                // create vertex objects based on vertex data from database
                vertex = new Vertex();
                vertex.setId(Integer.parseInt(cursor.getString(0)));
                vertex.setNode(cursor.getInt(1));
            } while (cursor.moveToNext());
        }

        return vertex;
    }

    public Vertex updateVertex(Vertex vertex) {

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(OtashuDatabaseHelper.COLUMN_ID, vertex.getId());
        contentValues.put(OtashuDatabaseHelper.COLUMN_NODE, vertex.getNode());

        db.update(OtashuDatabaseHelper.TABLE_VERTICES, contentValues, OtashuDatabaseHelper.COLUMN_ID
                + "=" + vertex.getId(), null);

        return vertex;
    }

    public Vertex getRandomVertex() {
        Vertex vertex = new Vertex();

        // get all vertices first
        List<Vertex> allVertices = getAllVertices();

        // choose random vertex
        int chosenIndex = new Random().nextInt(allVertices.size());

        vertex = allVertices.get(chosenIndex);

        return vertex;
    }
}
