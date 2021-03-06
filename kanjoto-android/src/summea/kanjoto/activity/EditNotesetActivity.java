
package summea.kanjoto.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import summea.kanjoto.data.EmotionsDataSource;
import summea.kanjoto.data.NotesDataSource;
import summea.kanjoto.data.NotesetsDataSource;
import summea.kanjoto.model.Emotion;
import summea.kanjoto.model.Note;
import summea.kanjoto.model.Noteset;
import summea.kanjoto.model.NotesetAndRelated;

import summea.kanjoto.R;

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
import android.widget.ToggleButton;

/**
 * EditNotesetActivity is an Activity which provides users the ability to edit notesets.
 * <p>
 * This activity provides a form for editing existing Notesets. Noteset to edit is selected either
 * via the "view all notesets" activity or by the related "edit" context menu. The edit form fills
 * in data found (from the database) for specified Noteset to edit and (if successful) any saved
 * updates will then be saved in the database.
 * </p>
 */
public class EditNotesetActivity extends Activity implements OnClickListener {
    private Button buttonSave = null;
    private Noteset editNoteset; // keep track of which Noteset is currently being edited
    private List<Note> editNotes = new LinkedList<Note>(); // keep track of which Notes are
                                                           // currently being edited
    private Button buttonPlayNoteset = null;
    private File path = Environment.getExternalStorageDirectory();
    private String externalDirectory = path.toString() + "/kanjoto/";
    private File musicSource = new File(externalDirectory + "kanjoto_preview.mid");
    private static MediaPlayer mediaPlayer;
    private SharedPreferences sharedPref;
    private long apprenticeId = 0;

    /**
     * onCreate override that provides noteset creation view to user .
     * 
     * @param savedInstanceState Current application state data.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // get specific layout for content view
        setContentView(R.layout.activity_edit_noteset);

        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        apprenticeId = Long.parseLong(sharedPref.getString(
                "pref_selected_apprentice", "1"));

        // add listeners to buttons
        buttonSave = (Button) findViewById(R.id.button_save);
        buttonSave.setOnClickListener(this);

        long notesetIdInTable = 0;
        notesetIdInTable = getIntent().getExtras().getLong("menu_noteset_id");

        // get noteset and notes information
        long notesetId = getIntent().getExtras().getLong("list_id");

        // from menubar option route
        if (notesetIdInTable > 0) {
            notesetId = notesetIdInTable;
        }

        NotesetsDataSource nds = new NotesetsDataSource(this);
        Noteset noteset = new Noteset();

        // if requested id is from ViewAllNotesetsActivity, get actual (long) id from allNotesets
        // array
        if (notesetIdInTable == 0) {
            noteset = nds.getNoteset(notesetId);
        }
        // otherwise, requested id is an actual (long) id (no extra lookup necessary)
        else {
            noteset = nds.getNoteset(notesetIdInTable);
        }

        editNoteset = nds.getNoteset(notesetId);

        // get data for noteset that is being edited
        HashMap<String, List<Object>> editingNoteset = new HashMap<String, List<Object>>();
        editingNoteset = nds.getNotesetBundleDetailById(noteset.getId());

        List<Object> notesets = editingNoteset.get("noteset");
        noteset = (Noteset) notesets.get(0);

        List<Object> notes = editingNoteset.get("notes");

        for (int i = 0; i < notes.size(); i++) {
            Note note = new Note();
            note = (Note) notes.get(i);
            editNotes.add(note);
        }

        nds.close();

        List<Emotion> allEmotions = new ArrayList<Emotion>();
        EmotionsDataSource eds = new EmotionsDataSource(this);
        allEmotions = eds.getAllEmotions(apprenticeId);
        eds.close();

        // locate next spinner in layout
        Spinner spinner = (Spinner) findViewById(R.id.spinner_emotion);

        // create array adapter for list of emotions
        ArrayAdapter<Emotion> emotionsAdapter = new ArrayAdapter<Emotion>(this,
                android.R.layout.simple_spinner_item);
        emotionsAdapter.addAll(allEmotions);

        // specify the default layout when list of choices appears
        emotionsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // apply this adapter to the spinner
        spinner.setAdapter(emotionsAdapter);

        // set current selection for spinner
        for (int i = 0; i < allEmotions.size(); i++) {
            if (allEmotions.get(i).getId() == noteset.getEmotion()) {
                spinner.setSelection(i);
                break;
            }
        }

        // is noteset enabled?
        ToggleButton enabledButton = (ToggleButton) findViewById(R.id.toggle_enabled);
        if (editNoteset.getEnabled() == 1) {
            enabledButton.setChecked(true);
        }

        String[] noteLabelsArray = getResources().getStringArray(R.array.note_labels_array);
        String[] noteValuesArray = getResources().getStringArray(R.array.note_values_array);

        ArrayAdapter<CharSequence> adapter = null;

        int[] spinnerItems = {
                R.id.spinner_note1, R.id.spinner_note2, R.id.spinner_note3, R.id.spinner_note4
        };

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

        int[] velocitySpinnerItems = {
                R.id.spinner_note1_velocity, R.id.spinner_note2_velocity,
                R.id.spinner_note3_velocity, R.id.spinner_note4_velocity
        };

        for (int i = 0; i < velocitySpinnerItems.length; i++) {
            Note note = (Note) notes.get(i);
            spinner = (Spinner) findViewById(velocitySpinnerItems[i]);
            adapter = ArrayAdapter
                    .createFromResource(this, R.array.velocity_values_array,
                            android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
            if (note.getVelocity() != 0) {
                // get current length value for spinner default
                spinner.setSelection(adapter.getPosition(String.valueOf(note.getVelocity())));
            } else {
                spinner.setSelection(75); // get current velocity value for spinner default
            }
        }

        int[] lengthSpinnerItems = {
                R.id.spinner_note1_length, R.id.spinner_note2_length, R.id.spinner_note3_length,
                R.id.spinner_note4_length
        };

        for (int i = 0; i < lengthSpinnerItems.length; i++) {
            Note note = (Note) notes.get(i);
            spinner = (Spinner) findViewById(lengthSpinnerItems[i]);
            adapter = ArrayAdapter
                    .createFromResource(this, R.array.length_values_array,
                            android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
            if (note.getLength() != 0) {
                // get current length value for spinner default
                spinner.setSelection(adapter.getPosition(String.valueOf(note.getLength())));
            } else {
                spinner.setSelection(2);
            }
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
                // check if noteset already exists, first
                NotesetAndRelated notesetAndRelated = new NotesetAndRelated();

                // gather noteset data from form
                Noteset notesetToUpdate = new Noteset();
                List<Note> notesToUpdate = new ArrayList<Note>();

                // get select emotion's id

                EmotionsDataSource eds = new EmotionsDataSource(this);
                List<Integer> allEmotionIds = new ArrayList<Integer>();
                allEmotionIds = eds.getAllEmotionIds(apprenticeId);

                Spinner emotionSpinner = (Spinner) findViewById(R.id.spinner_emotion);

                // is noteset enabled?
                ToggleButton enabledButton = (ToggleButton) findViewById(R.id.toggle_enabled);
                if (enabledButton.isChecked()) {
                    notesetToUpdate.setEnabled(1);
                } else {
                    notesetToUpdate.setEnabled(0);
                }

                eds.close();

                notesetToUpdate.setId(editNoteset.getId());
                notesetToUpdate.setEmotion(allEmotionIds.get((int) emotionSpinner
                        .getSelectedItemId()));
                notesetToUpdate.setApprenticeId(apprenticeId);

                for (int i = 0; i < spinnerIds.length; i++) {
                    Spinner spinner = (Spinner) findViewById(spinnerIds[i]);
                    Spinner velocitySpinner = (Spinner) findViewById(velocitySpinnerIds[i]);
                    Spinner lengthSpinner = (Spinner) findViewById(lengthSpinnerIds[i]);

                    Note noteToUpdate = new Note();
                    noteToUpdate = editNotes.get(i);
                    noteToUpdate.setNotevalue(Integer.parseInt(noteValuesArray[spinner
                            .getSelectedItemPosition()]));
                    noteToUpdate.setVelocity(Integer
                            .parseInt(velocityValuesArray[velocitySpinner
                                    .getSelectedItemPosition()]));
                    noteToUpdate.setLength(Float.parseFloat(lengthValuesArray[lengthSpinner
                            .getSelectedItemPosition()]));
                    noteToUpdate.setPosition(i + 1);

                    notesToUpdate.add(noteToUpdate);
                }

                notesetAndRelated.setNoteset(notesetToUpdate);
                notesetAndRelated.setNotes(notesToUpdate);

                boolean notesetExists = doesNotesetExist(notesetAndRelated);

                if (!notesetExists) {
                    // first insert new noteset (parent of all related notes)
                    saveNotesetUpdates(v, notesetToUpdate);

                    // then save individual notes
                    for (Note note : notesetAndRelated.getNotes()) {
                        saveNoteUpdates(v, note);
                    }

                    // close activity
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
                    Spinner spinner = (Spinner) findViewById(spinnerIds[i]);
                    Spinner velocitySpinner = (Spinner) findViewById(velocitySpinnerIds[i]);
                    Spinner lengthSpinner = (Spinner) findViewById(lengthSpinnerIds[i]);

                    Note note = editNotes.get(i);
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

                // generate music for playback
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
    private void saveNotesetUpdates(View v, Noteset noteset) {
        // update noteset in database
        NotesetsDataSource nds = new NotesetsDataSource(this);
        nds.updateNoteset(noteset);
        nds.close();

        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context,
                context.getResources().getString(R.string.noteset_saved),
                duration);
        toast.show();
    }

    private void saveNoteUpdates(View v, Note note) {
        // update note in database
        NotesDataSource nds = new NotesDataSource(this);
        nds.updateNote(note);
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
