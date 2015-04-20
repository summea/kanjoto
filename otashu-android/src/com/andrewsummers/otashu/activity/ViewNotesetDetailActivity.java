
package com.andrewsummers.otashu.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.andrewsummers.otashu.R;
import com.andrewsummers.otashu.data.EmotionsDataSource;
import com.andrewsummers.otashu.data.LabelsDataSource;
import com.andrewsummers.otashu.data.NotesDataSource;
import com.andrewsummers.otashu.data.NotesetsDataSource;
import com.andrewsummers.otashu.data.NotevaluesDataSource;
import com.andrewsummers.otashu.model.Emotion;
import com.andrewsummers.otashu.model.Label;
import com.andrewsummers.otashu.model.Note;
import com.andrewsummers.otashu.model.Noteset;
import com.andrewsummers.otashu.model.NotesetAndRelated;
import com.andrewsummers.otashu.model.Notevalue;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
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
 * View details of a particular Noteset.
 * <p>
 * This activity allows a user to see more information about a particular Noteset. Notesets are
 * groups of musical Notes ordered in a specific sequence.
 * </p>
 */
public class ViewNotesetDetailActivity extends Activity implements OnClickListener {
    private int key = 0;
    private int notesetId = 0;
    private SparseArray<List<Note>> notesetBundle = new SparseArray<List<Note>>();
    private Button buttonPlayNoteset = null;
    private File path = Environment.getExternalStorageDirectory();
    private String externalDirectory = path.toString() + "/otashu/";
    private File musicSource = new File(externalDirectory + "otashu_preview.mid");
    private static MediaPlayer mediaPlayer;
    private SharedPreferences sharedPref;
    private long apprenticeId = 0;

    /**
     * onCreate override used to get details view.
     * 
     * @param savedInstanceState Current application state data.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // get specific layout for content view
        setContentView(R.layout.activity_view_noteset_detail);
        
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        apprenticeId = Long.parseLong(sharedPref.getString(
                "pref_selected_apprentice", "1"));

        notesetId = (int) getIntent().getExtras().getLong("list_id");

        NotesetsDataSource nsds = new NotesetsDataSource(this);

        // get noteset and notes information
        notesetBundle = nsds.getNotesetBundle(apprenticeId, notesetId);
        Noteset noteset = nsds.getNoteset(notesetId);
        nsds.close();

        NotesDataSource nds = new NotesDataSource(this);
        List<Note> relatedNotes = nds.getAllNotes(noteset.getId());
        nds.close();

        EmotionsDataSource eds = new EmotionsDataSource(this);
        Emotion relatedEmotion = eds.getEmotion(apprenticeId, noteset.getEmotion());
        eds.close();

        LabelsDataSource lds = new LabelsDataSource(this);
        Label relatedLabel = lds.getLabel(relatedEmotion.getLabelId());
        List<Label> allLabels = lds.getAllLabels();
        lds.close();

        NotevaluesDataSource nvds = new NotevaluesDataSource(this);
        List<Notevalue> relatedNotevalues = new ArrayList<Notevalue>();
        for (Note note : relatedNotes) {
            relatedNotevalues.add(nvds.getNotevalueByNoteValue(note.getNotevalue()));
        }
        nvds.close();

        NotesetAndRelated notesetAndRelated = new NotesetAndRelated();
        notesetAndRelated.setEmotion(relatedEmotion);
        notesetAndRelated.setLabel(relatedLabel);
        notesetAndRelated.setNoteset(noteset);
        notesetAndRelated.setNotes(relatedNotes);
        notesetAndRelated.setNotevalues(relatedNotevalues);

        // used for playback
        key = notesetId;

        TextView emotionName = (TextView) findViewById(R.id.noteset_detail_emotion_value);
        emotionName.setText(notesetAndRelated.getEmotion().getName());
        String backgroundColor = "#ffffff";
        for (Label label : allLabels) {
            if (label.getId() == notesetAndRelated.getEmotion().getLabelId()) {
                backgroundColor = label.getColor();
            }
        }
        emotionName.setBackgroundColor(Color.parseColor(backgroundColor));

        TextView enabled = (TextView) findViewById(R.id.noteset_detail_enabled_value);
        if (notesetAndRelated.getNoteset().getEnabled() == 1) {
            enabled.setText("yes");
            enabled.setBackgroundColor(this.getResources().getColor(R.color.button_yes));
        } else {
            enabled.setText("no");
            enabled.setBackgroundColor(this.getResources().getColor(R.color.button_no));
        }

        int[] textViewIds = {
                R.id.noteset_detail_note1,
                R.id.noteset_detail_note2,
                R.id.noteset_detail_note3,
                R.id.noteset_detail_note4
        };

        TextView note = null;

        for (int i = 0; i < textViewIds.length; i++) {
            note = (TextView) findViewById(textViewIds[i]);
            note.setText(notesetAndRelated.getNotevalues().get(i).getNotelabel());
            backgroundColor = "#dddddd";
            if (notesetAndRelated.getNotevalues().get(i).getLabelId() > 0) {
                // TODO: getting note-related labels could be done in a different way...
                for (Label label : allLabels) {
                    if (label.getId() == notesetAndRelated.getNotevalues().get(i).getLabelId()) {
                        backgroundColor = label.getColor();
                    }
                }
            }
            note.setBackgroundColor(Color.parseColor(backgroundColor));
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
     * @param view Incoming view.
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_noteset_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = null;

        // handle menu item selection
        switch (item.getItemId()) {
            case R.id.context_menu_edit:
                intent = new Intent(this, EditNotesetActivity.class);
                intent.putExtra("menu_noteset_id", notesetId);
                startActivity(intent);
                finish();
                return true;
            case R.id.context_menu_delete:
                confirmDelete();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void confirmDelete() {
        final NotesetsDataSource nds = new NotesetsDataSource(this);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.dialog_confirm_delete_message).setTitle(
                R.string.dialog_confirm_delete_title);
        builder.setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // user clicked ok
                // go ahead and delete noteset
                Noteset notesetToDelete = nds.getNoteset(notesetId);

                deleteNoteset(notesetToDelete);

                // close activity
                finish();
            }
        });
        builder.setNegativeButton(R.string.button_cancel, new DialogInterface.OnClickListener() {
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
