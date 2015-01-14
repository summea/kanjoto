
package com.andrewsummers.otashu.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.andrewsummers.otashu.R;
import com.andrewsummers.otashu.data.EmotionsDataSource;
import com.andrewsummers.otashu.data.NotesDataSource;
import com.andrewsummers.otashu.data.NotesetsDataSource;
import com.andrewsummers.otashu.model.Emotion;
import com.andrewsummers.otashu.model.Note;
import com.andrewsummers.otashu.model.Noteset;
import com.andrewsummers.otashu.model.NotesetAndRelated;

import android.app.Activity;
import android.content.Context;
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
import android.widget.Toast;

/**
 * CreateNotesetActivity is an Activity which provides users the ability to create new notesets.
 */
public class CreateNotesetActivity extends Activity implements OnClickListener {
    private Button buttonSave = null;
    private Noteset newlyInsertedNoteset;
    private Button buttonPlayNoteset = null;
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
        setContentView(R.layout.activity_create_noteset);

        // add listeners to buttons
        buttonSave = (Button) findViewById(R.id.button_save);
        buttonSave.setOnClickListener(this);

        // open data source handle
        EmotionsDataSource eds = new EmotionsDataSource(this);
        eds.open();

        List<Emotion> allEmotions = new ArrayList<Emotion>();
        allEmotions = eds.getAllEmotions();

        eds.close();

        Spinner spinner = null;

        // locate next spinner in layout
        spinner = (Spinner) findViewById(R.id.spinner_emotion);

        // create array adapter for list of emotions
        ArrayAdapter<Emotion> emotionsAdapter = new ArrayAdapter<Emotion>(this,
                android.R.layout.simple_spinner_item);
        emotionsAdapter.addAll(allEmotions);

        // specify the default layout when list of choices appears
        emotionsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // apply this adapter to the spinner
        spinner.setAdapter(emotionsAdapter);

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

        int[] velocitySpinnerItems = {
                R.id.spinner_note1_velocity, R.id.spinner_note2_velocity,
                R.id.spinner_note3_velocity, R.id.spinner_note4_velocity
        };

        for (int i = 0; i < velocitySpinnerItems.length; i++) {
            spinner = (Spinner) findViewById(velocitySpinnerItems[i]);
            adapter = ArrayAdapter
                    .createFromResource(this, R.array.velocity_values_array,
                            android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
            spinner.setSelection(adapter.getPosition(String.valueOf("100"))); // start at 80
                                                                              // velocity
        }

        int[] lengthSpinnerItems = {
                R.id.spinner_note1_length, R.id.spinner_note2_length, R.id.spinner_note3_length,
                R.id.spinner_note4_length
        };

        for (int i = 0; i < lengthSpinnerItems.length; i++) {
            spinner = (Spinner) findViewById(lengthSpinnerItems[i]);
            adapter = ArrayAdapter
                    .createFromResource(this, R.array.length_values_array,
                            android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
            spinner.setSelection(adapter.getPosition(String.valueOf("0.5"))); // start at 0.5 length
        }

        try {
            // add listeners to buttons
            buttonPlayNoteset = (Button) findViewById(R.id.button_play_noteset);
            buttonPlayNoteset.setOnClickListener(this);
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
        String[] velocityValuesArray = getResources().getStringArray(R.array.velocity_values_array);
        String[] lengthValuesArray = getResources().getStringArray(R.array.length_values_array);

        int[] spinnerIds = {
                R.id.spinner_note1,
                R.id.spinner_note2,
                R.id.spinner_note3,
                R.id.spinner_note4
        };

        int[] velocitySpinnerIds = {
                R.id.spinner_note1_velocity,
                R.id.spinner_note2_velocity,
                R.id.spinner_note3_velocity,
                R.id.spinner_note4_velocity
        };

        int[] lengthSpinnerIds = {
                R.id.spinner_note1_length,
                R.id.spinner_note2_length,
                R.id.spinner_note3_length,
                R.id.spinner_note4_length
        };

        switch (v.getId()) {
            case R.id.button_save:
                // gather noteset data from form
                Spinner spinner;

                Noteset notesetToInsert = new Noteset();
                List<Note> notesToInsert = new ArrayList<Note>();

                // get select emotion's id

                EmotionsDataSource eds = new EmotionsDataSource(this);
                eds.open();

                List<Integer> allEmotionIds = new ArrayList<Integer>();
                allEmotionIds = eds.getAllEmotionIds();
                eds.close();

                Spinner emotionSpinner = (Spinner) findViewById(R.id.spinner_emotion);
                int selectedEmotionValue = 0;

                // for times when emotion is not selected
                try {
                    selectedEmotionValue = allEmotionIds.get(emotionSpinner
                            .getSelectedItemPosition());
                } catch (Exception e) {
                    Log.d("MYLOG", e.getStackTrace().toString());
                }

                eds.close();

                notesetToInsert.setEmotion(selectedEmotionValue);
                notesetToInsert.setEnabled(1);

                for (int i = 0; i < spinnerIds.length; i++) {
                    spinner = (Spinner) findViewById(spinnerIds[i]);
                    Spinner velocitySpinner = (Spinner) findViewById(velocitySpinnerIds[i]);
                    Spinner lengthSpinner = (Spinner) findViewById(lengthSpinnerIds[i]);

                    Note noteToInsert = new Note();
                    noteToInsert.setNotevalue(Integer.parseInt(noteValuesArray[spinner
                            .getSelectedItemPosition()]));
                    noteToInsert.setVelocity(Integer.parseInt(velocityValuesArray[velocitySpinner
                            .getSelectedItemPosition()]));
                    noteToInsert.setLength(Float.parseFloat(lengthValuesArray[lengthSpinner
                            .getSelectedItemPosition()]));
                    noteToInsert.setPosition(i + 1); // positions 1, 2, 3, 4, etc.

                    notesToInsert.add(noteToInsert);
                }

                // check if noteset already exists, first
                NotesetAndRelated notesetAndRelated = new NotesetAndRelated();
                notesetAndRelated.setNoteset(notesetToInsert);
                notesetAndRelated.setNotes(notesToInsert);
                boolean notesetExists = doesNotesetExist(notesetAndRelated);

                if (!notesetExists) {
                    // first insert new noteset (parent of all related notes)
                    saveNoteset(v, notesetToInsert);

                    // then save individual notes
                    for (Note note : notesetAndRelated.getNotes()) {
                        note.setNotesetId(newlyInsertedNoteset.getId());
                        saveNote(v, note);
                    }

                    finish();
                } else {
                    Context context = getApplicationContext();
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context,
                            context.getResources().getString(R.string.noteset_exists),
                            duration);
                    toast.show();
                }

                break;

            case R.id.button_play_noteset:

                // disable play button while playing
                buttonPlayNoteset = (Button) findViewById(R.id.button_play_noteset);
                buttonPlayNoteset.setClickable(false);

                List<Note> notes = new ArrayList<Note>();

                for (int i = 0; i < spinnerIds.length; i++) {
                    spinner = (Spinner) findViewById(spinnerIds[i]);
                    Spinner velocitySpinner = (Spinner) findViewById(velocitySpinnerIds[i]);
                    Spinner lengthSpinner = (Spinner) findViewById(lengthSpinnerIds[i]);

                    Note note = new Note();
                    note.setNotevalue(Integer.parseInt(noteValuesArray[spinner
                            .getSelectedItemPosition()]));
                    note.setVelocity(Integer.parseInt(velocityValuesArray[velocitySpinner
                            .getSelectedItemPosition()]));
                    note.setLength(Float.parseFloat(lengthValuesArray[lengthSpinner
                            .getSelectedItemPosition()]));

                    notes.add(note);
                }

                // get default instrument for playback
                SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
                String defaultInstrument = sharedPref.getString("pref_default_instrument", "");
                int playbackSpeed = Integer.valueOf(sharedPref.getString(
                        "pref_default_playback_speed", "120"));

                GenerateMusicActivity generateMusic = new GenerateMusicActivity();
                generateMusic.generateMusic(notes, musicSource, defaultInstrument, playbackSpeed);

                // play generated notes for user
                playMusic(musicSource);

                // return to previous activity when done playing
                mediaPlayer.setOnCompletionListener(new OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer aMediaPlayer) {
                        // enable play button again
                        buttonPlayNoteset.setClickable(true);
                    }
                });

                break;
        }
    }

    private boolean doesNotesetExist(NotesetAndRelated notesetAndRelated) {
        boolean notesetExists = true;
        NotesDataSource nds = new NotesDataSource(this);

        notesetExists = nds.doesNotesetExist(notesetAndRelated);

        return notesetExists;
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

    /**
     * Save noteset data.
     * 
     * @param v Incoming view.
     * @param data Incoming string of data to be saved.
     */
    private void saveNoteset(View v, Noteset noteset) {
        // save noteset in database
        NotesetsDataSource nds = new NotesetsDataSource(this);
        newlyInsertedNoteset = nds.createNoteset(noteset);
        nds.close();

        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context,
                context.getResources().getString(R.string.noteset_saved),
                duration);
        toast.show();
    }

    private void saveNote(View v, Note note) {

        // save noteset in database
        NotesDataSource nds = new NotesDataSource(this);
        nds.createNote(note);
        nds.close();

        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context,
                context.getResources().getString(R.string.noteset_saved),
                duration);
        toast.show();
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
