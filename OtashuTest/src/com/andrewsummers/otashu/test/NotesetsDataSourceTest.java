
package com.andrewsummers.otashu.test;

import java.util.ArrayList;
import java.util.List;

import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;
import android.util.Log;

import com.andrewsummers.otashu.data.NotesetsDataSource;
import com.andrewsummers.otashu.data.OtashuDatabaseHelper;
import com.andrewsummers.otashu.model.Noteset;

public class NotesetsDataSourceTest extends AndroidTestCase {

    private NotesetsDataSource nds;
    private OtashuDatabaseHelper db;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        RenamingDelegatingContext context = new RenamingDelegatingContext(getContext(), "test_");
        db = new OtashuDatabaseHelper(context);
        nds = new NotesetsDataSource(context);
    }

    @Override
    public void tearDown() throws Exception {
        db.close();
        super.tearDown();
    }

    public void testCreateNoteset() throws Throwable {
        Noteset noteset = new Noteset();
        noteset.setApprenticeId(1);
        noteset.setEmotion(1);
        noteset.setEnabled(1);
        Noteset result = nds.createNoteset(noteset);
        assertNotNull("created noteset is not null", result);
    }

    public void testDeleteNoteset() throws Throwable {
        Noteset noteset = new Noteset();
        noteset.setId(1);
        noteset.setApprenticeId(1);
        noteset.setEmotion(1);
        noteset.setEnabled(1);
        Noteset result = nds.createNoteset(noteset);
        assertNotNull("created noteset is not null", result);
        nds.deleteNoteset(noteset);
        noteset = new Noteset();
        noteset = nds.getNoteset(1);
        assertTrue(noteset.getId() != 1);
    }

    public void testGetAllNotesets() throws Throwable {
        testCreateNoteset();
        List<Noteset> notesets = new ArrayList<Noteset>();
        notesets = nds.getAllNotesets(1);
        Log.d("MYLOG", "all noteset: " + notesets.toString());
        assertNotNull("get all notesets is not null", notesets);
        assertFalse(notesets.isEmpty());
    }
}
