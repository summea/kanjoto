
package com.andrewsummers.otashu.test;

import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;

import com.andrewsummers.otashu.data.EmotionsDataSource;
import com.andrewsummers.otashu.data.NotesDataSource;
import com.andrewsummers.otashu.data.NotesetsDataSource;
import com.andrewsummers.otashu.data.OtashuDatabaseHelper;
import com.andrewsummers.otashu.model.Emotion;
import com.andrewsummers.otashu.model.Note;
import com.andrewsummers.otashu.model.Noteset;

public class NotesDataSourceTest extends AndroidTestCase {
    private EmotionsDataSource eds;
    private NotesDataSource nds;
    private NotesetsDataSource nsds;
    private OtashuDatabaseHelper db;
    private RenamingDelegatingContext context;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        context = new RenamingDelegatingContext(getContext(), "test_");
        db = new OtashuDatabaseHelper(context);
        eds = new EmotionsDataSource(context);
        nds = new NotesDataSource(context);
        nsds = new NotesetsDataSource(context);

        Emotion emotion = new Emotion();
        emotion.setId(1);
        emotion.setName("happy");
        eds.createEmotion(emotion);
        emotion = new Emotion();
        emotion.setId(2);
        emotion.setName("sad");
        eds.createEmotion(emotion);

        // create noteset
        Noteset noteset = new Noteset();
        noteset.setApprenticeId(1);
        noteset.setEmotion(1);
        noteset.setEnabled(1);
        nsds.createNoteset(noteset);
    }

    @Override
    public void tearDown() throws Exception {
        db.close();
        super.tearDown();
    }

    public void test_createNote() throws Throwable {
        Note note = new Note();
        note.setNotesetId(1);
        note.setNotevalue(60);
        note.setPosition(1);
        Note result = nds.createNote(note);
        assertNotNull("created note is not null", result);
    }

    public void test_deleteNote() throws Throwable {
        test_createNote();
        Note note = nds.getNote(1);
        nds.deleteNote(note);
        note = new Note();
        note = nds.getNote(1);
        assertTrue(note.getId() != 1);
    }
    
    public void test_getNote() throws Throwable {
        test_createNote();
        Note note = nds.getNote(1);
        assertNotNull("get note is not null", note);
        assertTrue(note.getNotesetId() == 1);
    }
}
