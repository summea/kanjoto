
package com.andrewsummers.kanjoto.data;

import java.util.ArrayList;
import java.util.List;

import com.andrewsummers.kanjoto.model.Vertex;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class VerticesDataSource {
    private SQLiteDatabase database;
    private KanjotoDatabaseHelper dbHelper;

    // database table columns
    private String[] allColumns = {
            KanjotoDatabaseHelper.COLUMN_ID,
            KanjotoDatabaseHelper.COLUMN_NODE,
    };

    /**
     * VerticesDataSource constructor.
     * 
     * @param context Current state.
     */
    public VerticesDataSource(Context context) {
        dbHelper = new KanjotoDatabaseHelper(context);
    }

    /**
     * VerticesDataSource constructor.
     * 
     * @param context Current state.
     * @param databaseName Database to use.
     */
    public VerticesDataSource(Context context, String databaseName) {
        dbHelper = new KanjotoDatabaseHelper(context, databaseName);
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
     * Create vertex row in database.
     * 
     * @param vertexvalues String of vertex values to insert.
     * @return Vertex of newly-created vertex data.
     */
    public Vertex createVertex(Vertex vertex) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(KanjotoDatabaseHelper.COLUMN_NODE, vertex.getNode());

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        long insertId = db.insert(KanjotoDatabaseHelper.TABLE_VERTICES, null,
                contentValues);

        Cursor cursor = db.query(
                KanjotoDatabaseHelper.TABLE_VERTICES, allColumns,
                KanjotoDatabaseHelper.COLUMN_ID + " = " + insertId, null,
                null, null, null);

        cursor.moveToFirst();
        Vertex newVertex = cursorToVertex(cursor);
        cursor.close();
        db.close();

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
        db.delete(KanjotoDatabaseHelper.TABLE_VERTICES,
                KanjotoDatabaseHelper.COLUMN_ID + " = " + id, null);

        db.close();
    }

    /**
     * Get all vertices from database table.
     * 
     * @return List of Vertices.
     */
    public List<Vertex> getAllVertices() {
        List<Vertex> vertices = new ArrayList<Vertex>();

        String query = "SELECT * FROM " + KanjotoDatabaseHelper.TABLE_VERTICES;

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // select all notes from database
        Cursor cursor = db.rawQuery(query, null);

        Vertex vertex = null;
        if (cursor.moveToFirst()) {
            do {
                // create note objects based on note data from database
                vertex = new Vertex();
                vertex.setId(cursor.getLong(0));
                vertex.setNode(cursor.getInt(1));

                // add note string to list of strings
                vertices.add(vertex);
            } while (cursor.moveToNext());
        }

        db.close();

        return vertices;
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

    public Vertex getVertex(int vertexNodeId) {
        Vertex vertex = new Vertex();

        String query = "SELECT * FROM " + KanjotoDatabaseHelper.TABLE_VERTICES
                + " WHERE " + KanjotoDatabaseHelper.COLUMN_NODE + "=?";

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // select all vertices from database
        Cursor cursor = db.rawQuery(query, new String[] {
                String.valueOf(vertexNodeId),
        });

        if (cursor.moveToFirst()) {
            do {
                // create vertex objects based on vertex data from database
                vertex = new Vertex();
                vertex.setId(cursor.getLong(0));
                vertex.setNode(cursor.getInt(1));
            } while (cursor.moveToNext());
        }

        db.close();

        return vertex;
    }

    public Vertex updateVertex(Vertex vertex) {

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(KanjotoDatabaseHelper.COLUMN_ID, vertex.getId());
        contentValues.put(KanjotoDatabaseHelper.COLUMN_NODE, vertex.getNode());

        db.update(KanjotoDatabaseHelper.TABLE_VERTICES, contentValues,
                KanjotoDatabaseHelper.COLUMN_ID
                        + "=" + vertex.getId(), null);

        db.close();

        return vertex;
    }

    public SQLiteDatabase getDatabase() {
        return database;
    }

    public void setDatabase(SQLiteDatabase database) {
        this.database = database;
    }
}
