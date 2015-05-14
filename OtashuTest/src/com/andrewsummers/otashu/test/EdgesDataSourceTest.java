
package com.andrewsummers.otashu.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;
import android.util.SparseArray;

import com.andrewsummers.otashu.data.EdgesDataSource;
import com.andrewsummers.otashu.data.OtashuDatabaseHelper;
import com.andrewsummers.otashu.model.Edge;

public class EdgesDataSourceTest extends AndroidTestCase {

    private EdgesDataSource eds;
    private OtashuDatabaseHelper db;
    private RenamingDelegatingContext context;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        context = new RenamingDelegatingContext(getContext(), "test_");
        db = new OtashuDatabaseHelper(context);
        eds = new EdgesDataSource(context);
    }

    @Override
    public void tearDown() throws Exception {
        db.close();
        super.tearDown();
    }

    public void test_createEdge_paramEdge() throws Throwable {
        for (int i = 0; i < 4; i++) {
            Edge edge = new Edge();
            edge.setApprenticeId(1);
            edge.setEmotionId(1);
            edge.setGraphId(1);
            edge.setFromNodeId(60 + i);
            edge.setToNodeId(60 + (i + 2));
            edge.setPosition(i + 1);
            edge.setWeight(0.5f);
            Edge result = eds.createEdge(edge);
            assertNotNull("created edge is not null", result);
        }
    }

    public void test_deleteEdge_paramEdge() throws Throwable {
        Edge edge = new Edge();
        edge.setId(5);
        edge.setEmotionId(1);
        Edge result = eds.createEdge(edge);
        assertNotNull("created edge is not null", result);
        eds.deleteEdge(edge);
        edge = new Edge();
        edge = eds.getEdge(5);
        assertTrue(edge.getId() != 5);
    }

    public void test_getAllEdgesByApprentice_paramApprenticeId() throws Throwable {
        test_createEdge_paramEdge();
        List<Edge> edges = new ArrayList<Edge>();
        edges = eds.getAllEdgesByApprentice(1);
        assertNotNull("get all edges by apprentice is not null", edges);
        assertFalse(edges.isEmpty());
    }

    public void test_getAllEdges_paramApprenticeId_paramGraphId_paramEmotionId() throws Throwable {
        test_createEdge_paramEdge();
        List<Edge> edges = new ArrayList<Edge>();
        edges = eds.getAllEdges(1, 1, 1);
        assertNotNull("get all edges (apprentice id, graph id, emotion id) is not null", edges);
        assertFalse(edges.isEmpty());
    }

    public void test_getAllEdges_paramApprenticeId_paramGraphId_paramEmotionId_paramWeightLimit_paramPosition()
            throws Throwable {
        test_createEdge_paramEdge();
        List<Edge> edges = new ArrayList<Edge>();
        edges = eds.getAllEdges(1, 1, 1, 1.0f, 1);
        assertNotNull(
                "get all edges (apprentice id, graph id, emotion id, weight limit, position) is not null",
                edges);
        assertFalse(edges.isEmpty());
    }

    public void test_getAllEdges_paramApprenticeId_paramGraphId_paramEmotionId_paramFromNodeId_paramToNodeId_paramPosition_paramMode()
            throws Throwable {
        test_createEdge_paramEdge();
        List<Edge> edges = new ArrayList<Edge>();
        edges = eds.getAllEdges(1, 1, 1, 61, 63, 1, 1);
        assertNotNull(
                "get all edges (apprentice id, graph id, emotion id, from node id, to node id, position, mode) is not null",
                edges);
        assertFalse(edges.isEmpty());
    }

    public void test_getEdge_paramEdgeId() throws Throwable {
        Edge edge = eds.getEdge(1);
        assertNotNull("get edge is not null", edge);
    }

    public void test_getEdge_paramGraphId_paramEmotionId_paramFromNodeId_paramToNodeId()
            throws Throwable {
        Edge edge = eds.getEdge(1, 1, 61, 63);
        assertNotNull("get edge (graph id, emotion id, from node id, to node id) is not null", edge);
    }

    public void test_updateEdge_paramEdge() throws Throwable {
        test_createEdge_paramEdge();
        Edge edge = eds.getEdge(1);
        edge.setEmotionId(2);
        eds.updateEdge(edge);
        Edge edge2 = eds.getEdge(1);
        assertNotNull("update edge is not null", edge2);
        assertTrue(edge.getEmotionId() == 2);
    }

    public void test_getRandomEdge_paramApprenticeId_paramGraphId_paramEmotionId_paramFromNodeId_paramToNodeId_paramPosition_paramMode()
            throws Throwable {
        Edge edge = eds.getRandomEdge(1, 1, 1, 61, 63, 1, 1);
        assertNotNull(
                "get random edge (apprenticeId, graphId, emotionId, fromNodeId, toNodeId, position, mode) is not null",
                edge);
    }

    public void test_getStrongPath_paramApprenticeId_paramGraphId_paramEmotionId_paramPosition_paramFromNodeId_paramImprovisationLevel()
            throws Throwable {
        List<Edge> path = eds.getStrongPath(1, 1, 1, 1, 61, 1);
        assertNotNull(
                "get strong path (apprentice id, graph id, emotion id, position, from node id, improvisation level) is not null",
                path);
        assertFalse(path.isEmpty());
    }

    public void test_getStrongTransitionPath_paramApprenticeId_paramGraphId_paramEmotionId_paramFromNodeId()
            throws Throwable {
        Edge path = eds.getStrongTransitionPath(1, 1, 1, 61);
        assertNotNull(
                "get strong transition path (apprenticeId, graphId, emotionId, fromNodeId) is not null",
                path);
    }

    public void test_getPathsForEmotion_paramApprenticeId_paramGraphId_paramEmotionId_paramWeightLimit()
            throws Throwable {
        SparseArray<List<Edge>> paths = eds.getPathsForEmotion(1, 1, 1, 1.0f);
        assertNotNull(
                "get paths for emotion (apprentice id, graph id, emotion id, weight limit) is not null",
                paths);
        assertFalse(paths.size() <= 0);
    }

    public void test_getEmotionFromNotes_paramApprenticeId_paramGraphId_paramNotes()
            throws Throwable {
        List<Integer> notes = new ArrayList<Integer>();
        notes.add(61);
        notes.add(62);
        notes.add(63);
        notes.add(64);
        HashMap<String, String> emotion = eds.getEmotionFromNotes(1, 1, notes);
        assertNotNull(
                "get paths for emotion (apprentice id, graph id, emotion id, weight limit) is not null",
                emotion);
        assertTrue(Long.valueOf(emotion.get("emotionId")) == 1);
    }
}
