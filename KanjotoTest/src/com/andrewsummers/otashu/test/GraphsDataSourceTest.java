
package com.andrewsummers.otashu.test;

import java.util.ArrayList;
import java.util.List;

import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;

import com.andrewsummers.otashu.data.GraphsDataSource;
import com.andrewsummers.otashu.data.OtashuDatabaseHelper;
import com.andrewsummers.otashu.model.Graph;

public class GraphsDataSourceTest extends AndroidTestCase {

    private GraphsDataSource gds;
    private OtashuDatabaseHelper db;
    private RenamingDelegatingContext context;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        context = new RenamingDelegatingContext(getContext(), "test_");
        db = new OtashuDatabaseHelper(context);
        gds = new GraphsDataSource(context);
    }

    @Override
    public void tearDown() throws Exception {
        db.close();
        super.tearDown();
    }

    public void test_createGraph_paramGraph() throws Throwable {
        Graph graph = new Graph();
        graph.setName("test graph");
        graph.setLabelId(1);
        Graph result = gds.createGraph(graph);
        assertNotNull("created graph is not null", result);
    }

    public void test_deleteGraph_paramGraph() throws Throwable {
        Graph graph = new Graph();
        graph.setId(1);
        graph.setName("test graph");
        graph.setLabelId(1);
        Graph result = gds.createGraph(graph);
        assertNotNull("created graph is not null", result);
        gds.deleteGraph(graph);
        graph = new Graph();
        graph = gds.getGraph(1);
        assertTrue(graph.getId() != 1);
    }

    public void test_getAllGraphs() throws Throwable {
        test_createGraph_paramGraph();
        List<Graph> graphs = new ArrayList<Graph>();
        graphs = gds.getAllGraphs();
        assertNotNull("get all graphs is not null", graphs);
        assertFalse(graphs.isEmpty());
    }

    public void test_getAllGraphListDBTableIds() throws Throwable {
        test_createGraph_paramGraph();
        List<Long> graphs = gds.getAllGraphListDBTableIds();
        assertNotNull("get all graphs is not null", graphs);
        assertFalse(graphs.isEmpty());
    }

    public void test_getGraph_paramGraphId() throws Throwable {
        test_createGraph_paramGraph();
        Graph graph = gds.getGraph(1);
        assertNotNull("get graph is not null", graph);
    }

    public void test_updateGraph_paramGraph() throws Throwable {
        test_createGraph_paramGraph();
        Graph graph = gds.getGraph(1);
        graph.setName("another");
        gds.updateGraph(graph);
        Graph graph2 = gds.getGraph(1);
        assertNotNull("update graph is not null", graph2);
        assertTrue(graph.getName().equals("another"));
    }
}
