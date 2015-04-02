
package com.andrewsummers.otashu.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.andrewsummers.otashu.R;
import com.andrewsummers.otashu.data.EdgesDataSource;
import com.andrewsummers.otashu.data.NotesDataSource;
import com.andrewsummers.otashu.model.Note;

import android.app.Activity;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

public class GetEmotionFromNotesetActivity extends Activity implements OnClickListener {
    private long emotionGraphId;
    private SharedPreferences sharedPref;
    private List<Note> notes = null;
    private Button buttonPlayNoteset = null;
    private Button buttonGetEmotion = null;
    private File path = Environment.getExternalStorageDirectory();
    private String externalDirectory = path.toString() + "/otashu/";
    private File musicSource = new File(externalDirectory + "otashu_preview.mid");
    private static MediaPlayer mediaPlayer;

    /**
     * onCreate override that provides noteset creation view to user .
     * 
     * @param savedInstanceState Current application state data.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // get specific layout for content view
        setContentView(R.layout.activity_get_emotion_from_noteset);

        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        emotionGraphId = Long.parseLong(sharedPref.getString(
                "pref_emotion_graph_for_apprentice", "1"));

        // locate next spinner in layout
        Spinner spinner = (Spinner) findViewById(R.id.spinner_emotion);

        ArrayAdapter<CharSequence> adapter = null;

        int[] spinnerItems = {
                R.id.spinner_note1, R.id.spinner_note2, R.id.spinner_note3, R.id.spinner_note4
        };

        for (int i = 0; i < spinnerItems.length; i++) {
            spinner = (Spinner) findViewById(spinnerItems[i]);
            adapter = ArrayAdapter
                    .createFromResource(this, R.array.note_labels_array,
                            android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
            spinner.setSelection(adapter.getPosition(String.valueOf("C4"))); // start at note C4
        }

        try {
            // add listeners to buttons
            buttonPlayNoteset = (Button) findViewById(R.id.button_play_noteset);
            buttonPlayNoteset.setOnClickListener(this);

            buttonGetEmotion = (Button) findViewById(R.id.button_get_emotion);
            buttonGetEmotion.setOnClickListener(this);
        } catch (Exception e) {
            Log.d("MYLOG", e.getStackTrace().toString());
        }
    }

    /**
     * onClick override used to save noteset data once user clicks save button.
     * 
     * @param view Incoming view.
     */
    @Override
    public void onClick(View v) {

        String[] noteValuesArray = getResources().getStringArray(R.array.note_values_array);

        int[] spinnerIds = {
                R.id.spinner_note1,
                R.id.spinner_note2,
                R.id.spinner_note3,
                R.id.spinner_note4
        };

        Spinner spinner;

        notes = new ArrayList<Note>();

        for (int i = 0; i < spinnerIds.length; i++) {
            spinner = (Spinner) findViewById(spinnerIds[i]);

            Note note = new Note();
            note.setNotevalue(Integer.parseInt(noteValuesArray[spinner
                    .getSelectedItemPosition()]));

            notes.add(note);
        }

        switch (v.getId()) {
            case R.id.button_play_noteset:
                // get default instrument for playback
                SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
                String defaultInstrument = sharedPref.getString("pref_default_instrument", "");
                int playbackSpeed = Integer.valueOf(sharedPref.getString(
                        "pref_default_playback_speed", "120"));

                // generate music for playback
                GenerateMusicActivity generateMusic = new GenerateMusicActivity();
                generateMusic.generateMusic(notes, musicSource, defaultInstrument, playbackSpeed);

                // play generated notes for user
                playMusic(musicSource);

                // return to previous activity when done playing
                mediaPlayer.setOnCompletionListener(new OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer aMediaPlayer) {
                        // enable buttons if necessary
                    }
                });

                break;
            case R.id.button_get_emotion:
                // try to find an emotion match for given noteset
                Log.d("MYLOG", "looking for emotion match...");

                long foundEmotionId = 0;

                List<Integer> notevalues = new ArrayList<Integer>();
                for (Note note : notes) {
                    notevalues.add(note.getNotevalue());
                }

                // check emotion graph edges for a match
                EdgesDataSource eds = new EdgesDataSource(this);
                HashMap<String, String> result = eds
                        .getEmotionFromNotes(emotionGraphId, notevalues);

                String method = "Graph Approach";
                long emotionId = Long.parseLong(result.get("emotionId"));
                float certainty = Float.parseFloat(result.get("certainty"));

                if ((certainty <= 50.0) || (emotionId <= 0)) {
                    // check user's notesets for a match
                    // TODO

                    NotesDataSource nds = new NotesDataSource(this);
                    result = nds.getEmotionFromNotes(notevalues);

                    float notesetApproachCertainty = Float.parseFloat(result.get("certainty"));

                    if (notesetApproachCertainty > 50.0) {
                        method = "Noteset Approach";
                        emotionId = Long.parseLong(result.get("emotionId"));
                        certainty = Float.parseFloat(result.get("certainty"));
                    }
                }

                Log.d("MYLOG", "> final match result... method: " + method + " emotionId: "
                        + emotionId + " certainty: " + certainty);

                break;
        }
    }

    /**
     * onResume override used to open up data source when resuming activity.
     */
    @Override
    protected void onResume() {
        super.onResume();
    }

    /**
     * onPause override used to close data source when activity paused.
     */
    @Override
    protected void onPause() {
        super.onPause();
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
