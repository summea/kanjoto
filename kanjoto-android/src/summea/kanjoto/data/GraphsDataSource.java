
package summea.kanjoto.data;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import summea.kanjoto.model.Graph;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class GraphsDataSource {
    private KanjotoDatabaseHelper dbHelper;

    // database table columns
    private String[] allColumns = {
            KanjotoDatabaseHelper.COLUMN_ID,
            KanjotoDatabaseHelper.COLUMN_NAME,
            KanjotoDatabaseHelper.COLUMN_LABEL_ID,
    };

    /**
     * GraphsDataSource constructor.
     * 
     * @param context Current state.
     */
    public GraphsDataSource(Context context) {
        dbHelper = new KanjotoDatabaseHelper(context);
    }

    /**
     * GraphsDataSource constructor.
     * 
     * @param context Current state.
     * @param databaseName Database to use.
     */
    public GraphsDataSource(Context context, String databaseName) {
        dbHelper = new KanjotoDatabaseHelper(context, databaseName);
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
     * Create graph row in database.
     * 
     * @param graphvalues String of graph values to insert.
     * @return Graph of newly-created graph data.
     */
    public Graph createGraph(Graph graph) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(KanjotoDatabaseHelper.COLUMN_NAME, graph.getName());
        contentValues.put(KanjotoDatabaseHelper.COLUMN_LABEL_ID, graph.getLabelId());

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        long insertId = db.insert(KanjotoDatabaseHelper.TABLE_GRAPHS, null,
                contentValues);

        Cursor cursor = db.query(
                KanjotoDatabaseHelper.TABLE_GRAPHS, allColumns,
                KanjotoDatabaseHelper.COLUMN_ID + " = " + insertId, null,
                null, null, null);

        cursor.moveToFirst();
        Graph newGraph = cursorToGraph(cursor);
        cursor.close();
        db.close();

        return newGraph;
    }

    /**
     * Delete graph row from database.
     * 
     * @param graph Graph to delete.
     */
    public void deleteGraph(Graph graph) {
        long id = graph.getId();

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // delete graph
        db.delete(KanjotoDatabaseHelper.TABLE_GRAPHS,
                KanjotoDatabaseHelper.COLUMN_ID + " = " + id, null);

        db.close();
    }

    /**
     * Get all graphs from database table.
     * 
     * @return List of Graphs.
     */
    public List<Graph> getAllGraphs() {
        List<Graph> graphs = new ArrayList<Graph>();

        String query = "SELECT * FROM " + KanjotoDatabaseHelper.TABLE_GRAPHS;

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // select all notes from database
        Cursor cursor = db.rawQuery(query, null);

        Graph graph = null;
        if (cursor.moveToFirst()) {
            do {
                // create note objects based on note data from database
                graph = new Graph();
                graph.setId(cursor.getLong(0));
                graph.setName(cursor.getString(1));
                graph.setLabelId(cursor.getLong(2));

                // add note string to list of strings
                graphs.add(graph);
            } while (cursor.moveToNext());
        }

        db.close();

        return graphs;
    }

    /**
     * Access column data at current position of result.
     * 
     * @param cursor Current cursor location.
     * @return Graph
     */
    private Graph cursorToGraph(Cursor cursor) {
        Graph graph = new Graph();
        graph.setId(cursor.getLong(0));
        graph.setName(cursor.getString(1));
        graph.setLabelId(cursor.getLong(2));
        return graph;
    }

    /**
     * Get a list of all graphs ids.
     * 
     * @return List of Graph ids.
     */
    public List<Long> getAllGraphListDBTableIds() {
        List<Long> graphs = new LinkedList<Long>();

        String query = "SELECT " + KanjotoDatabaseHelper.COLUMN_ID + " FROM "
                + KanjotoDatabaseHelper.TABLE_GRAPHS;

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // select all graphs from database
        Cursor cursor = db.rawQuery(query, null);

        Graph graph = null;
        if (cursor.moveToFirst()) {
            do {
                // create graph objects based on graph data from database
                graph = new Graph();
                graph.setId(cursor.getLong(0));
                graph.setName(cursor.getString(1));
                graph.setLabelId(cursor.getLong(2));

                // add graph to graphs list
                graphs.add(graph.getId());
            } while (cursor.moveToNext());
        }

        db.close();

        return graphs;
    }

    public Graph getGraph(long graphId) {
        Graph graph = new Graph();

        String query = "SELECT * FROM " + KanjotoDatabaseHelper.TABLE_GRAPHS + " WHERE "
                + KanjotoDatabaseHelper.COLUMN_ID + "=?";

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // select all graphs from database
        Cursor cursor = db.rawQuery(query, new String[] {
                String.valueOf(graphId),
        });

        if (cursor.moveToFirst()) {
            do {
                // create graph objects based on graph data from database
                graph = new Graph();
                graph.setId(cursor.getLong(0));
                graph.setName(cursor.getString(1));
                graph.setLabelId(cursor.getLong(2));
            } while (cursor.moveToNext());
        }

        db.close();

        return graph;
    }

    public Graph updateGraph(Graph graph) {

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(KanjotoDatabaseHelper.COLUMN_ID, graph.getId());
        contentValues.put(KanjotoDatabaseHelper.COLUMN_NAME, graph.getName());
        contentValues.put(KanjotoDatabaseHelper.COLUMN_LABEL_ID, graph.getLabelId());

        db.update(KanjotoDatabaseHelper.TABLE_GRAPHS, contentValues, KanjotoDatabaseHelper.COLUMN_ID
                + "=" + graph.getId(), null);

        db.close();

        return graph;
    }
}
