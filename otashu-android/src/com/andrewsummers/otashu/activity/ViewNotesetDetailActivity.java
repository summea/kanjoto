package com.andrewsummers.otashu.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import com.andrewsummers.otashu.R;
import com.andrewsummers.otashu.data.EmotionsDataSource;
import com.andrewsummers.otashu.data.NotesetsDataSource;
import com.andrewsummers.otashu.model.Emotion;
import com.andrewsummers.otashu.model.Note;
import com.andrewsummers.otashu.model.Noteset;

import android.app.Activity;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

/**
 * View details of a particular noteset.
 */
public class ViewNotesetDetailActivity extends Activity implements OnClickListener {
    
    private int key = 0;
    private int notesetId = 0;
    private HashMap<Integer, List<Note>> notesetBundle = new HashMap<Integer, List<Note>>();
    private Button buttonPlayNoteset = null;
    private File path = Environment.getExternalStorageDirectory();
    private String externalDirectory = path.toString() + "/otashu/";
    private File musicSource = new File(externalDirectory + "otashu_preview.mid");
    private MediaPlayer mediaPlayer = new MediaPlayer();
    
    /**
     * onCreate override used to get details view.
     * 
     * @param savedInstanceState
     *            Current application state data.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // get specific layout for content view
        setContentView(R.layout.activity_view_noteset_detail);
        
        Log.d("MYLOG", "got list item id: " + getIntent().getExtras().getLong("list_id"));
        notesetId = (int) getIntent().getExtras().getLong("list_id");
        
        List<Long> allNotesetsData = new LinkedList<Long>();
        NotesetsDataSource ds = new NotesetsDataSource(this);

        // get string version of returned noteset list
        allNotesetsData = ds.getAllNotesetListDBTableIds();
        
        Log.d("MYLOG", allNotesetsData.toString());

        // prevent crashes due to lack of database data
        if (allNotesetsData.isEmpty())
            allNotesetsData.add((long) 0);

        Log.d("MYLOG", "notesetId:: " + notesetId);
        
        Long[] allNotesets = allNotesetsData
                .toArray(new Long[allNotesetsData.size()]);
        
        Log.d("MYLOG", "found noteset data: " + allNotesets[notesetId]);
        
        // get noteset and notes information        
        notesetBundle = ds.getNotesetBundle(allNotesets[notesetId]);
        
        Log.d("MYLOG", "noteset bundle: " + notesetBundle);
        Log.d("MYLOG", "notesetId::: " + allNotesets[notesetId]);
        
        Noteset noteset = ds.getNoteset(allNotesets[notesetId]);        
        
        ds.close();
        
        EmotionsDataSource eds = new EmotionsDataSource(this);
        
        Emotion emotion = eds.getEmotion(noteset.getEmotion());
        
        eds.close();
        
        String[] noteLabelsArray = getResources().getStringArray(R.array.note_labels_array);
        String[] noteValuesArray = getResources().getStringArray(R.array.note_values_array);
        
        // conversion issues...
        key = (int) (long) allNotesets[notesetId];
        
        TextView notesetName = (TextView) findViewById(R.id.noteset_detail_name_value);
        notesetName.setText(noteset.getName());
        
        TextView emotionName = (TextView) findViewById(R.id.noteset_detail_emotion_value);
        emotionName.setText(emotion.getName());
        
        //String noteName = "C4";
        
        int[] textViewIds = {
                R.id.noteset_detail_note1,
                R.id.noteset_detail_note2,
                R.id.noteset_detail_note3,
                R.id.noteset_detail_note4
        };
        
        TextView note = null;
        
        for (int i = 0; i < textViewIds.length; i++) {
            note = (TextView) findViewById(textViewIds[i]);
            for (int j = 0; j < noteValuesArray.length; j++) {
                // get actual note name (C3, D3, E3, etc.)
                if (notesetBundle.get(key).get(i).getNotevalue() == Integer.valueOf(noteValuesArray[j])) {
                    note.setText(noteLabelsArray[j]);
                    break;
                }
            }
        }
        
        try {
            // add listeners to buttons
            // have to cast to Button in this case    
            buttonPlayNoteset = (Button) findViewById(R.id.button_play_noteset);
            buttonPlayNoteset.setOnClickListener(this);
        } catch (Exception e) {
            Log.d("MYLOG", e.getStackTrace().toString());
        }
    }
    
    /**
     * onClick override that acts as a router to start desired activities.
     * 
     * @param view
     *            Incoming view.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.button_play_noteset:
            
            List<Note> notes = new ArrayList<Note>();
            
            for (int i = 0; i < notesetBundle.get(key).size(); i++) {
                Note note = new Note();
                note = notesetBundle.get(key).get(i);
                notes.add(note);
            }
            
            GenerateMusicActivity generateMusic = new GenerateMusicActivity();
            generateMusic.generateMusic(notes, musicSource);

            // play generated notes for user
            playMusic(musicSource);
            
            break;
        }
    }
    
    public void playMusic(File musicSource) {
        // get media player ready
        mediaPlayer = MediaPlayer.create(this, Uri.fromFile(musicSource));
        
        // play music
        mediaPlayer.start();
    }
    
    /**
     * onBackPressed override used to stop playing music when done with activity
     */
    @Override
    public void onBackPressed() {
        Log.d("MYLOG", "stop playing music!");
        
        // stop playing music
        mediaPlayer.stop();
        
        super.onBackPressed();
    }
}