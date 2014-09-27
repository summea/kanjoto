package com.andrewsummers.otashu.activity;

import java.util.List;

import com.andrewsummers.otashu.R;
import com.andrewsummers.otashu.data.NotesDataSource;
import com.andrewsummers.otashu.data.OtashuDatabaseHelper;
import com.andrewsummers.otashu.model.Note;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

public class DatabaseDumperActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // get specific layout for content view
        setContentView(R.layout.activity_database_dumper);
        
        NotesDataSource ds = new NotesDataSource(this);
        List<Note> allNotes = ds.getAllNotes();
        
        EditText debugText = (EditText) findViewById(R.id.debug_text);
        
        debugText.setText("Table: Notes\n" + OtashuDatabaseHelper.COLUMN_ID + "|" + OtashuDatabaseHelper.COLUMN_NOTESET_ID + "|" + OtashuDatabaseHelper.COLUMN_NOTEVALUE + "|" + OtashuDatabaseHelper.COLUMN_VELOCITY + "|" + OtashuDatabaseHelper.COLUMN_LENGTH + "|" + OtashuDatabaseHelper.COLUMN_POSITION + "\n");
        
        for (Note note : allNotes) {
            
            String newText = debugText.getText().toString();
            newText += note.getId() + "|" + note.getNotesetId() + "|" + note.getNotevalue() + "|" + note.getVelocity() + "|" + note.getLength() + "|" + note.getPosition() + "\n";
            
            debugText.setText(newText);
        }
    }
}