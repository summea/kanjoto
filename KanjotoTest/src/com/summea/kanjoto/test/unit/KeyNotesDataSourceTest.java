
package com.summea.kanjoto.test.unit;

import java.util.ArrayList;
import java.util.List;

import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;

import com.summea.kanjoto.data.KanjotoDatabaseHelper;
import com.summea.kanjoto.data.KeyNotesDataSource;
import com.summea.kanjoto.model.KeyNote;

public class KeyNotesDataSourceTest extends AndroidTestCase {

    private KeyNotesDataSource knds;
    private KanjotoDatabaseHelper db;
    private RenamingDelegatingContext context;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        context = new RenamingDelegatingContext(getContext(), "test_");
        db = new KanjotoDatabaseHelper(context);
        knds = new KeyNotesDataSource(context);
    }

    @Override
    public void tearDown() throws Exception {
        db.close();
        super.tearDown();
    }

    public void test_createKeyNote_paramKeyNote() throws Throwable {
        KeyNote keyNote = new KeyNote();
        keyNote.setKeySignatureId(1);
        keyNote.setNotevalue(60);
        keyNote.setWeight(0.5f);
        keyNote.setApprenticeId(1);
        KeyNote result = knds.createKeyNote(keyNote);
        assertNotNull("created key note is not null", result);
    }

    public void test_deleteKeyNote_paramKeyNote() throws Throwable {
        KeyNote keyNote = new KeyNote();
        keyNote.setId(1);
        keyNote.setKeySignatureId(1);
        keyNote.setNotevalue(60);
        keyNote.setWeight(0.5f);
        keyNote.setApprenticeId(1);
        KeyNote result = knds.createKeyNote(keyNote);
        assertNotNull("created key note is not null", result);
        knds.deleteKeyNote(keyNote);
        keyNote = new KeyNote();
        keyNote = knds.getKeyNote(1);
        assertTrue(keyNote.getId() != 1);
    }

    public void test_getAllKeyNotes_paramApprenticeId() throws Throwable {
        test_createKeyNote_paramKeyNote();
        List<KeyNote> keyNotes = new ArrayList<KeyNote>();
        keyNotes = knds.getAllKeyNotes(1);
        assertNotNull("get all key notes is not null", keyNotes);
        assertFalse(keyNotes.isEmpty());
    }

    public void test_getKeyNote_paramKeyNoteId() throws Throwable {
        test_createKeyNote_paramKeyNote();
        KeyNote keyNote = knds.getKeyNote(1);
        assertNotNull("get key note is not null", keyNote);
    }

    public void test_getKeyNotesByKeySignature_paramKeySignatureId() throws Throwable {
        test_createKeyNote_paramKeyNote();
        List<KeyNote> keyNotes = new ArrayList<KeyNote>();
        keyNotes = knds.getKeyNotesByKeySignature(1);
        assertNotNull("get all key notes by key signature is not null", keyNotes);
        assertFalse(keyNotes.isEmpty());
    }

    public void test_getKeyNoteNotevaluesByKeySignature_paramKeySignatureId() throws Throwable {
        test_createKeyNote_paramKeyNote();
        List<Integer> keyNotes = knds.getKeyNoteNotevaluesByKeySignature(1);
        assertNotNull("get all key note note values by key signature is not null", keyNotes);
        assertFalse(keyNotes.isEmpty());
    }

    public void test_updateKeyNote_paramKeyNote() throws Throwable {
        test_createKeyNote_paramKeyNote();
        KeyNote keyNote = knds.getKeyNote(1);
        keyNote.setNotevalue(61);
        knds.updateKeyNote(keyNote);
        KeyNote keyNote2 = knds.getKeyNote(1);
        assertNotNull("update key note is not null", keyNote2);
        assertTrue(keyNote.getNotevalue() == 61);
    }

    public void test_keySignatureIdsThatContain_paramApprenticeId_paramNotevalue() throws Throwable {
        test_createKeyNote_paramKeyNote();
        List<Long> keyNotes = knds.keySignatureIdsThatContain(1, 60);
        assertNotNull("get all key signature ids that contain ... is not null", keyNotes);
        assertFalse(keyNotes.isEmpty());
    }

    public void test_getKeySignatureByNotes_paramApprenticeId_paramNotevaluesInKeySignature()
            throws Throwable {
        test_createKeyNote_paramKeyNote();
        List<Integer> notes = new ArrayList<Integer>();
        notes.add(60);
        long keySignature = knds.getKeySignatureByNotes(1, notes);
        assertNotNull("get key signature by notes is not null", keySignature);
        assertTrue(keySignature == 1);
    }
}
