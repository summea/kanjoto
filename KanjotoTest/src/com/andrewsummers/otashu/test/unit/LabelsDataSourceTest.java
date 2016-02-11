
package com.andrewsummers.otashu.test.unit;

import java.util.ArrayList;
import java.util.List;

import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;

import com.andrewsummers.otashu.data.LabelsDataSource;
import com.andrewsummers.otashu.data.OtashuDatabaseHelper;
import com.andrewsummers.otashu.model.Label;

public class LabelsDataSourceTest extends AndroidTestCase {

    private LabelsDataSource lds;
    private OtashuDatabaseHelper db;
    private RenamingDelegatingContext context;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        context = new RenamingDelegatingContext(getContext(), "test_");
        db = new OtashuDatabaseHelper(context);
        lds = new LabelsDataSource(context);
    }

    @Override
    public void tearDown() throws Exception {
        db.close();
        super.tearDown();
    }

    public void test_createLabel_paramLabel() throws Throwable {
        Label label = new Label();
        label.setName("testcolor");
        label.setColor("#ff0000");
        Label result = lds.createLabel(label);
        assertNotNull("created label is not null", result);
    }

    public void test_deleteLabel_paramLabel() throws Throwable {
        Label label = new Label();
        label.setId(1);
        label.setName("testcolor");
        label.setColor("#ff0000");
        Label result = lds.createLabel(label);
        assertNotNull("created label is not null", result);
        lds.deleteLabel(label);
        label = new Label();
        label = lds.getLabel(1);
        assertTrue(label.getId() != 1);
    }

    public void test_getAllLabels() throws Throwable {
        test_createLabel_paramLabel();
        List<Label> labels = new ArrayList<Label>();
        labels = lds.getAllLabels();
        assertNotNull("get all labels is not null", labels);
        assertFalse(labels.isEmpty());
    }

    public void test_getAllLabelListPreviews() throws Throwable {
        test_createLabel_paramLabel();
        List<String> labels = lds.getAllLabelListPreviews();
        assertNotNull("get all label list previews is not null", labels);
        assertFalse(labels.isEmpty());
    }

    public void test_getAllLabelListDBTableIds() throws Throwable {
        test_createLabel_paramLabel();
        List<Long> labels = lds.getAllLabelListDBTableIds();
        assertNotNull("get all label list db table ids is not null", labels);
        assertFalse(labels.isEmpty());
    }

    public void test_getLabel_paramLabelId() throws Throwable {
        test_createLabel_paramLabel();
        Label label = lds.getLabel(1);
        assertNotNull("get label is not null", label);
    }

    public void test_updateLabel_paramLabel() throws Throwable {
        test_createLabel_paramLabel();
        Label label = lds.getLabel(1);
        label.setName("another");
        lds.updateLabel(label);
        Label label2 = lds.getLabel(1);
        assertNotNull("update label is not null", label2);
        assertTrue(label.getName().equals("another"));
    }
}
