package com.andrewsummers.otashu.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.andrewsummers.otashu.R;
import com.andrewsummers.otashu.data.EmotionsDataSource;
import com.andrewsummers.otashu.data.NotesetsDataSource;
import com.andrewsummers.otashu.model.Emotion;
import com.andrewsummers.otashu.model.Note;
import com.andrewsummers.otashu.model.Noteset;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

/**
 * View details of a particular noteset.
 */
public class ViewNotesetDetailActivity extends Activity implements OnClickListener {
    
    private int key = 0;
    private int notesetId = 0;
    private long notesetIdInTable = 0;
    private SparseArray<List<Note>> notesetBundle = new SparseArray<List<Note>>();
    private Button buttonPlayNoteset = null;
    private File path = Environment.getExternalStorageDirectory();
    private String externalDirectory = path.toString() + "/otashu/";
    private File musicSource = new File(externalDirectory + "otashu_preview.mid");
    private MediaPlayer mediaPlayer = new MediaPlayer();
    
    /**
     * onCreate override used to get details view.
     * 
     * @param savedInstanceState
     *            Current application state data.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // get specific layout for content view
        setContentView(R.layout.activity_view_noteset_detail);
        
        Log.d("MYLOG", "got list item id: " + getIntent().getExtras().getLong("list_id"));
        notesetId = (int) getIntent().getExtras().getLong("list_id");
                
        NotesetsDataSource nds = new NotesetsDataSource(this);
        
        // get noteset and notes information        
        notesetBundle = nds.getNotesetBundle(notesetId);

        Noteset noteset = nds.getNoteset(notesetId);        
        nds.close();
        
        EmotionsDataSource eds = new EmotionsDataSource(this);
        Emotion emotion = eds.getEmotion(noteset.getEmotion());
        eds.close();
        
        String[] noteLabelsArray = getResources().getStringArray(R.array.note_labels_array);
        String[] noteValuesArray = getResources().getStringArray(R.array.note_values_array);
        
        // used for playback
        key = notesetId;
        
        TextView emotionName = (TextView) findViewById(R.id.noteset_detail_emotion_value);
        emotionName.setText(emotion.getName());

        int[] textViewIds = {
                R.id.noteset_detail_note1,
                R.id.noteset_detail_note2,
                R.id.noteset_detail_note3,
                R.id.noteset_detail_note4
        };
        
        TextView note = null;
        
        for (int i = 0; i < textViewIds.length; i++) {
            note = (TextView) findViewById(textViewIds[i]);
            for (int j = 0; j < noteValuesArray.length; j++) {
                // get actual note name (C3, D3, E3, etc.)
                if (notesetBundle.get(notesetId).get(i).getNotevalue() == Integer.valueOf(noteValuesArray[j])) {
                    note.setText(noteLabelsArray[j]);
                    break;
                }
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
     * onClick override that acts as a router to start desired activities.
     * 
     * @param view
     *            Incoming view.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.button_play_noteset:
            
            // disable play button while playing
            buttonPlayNoteset = (Button) findViewById(R.id.button_play_noteset);
            buttonPlayNoteset.setClickable(false);
            
            List<Note> notes = new ArrayList<Note>();
            
            for (int i = 0; i < notesetBundle.get(key).size(); i++) {
                Note note = new Note();
                note = notesetBundle.get(key).get(i);
                notes.add(note);
                Log.d("MYLOG", "note value: " + note.getLength());
            }
        
            // get default instrument for playback
            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
            String defaultInstrument = sharedPref.getString("pref_default_instrument", "");
            
            GenerateMusicActivity generateMusic = new GenerateMusicActivity();
            generateMusic.generateMusic(notes, musicSource, defaultInstrument);

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
        if (mediaPlayer.isPlaying()) {
            // stop playing music
            mediaPlayer.stop();
        }

        super.onBackPressed();
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_noteset_details, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = null;
        //AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
        
        // handle menu item selection
        switch (item.getItemId()) {
        case R.id.context_menu_edit:
            intent = new Intent(this, EditNotesetActivity.class);
            intent.putExtra("menu_noteset_id", notesetIdInTable);
            startActivity(intent);
            finish();
            return true;
        case R.id.context_menu_delete:
            Log.d("MYLOG", "confirming delete");
            confirmDelete();
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }
       
    public void confirmDelete() {
        final NotesetsDataSource nds = new NotesetsDataSource(this);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.dialog_confirm_delete_message).setTitle(R.string.dialog_confirm_delete_title);
        builder.setPositiveButton(R.string.button_ok,  new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // user clicked ok
                // go ahead and delete noteset
                Noteset notesetToDelete = nds.getNoteset(notesetIdInTable);

                Log.d("MYLOG", "deleting noteset: " + notesetToDelete.getId());
                deleteNoteset(notesetToDelete);

                finish();
            }
        });
        builder.setNegativeButton(R.string.button_cancel,  new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // user clicked cancel
                // just go back to list for now
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
        nds.close();
    }

    public void deleteNoteset(Noteset noteset) {
        NotesetsDataSource nds = new NotesetsDataSource(this);
        nds.deleteNoteset(noteset);
        nds.close();
    }
}