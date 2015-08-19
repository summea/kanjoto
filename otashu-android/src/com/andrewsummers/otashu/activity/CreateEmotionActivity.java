
package com.andrewsummers.otashu.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.andrewsummers.otashu.R;
import com.andrewsummers.otashu.data.EmotionsDataSource;
import com.andrewsummers.otashu.data.LabelsDataSource;
import com.andrewsummers.otashu.model.Emotion;
import com.andrewsummers.otashu.model.Note;
import com.andrewsummers.otashu.model.Noteset;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

/**
 * CreateEmotionActivity is an Activity which provides users the ability to create new emotions.
 * <p>
 * This activity provides a form for creating a new Emotion. An Emotion is basically a label or tag
 * for an emotion that is saved into the database for later use. Emotions are typically used in
 * connection with Noteset objects (e.g. noteset-emotion combinations).
 * </p>
 */
public class CreateEmotionActivity extends Activity implements OnClickListener {
    private Button buttonSave;
    private Emotion newlyInsertedEmotion = new Emotion();
    private Noteset newlyInsertedNoteset = new Noteset();
    private SharedPreferences sharedPref;
    private long apprenticeId = 0;

    /**
     * onCreate override that provides emotion creation view to user .
     * 
     * @param savedInstanceState Current application state data.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // get specific layout for content view
        setContentView(R.layout.activity_create_emotion);

        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        apprenticeId = Long.parseLong(sharedPref.getString(
                "pref_selected_apprentice", "1"));

        // add listeners to buttons
        buttonSave = (Button) findViewById(R.id.button_save);
        buttonSave.setOnClickListener(this);

        LabelsDataSource lds = new LabelsDataSource(this);

        List<String> allLabels = new ArrayList<String>();
        allLabels = lds.getAllLabelListPreviews();
        lds.close();

        // locate next spinner in layout
        Spinner spinner = (Spinner) findViewById(R.id.spinner_emotion_label);

        // create array adapter for list of emotions
        ArrayAdapter<String> labelsAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item);
        labelsAdapter.addAll(allLabels);

        // specify the default layout when list of choices appears
        labelsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // apply this adapter to the spinner
        spinner.setAdapter(labelsAdapter);
    }

    /**
     * onClick override used to save emotion data once user clicks save button.
     * 
     * @param view Incoming view.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_save:
                LabelsDataSource lds = new LabelsDataSource(this);
                List<Long> allLabelIds = lds.getAllLabelListDBTableIds();
                lds.close();

                // gather emotion data from form
                Emotion emotionToInsert = new Emotion();

                String emotionName = ((EditText) findViewById(R.id.edittext_emotion_name))
                        .getText()
                        .toString();
                Spinner emotionLabel = (Spinner) findViewById(R.id.spinner_emotion_label);

                emotionToInsert.setName(emotionName.toString());
                emotionToInsert.setLabelId(allLabelIds.get(emotionLabel.getSelectedItemPosition()));
                emotionToInsert.setApprenticeId(apprenticeId);

                // save emotion to database
                saveEmotion(v, emotionToInsert);
    
                /*
                // TODO: look at this later 
                // save randomly-created notesets too for this emotion
                // this is in order to help speed up the apprentice's process
                // of finding new notes during apprentice tests
                for (int j = 0; j < 4; j++) {
                    List<Note> notesToInsert = generateNotes(39, 50);
                    Noteset notesetToInsert = new Noteset();
                    
                    // prepare noteset
                    notesetToInsert.setEmotion((int) getNewlyInsertedEmotion().getId());
                    notesetToInsert.setEnabled(1);
                    notesetToInsert.setApprenticeId(apprenticeId);
    
                    // check if noteset already exists, first
                    NotesetAndRelated notesetAndRelated = new NotesetAndRelated();
                    notesetAndRelated.setNoteset(notesetToInsert);
                    notesetAndRelated.setNotes(notesToInsert);
                    boolean notesetExists = doesNotesetExist(notesetAndRelated);
    
                    if (!notesetExists) {
                        // save noteset
                        saveNoteset(v, notesetToInsert);
    
                        // save notes
                        for (int i = 0; i < notesToInsert.size(); i++) {
                            Note note = notesToInsert.get(i);
                            note.setNotesetId(newlyInsertedNoteset.getId());
    
                            // TODO: these could be generated by Apprentice in the future
                            note.setVelocity(100);
                            note.setLength(0.5f);
    
                            note.setPosition(i + 1);
    
                            saveNote(v, notesToInsert.get(i));
                        }
                    }
                }
                */

                // close activity
                finish();
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

    /**
     * Save emotion data.
     * 
     * @param v Incoming view.
     * @param data Incoming string of data to be saved.
     */
    private void saveEmotion(View v, Emotion emotion) {
        // save emotion in database
        EmotionsDataSource eds = new EmotionsDataSource(this);
        setNewlyInsertedEmotion(eds.createEmotion(emotion));
        eds.close();

        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context,
                context.getResources().getString(R.string.emotion_saved),
                duration);
        toast.show();
    }

    public Emotion getNewlyInsertedEmotion() {
        return newlyInsertedEmotion;
    }

    public void setNewlyInsertedEmotion(Emotion newlyInsertedEmotion) {
        this.newlyInsertedEmotion = newlyInsertedEmotion;
    }
    
    public Noteset getNewlyInsertedNoteset() {
        return newlyInsertedNoteset;
    }

    public void setNewlyInsertedNoteset(Noteset newlyInsertedNoteset) {
        this.newlyInsertedNoteset = newlyInsertedNoteset;
    }
    
    public List<Note> generateNotes(int fromIndex, int toIndex) {
        String[] noteValuesArray = getResources().getStringArray(R.array.note_values_array);
        List<Note> notes = new ArrayList<Note>();

        int randomNoteIndex = 0;
        String randomNote = "";
        float randomLength = 0.0f;
        int randomVelocity = 100;
        float lengthValues[] = {
                0.25f, 0.5f, 0.75f, 1.0f
        };

        for (int i = 0; i < 4; i++) {
            randomNoteIndex = new Random().nextInt((toIndex - fromIndex) + 1) + fromIndex;
            randomNote = noteValuesArray[randomNoteIndex];
            int randomLengthIndex = new Random().nextInt(lengthValues.length);
            randomLength = lengthValues[randomLengthIndex];
            randomVelocity = new Random().nextInt(120 - 60 + 1) + 60;

            Note note = new Note();
            note.setNotevalue(Integer.valueOf((randomNote)));
            note.setLength(randomLength);
            note.setVelocity(randomVelocity);
            note.setPosition(i + 1);

            notes.add(note);
        }

        return notes;
    }
}
