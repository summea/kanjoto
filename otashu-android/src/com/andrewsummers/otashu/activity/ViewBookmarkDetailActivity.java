package com.andrewsummers.otashu.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.andrewsummers.otashu.R;
import com.andrewsummers.otashu.data.BookmarksDataSource;
import com.andrewsummers.otashu.model.Bookmark;
import com.andrewsummers.otashu.model.Note;

import android.app.Activity;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

/**
 * View details of a particular bookmark.
 */
public class ViewBookmarkDetailActivity extends Activity implements OnClickListener {
    
    private int bookmarkId = 0;
    private String currentBookmarkSerializedValue = "";
    private Button buttonPlayBookmark = null;
    private File path = Environment.getExternalStorageDirectory();
    private String externalDirectory = path.toString() + "/otashu/";
    private File musicSource = new File(externalDirectory + "otashu_bookmark.mid");
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
        setContentView(R.layout.activity_view_bookmark_detail);
        
        Log.d("MYLOG", "got list item id: " + getIntent().getExtras().getLong("list_id"));
        bookmarkId = (int) getIntent().getExtras().getLong("list_id");
        
        List<Long> allBookmarksData = new LinkedList<Long>();
        BookmarksDataSource ds = new BookmarksDataSource(this);

        allBookmarksData = ds.getAllBookmarkListDBTableIds();

        // prevent crashes due to lack of database data
        if (allBookmarksData.isEmpty())
            allBookmarksData.add((long) 0);

        
        Long[] allBookmarks = allBookmarksData
                .toArray(new Long[allBookmarksData.size()]);
        
        Bookmark bookmark = new Bookmark();
        bookmark = ds.getBookmark(allBookmarks[bookmarkId]);
        
        ds.close();

        TextView bookmarkName = (TextView) findViewById(R.id.bookmark_detail_name_value);
        bookmarkName.setText(bookmark.getName());
        
        TextView bookmarkSerializedValue = (TextView) findViewById(R.id.bookmark_detail_serialized_value_value);
        bookmarkSerializedValue.setText(bookmark.getSerializedValue());
        
        currentBookmarkSerializedValue = bookmark.getSerializedValue();
        
        try {
            // add listeners to buttons
            // have to cast to Button in this case    
            buttonPlayBookmark = (Button) findViewById(R.id.button_play_bookmark);
            buttonPlayBookmark.setOnClickListener(this);
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
        case R.id.button_play_bookmark:
            
            Log.d("MYLOG", "note value: " + currentBookmarkSerializedValue);
            
            //List<String> notesFromString = Arrays.asList(currentBookmarkSerializedValue.split("\\|"));
            String[] notesFromString = currentBookmarkSerializedValue.split("\\|");
            List<Note> notes = new ArrayList<Note>();
                        
            
            for (String nextNote : notesFromString) {
                
                Log.d("MYLOG", "note value: " + nextNote);
                                
                String[] itemsFromNotes = nextNote.split(":");
                
                Note note = new Note();
                note.setNotevalue(Integer.parseInt(itemsFromNotes[0]));
                note.setVelocity(Integer.parseInt(itemsFromNotes[1]));
                note.setLength(Float.parseFloat(itemsFromNotes[2]));
                note.setPosition(Integer.parseInt(itemsFromNotes[3]));
                notes.add(note);
            }
            
            GenerateMusicActivity generateMusic = new GenerateMusicActivity();
            generateMusic.generateMusic(notes, musicSource);

            // play generated notes for user
            playMusic(musicSource);
            
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
        Log.d("MYLOG", "stop playing music!");
        
        // stop playing music
        mediaPlayer.stop();
        
        super.onBackPressed();
    }

}