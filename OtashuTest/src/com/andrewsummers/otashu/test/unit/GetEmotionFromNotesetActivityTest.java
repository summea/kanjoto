
package com.andrewsummers.otashu.test.unit;

import java.util.ArrayList;

import android.content.Intent;
import android.test.ActivityUnitTestCase;
import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;

import com.andrewsummers.otashu.activity.GetEmotionFromNotesetActivity;
import com.andrewsummers.otashu.activity.MainActivity;
import com.andrewsummers.otashu.data.OtashuDatabaseHelper;
import com.andrewsummers.otashu.model.EmotionAndRelated;
import com.andrewsummers.otashu.model.Note;

public class GetEmotionFromNotesetActivityTest extends
        ActivityUnitTestCase<GetEmotionFromNotesetActivity> {

    private Intent mIntent;
    private OtashuDatabaseHelper db;
    private RenamingDelegatingContext context;

    public GetEmotionFromNotesetActivityTest() {
        super(GetEmotionFromNotesetActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        context = new RenamingDelegatingContext(getInstrumentation().getContext(), "test_");
        db = new OtashuDatabaseHelper(context);
        mIntent = new Intent(getInstrumentation().getTargetContext(),
                GetEmotionFromNotesetActivity.class);
        startActivity(mIntent, null, null);
    }

    @Override
    public void tearDown() throws Exception {
        db.close();
        super.tearDown();
    }

    public void test_getEmotion() throws Throwable {
        ArrayList<Note> notes = new ArrayList<Note>();
        int[] notevalues = new int[] {
                60, 60, 60, 60
        };
        for (int i = 0; i < 4; i++) {
            Note note = new Note();
            note.setNotevalue(notevalues[i]);
            notes.add(note);
        }

        EmotionAndRelated result = getActivity().getEmotion(notes);
        assertNotNull("emotion result is not null", result);
        // TODO: 100% test
        // TODO: 50% test
    }
}
