package com.andrewsummers.otashu;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class GenerateMusicActivity extends Activity {

    /**
     * onCreate override that provides entry point for activity.
     * 
     * @param savedInstanceState
     *            Current application state data.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        gatherRelatedEmotions();
        
        finish();
    }
    
    public HashMap<Integer, List<Note>> gatherRelatedEmotions() {
        HashMap<Integer, List<Note>> allNotesetBundles = new HashMap<Integer, List<Note>>();
        Note emptyNote = new Note();
        List<Note> emptyNoteList = new LinkedList<Note>();
        emptyNoteList.add(emptyNote);
        
        // get selected emotion_id from spinner
        Bundle bundle = getIntent().getExtras();
        int selectedEmotionValue = bundle.getInt("emotion_id");
        Log.d("MYLOG", "emotion_id: " + selectedEmotionValue);
        
        NotesetsDataSource ds = new NotesetsDataSource(this);
        
        allNotesetBundles = ds.getAllNotesetBundles(selectedEmotionValue);
        
        Log.d("MYLOG", allNotesetBundles.toString());
        
        // prevent crashes due to lack of database data
        if (allNotesetBundles.isEmpty())
            allNotesetBundles.put(1, emptyNoteList);
        
        return allNotesetBundles;
    }
}