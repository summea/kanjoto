
package summea.kanjoto.test.unit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import summea.kanjoto.data.EmotionsDataSource;
import summea.kanjoto.data.KanjotoDatabaseHelper;
import summea.kanjoto.data.NotesDataSource;
import summea.kanjoto.data.NotesetsDataSource;
import summea.kanjoto.model.Emotion;
import summea.kanjoto.model.Note;
import summea.kanjoto.model.Noteset;
import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;
import android.util.SparseArray;

public class NotesetsDataSourceTest extends AndroidTestCase {

    private EmotionsDataSource eds;
    private NotesetsDataSource nsds;
    private KanjotoDatabaseHelper db;
    private RenamingDelegatingContext context;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        context = new RenamingDelegatingContext(getContext(), "test_");
        db = new KanjotoDatabaseHelper(context);
        eds = new EmotionsDataSource(context);
        nsds = new NotesetsDataSource(context);

        Emotion emotion = new Emotion();
        emotion.setId(1);
        emotion.setName("happy");
        eds.createEmotion(emotion);
        emotion = new Emotion();
        emotion.setId(2);
        emotion.setName("sad");
        eds.createEmotion(emotion);

        // create notes
        NotesDataSource nds = new NotesDataSource(context);
        for (int i = 0; i < 4; i++) {
            Note note = new Note();
            note.setId(i + 1);
            note.setNotesetId(1);
            note.setNotevalue(60 + i);
            note.setPosition(i + 1);
            nds.createNote(note);
        }
    }

    @Override
    public void tearDown() throws Exception {
        db.close();
        super.tearDown();
    }

    public void test_createNoteset_paramNoteset() throws Throwable {
        Noteset noteset = new Noteset();
        noteset.setApprenticeId(1);
        noteset.setEmotion(1);
        noteset.setEnabled(1);
        Noteset result = nsds.createNoteset(noteset);
        assertNotNull("created noteset is not null", result);
    }

    public void test_deleteNoteset_paramNoteset() throws Throwable {
        Noteset noteset = new Noteset();
        noteset.setId(1);
        noteset.setApprenticeId(1);
        noteset.setEmotion(1);
        noteset.setEnabled(1);
        Noteset result = nsds.createNoteset(noteset);
        assertNotNull("created noteset is not null", result);
        nsds.deleteNoteset(noteset);
        noteset = new Noteset();
        noteset = nsds.getNoteset(1);
        assertTrue(noteset.getId() != 1);
    }

    public void test_getAllNotesets_paramApprenticeId() throws Throwable {
        test_createNoteset_paramNoteset();
        List<Noteset> notesets = new ArrayList<Noteset>();
        notesets = nsds.getAllNotesets(1);
        assertNotNull("get all notesets is not null", notesets);
        assertFalse(notesets.isEmpty());
    }

    public void test_getAllNotesetsWithLimit_paramOffset() throws Throwable {
        test_createNoteset_paramNoteset();
        List<Noteset> notesets = new ArrayList<Noteset>();
        notesets = nsds.getAllNotesets(1, 1, 0);
        assertNotNull("get all notesets with limit is not null", notesets);
        assertFalse(notesets.isEmpty());
    }

    public void test_getAllNotesetBundles_paramApprenticeId() throws Throwable {
        test_createNoteset_paramNoteset();
        SparseArray<List<Note>> notesetBundles = new SparseArray<List<Note>>();
        notesetBundles = nsds.getAllNotesetBundles(1);
        assertNotNull("get all noteset bundles is not null", notesetBundles);
        assertTrue(notesetBundles.size() > 0);
    }

    public void test_getAllNotesetBundlesByEmotion_paramApprenticeId_paramEmotionId()
            throws Throwable {
        test_createNoteset_paramNoteset();
        SparseArray<List<Note>> notesetBundles = new SparseArray<List<Note>>();
        notesetBundles = nsds.getAllNotesetBundlesByEmotion(1, 1);
        assertNotNull("get all noteset bundles by emotion is not null", notesetBundles);
        assertTrue(notesetBundles.size() > 0);
    }

    public void test_getNotesetBundleDetailById() throws Throwable {
        test_createNoteset_paramNoteset();
        HashMap<String, List<Object>> notesetBundles = new HashMap<String, List<Object>>();
        notesetBundles = nsds.getNotesetBundleDetailById(1);
        assertNotNull("get noteset bundle detail by id is not null", notesetBundles);
        assertFalse(notesetBundles.isEmpty());
    }

    public void test_getAllNotesetListPreviews_paramApprenticeId() throws Throwable {
        test_createNoteset_paramNoteset();
        List<String> notesetPreviews = new ArrayList<String>();
        notesetPreviews = nsds.getAllNotesetListPreviews(1);
        assertNotNull("get all noteset list previews is not null", notesetPreviews);
        assertFalse(notesetPreviews.isEmpty());
    }

    public void test_getNoteset_paramId() throws Throwable {
        test_createNoteset_paramNoteset();
        Noteset noteset = new Noteset();
        noteset = nsds.getNoteset(1);
        assertNotNull("get noteset is not null", noteset);
        assertTrue(noteset.getId() > 0);
    }

    public void test_updateNoteset_paramNoteset() throws Throwable {
        test_createNoteset_paramNoteset();
        Noteset noteset = new Noteset();
        noteset = nsds.getNoteset(1);
        noteset.setEmotion(2);
        nsds.updateNoteset(noteset);
        Noteset noteset2 = new Noteset();
        noteset2 = nsds.getNoteset(1);
        assertNotNull("update noteset is not null", noteset2);
        assertTrue(noteset2.getEmotion() == 2);
    }

    public void test_getCount_paramApprenticeId() throws Throwable {
        test_createNoteset_paramNoteset();
        int count = nsds.getCount(1);
        assertNotNull("noteset count is not null", count);
        assertTrue(count == 1);
    }
}
