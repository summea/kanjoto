package com.andrewsummers.otashu;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import com.leff.midi.MidiFile;
import com.leff.midi.MidiTrack;
import com.leff.midi.event.NoteOff;
import com.leff.midi.event.NoteOn;
import com.leff.midi.event.meta.Tempo;
import com.leff.midi.event.meta.TimeSignature;

import android.app.Activity;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

public class GenerateMusicActivity extends Activity {

    File path = Environment.getExternalStorageDirectory();
    String externalDirectory = path.toString() + "/otashu/";
    File musicSource = new File(externalDirectory + "otashu.mid");
    MediaPlayer mediaPlayer = new MediaPlayer();
    
    /**
     * onCreate override that provides entry point for activity.
     * 
     * @param savedInstanceState
     *            Current application state data.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // get specific layout for content view
        setContentView(R.layout.activity_generate_music);
        
        HashMap<Integer, List<Note>> allNotesets = gatherRelatedEmotions();
        
        List<Note> notes = new ArrayList<Note>();
        
        for (int i = 0; i < 4; i++) {
            notes.addAll(getRandomNoteset(allNotesets));
        }
        
        generateMusic(notes);
        
        playMusic(musicSource);
        
        //finish();
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
    
    public List<Note> getRandomNoteset(HashMap<Integer, List<Note>> notesets) {
        List<Note> notes = new LinkedList<Note>();
        
        // get random noteset
        Random random = new Random();
        List<Integer> keys = new ArrayList<Integer>(notesets.keySet());
        Integer randomKey = keys.get(random.nextInt(keys.size()));
        notes = notesets.get(randomKey);
        
        Log.d("MYLOG", notes.toString());
        
        return notes;
    }
    
    public void generateMusic(List<Note> notes) {
        MidiTrack tempoTrack = new MidiTrack();
        MidiTrack noteTrack = new MidiTrack();
        
        TimeSignature ts = new TimeSignature();        
        ts.setTimeSignature(4, 4, TimeSignature.DEFAULT_METER, TimeSignature.DEFAULT_DIVISION);
        
        Tempo t = new Tempo();
        t.setBpm(120);
        
        tempoTrack.insertEvent(ts);
        tempoTrack.insertEvent(t);
        
        for (int i = 0; i < notes.size(); i++) {
            int channel = 0;
            int pitch = notes.get(i).getNotevalue();
            int velocity = 100;
            
            NoteOn on = new NoteOn(i * 480, channel, pitch, velocity);
            NoteOff off = new NoteOff(i * 480 + 120, channel, pitch, 0);
            
            noteTrack.insertEvent(on);
            noteTrack.insertEvent(off);
            
            noteTrack.insertNote(channel, pitch, velocity, i * 480, 120);
            Log.d("MYLOG", "writing note: " + pitch);
        }
        
        ArrayList<MidiTrack> tracks = new ArrayList<MidiTrack>();
        tracks.add(tempoTrack);
        tracks.add(noteTrack);
        
        MidiFile midi = new MidiFile(MidiFile.DEFAULT_RESOLUTION, tracks);

        try {
            midi.writeToFile(musicSource);
        } catch (IOException e) {
            Log.d("MYLOG", e.getStackTrace().toString());
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