
package com.andrewsummers.kanjoto.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.andrewsummers.kanjoto.R;
import com.andrewsummers.kanjoto.adapter.BookmarkAdapter;
import com.andrewsummers.kanjoto.data.BookmarksDataSource;
import com.andrewsummers.kanjoto.model.Bookmark;
import com.andrewsummers.kanjoto.model.Note;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;

/**
 * View all Bookmarks as a list.
 * <p>
 * This activity allows a user to view a list of all saved Bookmarks.
 * </p>
 */
public class ViewAllBookmarksActivity extends ListActivity {
    private ListView listView = null;
    private String currentBookmarkSerializedValue = "";
    private File path = Environment.getExternalStorageDirectory();
    private String externalDirectory = path.toString() + "/kanjoto/";
    private File musicSource = new File(externalDirectory + "kanjoto_bookmark.mid");
    private static MediaPlayer mediaPlayer;
    private BookmarkAdapter adapter = null;

    /**
     * onCreate override used to gather and display a list of all bookmarks saved in database.
     * 
     * @param savedInstanceState Current application state data.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // initialize ListView
        listView = getListView();

        // set title for list activity
        ViewGroup listHeader = (ViewGroup) getLayoutInflater().inflate(R.layout.list_header,
                listView, false);
        TextView headerText = (TextView) listHeader.findViewById(R.id.list_header_title);
        headerText.setText(R.string.view_all_bookmarks_list_header);
        listView.addHeaderView(listHeader, "", false);

        fillList();
    }

    public void fillList() {
        List<Bookmark> allBookmarks = new LinkedList<Bookmark>();
        BookmarksDataSource bds = new BookmarksDataSource(this);
        allBookmarks = bds.getAllBookmarks();
        bds.close();

        // pass list data to adapter
        adapter = new BookmarkAdapter(this, allBookmarks);

        listView.setAdapter(adapter);

        // get individual bookmark details
        listView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {

                // launch details activity
                Intent intent = new Intent(view.getContext(),
                        ViewBookmarkDetailActivity.class);

                intent.putExtra("list_id", id);
                startActivity(intent);
            }
        });

        // register context menu
        registerForContextMenu(listView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_bookmarks, menu);
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

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_menu_bookmark, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
        Intent intent = null;
        switch (item.getItemId()) {
            case R.id.context_menu_play:
                play_bookmark(info.id);
                return true;
            case R.id.context_menu_view:
                intent = new Intent(this, ViewBookmarkDetailActivity.class);
                intent.putExtra("list_id", info.id);
                startActivity(intent);
                return true;
            case R.id.context_menu_edit:
                intent = new Intent(this, EditBookmarkActivity.class);
                intent.putExtra("list_id", info.id);
                startActivity(intent);
                return true;
            case R.id.context_menu_delete:
                confirmDelete(info);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    public void confirmDelete(final AdapterContextMenuInfo info) {
        final BookmarksDataSource bds = new BookmarksDataSource(this);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.dialog_confirm_delete_message).setTitle(
                R.string.dialog_confirm_delete_title);
        builder.setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // user clicked ok, so go ahead and delete bookmark
                // get correct bookmark id to delete
                Bookmark bookmarkToDelete = bds.getBookmark(info.id);
                deleteBookmark(bookmarkToDelete);

                Context context = getApplicationContext();
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context,
                        context.getResources().getString(R.string.bookmark_deleted),
                        duration);
                toast.show();

                // refresh list
                adapter.removeItem(info.position - 1);
                adapter.notifyDataSetChanged();
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
        bds.close();
    }

    public void deleteBookmark(Bookmark bookmark) {
        BookmarksDataSource bds = new BookmarksDataSource(this);
        bds.deleteBookmark(bookmark);
        bds.close();
    }

    @Override
    public void onResume() {
        super.onResume();

        // refresh list
        adapter.clear();
        fillList();
    }

    void play_bookmark(long listId) {
        int bookmarkId = (int) listId;

        Bookmark bookmark = new Bookmark();
        BookmarksDataSource bds = new BookmarksDataSource(this);
        bookmark = bds.getBookmark(bookmarkId);
        bds.close();

        // get bookmark's serialized value
        currentBookmarkSerializedValue = bookmark.getSerializedValue();

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
        int playbackSpeed = Integer.valueOf(sharedPref.getString("pref_default_playback_speed",
                "120"));

        // generate music for playback
        GenerateMusicActivity generateMusic = new GenerateMusicActivity();
        generateMusic.generateMusic(notes, musicSource, defaultInstrument, playbackSpeed);

        // play generated notes for user
        playMusic(musicSource);
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
