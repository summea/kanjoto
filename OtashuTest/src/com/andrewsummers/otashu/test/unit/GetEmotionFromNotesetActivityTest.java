// TODO: move this to the functional folder later on?

package com.andrewsummers.otashu.test.unit;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.test.ActivityUnitTestCase;
import android.util.Log;

import com.andrewsummers.otashu.activity.GetEmotionFromNotesetActivity;
import com.andrewsummers.otashu.data.EdgesDataSource;
import com.andrewsummers.otashu.data.OtashuDatabaseHelper;
import com.andrewsummers.otashu.model.Edge;
import com.andrewsummers.otashu.model.EmotionAndRelated;
import com.andrewsummers.otashu.model.Note;

public class GetEmotionFromNotesetActivityTest extends
        ActivityUnitTestCase<GetEmotionFromNotesetActivity> {

    private Intent mIntent;
    private OtashuDatabaseHelper db;
    private Context context;
    private EdgesDataSource eds;

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
        db = new OtashuDatabaseHelper(context, OtashuDatabaseHelper.TEST_DATABASE_NAME);
        db.getWritableDatabase();
        eds = new EdgesDataSource(context, OtashuDatabaseHelper.TEST_DATABASE_NAME);
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
    }

    @Override
    public void tearDown() throws Exception {
        db.close();
        context.deleteDatabase(OtashuDatabaseHelper.DATABASE_PATH
                + OtashuDatabaseHelper.TEST_DATABASE_NAME);
        super.tearDown();
    }

    public void test_getEmotion() throws Throwable {
        // 0% test
        ArrayList<Note> notes = new ArrayList<Note>();
        int[] notevalues = new int[] {
                64, 64, 64, 64
        };
        for (int i = 0; i < 4; i++) {
            Note note = new Note();
            note.setNotevalue(notevalues[i]);
            note.setPosition(i + 1);
            notes.add(note);
        }

        EmotionAndRelated result = getActivity().getEmotion(notes);
        assertNotNull("emotion result is not null", result);
        assertEquals(0.0f, result.getCertainty());
        Log.d("MYLOG", "certainty: " + result.getCertainty());

        // 25% test
        notes = new ArrayList<Note>();
        notevalues = new int[] {
                60, 64, 64, 64
        };
        for (int i = 0; i < 4; i++) {
            Note note = new Note();
            note.setNotevalue(notevalues[i]);
            note.setPosition(i + 1);
            notes.add(note);
        }

        result = getActivity().getEmotion(notes);
        assertNotNull("emotion result is not null", result);
        assertEquals(25.0f, result.getCertainty());
        Log.d("MYLOG", "certainty: " + result.getCertainty());

        // 50% test
        notes = new ArrayList<Note>();
        notevalues = new int[] {
                60, 61, 64, 64
        };
        for (int i = 0; i < 4; i++) {
            Note note = new Note();
            note.setNotevalue(notevalues[i]);
            note.setPosition(i + 1);
            notes.add(note);
        }

        result = getActivity().getEmotion(notes);
        assertNotNull("emotion result is not null", result);
        assertEquals(50.0f, result.getCertainty());
        Log.d("MYLOG", "certainty: " + result.getCertainty());

        // 75% test
        notes = new ArrayList<Note>();
        notevalues = new int[] {
                60, 61, 62, 64
        };
        for (int i = 0; i < 4; i++) {
            Note note = new Note();
            note.setNotevalue(notevalues[i]);
            note.setPosition(i + 1);
            notes.add(note);
        }

        result = getActivity().getEmotion(notes);
        assertNotNull("emotion result is not null", result);
        assertEquals(75.0f, result.getCertainty());
        Log.d("MYLOG", "certainty: " + result.getCertainty());

        // 100% test
        notes = new ArrayList<Note>();
        notevalues = new int[] {
                60, 61, 62, 63
        };
        for (int i = 0; i < 4; i++) {
            Note note = new Note();
            note.setNotevalue(notevalues[i]);
            note.setPosition(i + 1);
            notes.add(note);
        }

        result = getActivity().getEmotion(notes);
        assertNotNull("emotion result is not null", result);
        assertEquals(100.0f, result.getCertainty());
        Log.d("MYLOG", "certainty: " + result.getCertainty());
    }
}
