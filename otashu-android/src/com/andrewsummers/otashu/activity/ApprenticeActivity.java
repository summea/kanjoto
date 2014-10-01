package com.andrewsummers.otashu.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.andrewsummers.otashu.R;
import com.andrewsummers.otashu.model.Note;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class ApprenticeActivity extends Activity {
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // get specific layout for content view
        setContentView(R.layout.activity_apprentice);
        
        List<Note> notes = new ArrayList<Note>();
        notes = generateNotes();
 
        // TODO: implement ask question
        askQuestion();
        
        // TODO: play generated notes for user
        
        // TODO: implement no/yes buttons
        
        // TODO: save result if "yes"
    }
    
    public List<Note> generateNotes() {
        List<Note> notes = new ArrayList<Note>();
        
        String[] noteValuesArray = getResources().getStringArray(R.array.note_values_array);
        
        int randomIndex = 0;
        String randomNote = "";
        
        for (int i = 0; i < 4; i++) {
            randomIndex = new Random().nextInt(noteValuesArray.length);
            randomNote = noteValuesArray[randomIndex];

            Note note = new Note();
            note.setNotevalue(Integer.valueOf((randomNote)));
            
            notes.add(note);
            
            Log.d("MYLOG", "random note: " + randomNote);
        }
        
        return notes;
    }

    public void askQuestion() {
        
    }
}