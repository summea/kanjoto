// TODO: move this to the functional folder later on?

package com.andrewsummers.kanjoto.test.unit;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.test.ActivityUnitTestCase;
import android.util.Log;

import com.andrewsummers.kanjoto.activity.GetEmotionFromNotesetActivity;
import com.andrewsummers.kanjoto.data.EdgesDataSource;
import com.andrewsummers.kanjoto.data.KanjotoDatabaseHelper;
import com.andrewsummers.kanjoto.data.NotesetsDataSource;
import com.andrewsummers.kanjoto.model.Edge;
import com.andrewsummers.kanjoto.model.EmotionAndRelated;
import com.andrewsummers.kanjoto.model.Note;
import com.andrewsummers.kanjoto.model.Noteset;

public class GetEmotionFromNotesetActivityTest extends
        ActivityUnitTestCase<GetEmotionFromNotesetActivity> {

    private Intent mIntent;
    private KanjotoDatabaseHelper db;
    private Context context;
    private EdgesDataSource eds;
    private NotesetsDataSource nsds;

    public GetEmotionFromNotesetActivityTest() {
        super(GetEmotionFromNotesetActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();

        mIntent = new Intent(getInstrumentation().getTargetContext(),
                GetEmotionFromNotesetActivity.class);
        startActivity(mIntent, null, null);
        // context = new RenamingDelegatingContext(getInstrumentation().getContext(), "test_");
        context = getInstrumentation().getTargetContext();
        db = new KanjotoDatabaseHelper(context, KanjotoDatabaseHelper.TEST_DATABASE_NAME);
        db.getWritableDatabase();
        
        eds = new EdgesDataSource(context, KanjotoDatabaseHelper.TEST_DATABASE_NAME);
        nsds = new NotesetsDataSource(context);
        
        Edge edge;
        for (int i = 0; i < 4; i++) {
            edge = new Edge();
            edge.setApprenticeId(1);
            edge.setEmotionId(1);
            edge.setGraphId(1);
            edge.setFromNodeId(60 + i);
            edge.setToNodeId(61 + i);
            edge.setPosition(i + 1);
            Log.d("MYLOG", "creating edge: " + edge.toString());
            eds.createEdge(edge);
        }
        
        Noteset noteset = new Noteset();
        noteset.setApprenticeId(1);
        noteset.setEmotion(1);
        noteset.setEnabled(1);
        nsds.createNoteset(noteset);
    }

    @Override
    public void tearDown() throws Exception {
        db.close();
        context.deleteDatabase(KanjotoDatabaseHelper.DATABASE_PATH
                + KanjotoDatabaseHelper.TEST_DATABASE_NAME);
        super.tearDown();
    }

    public void test_getEmotionZeroPercent() throws Throwable {
        // 0% test 
        ArrayList<Note> notes = new ArrayList<Note>();
        int[] notevalues = new int[] {
                64, 64, 64, 64
        };
        for (int i = 0; i < 4; i++) {
            Note note = new Note();
            note.setNotevalue(notevalues[i]);
            note.setPosition(i + 1);
            note.setNotesetId(1);
            notes.add(note);
        }

        EmotionAndRelated result = getActivity().getEmotion(notes);
        assertNotNull("emotion result is not null", result);
        assertEquals(0.0f, result.getCertainty());
        Log.d("MYLOG", "certainty: " + result.getCertainty());
    }

    public void test_getEmotionTwentyFivePercent() throws Throwable {
        // 25% test
        ArrayList<Note> notes = new ArrayList<Note>();
        int[] notevalues = new int[] {
                60, 64, 64, 64
        };
        for (int i = 0; i < 4; i++) {
            Note note = new Note();
            note.setNotevalue(notevalues[i]);
            note.setPosition(i + 1);
            notes.add(note);
        }

        EmotionAndRelated result = getActivity().getEmotion(notes);
        assertNotNull("emotion result is not null", result);
        assertEquals(25.0f, result.getCertainty());
        Log.d("MYLOG", "certainty: " + result.getCertainty());
    }
    
    public void test_getEmotionFiftyPercent() throws Throwable {
        // 50% test
        ArrayList<Note> notes = new ArrayList<Note>();
        int[] notevalues = new int[] {
                60, 61, 64, 64
        };
        for (int i = 0; i < 4; i++) {
            Note note = new Note();
            note.setNotevalue(notevalues[i]);
            note.setPosition(i + 1);
            notes.add(note);
        }

        EmotionAndRelated result = getActivity().getEmotion(notes);
        assertNotNull("emotion result is not null", result);
        assertEquals(50.0f, result.getCertainty());
        Log.d("MYLOG", "certainty: " + result.getCertainty());
    }
    
    public void test_getEmotionSeventyFivePercent() throws Throwable {
        // 75% test
        ArrayList<Note> notes = new ArrayList<Note>();
        int[] notevalues = new int[] {
                60, 61, 62, 64
        };
        for (int i = 0; i < 4; i++) {
            Note note = new Note();
            note.setNotevalue(notevalues[i]);
            note.setPosition(i + 1);
            notes.add(note);
        }

        EmotionAndRelated result = getActivity().getEmotion(notes);
        assertNotNull("emotion result is not null", result);
        assertEquals(75.0f, result.getCertainty());
        Log.d("MYLOG", "certainty: " + result.getCertainty());
    }
    
    public void test_getEmotionOneHundredPercent() throws Throwable {
        // 100% test
        ArrayList<Note> notes = new ArrayList<Note>();
        int[] notevalues = new int[] {
                60, 61, 62, 63
        };
        for (int i = 0; i < 4; i++) {
            Note note = new Note();
            note.setNotevalue(notevalues[i]);
            note.setPosition(i + 1);
            notes.add(note);
        }

        EmotionAndRelated result = getActivity().getEmotion(notes);
        assertNotNull("emotion result is not null", result);
        assertEquals(100.0f, result.getCertainty());
        Log.d("MYLOG", "certainty: " + result.getCertainty());
    }
}
