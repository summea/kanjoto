
package com.andrewsummers.otashu.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.andrewsummers.otashu.R;
import com.andrewsummers.otashu.data.BookmarksDataSource;
import com.andrewsummers.otashu.model.Bookmark;
import com.andrewsummers.otashu.model.Note;

import android.app.Activity;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

/**
 * View details of a particular Bookmark.
 * <p>
 * This activity allows a user to see more information about a particular Bookmark. Bookmarks are
 * serialized Notes saved in the database and can be played back by the user in this activity.
 * </p>
 */
public class ViewBookmarkDetailActivity extends Activity implements OnClickListener {
    private long bookmarkId = 0;
    private String currentBookmarkSerializedValue = "";
    private Button buttonPlayBookmark = null;
    private File path = Environment.getExternalStorageDirectory();
    private String externalDirectory = path.toString() + "/otashu/";
    private File musicSource = new File(externalDirectory + "otashu_bookmark.mid");
    private static MediaPlayer mediaPlayer;

    /**
     * onCreate override used to get details view.
     * 
     * @param savedInstanceState Current application state data.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // get specific layout for content view
        setContentView(R.layout.activity_view_bookmark_detail);

        bookmarkId = getIntent().getExtras().getLong("list_id");

        Bookmark bookmark = new Bookmark();
        BookmarksDataSource bds = new BookmarksDataSource(this);
        bookmark = bds.getBookmark(bookmarkId);
        bds.close();

        // fill in form data
        TextView bookmarkName = (TextView) findViewById(R.id.bookmark_detail_name_value);
        bookmarkName.setText(bookmark.getName());

        currentBookmarkSerializedValue = bookmark.getSerializedValue();

        try {
            // add listeners to buttons
            buttonPlayBookmark = (Button) findViewById(R.id.button_play_bookmark);
            buttonPlayBookmark.setOnClickListener(this);
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
            case R.id.button_play_bookmark:
                List<Note> notes = new ArrayList<Note>();

                JSONArray jsonArr = new JSONArray();
                try {
                    JSONObject mainJsonObj = new JSONObject(currentBookmarkSerializedValue);
                    jsonArr = mainJsonObj.getJSONArray("notes");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                for (int i = 0; i < jsonArr.length(); i++) {
                    JSONObject jsonNote;
                    int notevalue = 0;
                    int velocity = 0;
                    float length = 1.0f;
                    int position = 1;
                    try {
                        jsonNote = jsonArr.getJSONObject(i);
                        notevalue = jsonNote.getInt("notevalue");
                        velocity = jsonNote.getInt("velocity");
                        length = Float.parseFloat(jsonNote.getString("length"));
                        position = jsonNote.getInt("position");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    Note note = new Note();
                    note.setNotevalue(notevalue);
                    note.setVelocity(velocity);
                    note.setLength(length);
                    note.setPosition(position);
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
}
