package com.andrewsummers.otashu;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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
        
        spinner = (Spinner) findViewById(R.id.spinner_note1);
        adapter = ArrayAdapter
                .createFromResource(this, R.array.note_values_array,
                        android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner = (Spinner) findViewById(R.id.spinner_note2);
        adapter = ArrayAdapter
                .createFromResource(this, R.array.note_values_array,
                        android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner = (Spinner) findViewById(R.id.spinner_note3);
        adapter = ArrayAdapter
                .createFromResource(this, R.array.note_values_array,
                        android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner = (Spinner) findViewById(R.id.spinner_note4);
        adapter = ArrayAdapter
                .createFromResource(this, R.array.note_values_array,
                        android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    /**
     * onClick override used to save noteset data once user clicks save button.
     * 
     * @param view
     *            Incoming view.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.button_save:
            // gather noteset data from form
            String notesetName;
            Spinner spinner;
            
            Noteset notesetToInsert = new Noteset();
            Note noteToInsert = new Note();
            
            notesetName = ((EditText) findViewById(R.id.edittext_noteset_name)).getText().toString();
            
            // get select emotion's id
            
            emotionsDataSource = new EmotionsDataSource(this);
            emotionsDataSource.open();

            List<Integer> allEmotionIds = new ArrayList<Integer>();
            allEmotionIds = emotionsDataSource.getAllEmotionIds();
            
            Spinner emotionSpinner = (Spinner) findViewById(R.id.spinner_emotion);
            int selectedEmotionValue = allEmotionIds.get(emotionSpinner.getSelectedItemPosition());
            
            emotionsDataSource.close();
            
            Log.d("MYLOG", "selected emotion value: " + selectedEmotionValue);
            
            notesetToInsert.setName(notesetName.toString());
            notesetToInsert.setEmotion(selectedEmotionValue);
            Log.d("MYLOG", "new noteset: " + notesetName + " " + selectedEmotionValue);
            
            // first insert new noteset (parent of all related notes)
            saveNoteset(v, notesetToInsert);
            
            int[] spinnerIds = {
                    R.id.spinner_note1,
                    R.id.spinner_note2,
                    R.id.spinner_note3,
                    R.id.spinner_note4
            };
            
            for (int i = 0; i < spinnerIds.length; i++) {
                spinner = (Spinner) findViewById(spinnerIds[i]);
                noteToInsert.setNotesetId(newlyInsertedNoteset.getId());
                Log.d("MYLOG", spinner.getSelectedItem().toString());
                noteToInsert.setNotevalue(Integer.parseInt(spinner.getSelectedItem().toString()));
                saveNote(v, noteToInsert);
            }
            
            finish();
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
        
        //Note noteToSave = new Note();
        //noteToSave = note;
        
        // save noteset in database
        notesDataSource.createNote(note);

        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context,
                context.getResources().getString(R.string.noteset_saved),
                duration);
        toast.show();
    }
}