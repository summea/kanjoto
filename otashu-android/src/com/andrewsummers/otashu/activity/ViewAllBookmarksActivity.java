package com.andrewsummers.otashu.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.andrewsummers.otashu.R;
import com.andrewsummers.otashu.adapter.BookmarkAdapter;
import com.andrewsummers.otashu.data.BookmarksDataSource;
import com.andrewsummers.otashu.model.Bookmark;
import com.andrewsummers.otashu.model.Note;

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
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;

public class ViewAllBookmarksActivity extends ListActivity {

private int selectedPositionInList = 0;
private String currentBookmarkSerializedValue = "";
private File path = Environment.getExternalStorageDirectory();
private String externalDirectory = path.toString() + "/otashu/";
private File musicSource = new File(externalDirectory + "otashu_bookmark.mid");
private MediaPlayer mediaPlayer = new MediaPlayer();
private BookmarkAdapter adapter = null;
    
    /**
     * onCreate override used to gather and display a list of all bookmarks saved
     * in database.
     * 
     * @param savedInstanceState
     *            Current application state data.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fillList();
    }
    
    public void fillList() {
        List<Bookmark> allBookmarks = new LinkedList<Bookmark>();
        BookmarksDataSource bds = new BookmarksDataSource(this);
        allBookmarks = bds.getAllBookmarks();
        bds.close();

        /*
        // prevent crashes due to lack of database data
        if (allBookmarksData.isEmpty())
            allBookmarksData.add("empty");
        */

        // pass list data to adapter
        adapter = new BookmarkAdapter(this, allBookmarks);

        final ListView listView = getListView();
        listView.setTextFilterEnabled(true);
        listView.setAdapter(adapter);
        
        // get individual bookmark details
        listView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
                
                Log.d("MYLOG", "list item id: " + id);
                
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
        
        AdapterContextMenuInfo info = (AdapterContextMenuInfo) menuInfo;
        selectedPositionInList = info.position;
        
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_menu_bookmark, menu);
    }
    
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
        Intent intent = null;
        switch (item.getItemId()) {
            case R.id.context_menu_play:
                //intent = new Intent(this, ViewBookmarkDetailActivity.class);
                //intent.putExtra("list_id", info.id);
                //startActivity(intent);
                
                // play bookmark
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
                Log.d("MYLOG", "confirming delete");
                confirmDelete();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }
    
    public void confirmDelete() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.dialog_confirm_delete_message).setTitle(R.string.dialog_confirm_delete_title);
        builder.setPositiveButton(R.string.button_ok,  new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // user clicked ok
                // go ahead and delete bookmark
                
                // get correct bookmark id to delete
                Log.d("MYLOG", "selected row item: " + selectedPositionInList);
                
                Bookmark bookmarkToDelete = getBookmarkFromListPosition(selectedPositionInList);

                Log.d("MYLOG", "deleting bookmark: " + bookmarkToDelete.getId());
                deleteBookmark(bookmarkToDelete);
                
                Context context = getApplicationContext();
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context,
                        context.getResources().getString(R.string.bookmark_deleted),
                        duration);
                toast.show();
                
                // refresh list
                adapter.removeItem(selectedPositionInList);
                adapter.notifyDataSetChanged();
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
    }
    
    public Bookmark getBookmarkFromListPosition(long rowId) {
        
        long bookmarkId = rowId;
        
        List<Long> allBookmarksData = new LinkedList<Long>();
        BookmarksDataSource bds = new BookmarksDataSource(this);

        // get string version of returned bookmark list
        allBookmarksData = bds.getAllBookmarkListDBTableIds();
        bds.close();
        
        Log.d("MYLOG", allBookmarksData.toString());

        // prevent crashes due to lack of database data
        if (allBookmarksData.isEmpty())
            allBookmarksData.add((long) 0);
        
        Long[] allBookmarks = allBookmarksData
                .toArray(new Long[allBookmarksData.size()]);
        
        Log.d("MYLOG", "rowId: " + rowId);
        Log.d("MYLOG", "found bookmark data: " + allBookmarks[(int) bookmarkId]);
                
        Bookmark bookmark = bds.getBookmark(allBookmarks[(int) bookmarkId]);        
        
        bds.close();
        
        return bookmark;
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
        
        // TODO: get bookmark serialized value here
        
        int bookmarkId = (int) listId;
        
        Log.d("MYLOG", "bookmark id: " + bookmarkId);
        
        List<Long> allBookmarksData = new LinkedList<Long>();
        BookmarksDataSource bds = new BookmarksDataSource(this);

        allBookmarksData = bds.getAllBookmarkListDBTableIds();
        bds.close();

        // prevent crashes due to lack of database data
        if (allBookmarksData.isEmpty())
            allBookmarksData.add((long) 0);

        
        Long[] allBookmarks = allBookmarksData
                .toArray(new Long[allBookmarksData.size()]);
        
        Bookmark bookmark = new Bookmark();
        bookmark = bds.getBookmark(allBookmarks[bookmarkId]);
        
        bds.close();
        
        currentBookmarkSerializedValue = bookmark.getSerializedValue();
        
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
        
        // get default instrument for playback
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        String defaultInstrument = sharedPref.getString("pref_default_instrument", "");
        
        GenerateMusicActivity generateMusic = new GenerateMusicActivity();
        generateMusic.generateMusic(notes, musicSource, defaultInstrument);

        // play generated notes for user
        playMusic(musicSource);
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