
package com.summea.kanjoto.test.unit;

import java.util.ArrayList;
import java.util.List;

import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;

import com.summea.kanjoto.data.KanjotoDatabaseHelper;
import com.summea.kanjoto.data.VerticesDataSource;
import com.summea.kanjoto.model.Vertex;

public class VerticesDataSourceTest extends AndroidTestCase {

    private VerticesDataSource vds;
    private KanjotoDatabaseHelper db;
    private RenamingDelegatingContext context;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        context = new RenamingDelegatingContext(getContext(), "test_");
        db = new KanjotoDatabaseHelper(context);
        vds = new VerticesDataSource(context);
    }

    @Override
    public void tearDown() throws Exception {
        db.close();
        super.tearDown();
    }

    public void test_createVertex_paramVertex() throws Throwable {
        Vertex learningStyle = new Vertex();
        learningStyle.setNode(60);
        Vertex result = vds.createVertex(learningStyle);
        assertNotNull("created vertex is not null", result);
    }

    public void test_deleteVertex_paramVertex() throws Throwable {
        Vertex learningStyle = new Vertex();
        learningStyle.setId(1);
        learningStyle.setNode(60);
        Vertex result = vds.createVertex(learningStyle);
        assertNotNull("created vertex is not null", result);
        vds.deleteVertex(learningStyle);
        learningStyle = new Vertex();
        learningStyle = vds.getVertex(1);
        assertTrue(learningStyle.getId() != 1);
    }

    public void test_getAllVertices() throws Throwable {
        test_createVertex_paramVertex();
        List<Vertex> learningStyles = new ArrayList<Vertex>();
        learningStyles = vds.getAllVertices();
        assertNotNull("get all vertices is not null", learningStyles);
        assertFalse(learningStyles.isEmpty());
    }

    public void test_getVertex_paramVertexId() throws Throwable {
        test_createVertex_paramVertex();
        Vertex learningStyle = vds.getVertex(1);
        assertNotNull("get vertex is not null", learningStyle);
    }

    public void test_updateVertex_paramVertex() throws Throwable {
        test_createVertex_paramVertex();
        Vertex learningStyle = vds.getVertex(1);
        learningStyle.setNode(61);
        vds.updateVertex(learningStyle);
        Vertex learningStyle2 = vds.getVertex(1);
        assertNotNull("update vertex is not null", learningStyle2);
        assertTrue(learningStyle.getNode() == 61);
    }
}
