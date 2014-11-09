package com.andrewsummers.otashu.activity;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import com.andrewsummers.otashu.data.NotesetsDataSource;
import com.andrewsummers.otashu.model.Note;
import com.andrewsummers.otashu.view.PlaybackGLSurfaceView;
import com.leff.midi.MidiFile;
import com.leff.midi.MidiTrack;
import com.leff.midi.event.NoteOff;
import com.leff.midi.event.NoteOn;
import com.leff.midi.event.ProgramChange;
import com.leff.midi.event.meta.Tempo;
import com.leff.midi.event.meta.TimeSignature;

import android.app.Activity;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.util.SparseArray;

public class GenerateMusicActivity extends Activity {

    private GLSurfaceView mGLView;
    
    int selectedInstrumentId = 0;
    File path = Environment.getExternalStorageDirectory();
    String externalDirectory = path.toString() + "/otashu/";
    File musicSource = new File(externalDirectory + "otashu.mid");
    MediaPlayer mediaPlayer = new MediaPlayer();
    
    private SparseArray<List<Integer>> musicalKeys = new SparseArray<List<Integer>>(); 
    
    private static SparseArray<String> noteMap;
    static
    {
        noteMap = new SparseArray<String>();
        noteMap.put(60, "C4");
        noteMap.put(61, "C#4");
        noteMap.put(62, "D4");
        noteMap.put(63, "D#4");
        noteMap.put(64, "E4");
        noteMap.put(65, "F4");
        noteMap.put(66, "F#4");
        noteMap.put(67, "G4");
        noteMap.put(68, "G#4");
        noteMap.put(69, "A4");
        noteMap.put(70, "A#4");
        noteMap.put(71, "B4");
    }
    
    
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
        // setContentView(R.layout.activity_generate_music);
        
        // Create a GLSurfaceView instance and set it
        // as the ContentView for this Activity.
        mGLView = new PlaybackGLSurfaceView(this);
        setContentView(mGLView);
        
        // get selected instrument_id from spinner
        Bundle bundle = getIntent().getExtras();
        selectedInstrumentId = bundle.getInt("instrument_id");
        Log.d("MYLOG", "selected instrument: " + selectedInstrumentId);
        
        // TODO: double-check this section later
        
        // C4, E4, G4
        musicalKeys.put(60, new ArrayList<Integer>() {
            private static final long serialVersionUID = 1L;
            { add(60); add(64); add(67); }
        });
        
        // C#4, F4, G#4
        musicalKeys.put(61, new ArrayList<Integer>() {
            private static final long serialVersionUID = 1L;
            { add(61); add(65); add(67); }
        });
        
        // D4, F#4, A4
        musicalKeys.put(62, new ArrayList<Integer>() {
            private static final long serialVersionUID = 1L;
            { add(62); add(66); add(69); }
        });

        // D#4, G4, A#4
        musicalKeys.put(63, new ArrayList<Integer>() {
            private static final long serialVersionUID = 1L;
            { add(63); add(67); add(70); }
        });
        
        // E4, G#4, B4
        musicalKeys.put(64, new ArrayList<Integer>() {
            private static final long serialVersionUID = 1L;
            { add(64); add(68); add(71); }
        });
        
        // F4, A4, C4
        musicalKeys.put(65, new ArrayList<Integer>() {
            private static final long serialVersionUID = 1L;
            { add(65); add(69); add(60); }
        });
        
        // F#4, A#4, C#4
        musicalKeys.put(66, new ArrayList<Integer>() {
            private static final long serialVersionUID = 1L;
            { add(66); add(70); add(61); }
        });
        
        // G4, B4, D4
        musicalKeys.put(67, new ArrayList<Integer>() {
            private static final long serialVersionUID = 1L;
            { add(67); add(71); add(62); }
        });
        
        // G#4, C4, D#4
        musicalKeys.put(68, new ArrayList<Integer>() {
            private static final long serialVersionUID = 1L;
            { add(68); add(60); add(63); }
        });
        
        // A4, C#4, E4
        musicalKeys.put(69, new ArrayList<Integer>() {
            private static final long serialVersionUID = 1L;
            { add(69); add(61); add(64); }
        });
        
        // A#4, D4, F4
        musicalKeys.put(70, new ArrayList<Integer>() {
            private static final long serialVersionUID = 1L;
            { add(70); add(62); add(65); }
        });
        
        // B4, D#4, F#4
        musicalKeys.put(71, new ArrayList<Integer>() {
            private static final long serialVersionUID = 1L;
            { add(71); add(63); add(66); }
        }); 
        
        HashMap<Integer, List<Note>> allNotesets = gatherRelatedEmotions();
        
        List<Note> notes = new ArrayList<Note>();
        
        notes = logicA(allNotesets);
        
        /*
        // original logic
        for (int i = 0; i < 4; i++) {
            notes.addAll(getRandomNoteset(allNotesets));
        }
        */
        
        String notesText = "";
        int lineBreak = 1;
        for (Note note : notes) {
            notesText += noteMap.get(note.getNotevalue()) + ", ";
            if (lineBreak % 4 == 0)
                notesText += "\n";
            lineBreak++;
        }
        
        //TextView playbackText = (TextView) findViewById(R.id.generate_music_placeholder);
        //playbackText.setText(notesText);
        Log.d("MYLOG", notesText);
        
        generateMusic(notes, musicSource);
        
        playMusic(musicSource);
        
        // return to previous activity when done playing
        mediaPlayer.setOnCompletionListener(new OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer aMediaPlayer) {
                finish();
            }
        });
        
    }

    public HashMap<Integer, List<Note>> gatherRelatedEmotions() {
        HashMap<Integer, List<Note>> allNotesetBundles = new HashMap<Integer, List<Note>>();
        Note emptyNote = new Note();
        List<Note> emptyNoteList = new LinkedList<Note>();
        emptyNoteList.add(emptyNote);
        
        // get selected emotion_id from spinner
        Bundle bundle = getIntent().getExtras();
        int selectedEmotionValue = bundle.getInt("emotion_id");        
        
        NotesetsDataSource ds = new NotesetsDataSource(this);
        allNotesetBundles = ds.getAllNotesetBundles(selectedEmotionValue);
        
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
        
        return notes;
    }
    
    public void generateMusic(List<Note> notes, File musicSource) {
        MidiTrack tempoTrack = new MidiTrack();
        MidiTrack noteTrack = new MidiTrack();
        
        TimeSignature ts = new TimeSignature();
        ts.setTimeSignature(4, 4, TimeSignature.DEFAULT_METER, TimeSignature.DEFAULT_DIVISION);
        
        Tempo t = new Tempo();
        t.setBpm(120);
        
        // set instrument type
        ProgramChange pc = new ProgramChange(0, 0, selectedInstrumentId);
        
        tempoTrack.insertEvent(ts);
        tempoTrack.insertEvent(t);
        tempoTrack.insertEvent(pc);
        
        int currentTotalNoteLength = 480;
        
        for (int i = 0; i < notes.size(); i++) {
            int channel = 0;
            int pitch = notes.get(i).getNotevalue();
            int velocity = 100;
            int length = 480;
            float fLength = 480.0f;

            Log.d("MYLOG", "length of current note: " + notes.get(i).getLength());
            
            fLength = notes.get(i).getLength();
            if (notes.get(i).getVelocity() > 0)
                velocity = notes.get(i).getVelocity();
            
            if (fLength > 0.0)
                fLength = (480 * notes.get(i).getLength());
            else
                fLength = 480;
            
            length = (int) fLength;
                        
            NoteOn on = new NoteOn(i * currentTotalNoteLength, channel, pitch, velocity);
            NoteOff off = new NoteOff(i * currentTotalNoteLength + length, channel, pitch, 0);
            
            noteTrack.insertEvent(on);
            noteTrack.insertEvent(off);
            
            noteTrack.insertNote(channel, pitch, velocity, i * currentTotalNoteLength, length);
            
            if (length > 0)
                currentTotalNoteLength = 480; // TODO: make this match note length (better) somehow
            else
                currentTotalNoteLength = 480;
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
    
    private List<Note> logicA(HashMap<Integer, List<Note>> allNotesets) {
        List<Note> notes = new ArrayList<Note>();

        List<Integer> lookingForNotesInKey = new ArrayList<Integer>();
        Note lastNote = new Note();
        lastNote.setNotevalue(0);
        
        Random random = new Random();
        List<Integer> keys = new ArrayList<Integer>(allNotesets.keySet());
        Integer randomKey = keys.get(random.nextInt(keys.size()));
        
        // loop through all found emotion-related notesets
        for (int i = 0; i < 8; i++) {
        
            List<Note> nsets = new ArrayList<Note>();
            
            Log.d("MYLOG", "> last note: " + lastNote.getNotevalue());
            
            if (lastNote.getNotevalue() == 0) {
            // get random noteset (and try to find one that matches the new musical key search focus)
            random = new Random();
            keys = new ArrayList<Integer>(allNotesets.keySet());
            randomKey = keys.get(random.nextInt(keys.size()));
            
            // get individual noteset
            nsets = allNotesets.get(randomKey);
            } else {
                if (allNotesets.get(lastNote) != null) {
                    nsets = allNotesets.get(lastNote);
                } else {
                    nsets = allNotesets.get(60);
                }
            }
            
            try {
                // check if last note in current noteset sequence matches first note in a musical key list
                // a match gives us criteria for finding another, similar noteset to append for playback
                Log.d("MYLOG", "found matching musical key for end note: " + nsets.get(3).getNotevalue());
                
                if (musicalKeys.get(nsets.get(3).getNotevalue()) != null) {
                    lookingForNotesInKey = musicalKeys.get(nsets.get(3).getNotevalue());
                } else {
                    lookingForNotesInKey = musicalKeys.get(60);
                }
            } catch (Exception e) {
                Log.d("MYLOG", e.getStackTrace().toString());
            }        
                
            for (int j = 0; j < 50; j++) {
                random = new Random();
                keys = new ArrayList<Integer>(allNotesets.keySet());
                randomKey = keys.get(random.nextInt(keys.size()));
                
                List<Note> noteset = allNotesets.get(randomKey);
                
                if (lookingForNotesInKey.contains(noteset.get(0).getNotevalue())) {
                    Log.d("MYLOG", "looking for notes in key: " + lookingForNotesInKey.toString() + " -- note 0: " + noteset.get(0).getNotevalue());
                    
                    for (int k = 0; k < 4; k++) {
                        try {
                            notes.add(noteset.get(k));
                        } catch (Exception e) {
                            Log.d("MYLOG", e.getStackTrace().toString());
                        }
                    }
                    lastNote = noteset.get(3);
                    break;
                }
                
            }
        }
        
        return notes;
    }
}