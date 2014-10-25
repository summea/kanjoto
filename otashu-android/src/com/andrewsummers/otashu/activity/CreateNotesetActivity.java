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

import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

/**
 * CreateNotesetActivity is an Activity which provides users the ability to
 * create new notesets.
 */
public class CreateNotesetActivity extends Activity implements OnClickListener {
    private Button buttonSave = null;
    private EmotionsDataSource emotionsDataSource;
    private NotesetsDataSource notesetsDataSource;
    private NotesDataSource notesDataSource;
    private Noteset newlyInsertedNoteset;
    private Button buttonPlayNoteset = null;
    private File path = Environment.getExternalStorageDirectory();
    private String externalDirectory = path.toString() + "/otashu/";
    private File musicSource = new File(externalDirectory + "otashu_preview.mid");
    private MediaPlayer mediaPlayer = new MediaPlayer();

    /**
     * onCreate override that provides noteset creation view to user .
     * 
     * @param savedInstanceState
     *            Current application state data.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // get specific layout for content view
        setContentView(R.layout.activity_create_noteset);

        // add listeners to buttons
        // have to cast to Button in this case
        buttonSave = (Button) findViewById(R.id.button_save);
        buttonSave.setOnClickListener(this);

        // open data source handle
        notesetsDataSource = new NotesetsDataSource(this);
        notesetsDataSource.open();
        
        notesDataSource = new NotesDataSource(this);
        notesDataSource.open();
        
        emotionsDataSource = new EmotionsDataSource(this);
        emotionsDataSource.open();

        List<Emotion> allEmotions = new ArrayList<Emotion>();
        allEmotions = emotionsDataSource.getAllEmotions();
        
        emotionsDataSource.close();
        
        Log.d("MYLOG", "emotions: " + allEmotions);

        Spinner spinner = null;

        // locate next spinner in layout
        spinner = (Spinner) findViewById(R.id.spinner_emotion);
        
        // create array adapter for list of emotions
        ArrayAdapter<Emotion> emotionsAdapter = new ArrayAdapter<Emotion>(this, android.R.layout.simple_spinner_item);
        emotionsAdapter.addAll(allEmotions);
        
        // specify the default layout when list of choices appears
        emotionsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        
        // apply this adapter to the spinner
        spinner.setAdapter(emotionsAdapter);

        
        ArrayAdapter<CharSequence> adapter = null;
        
        int[] spinnerItems = {R.id.spinner_note1, R.id.spinner_note2, R.id.spinner_note3, R.id.spinner_note4};
        
        for (int i = 0; i < spinnerItems.length; i++) {
            spinner = (Spinner) findViewById(spinnerItems[i]);
            adapter = ArrayAdapter
                    .createFromResource(this, R.array.note_labels_array,
                            android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
            spinner.setSelection(adapter.getPosition(String.valueOf("C4")));  // start at note C4
        }
        
        int[] velocitySpinnerItems = {R.id.spinner_note1_velocity, R.id.spinner_note2_velocity, R.id.spinner_note3_velocity, R.id.spinner_note4_velocity};
        
        for (int i = 0; i < velocitySpinnerItems.length; i++) {
            spinner = (Spinner) findViewById(velocitySpinnerItems[i]);
            adapter = ArrayAdapter
                    .createFromResource(this, R.array.velocity_values_array,
                            android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
            spinner.setSelection(adapter.getPosition(String.valueOf("80")));  // start at 80 velocity
        }
        
        int[] lengthSpinnerItems = {R.id.spinner_note1_length, R.id.spinner_note2_length, R.id.spinner_note3_length, R.id.spinner_note4_length};
        
        for (int i = 0; i < lengthSpinnerItems.length; i++) {
            spinner = (Spinner) findViewById(lengthSpinnerItems[i]);
            adapter = ArrayAdapter
                    .createFromResource(this, R.array.length_values_array,
                            android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
            spinner.setSelection(adapter.getPosition(String.valueOf("0.5")));  // start at 0.5 length
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
     * onClick override used to save noteset data once user clicks save button.
     * 
     * @param view
     *            Incoming view.
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
            String notesetName;
            Spinner spinner;
            
            Noteset notesetToInsert = new Noteset();
            Note noteToInsert = new Note();
            
            // get select emotion's id
            
            emotionsDataSource = new EmotionsDataSource(this);
            emotionsDataSource.open();

            List<Integer> allEmotionIds = new ArrayList<Integer>();
            allEmotionIds = emotionsDataSource.getAllEmotionIds();
            
            Spinner emotionSpinner = (Spinner) findViewById(R.id.spinner_emotion);
            int selectedEmotionValue = 0;
            
            // for times when emotion is not selected
            try {
                selectedEmotionValue = allEmotionIds.get(emotionSpinner.getSelectedItemPosition());
            } catch (Exception e) {
                Log.d("MYLOG", e.getStackTrace().toString());
            }
            
            emotionsDataSource.close();
            
            Log.d("MYLOG", "selected emotion value: " + selectedEmotionValue);
            
            notesetToInsert.setEmotion(selectedEmotionValue);
            
            // first insert new noteset (parent of all related notes)
            saveNoteset(v, notesetToInsert);
            
            for (int i = 0; i < spinnerIds.length; i++) {
                spinner = (Spinner) findViewById(spinnerIds[i]);
                Spinner velocitySpinner = (Spinner) findViewById(velocitySpinnerIds[i]);
                Spinner lengthSpinner = (Spinner) findViewById(lengthSpinnerIds[i]);
                
                noteToInsert.setNotesetId(newlyInsertedNoteset.getId());
                noteToInsert.setPosition(i+1);  // positions 1, 2, 3, 4, etc.
                noteToInsert.setVelocity(Integer.parseInt(velocityValuesArray[velocitySpinner.getSelectedItemPosition()]));
                noteToInsert.setLength(Float.parseFloat(lengthValuesArray[lengthSpinner.getSelectedItemPosition()]));
                
                Log.d("MYLOG", String.valueOf(noteValuesArray[spinner.getSelectedItemPosition()]));
                noteToInsert.setNotevalue(Integer.parseInt(noteValuesArray[spinner.getSelectedItemPosition()]));
                
                saveNote(v, noteToInsert);
            }
            
            finish();
            break;
            
        case R.id.button_play_noteset:
            List<Note> notes = new ArrayList<Note>();
            
            for (int i = 0; i < spinnerIds.length; i++) {
                spinner = (Spinner) findViewById(spinnerIds[i]);
                Spinner velocitySpinner = (Spinner) findViewById(velocitySpinnerIds[i]);
                Spinner lengthSpinner = (Spinner) findViewById(lengthSpinnerIds[i]);
                
                Note note = new Note();
                note.setNotevalue(Integer.parseInt(noteValuesArray[spinner.getSelectedItemPosition()]));
                note.setVelocity(Integer.parseInt(velocityValuesArray[velocitySpinner.getSelectedItemPosition()]));
                note.setLength(Float.parseFloat(lengthValuesArray[lengthSpinner.getSelectedItemPosition()]));
                
                notes.add(note);
            }
            
            GenerateMusicActivity generateMusic = new GenerateMusicActivity();
            generateMusic.generateMusic(notes, musicSource);

            // play generated notes for user
            playMusic(musicSource);
            
            break;
        }
    }

    /**
     * onResume override used to open up data source when resuming activity.
     */
    @Override
    protected void onResume() {
        emotionsDataSource.open();
        notesetsDataSource.open();
        notesDataSource.open();
        super.onResume();
    }

    /**
     * onPause override used to close data source when activity paused.
     */
    @Override
    protected void onPause() {
        emotionsDataSource.close();
        notesetsDataSource.close();
        notesDataSource.close();
        super.onPause();
    }

    /**
     * Save noteset data.
     * 
     * @param v
     *            Incoming view.
     * @param data
     *            Incoming string of data to be saved.
     */
    private void saveNoteset(View v, Noteset noteset) {

        // save noteset in database
        newlyInsertedNoteset = notesetsDataSource.createNoteset(noteset);

        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context,
                context.getResources().getString(R.string.noteset_saved),
                duration);
        toast.show();
    }
    
    private void saveNote(View v, Note note) {

        Log.d("MYLOG", Long.toString(note.getNotesetId()));
        Log.d("MYLOG", Integer.toString(note.getNotevalue()));
                
        // save noteset in database
        notesDataSource.createNote(note);

        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context,
                context.getResources().getString(R.string.noteset_saved),
                duration);
        toast.show();
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