
package com.andrewsummers.kanjoto.test.unit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;

import com.andrewsummers.kanjoto.data.EmotionsDataSource;
import com.andrewsummers.kanjoto.data.NotesDataSource;
import com.andrewsummers.kanjoto.data.NotesetsDataSource;
import com.andrewsummers.kanjoto.data.KanjotoDatabaseHelper;
import com.andrewsummers.kanjoto.model.Emotion;
import com.andrewsummers.kanjoto.model.Note;
import com.andrewsummers.kanjoto.model.Noteset;
import com.andrewsummers.kanjoto.model.NotesetAndRelated;

public class NotesDataSourceTest extends AndroidTestCase {
    private EmotionsDataSource eds;
    private NotesDataSource nds;
    private NotesetsDataSource nsds;
    private KanjotoDatabaseHelper db;
    private RenamingDelegatingContext context;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        context = new RenamingDelegatingContext(getContext(), "test_");
        db = new KanjotoDatabaseHelper(context);
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

    public void test_createNote_paramNote() throws Throwable {
        Note note = new Note();
        note.setNotesetId(1);
        note.setNotevalue(60);
        note.setPosition(1);
        Note result = nds.createNote(note);
        assertNotNull("created note is not null", result);
    }

    public void test_deleteNote_paramNote() throws Throwable {
        test_createNote_paramNote();
        Note note = nds.getNote(1);
        nds.deleteNote(note);
        note = new Note();
        note = nds.getNote(1);
        assertTrue(note.getId() != 1);
    }

    public void test_getNote_paramId() throws Throwable {
        test_createNote_paramNote();
        Note note = nds.getNote(1);
        assertNotNull("get note is not null", note);
        assertTrue(note.getNotesetId() == 1);
    }

    public void test_getAllNotes() throws Throwable {
        test_createNote_paramNote();
        List<Note> notes = new ArrayList<Note>();
        notes = nds.getAllNotes();
        assertNotNull("get all notes is not null", notes);
        assertFalse(notes.isEmpty());
    }

    public void test_getAllNotesByNotesetId_paramNotesetId() throws Throwable {
        test_createNote_paramNote();
        List<Note> notes = new ArrayList<Note>();
        notes = nds.getAllNotesByNotesetId(1);
        assertNotNull("get all notes by noteset id is not null", notes);
        assertFalse(notes.isEmpty());
    }

    public void test_getAllNotesByPosition_paramPosition() throws Throwable {
        test_createNote_paramNote();
        List<Note> notes = new ArrayList<Note>();
        notes = nds.getAllNotesByPosition(1);
        assertNotNull("get all notes by noteset id is not null", notes);
        assertFalse(notes.isEmpty());
    }

    public void test_doesNotesetExist_paramNotesetAndRelatedToCheck() throws Throwable {
        test_createNote_paramNote();

        Noteset noteset = nsds.getNoteset(1);
        List<Note> notes = nds.getAllNotes();

        // check if noteset already exists, first
        NotesetAndRelated notesetAndRelated = new NotesetAndRelated();
        notesetAndRelated.setNoteset(noteset);
        notesetAndRelated.setNotes(notes);
        boolean notesetExists = nds.doesNotesetExist(notesetAndRelated);

        assertNotNull("does noteset exist is not null", notesetExists);
        assertTrue(notesetExists);
    }

    public void test_updateNote_paramNote() throws Throwable {
        test_createNote_paramNote();
        Note note = new Note();
        note = nds.getNote(1);
        note.setNotevalue(61);
        nds.updateNote(note);
        Note note2 = new Note();
        note2 = nds.getNote(1);
        assertNotNull("update note is not null", note2);
        assertTrue(note2.getNotevalue() == 61);
    }

    public void test_getEmotionFromNotes_paramNotes() throws Throwable {
        List<Integer> notes = new ArrayList<Integer>();
        for (int i = 0; i < 4; i++) {
            Note note = new Note();
            note.setId(i + 1);
            note.setNotevalue(60 + i);
            note.setPosition(i + 1);
            nds.createNote(note);
            notes.add(60 + i);
        }

        HashMap<String, String> emotion = nds.getEmotionFromNotes(notes);
        assertNotNull("get emotion from notes is not null", emotion);
        assertTrue(Long.valueOf(emotion.get("emotionId")) > 0);
    }
}
