
package com.andrewsummers.otashu.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import com.andrewsummers.otashu.model.Edge;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.util.SparseArray;

/**
 * The EdgesDataSource class provides a way to interact with the Edges database table. This class is
 * primarily used to create, read, update, or delete data from the database table.
 */
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
            OtashuDatabaseHelper.COLUMN_APPRENTICE_ID,
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
     * EdgesDataSource constructor.
     * 
     * @param context Current state.
     * @param databaseName Database to use.
     */
    public EdgesDataSource(Context context, String databaseName) {
        dbHelper = new OtashuDatabaseHelper(context, databaseName);
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
        contentValues.put(OtashuDatabaseHelper.COLUMN_APPRENTICE_ID, edge.getApprenticeId());

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
        db.close();

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

        db.close();
    }

    /**
     * Get all edges from database table.
     * 
     * @return List of Edges.
     */
    public List<Edge> getAllEdgesByApprentice(long apprenticeId) {
        List<Edge> edges = new ArrayList<Edge>();

        String query = "SELECT * FROM " + OtashuDatabaseHelper.TABLE_EDGES
                + " WHERE " + OtashuDatabaseHelper.COLUMN_APPRENTICE_ID + "=?";

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // select all notes from database
        Cursor cursor = db.rawQuery(query, new String[] {
                String.valueOf(apprenticeId)
        });

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
                edge.setApprenticeId(cursor.getLong(7));

                // add note string to list of strings
                edges.add(edge);
            } while (cursor.moveToNext());
        }

        db.close();

        return edges;
    }

    /**
     * Get all edges from database table.
     * 
     * @param graphId
     * @param emotionId
     * @return List of Edges.
     */
    public List<Edge> getAllEdges(long apprenticeId, long graphId, long emotionId) {
        List<Edge> edges = new ArrayList<Edge>();

        String query = "SELECT * FROM " + OtashuDatabaseHelper.TABLE_EDGES
                + " WHERE " + OtashuDatabaseHelper.COLUMN_APPRENTICE_ID + "=?"
                + " AND " + OtashuDatabaseHelper.COLUMN_GRAPH_ID + "=?"
                + " AND " + OtashuDatabaseHelper.COLUMN_EMOTION_ID + "=?";

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // select all notes from database
        Cursor cursor = db.rawQuery(query, new String[] {
                String.valueOf(apprenticeId),
                String.valueOf(graphId),
                String.valueOf(emotionId)
        });

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
                edge.setApprenticeId(cursor.getLong(7));

                // add note string to list of strings
                edges.add(edge);
            } while (cursor.moveToNext());
        }

        db.close();

        return edges;
    }

    public List<Edge> getAllEdgesWithoutEmotionId(long apprenticeId, long graphId, int position) {
        List<Edge> edges = new ArrayList<Edge>();

        String query = "SELECT * FROM " + OtashuDatabaseHelper.TABLE_EDGES
                + " WHERE " + OtashuDatabaseHelper.COLUMN_APPRENTICE_ID + "=?"
                + " AND " + OtashuDatabaseHelper.COLUMN_GRAPH_ID + "=?"
                + " AND " + OtashuDatabaseHelper.COLUMN_POSITION + "=?";

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // select all notes from database
        Cursor cursor = db.rawQuery(query, new String[] {
                String.valueOf(apprenticeId),
                String.valueOf(graphId),
                String.valueOf(position)
        });

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
                edge.setApprenticeId(cursor.getLong(7));

                // add note string to list of strings
                edges.add(edge);
            } while (cursor.moveToNext());
        }

        db.close();

        return edges;
    }

    /**
     * Get all edges from database table.
     * 
     * @param graphId
     * @param emotionId
     * @param weightLimit
     * @param position
     * @return List of Edges.
     */
    public List<Edge> getAllEdges(long apprenticeId, long graphId, long emotionId,
            float weightLimit, int position) {
        List<Edge> edges = new ArrayList<Edge>();

        String query = "SELECT * FROM " + OtashuDatabaseHelper.TABLE_EDGES
                + " WHERE " + OtashuDatabaseHelper.COLUMN_APPRENTICE_ID + "=?"
                + " AND " + OtashuDatabaseHelper.COLUMN_GRAPH_ID + "=?"
                + " AND " + OtashuDatabaseHelper.COLUMN_EMOTION_ID + "=?"
                + " AND " + OtashuDatabaseHelper.COLUMN_WEIGHT + "<?"
                + " AND " + OtashuDatabaseHelper.COLUMN_POSITION + "=?"
                + " ORDER BY " + OtashuDatabaseHelper.COLUMN_WEIGHT + " ASC";

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // select all notes from database
        Cursor cursor = db.rawQuery(query, new String[] {
                String.valueOf(apprenticeId),
                String.valueOf(graphId),
                String.valueOf(emotionId),
                String.valueOf(weightLimit),
                String.valueOf(position)
        });

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
                edge.setApprenticeId(cursor.getLong(7));

                // add note string to list of strings
                edges.add(edge);
            } while (cursor.moveToNext());
        }

        db.close();

        return edges;
    }

    /**
     * Get all edges from database table.
     * 
     * @param graphId
     * @param emotionId
     * @param fromNodeId
     * @param toNodeId
     * @return List of Edges.
     */
    public List<Edge> getAllEdges(long apprenticeId, long graphId, long emotionId, int fromNodeId,
            int toNodeId,
            int position, int mode) {

        if (mode < 0) {
            mode = 1;
        }

        List<Edge> edges = new ArrayList<Edge>();

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Cursor cursor = null;

        // TODO: might be time to re-think this part...
        String query = "";
        // select all notes from database
        switch (mode) {
            case 0:
                query = "SELECT * FROM " + OtashuDatabaseHelper.TABLE_EDGES
                        + " WHERE " + OtashuDatabaseHelper.COLUMN_APPRENTICE_ID + "=?"
                        + " AND " + OtashuDatabaseHelper.COLUMN_GRAPH_ID + "=?"
                        + " AND " + OtashuDatabaseHelper.COLUMN_EMOTION_ID + "=?"
                        + " AND " + OtashuDatabaseHelper.COLUMN_POSITION + "=?";
                cursor = db.rawQuery(query, new String[] {
                        String.valueOf(apprenticeId),
                        String.valueOf(graphId),
                        String.valueOf(emotionId),
                        String.valueOf(position),
                });
                break;
            case 1:
                query = "SELECT * FROM " + OtashuDatabaseHelper.TABLE_EDGES
                        + " WHERE " + OtashuDatabaseHelper.COLUMN_APPRENTICE_ID + "=?"
                        + " AND " + OtashuDatabaseHelper.COLUMN_GRAPH_ID + "=?"
                        + " AND " + OtashuDatabaseHelper.COLUMN_EMOTION_ID + "=?"
                        + " AND " + OtashuDatabaseHelper.COLUMN_FROM_NODE_ID + "=?"
                        + " AND " + OtashuDatabaseHelper.COLUMN_POSITION + "=?";
                cursor = db.rawQuery(query, new String[] {
                        String.valueOf(apprenticeId),
                        String.valueOf(graphId),
                        String.valueOf(emotionId),
                        String.valueOf(fromNodeId),
                        String.valueOf(position),
                });
                break;
            case 2:
                query = "SELECT * FROM " + OtashuDatabaseHelper.TABLE_EDGES
                        + " WHERE " + OtashuDatabaseHelper.COLUMN_APPRENTICE_ID + "=?"
                        + " AND " + OtashuDatabaseHelper.COLUMN_GRAPH_ID + "=?"
                        + " AND " + OtashuDatabaseHelper.COLUMN_EMOTION_ID + "=?"
                        + " AND " + OtashuDatabaseHelper.COLUMN_TO_NODE_ID + "=?"
                        + " AND " + OtashuDatabaseHelper.COLUMN_POSITION + "=?";
                cursor = db.rawQuery(query, new String[] {
                        String.valueOf(apprenticeId),
                        String.valueOf(graphId),
                        String.valueOf(emotionId),
                        String.valueOf(toNodeId),
                        String.valueOf(position),
                });
                break;
            case 3:
                query = "SELECT * FROM " + OtashuDatabaseHelper.TABLE_EDGES
                        + " WHERE " + OtashuDatabaseHelper.COLUMN_APPRENTICE_ID + "=?"
                        + " AND " + OtashuDatabaseHelper.COLUMN_GRAPH_ID + "=?"
                        + " AND " + OtashuDatabaseHelper.COLUMN_EMOTION_ID + "=?"
                        + " AND " + OtashuDatabaseHelper.COLUMN_FROM_NODE_ID + "=?"
                        + " AND " + OtashuDatabaseHelper.COLUMN_POSITION + "=?";
                cursor = db.rawQuery(query, new String[] {
                        String.valueOf(apprenticeId),
                        String.valueOf(graphId),
                        String.valueOf(emotionId),
                        String.valueOf(fromNodeId),
                        String.valueOf(position),
                });
                break;
            case 4:
                query = "SELECT * FROM " + OtashuDatabaseHelper.TABLE_EDGES
                        + " WHERE " + OtashuDatabaseHelper.COLUMN_APPRENTICE_ID + "=?"
                        + " AND " + OtashuDatabaseHelper.COLUMN_GRAPH_ID + "=?"
                        + " AND " + OtashuDatabaseHelper.COLUMN_EMOTION_ID + "=?"
                        + " AND " + OtashuDatabaseHelper.COLUMN_TO_NODE_ID + "=?"
                        + " AND " + OtashuDatabaseHelper.COLUMN_POSITION + "=?";
                cursor = db.rawQuery(query, new String[] {
                        String.valueOf(apprenticeId),
                        String.valueOf(graphId),
                        String.valueOf(emotionId),
                        String.valueOf(toNodeId),
                        String.valueOf(position),
                });
                break;
        }

        // check if any results have been found
        if (cursor.getCount() > 0) {
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
                    edge.setApprenticeId(cursor.getLong(7));

                    // add note string to list of strings
                    edges.add(edge);
                } while (cursor.moveToNext());
            }
        } else {
            query = "SELECT * FROM " + OtashuDatabaseHelper.TABLE_EDGES
                    + " WHERE " + OtashuDatabaseHelper.COLUMN_APPRENTICE_ID + "=?"
                    + " AND " + OtashuDatabaseHelper.COLUMN_GRAPH_ID + "=?"
                    + " AND " + OtashuDatabaseHelper.COLUMN_EMOTION_ID + "=?"
                    + " AND " + OtashuDatabaseHelper.COLUMN_POSITION + "=?";

            cursor = db.rawQuery(query, new String[] {
                    String.valueOf(apprenticeId),
                    String.valueOf(graphId),
                    String.valueOf(emotionId),
                    String.valueOf(position),
            });

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
                    edge.setApprenticeId(cursor.getLong(7));

                    // add note string to list of strings
                    edges.add(edge);
                } while (cursor.moveToNext());
            }
        }

        db.close();

        return edges;
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
        edge.setApprenticeId(cursor.getLong(7));
        return edge;
    }

    public Edge getEdge(long edgeId) {
        Edge edge = new Edge();

        String query = "SELECT * FROM " + OtashuDatabaseHelper.TABLE_EDGES
                + " WHERE " + OtashuDatabaseHelper.COLUMN_ID + "=?";

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // select all edges from database
        Cursor cursor = db.rawQuery(query, new String[] {
                String.valueOf(edgeId)
        });

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
                edge.setApprenticeId(cursor.getLong(7));
            } while (cursor.moveToNext());
        }

        db.close();

        return edge;
    }

    public Edge getEdge(long graphId, long emotionId, int fromNodeId,
            int toNodeId) {
        Edge edge = new Edge();
        edge.setWeight(-1.0f);

        String query = "SELECT * FROM " + OtashuDatabaseHelper.TABLE_EDGES
                + " WHERE " + OtashuDatabaseHelper.COLUMN_GRAPH_ID + "=?"
                + " AND " + OtashuDatabaseHelper.COLUMN_EMOTION_ID + "=?"
                + " AND " + OtashuDatabaseHelper.COLUMN_FROM_NODE_ID + "=?"
                + " AND " + OtashuDatabaseHelper.COLUMN_TO_NODE_ID + "=?";

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // select all edges from database
        Cursor cursor = db.rawQuery(query, new String[] {
                String.valueOf(graphId),
                String.valueOf(emotionId),
                String.valueOf(fromNodeId),
                String.valueOf(toNodeId),
        });

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
                edge.setApprenticeId(cursor.getLong(7));
            } while (cursor.moveToNext());
        }

        db.close();

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
        contentValues.put(OtashuDatabaseHelper.COLUMN_APPRENTICE_ID, edge.getApprenticeId());

        db.update(OtashuDatabaseHelper.TABLE_EDGES, contentValues, OtashuDatabaseHelper.COLUMN_ID
                + "=" + edge.getId(), null);

        db.close();

        return edge;
    }

    public Edge getRandomEdge(long apprenticeId, long graphId, long emotionId, int fromNodeId,
            int toNodeId,
            int position, int mode) {
        // get all edges first
        List<Edge> allEdges = getAllEdges(apprenticeId, graphId, emotionId, fromNodeId, toNodeId,
                position, mode);

        if (allEdges.size() > 1) {
            // Process:
            // 1. Grab two random, node-related edges
            // 2. Look for the lower weight (easier choice) between the two chosen edges
            // 3. Return the edge with the lower weight

            // choose two random edges
            int chosenIndex = new Random().nextInt(allEdges.size());
            Edge edgeOne = allEdges.get(chosenIndex);

            chosenIndex = new Random().nextInt(allEdges.size());
            Edge edgeTwo = allEdges.get(chosenIndex);

            if (edgeOne.getWeight() < edgeTwo.getWeight()) {
                // edge one has a lower weight!
                return edgeOne;
            } else {
                // edge two has a lower or equal weight!
                return edgeTwo;
            }
        } else {
            if (allEdges.size() > 0) {
                Edge edge = allEdges.get(0);
                return edge;
            } else {
                Edge edge = new Edge();
                return edge;
            }
        }
    }

    public List<Edge> getStrongPath(long apprenticeId, long graphId, long emotionId, int position,
            int fromNode, int improvisationLevel) {
        List<Edge> results = new ArrayList<Edge>();

        // int lastToNotevalue = 0;
        List<String> parameters = new ArrayList<String>();
        parameters.add(String.valueOf(apprenticeId));
        parameters.add(String.valueOf(graphId));
        parameters.add(String.valueOf(emotionId));

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // TODO: there are better ways to do this section... =
        for (int i = 0; i < 3; i++) {
            // get lowest first edge
            Cursor cursor = null;
            String query = "SELECT * FROM " + OtashuDatabaseHelper.TABLE_EDGES
                    + " WHERE " + OtashuDatabaseHelper.COLUMN_APPRENTICE_ID + "=?"
                    + " AND " + OtashuDatabaseHelper.COLUMN_GRAPH_ID + "=?"
                    + " AND " + OtashuDatabaseHelper.COLUMN_EMOTION_ID + "=?";

            if (fromNode > 0 && position > 0) {
                query += " AND " + OtashuDatabaseHelper.COLUMN_FROM_NODE_ID + "=?";
                query += " AND " + OtashuDatabaseHelper.COLUMN_POSITION + "=?";
                query += " ORDER BY RANDOM() LIMIT 3";
                // select all edge from database
                cursor = db.rawQuery(query, new String[] {
                        String.valueOf(apprenticeId),
                        String.valueOf(graphId),
                        String.valueOf(emotionId),
                        String.valueOf(fromNode),
                        String.valueOf(position),
                });
            } else if (fromNode > 0) {
                query += " AND " + OtashuDatabaseHelper.COLUMN_FROM_NODE_ID + "=?";
                query += " ORDER BY RANDOM() LIMIT 3";
                // select all edge from database
                cursor = db.rawQuery(query, new String[] {
                        String.valueOf(apprenticeId),
                        String.valueOf(graphId),
                        String.valueOf(emotionId),
                        String.valueOf(fromNode),
                });
            } else if (position > 0) {
                query += " AND " + OtashuDatabaseHelper.COLUMN_POSITION + "=?";
                query += " ORDER BY RANDOM() LIMIT 3";
                // select all edge from database
                cursor = db.rawQuery(query, new String[] {
                        String.valueOf(apprenticeId),
                        String.valueOf(graphId),
                        String.valueOf(emotionId),
                        String.valueOf(position),
                });
            } else {
                int nextI = i + 1;
                query += " AND " + OtashuDatabaseHelper.COLUMN_POSITION + "=?";
                query += " ORDER BY RANDOM() LIMIT 3";
                cursor = db.rawQuery(query, new String[] {
                        String.valueOf(apprenticeId),
                        String.valueOf(graphId),
                        String.valueOf(emotionId),
                        String.valueOf(nextI),
                });
            }

            // query selects three random emotion-related notesets
            // now, find which of the notesets here has lowest (strongest) weight
            float lastWeight = 1.0f;
            int currentStrongestRow = 0;
            if (cursor.moveToFirst()) {
                do {
                    if (cursor.getFloat(5) < lastWeight) {
                        lastWeight = cursor.getFloat(5);
                        currentStrongestRow++;
                    }
                } while (cursor.moveToNext());
            }

            try {
                if (cursor.moveToPosition(currentStrongestRow)) {
                    // create edge objects based on edge data from database
                    Edge edge = new Edge();
                    edge.setId(cursor.getLong(0));
                    edge.setGraphId(cursor.getLong(1));
                    edge.setEmotionId(cursor.getLong(2));
                    if (improvisationLevel > 0) {
                        edge.setFromNodeId(cursor.getInt(3) + improvisationLevel);
                        edge.setToNodeId(cursor.getInt(4) + improvisationLevel);
                    } else {
                        edge.setFromNodeId(cursor.getInt(3));
                        edge.setToNodeId(cursor.getInt(4));
                    }
                    edge.setWeight(cursor.getFloat(5));
                    edge.setPosition(cursor.getInt(6));
                    edge.setApprenticeId(cursor.getLong(7));
                    results.add(edge);
                    // lastToNotevalue = edge.getToNodeId();
                }
            } catch (Exception e) {
                Log.d("MYLOG", e.getStackTrace().toString());
            }
        }

        db.close();

        return results;
    }

    public Edge getStrongTransitionPath(long apprenticeId, long graphId, long emotionId,
            int fromNodeId) {
        Edge result = new Edge();

        // get lowest first edge
        String query = "SELECT * FROM " + OtashuDatabaseHelper.TABLE_EDGES
                + " WHERE " + OtashuDatabaseHelper.COLUMN_APPRENTICE_ID + "=?"
                + " AND " + OtashuDatabaseHelper.COLUMN_GRAPH_ID + "=?"
                + " AND " + OtashuDatabaseHelper.COLUMN_EMOTION_ID + "=?"
                + " AND " + OtashuDatabaseHelper.COLUMN_FROM_NODE_ID + "=?"
                + " ORDER BY " + OtashuDatabaseHelper.COLUMN_WEIGHT + " ASC LIMIT 1";

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // select all edges from database
        Cursor cursor = db.rawQuery(query, new String[] {
                String.valueOf(apprenticeId),
                String.valueOf(graphId),
                String.valueOf(emotionId),
                String.valueOf(fromNodeId),
        });

        if (cursor.moveToFirst()) {
            do {
                // create edge objects based on edge data from database
                Edge edge = new Edge();
                edge.setId(cursor.getLong(0));
                edge.setGraphId(cursor.getLong(1));
                edge.setEmotionId(cursor.getLong(2));
                edge.setFromNodeId(cursor.getInt(3));
                edge.setToNodeId(cursor.getInt(4));
                edge.setWeight(cursor.getFloat(5));
                edge.setPosition(cursor.getInt(6));
                edge.setApprenticeId(cursor.getLong(7));
                result = edge;
            } while (cursor.moveToNext());
        }

        db.close();

        return result;
    }

    public SparseArray<List<Edge>> getPathsForEmotion(long apprenticeId, long graphId,
            long emotionId,
            float weightLimit) {
        SparseArray<List<Edge>> results = new SparseArray<List<Edge>>();

        /**
         * <pre>
         * 1. Get all edges between positions 1 and 2 below the weightLimit
         * 2. Get all edges between positions 2 and 3 below the weightLimit
         * 3. Get all edges between positions 3 and 4 below the weightLimit
         * 4. Check to see which paths exist between positions 1-4
         * 5. Return these paths
         * </pre>
         */

        // select an edge position
        int position = 1;

        // select all position one edges for given emotion with given threshold (e.g. all rows that
        // have a weight less than x)
        List<Edge> p1Edges = getAllEdges(apprenticeId, graphId, emotionId, weightLimit, position);

        position = 2;
        // select all position two edges for given emotion with given threshold (e.g. all rows that
        // have a weight less than x)
        List<Edge> p2Edges = getAllEdges(apprenticeId, graphId, emotionId, weightLimit, position);

        position = 3;
        // select all position three edges for given emotion with given threshold (e.g. all rows
        // that have a weight less than x)
        List<Edge> p3Edges = getAllEdges(apprenticeId, graphId, emotionId, weightLimit, position);

        int key = 1;

        // loop through all position 1-2 edges
        for (Edge edge1 : p1Edges) {
            // loop through all position 2-3 edges and compare with first
            for (Edge edge2 : p2Edges) {
                if (edge1.getToNodeId() != edge2.getFromNodeId()) {
                    break;
                }
                // loop through all position 3-4 edges and compare with first
                for (Edge edge3 : p3Edges) {
                    if (edge2.getToNodeId() == edge3.getFromNodeId()) {
                        // complete path found!
                        List<Edge> foundEdgePath = new ArrayList<Edge>();
                        foundEdgePath.add(edge1);
                        foundEdgePath.add(edge2);
                        foundEdgePath.add(edge3);

                        results.put(key, foundEdgePath);
                        key++;
                    }
                }
            }
        }

        return results;
    }

    public HashMap<String, String> getEmotionFromNotes(long apprenticeId, long graphId,
            List<Integer> notes) {
        HashMap<String, String> result = new HashMap<String, String>();
        long emotionId = 0;
        float certainty = 0.0f;

        // select an edge position
        int position = 1;
        List<Edge> p1Edges = getAllEdgesWithoutEmotionId(apprenticeId, graphId, position);

        position = 2;
        List<Edge> p2Edges = getAllEdgesWithoutEmotionId(apprenticeId, graphId, position);

        position = 3;
        List<Edge> p3Edges = getAllEdgesWithoutEmotionId(apprenticeId, graphId, position);

        int i = 0;
        // loop through all position 1-2 edges
        for (Edge edge1 : p1Edges) {
            if (notes.get(i) == edge1.getFromNodeId()) {

                if (certainty < 25.0) {
                    emotionId = edge1.getEmotionId();
                    certainty = 25.0f;
                }

                // loop through all position 2-3 edges and compare with first
                for (Edge edge2 : p2Edges) {
                    if (notes.get(i + 1) == edge2.getFromNodeId()) {

                        if (certainty < 50.0) {
                            if (edge1.getEmotionId() == edge2.getEmotionId()) {
                                emotionId = edge2.getEmotionId();
                            } else {
                                emotionId = edge1.getEmotionId();
                            }
                            certainty = 50.0f;
                        }

                        if (edge1.getToNodeId() != edge2.getFromNodeId()) {
                            break;
                        }
                        // loop through all position 3-4 edges and compare with first
                        for (Edge edge3 : p3Edges) {
                            if (notes.get(i + 2) == edge3.getFromNodeId()) {

                                if (certainty < 75.0) {
                                    if (edge2.getEmotionId() == edge3.getEmotionId()) {
                                        emotionId = edge3.getEmotionId();
                                    } else {
                                        emotionId = edge2.getEmotionId();
                                    }
                                    certainty = 75.0f;
                                }

                                if (edge2.getToNodeId() != edge3.getFromNodeId()) {
                                    break;
                                } else {
                                    if (notes.get(i + 3) == edge3.getToNodeId()) {
                                        // complete path found!
                                        List<Edge> foundEdgePath = new ArrayList<Edge>();
                                        foundEdgePath.add(edge1);
                                        foundEdgePath.add(edge2);
                                        foundEdgePath.add(edge3);

                                        emotionId = edge3.getEmotionId();
                                        certainty = 100.0f;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        result.put("emotionId", String.valueOf(emotionId));
        result.put("certainty", String.valueOf(certainty));

        return result;
    }

    public SQLiteDatabase getDatabase() {
        return database;
    }

    public void setDatabase(SQLiteDatabase database) {
        this.database = database;
    }
}
