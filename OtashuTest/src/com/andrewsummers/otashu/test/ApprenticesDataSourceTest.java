
package com.andrewsummers.otashu.test;

import java.util.ArrayList;
import java.util.List;

import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;

import com.andrewsummers.otashu.data.ApprenticesDataSource;
import com.andrewsummers.otashu.data.OtashuDatabaseHelper;
import com.andrewsummers.otashu.model.Apprentice;

public class ApprenticesDataSourceTest extends AndroidTestCase {

    private ApprenticesDataSource ads;
    private OtashuDatabaseHelper db;
    private RenamingDelegatingContext context;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        context = new RenamingDelegatingContext(getContext(), "test_");
        db = new OtashuDatabaseHelper(context);
        ads = new ApprenticesDataSource(context);
    }

    @Override
    public void tearDown() throws Exception {
        db.close();
        super.tearDown();
    }

    public void test_createApprentice_paramApprentice() throws Throwable {
        Apprentice apprentice = new Apprentice();
        apprentice.setName("test apprentice");
        apprentice.setLearningStyleId(1);
        Apprentice result = ads.createApprentice(apprentice);
        assertNotNull("created apprentice is not null", result);
    }

    public void test_deleteApprentice_paramApprentice() throws Throwable {
        Apprentice apprentice = new Apprentice();
        apprentice.setId(1);
        apprentice.setName("test apprentice");
        apprentice.setLearningStyleId(1);
        Apprentice result = ads.createApprentice(apprentice);
        assertNotNull("created apprentice is not null", result);
        ads.deleteApprentice(apprentice);
        apprentice = new Apprentice();
        apprentice = ads.getApprentice(1);
        assertTrue(apprentice.getId() != 1);
    }

    public void test_getAllApprentices() throws Throwable {
        test_createApprentice_paramApprentice();
        List<Apprentice> apprentices = new ArrayList<Apprentice>();
        apprentices = ads.getAllApprentices();
        assertNotNull("get all apprentices is not null", apprentices);
        assertFalse(apprentices.isEmpty());
    }

    public void test_getAllApprenticeListDBTableIds() throws Throwable {
        test_createApprentice_paramApprentice();
        List<Long> apprentices = new ArrayList<Long>();
        apprentices = ads.getAllApprenticeListDBTableIds();
        assertNotNull("get all apprentice list db table ids is not null", apprentices);
        assertFalse(apprentices.isEmpty());
    }

    public void test_getApprentice_paramApprenticeId() throws Throwable {
        test_createApprentice_paramApprentice();
        Apprentice apprentice = ads.getApprentice(1);
        assertNotNull("get apprentice is not null", apprentice);
    }

    public void test_updateApprentice_paramApprentice() throws Throwable {
        test_createApprentice_paramApprentice();
        Apprentice apprentice = ads.getApprentice(1);
        apprentice.setLearningStyleId(2);
        ads.updateApprentice(apprentice);
        Apprentice apprentice2 = ads.getApprentice(1);
        assertNotNull("update apprentice is not null", apprentice2);
        assertTrue(apprentice.getLearningStyleId() == 2);
    }
}
