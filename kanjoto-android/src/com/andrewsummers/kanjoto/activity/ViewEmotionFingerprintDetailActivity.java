
package com.andrewsummers.kanjoto.activity;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.andrewsummers.kanjoto.data.EdgesDataSource;
import com.andrewsummers.kanjoto.model.Edge;
import com.andrewsummers.kanjoto.model.Note;
import com.andrewsummers.kanjoto.view.DrawView;
import com.leff.midi.MidiFile;
import com.leff.midi.MidiTrack;
import com.leff.midi.event.NoteOff;
import com.leff.midi.event.NoteOn;
import com.leff.midi.event.ProgramChange;
import com.leff.midi.event.meta.Tempo;
import com.leff.midi.event.meta.TimeSignature;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.util.SparseArray;
import android.util.SparseIntArray;

/**
 * View details of a particular Emotion Fingerprint.
 * <p>
 * This activity allows a user to see more information about a particular Emotion Fingerprint.
 * </p>
 */
public class ViewEmotionFingerprintDetailActivity extends Activity {
    private long emotionId = 0;
    private SharedPreferences sharedPref;
    private long graphId;
    DrawView drawView;
    private long apprenticeId = 0;
    File path = Environment.getExternalStorageDirectory();
    String externalDirectory = path.toString() + "/kanjoto/";
    File bitmapSource = new File(externalDirectory + "emofing.png");
    private int sendEmofing = 0;
    int selectedInstrumentId = -1;
    int playbackSpeed = 120;
    File musicSource = new File(externalDirectory + "emofing.mid");
    private static MediaPlayer mediaPlayer;

    /**
     * onCreate override used to get details view.
     * 
     * @param savedInstanceState Current application state data.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 1. Choose an emotion
        long intentEmotionId = getIntent().getLongExtra("emotion_id", 0);
        if (intentEmotionId > 0) {
            emotionId = intentEmotionId;
        } else {
            emotionId = getIntent().getExtras().getLong("list_id");
        }

        // get emotion graph id for Apprentice's note relationships graph
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        graphId = Long.parseLong(sharedPref.getString(
                "pref_emotion_graph_for_apprentice", "1"));
        apprenticeId = Long.parseLong(sharedPref.getString(
                "pref_selected_apprentice", "1"));

        sendEmofing = getIntent().getIntExtra("send_emofing", 0);

        // 2. Gather all found paths
        SparseArray<SparseIntArray> emofingData = gatherEmotionPaths(0.5f);

        // 7. Plot root number reductions (the emofing)
        drawView = new DrawView(this, emofingData);
        drawView.setBackgroundColor(Color.BLACK);
        setContentView(drawView);
        drawView.setDrawingCacheEnabled(true);

        if (sendEmofing != 1) {
            // get default instrument for playback
            String defaultInstrument = sharedPref.getString("pref_default_instrument", "");
            playbackSpeed = Integer.valueOf(sharedPref.getString("pref_default_playback_speed",
                    "120"));

            List<Note> notes = new ArrayList<Note>();

            for (int i = 1; i <= emofingData.size(); i++) {
                for (int j = 1; j <= 12; j++) {
                    int notevalue = 0;
                    if (emofingData.get(i).get(j) > 0) {
                        switch (j) {
                            case 1:
                                notevalue = 60;
                                break;
                            case 2:
                                notevalue = 61;
                                break;
                            case 3:
                                notevalue = 62;
                                break;
                            case 4:
                                notevalue = 63;
                                break;
                            case 5:
                                notevalue = 64;
                                break;
                            case 6:
                                notevalue = 65;
                                break;
                            case 7:
                                notevalue = 66;
                                break;
                            case 8:
                                notevalue = 67;
                                break;
                            case 9:
                                notevalue = 68;
                                break;
                            case 10:
                                notevalue = 69;
                                break;
                            case 11:
                                notevalue = 70;
                                break;
                            case 12:
                                notevalue = 71;
                                break;
                        }

                        Note note = new Note();
                        note.setNotevalue(notevalue);

                        notes.add(note);
                    }
                }
            }

            generateMusic(notes, musicSource, defaultInstrument, playbackSpeed);

            playMusic(musicSource);
        }

        // close activity because if we are sending emofing after this
        if (sendEmofing == 1) {
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {

                @Override
                public void run() {
                    finish();
                }

            }, 5000);
        }
    }

    public SparseArray<SparseIntArray> gatherEmotionPaths(float maxWeight) {
        SparseArray<SparseIntArray> foundPaths = new SparseArray<SparseIntArray>();

        // initialize
        for (int i = 1; i <= 4; i++) {
            SparseIntArray values = new SparseIntArray();
            for (int j = 1; j <= 12; j++) {
                values.put(j, 0);
            }
            foundPaths.put(i, values);
        }

        // 3. Look for all possible Emotion Graph paths that are stronger (lower than) X weight
        EdgesDataSource eds = new EdgesDataSource(this);
        SparseArray<List<Edge>> foundEdges = eds.getPathsForEmotion(apprenticeId, graphId,
                emotionId, 0.5f);

        // 5. Store noteset path root number reductions into data structure
        SparseIntArray rootNumbersMap = new SparseIntArray();
        boolean rootNumbersMapCreated = false;

        try {
            for (int i = 1; i < foundEdges.size() + 1; i++) {
                for (Edge edge : foundEdges.get(i)) {

                    if (!rootNumbersMapCreated) {
                        // firstNote is what is used for the root number start (note "I")
                        int firstNote = 0;

                        if (edge.getPosition() == 1) {
                            firstNote = edge.getFromNodeId();
                            rootNumbersMap.put(firstNote, 1);
                        }

                        // 4. Convert noteset paths into root numbers +/- {1, ..., 12}
                        // (ex: I, II, III, IV ...)
                        // QUESTION: should this be done for each path?
                        // range of notevalues between C4...B4 (60...71)

                        int key = firstNote;
                        for (int j = 2; j < 13; j++) {
                            if (key > 70) {
                                key = 60;
                            } else {
                                key++;
                            }
                            rootNumbersMap.put(key, j);
                        }
                        rootNumbersMapCreated = true;
                    }

                    SparseIntArray values = foundPaths.get(edge.getPosition());
                    int newValue = values.get(rootNumbersMap.get(edge.getFromNodeId())) + 1;
                    values.put(rootNumbersMap.get(edge.getFromNodeId()), newValue);

                    // add found mapped notevalues to our result
                    foundPaths.put(edge.getPosition(), values);

                    if (edge.getPosition() == 3) {
                        values = foundPaths.get(edge.getPosition() + 1);
                        newValue = values.get(rootNumbersMap.get(edge.getToNodeId())) + 1;
                        values.put(rootNumbersMap.get(edge.getToNodeId()), newValue);

                        // add found mapped notevalues to our result
                        foundPaths.put(edge.getPosition() + 1, values);
                    }
                }
            }
        } catch (Exception e) {
            Log.d("MYLOG", e.getStackTrace().toString());
        }
        return foundPaths;
    }

    /**
     * Generate music and write results to a MIDI file.
     * 
     * @param notes List<Note> of notes to write to file.
     * @param musicSource File location of musicSource file for writing.
     * @param defaultInstrument
     */
    public void generateMusic(List<Note> notes, File musicSource, String defaultInstrument,
            int playbackSpeed) {
        MidiTrack tempoTrack = new MidiTrack();
        MidiTrack noteTrack = new MidiTrack();

        TimeSignature ts = new TimeSignature();
        ts.setTimeSignature(4, 4, TimeSignature.DEFAULT_METER, TimeSignature.DEFAULT_DIVISION);

        Tempo t = new Tempo();
        t.setBpm(playbackSpeed);

        if ((selectedInstrumentId < 0) && (defaultInstrument != null)) {
            try {
                selectedInstrumentId = Integer.valueOf(defaultInstrument);
            } catch (Exception e) {
                Log.d("MYLOG", e.getStackTrace().toString());
                // set default to 1 (piano) if no default preference found
                selectedInstrumentId = 1;
            }
        }

        // set instrument type
        ProgramChange pc = new ProgramChange(0, 0, selectedInstrumentId);

        tempoTrack.insertEvent(ts);
        tempoTrack.insertEvent(t);
        tempoTrack.insertEvent(pc);

        int currentTotalNoteLength = 960;

        for (int i = 0; i < notes.size(); i++) {
            int channel = 0;
            int pitch = notes.get(i).getNotevalue();
            int velocity = 100;
            int length = 960;
            float fLength = 960.0f;

            fLength = notes.get(i).getLength();
            if (notes.get(i).getVelocity() > 0)
                velocity = notes.get(i).getVelocity();

            if (fLength > 0.0)
                fLength = (960 * notes.get(i).getLength());
            else
                fLength = 960;

            length = (int) fLength;

            NoteOn on = new NoteOn(i * currentTotalNoteLength, channel, pitch, velocity);
            NoteOff off = new NoteOff(i * currentTotalNoteLength + length, channel, pitch, 0);

            noteTrack.insertEvent(on);
            noteTrack.insertEvent(off);

            noteTrack.insertNote(channel, pitch, velocity, i * currentTotalNoteLength, length);

            if (length > 0) {
                currentTotalNoteLength = 960; // TODO: make this match note length (better) somehow
            } else {
                currentTotalNoteLength = 960;
            }
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
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(this, Uri.fromFile(musicSource));
        } else {
            mediaPlayer.release();
            mediaPlayer = MediaPlayer.create(this, Uri.fromFile(musicSource));
        }

        // play music
        mediaPlayer.start();
    }

    /**
     * onBackPressed override used to stop playing music when done with activity
     */
    @Override
    public void onBackPressed() {
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                // stop playing music
                mediaPlayer.stop();
            }
            mediaPlayer.release();
        }
        super.onBackPressed();
    }

}
