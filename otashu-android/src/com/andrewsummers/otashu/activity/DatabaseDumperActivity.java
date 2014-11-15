package com.andrewsummers.otashu.activity;

import java.util.List;

import com.andrewsummers.otashu.R;
import com.andrewsummers.otashu.data.BookmarksDataSource;
import com.andrewsummers.otashu.data.EmotionsDataSource;
import com.andrewsummers.otashu.data.LabelsDataSource;
import com.andrewsummers.otashu.data.NotesDataSource;
import com.andrewsummers.otashu.data.NotesetsDataSource;
import com.andrewsummers.otashu.data.OtashuDatabaseHelper;
import com.andrewsummers.otashu.model.Bookmark;
import com.andrewsummers.otashu.model.Emotion;
import com.andrewsummers.otashu.model.Label;
import com.andrewsummers.otashu.model.Note;
import com.andrewsummers.otashu.model.Noteset;

import android.app.Activity;
import android.os.Bundle;
import android.widget.EditText;

public class DatabaseDumperActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // get specific layout for content view
        setContentView(R.layout.activity_database_dumper);
        
        BookmarksDataSource bds = new BookmarksDataSource(this);
        List<Bookmark> allBookmarks = bds.getAllBookmarks();
        
        NotesetsDataSource nsds = new NotesetsDataSource(this);
        List<Noteset> allNotesets = nsds.getAllNotesets();
        
        NotesDataSource nds = new NotesDataSource(this);
        List<Note> allNotes = nds.getAllNotes();
        
        EmotionsDataSource eds = new EmotionsDataSource(this);
        List<Emotion> allEmotions = eds.getAllEmotions();
        
        LabelsDataSource lds = new LabelsDataSource(this);
        List<Label> allLabels = lds.getAllLabels();
        
        EditText debugText = (EditText) findViewById(R.id.debug_text);
        
        debugText.setText(debugText.getText().toString() + "Table: Bookmarks\n" + OtashuDatabaseHelper.COLUMN_ID + "|" + OtashuDatabaseHelper.COLUMN_NAME + "|" + OtashuDatabaseHelper.COLUMN_SERIALIZED_VALUE + "\n");
        
        for (Bookmark bookmark : allBookmarks) {
            
            String newText = debugText.getText().toString();
            newText += bookmark.getId() + "|" + bookmark.getName() + "|" + bookmark.getSerializedValue() + "\n";
            
            debugText.setText(newText);
        }
        
        debugText.setText(debugText.getText().toString() + "Table: Emotions\n" + OtashuDatabaseHelper.COLUMN_ID + "|" + OtashuDatabaseHelper.COLUMN_NAME + "|" + OtashuDatabaseHelper.COLUMN_LABEL_ID + "\n");
        
        for (Emotion emotion : allEmotions) {
            
            String newText = debugText.getText().toString();
            newText += emotion.getId() + "|" + emotion.getName() + "|" + emotion.getLabelId() + "\n";
            
            debugText.setText(newText);
        }
        
        debugText.setText(debugText.getText().toString() + "\nTable: Notes\n" + OtashuDatabaseHelper.COLUMN_ID + "|" + OtashuDatabaseHelper.COLUMN_NOTESET_ID + "|" + OtashuDatabaseHelper.COLUMN_NOTEVALUE + "|" + OtashuDatabaseHelper.COLUMN_VELOCITY + "|" + OtashuDatabaseHelper.COLUMN_LENGTH + "|" + OtashuDatabaseHelper.COLUMN_POSITION + "\n");
        
        for (Note note : allNotes) {
            
            String newText = debugText.getText().toString();
            newText += note.getId() + "|" + note.getNotesetId() + "|" + note.getNotevalue() + "|" + note.getVelocity() + "|" + note.getLength() + "|" + note.getPosition() + "\n";
            
            debugText.setText(newText);
        }
        
        debugText.setText(debugText.getText().toString() + "\nTable: Notesets\n" + OtashuDatabaseHelper.COLUMN_ID + "|" + OtashuDatabaseHelper.COLUMN_NAME + "|" + OtashuDatabaseHelper.COLUMN_EMOTION_ID + "\n");
        
        for (Noteset noteset : allNotesets) {
            
            String newText = debugText.getText().toString();
            newText += noteset.getId() + "|" + noteset.getName() + "|" + noteset.getEmotion() + "\n";
            
            debugText.setText(newText);
        }
        
        debugText.setText(debugText.getText().toString() + "\nTable: Labels\n" + OtashuDatabaseHelper.COLUMN_ID + "|" + OtashuDatabaseHelper.COLUMN_NAME + "|" + OtashuDatabaseHelper.COLUMN_COLOR + "\n");
        
        for (Label label : allLabels) {
            
            String newText = debugText.getText().toString();
            newText += label.getId() + "|" + label.getName() + "|" + label.getColor() + "\n";
            
            debugText.setText(newText);
        }
    }
}