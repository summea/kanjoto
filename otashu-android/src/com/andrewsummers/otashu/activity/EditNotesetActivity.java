package com.andrewsummers.otashu.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
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
public class EditNotesetActivity extends Activity implements OnClickListener {
    private Button buttonSave = null;
    private EmotionsDataSource emotionsDataSource;
    private NotesetsDataSource notesetsDataSource;
    private NotesDataSource notesDataSource;
    private Noteset editNoteset;
    private List<Note> editNotes = new LinkedList<Note>();
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
        setContentView(R.layout.activity_edit_noteset);

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
        
        Log.d("MYLOG", "got list item id: " + getIntent().getExtras().getLong("list_id"));
        int notesetId = (int) getIntent().getExtras().getLong("list_id");
        
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
        HashMap<Integer, List<Note>> notesetBundle = new HashMap<Integer, List<Note>>();
        notesetBundle = ds.getNotesetBundle(allNotesets[notesetId]);
        
        Log.d("MYLOG", "noteset bundle: " + notesetBundle);
        Log.d("MYLOG", "notesetId::: " + allNotesets[notesetId]);
        
        Noteset noteset = ds.getNoteset(allNotesets[notesetId]); 
        editNoteset = ds.getNoteset(allNotesets[notesetId]);
        
        
        // get data for noteset that is being edited
        HashMap<String, List<Object>> editingNoteset = new HashMap<String, List<Object>>();
        editingNoteset = notesetsDataSource.getNotesetBundleDetail(noteset.getId());
                
        List<Object> notesets = editingNoteset.get("noteset");
        noteset = (Noteset) notesets.get(0);
        
        List<Object> notes = editingNoteset.get("notes");
        //Note note = new Note();
        int sizeOfNotes = notes.size();
        Log.d("MYLOG", String.valueOf(sizeOfNotes));
        
        for (int i = 0; i < notes.size(); i++) {
            Note note = new Note();
            note = (Note) notes.get(i);
            editNotes.add(note);
            Log.d("MYLOG", "editing note: " + note.getId());
        }

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
        
        
        //spinner.setSelection(noteset.getEmotion());
        for (int i=0; i < allEmotions.size(); i++) {
            if (allEmotions.get(i).getId() == noteset.getEmotion()) {
                spinner.setSelection(i);
                break;
            }
        }
        
        String[] noteLabelsArray = getResources().getStringArray(R.array.note_labels_array);
        String[] noteValuesArray = getResources().getStringArray(R.array.note_values_array);
        
        ArrayAdapter<CharSequence> adapter = null;
        
        int[] spinnerItems = {R.id.spinner_note1, R.id.spinner_note2, R.id.spinner_note3, R.id.spinner_note4};
        
        for (int i = 0; i < spinnerItems.length; i++) {
            Note note = (Note) notes.get(i);
            spinner = (Spinner) findViewById(spinnerItems[i]);
            adapter = ArrayAdapter
                    .createFromResource(this, R.array.note_labels_array,
                            android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
            String noteName = "C4";
            for (int j = 0; j < noteValuesArray.length; j++) {
                // get actual note name (C3, D3, E3, etc.)
                if (note.getNotevalue() == Integer.valueOf(noteValuesArray[j])) {
                    noteName = noteLabelsArray[j];
                }
            }
            spinner.setSelection(adapter.getPosition(noteName)); // get currently saved notevalue
        }
        
        int[] velocitySpinnerItems = {R.id.spinner_note1_velocity, R.id.spinner_note2_velocity, R.id.spinner_note3_velocity, R.id.spinner_note4_velocity};
        
        for (int i = 0; i < velocitySpinnerItems.length; i++) {
            Note note = (Note) notes.get(i);
            spinner = (Spinner) findViewById(velocitySpinnerItems[i]);
            adapter = ArrayAdapter
                    .createFromResource(this, R.array.velocity_values_array,
                            android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
            spinner.setSelection(adapter.getPosition(String.valueOf(note.getVelocity())));  // get current velocity value for spinner default
        }
        
        int[] lengthSpinnerItems = {R.id.spinner_note1_length, R.id.spinner_note2_length, R.id.spinner_note3_length, R.id.spinner_note4_length};
        
        for (int i = 0; i < lengthSpinnerItems.length; i++) {
            Note note = (Note) notes.get(i);
            spinner = (Spinner) findViewById(lengthSpinnerItems[i]);
            adapter = ArrayAdapter
                    .createFromResource(this, R.array.length_values_array,
                            android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
            spinner.setSelection(adapter.getPosition(String.valueOf(note.getLength())));  // get current length value for spinner default
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
            Spinner spinner;
            
            Noteset notesetToInsert = new Noteset();
            Note noteToInsert = new Note();
            
            // get select emotion's id
            
            emotionsDataSource = new EmotionsDataSource(this);
            emotionsDataSource.open();

            List<Integer> allEmotionIds = new ArrayList<Integer>();
            allEmotionIds = emotionsDataSource.getAllEmotionIds();
            
            Spinner emotionSpinner = (Spinner) findViewById(R.id.spinner_emotion);
            int selectedEmotionValue = allEmotionIds.get(emotionSpinner.getSelectedItemPosition());
            
            emotionsDataSource.close();
            
            Log.d("MYLOG", "selected emotion value: " + selectedEmotionValue);

            notesetToInsert.setId(editNoteset.getId());
            notesetToInsert.setEmotion(selectedEmotionValue);
            
            // first insert new noteset (parent of all related notes)
            saveNotesetUpdates(v, notesetToInsert);
            
            for (int i = 0; i < spinnerIds.length; i++) {
                spinner = (Spinner) findViewById(spinnerIds[i]);
                Spinner velocitySpinner = (Spinner) findViewById(velocitySpinnerIds[i]);
                Spinner lengthSpinner = (Spinner) findViewById(lengthSpinnerIds[i]);
                
                noteToInsert = editNotes.get(i);
                Log.d("MYLOG", String.valueOf(noteValuesArray[spinner.getSelectedItemPosition()]));
                noteToInsert.setNotevalue(Integer.parseInt(noteValuesArray[spinner.getSelectedItemPosition()]));
                noteToInsert.setVelocity(Integer.parseInt(velocityValuesArray[velocitySpinner.getSelectedItemPosition()]));
                noteToInsert.setLength(Float.parseFloat(lengthValuesArray[lengthSpinner.getSelectedItemPosition()]));
                
                saveNoteUpdates(v, noteToInsert);
            }
            
            finish();
            break;
            
        case R.id.button_play_noteset:
            List<Note> notes = new ArrayList<Note>();
            
            for (int i = 0; i < spinnerIds.length; i++) {
                spinner = (Spinner) findViewById(spinnerIds[i]);
                Spinner velocitySpinner = (Spinner) findViewById(velocitySpinnerIds[i]);
                Spinner lengthSpinner = (Spinner) findViewById(lengthSpinnerIds[i]);
                
                Note note = editNotes.get(i);
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
    private void saveNotesetUpdates(View v, Noteset noteset) {

        // update noteset in database
        notesetsDataSource.updateNoteset(noteset);

        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context,
                context.getResources().getString(R.string.noteset_saved),
                duration);
        toast.show();
    }
    
    private void saveNoteUpdates(View v, Note note) {

        Log.d("MYLOG", Long.toString(note.getNotesetId()));
        Log.d("MYLOG", Integer.toString(note.getNotevalue()));
        
        //  update note in database
        notesDataSource.updateNote(note);

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