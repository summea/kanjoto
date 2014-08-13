package com.andrewsummers.otashu;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
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
    private NotesetsDataSource datasource;

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
        datasource = new NotesetsDataSource(this);
        datasource.open();

        Spinner spinner = null;
        ArrayAdapter<CharSequence> adapter = null;

        // locate next spinner in layout
        spinner = (Spinner) findViewById(R.id.spinner_emotion);
        // create an ArrayAdapter using the string array in the related XML file
        // and use the default spinner layout
        adapter = ArrayAdapter.createFromResource(this,
                R.array.emotion_values_array,
                android.R.layout.simple_spinner_item);
        // specify the default layout when list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // apply this adapter to the spinner
        spinner.setAdapter(adapter);

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
            String noteset = null;
            Spinner spinner = null;
            String spinnerText = null;

            spinner = (Spinner) findViewById(R.id.spinner_note1);
            spinnerText = spinner.getSelectedItem().toString();
            noteset = spinnerText;

            spinner = (Spinner) findViewById(R.id.spinner_note2);
            spinnerText = spinner.getSelectedItem().toString();
            noteset += spinnerText;

            spinner = (Spinner) findViewById(R.id.spinner_note3);
            spinnerText = spinner.getSelectedItem().toString();
            noteset += spinnerText;

            spinner = (Spinner) findViewById(R.id.spinner_note4);
            spinnerText = spinner.getSelectedItem().toString();
            noteset += spinnerText;

            // save data
            saveNotesets(v, noteset);

            finish();
            break;
        }
    }

    /**
     * onResume override used to open up data source when resuming activity.
     */
    @Override
    protected void onResume() {
        datasource.open();
        super.onResume();
    }

    /**
     * onPause override used to close data source when activity paused.
     */
    @Override
    protected void onPause() {
        datasource.close();
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
    private void saveNotesets(View v, String data) {
        String notesetData = data;

        // save noteset in database
        datasource.createNoteset(notesetData);

        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context,
                context.getResources().getString(R.string.noteset_saved),
                duration);
        toast.show();
    }
}