package com.andrewsummers.otashu.activity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.andrewsummers.otashu.R;
import com.andrewsummers.otashu.data.BookmarksDataSource;
import com.andrewsummers.otashu.data.EmotionsDataSource;
import com.andrewsummers.otashu.model.Bookmark;
import com.andrewsummers.otashu.model.Emotion;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

public class ChooseEmotionActivity extends Activity implements OnClickListener {

    private Button buttonGo = null;
    private Button buttonBookmark = null;
    private String lastSerializedNotes = "";
    
    /**
     * onCreate override that provides emotion-choose view to user.
     * 
     * @param savedInstanceState
     *            Current application state data.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // get specific layout for content view
        setContentView(R.layout.activity_choose_emotion);

        // add listeners to buttons
        buttonBookmark = (Button) findViewById(R.id.button_bookmark);
        buttonBookmark.setOnClickListener(this);
        
        buttonGo = (Button) findViewById(R.id.button_go);
        buttonGo.setOnClickListener(this);
        
        EmotionsDataSource eds = new EmotionsDataSource(this);
        eds.open();

        List<Emotion> allEmotions = new ArrayList<Emotion>();
        allEmotions = eds.getAllEmotions();
        
        eds.close();
        
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
        
        // instrument spinner
        ArrayAdapter<CharSequence> adapter = null;
        spinner = (Spinner) findViewById(R.id.spinner_instrument);
        adapter = ArrayAdapter
                .createFromResource(this, R.array.instrument_labels_array,
                        android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        
        // get default instrument for playback
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        String defaultInstrument = sharedPref.getString("pref_default_instrument", "");
        
        String[] instrumentLabels = getResources().getStringArray(R.array.instrument_labels_array);
        String[] instrumentValues = getResources().getStringArray(R.array.instrument_values_array);
        
        int position = 0;
        if (Arrays.asList(instrumentValues).indexOf(defaultInstrument) > 0)
            position = Arrays.asList(instrumentValues).indexOf(defaultInstrument);
        
        spinner.setSelection(adapter.getPosition(instrumentLabels[position]));
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;

        switch (v.getId()) {
        case R.id.button_go:
            EmotionsDataSource eds = new EmotionsDataSource(this);
            eds.open();

            List<Integer> allEmotionIds = new ArrayList<Integer>();
            allEmotionIds = eds.getAllEmotionIds();
            
            Spinner emotionSpinner = (Spinner) findViewById(R.id.spinner_emotion);
            int selectedEmotionValue = allEmotionIds.get(emotionSpinner.getSelectedItemPosition());
            eds.close();

            Spinner instrumentSpinner = (Spinner) findViewById(R.id.spinner_instrument);
            String[] allInstrumentIds = getResources().getStringArray(R.array.instrument_values_array);
            int selectedInstrumentId = Integer.valueOf(allInstrumentIds[instrumentSpinner.getSelectedItemPosition()]);
            
            Bundle bundle = new Bundle();
            bundle.putInt("emotion_id", selectedEmotionValue);
            bundle.putInt("instrument_id", selectedInstrumentId);

            intent = new Intent(this, GenerateMusicActivity.class);
            intent.putExtras(bundle);            
            startActivityForResult(intent, 1);
            break;
        case R.id.button_bookmark:
            // save last generated noteset as a bookmark
            save_bookmark();
            break;
        }
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                Log.d("MYLOG", "serialized notes: " + data.getStringExtra("serialized_notes"));
                lastSerializedNotes = data.getStringExtra("serialized_notes");
            }
        }
    }
    
    public int save_bookmark() {
        Bookmark bookmark = new Bookmark();
        bookmark.setName("Untitled");
        bookmark.setSerializedValue(lastSerializedNotes);
        saveBookmark(bookmark);
        return 0;
    }
    
    private void saveBookmark(Bookmark bookmark) {

        // save bookmark in database
        BookmarksDataSource bds = new BookmarksDataSource(this);
        bds.createBookmark(bookmark);
        bds.close();
        
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context,
                context.getResources().getString(R.string.bookmark_saved),
                duration);
        toast.show();
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_choose_emotions, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = null;
        
        // handle menu item selection
        switch (item.getItemId()) {
        case R.id.view_settings:
            intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }

}