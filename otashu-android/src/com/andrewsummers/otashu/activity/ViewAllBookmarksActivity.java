package com.andrewsummers.otashu.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.andrewsummers.otashu.R;
import com.andrewsummers.otashu.data.BookmarksDataSource;
import com.andrewsummers.otashu.model.Bookmark;
import com.andrewsummers.otashu.model.Note;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;

public class ViewAllBookmarksActivity extends ListActivity {

private int selectedListPosition = 0;
private String currentBookmarkSerializedValue = "";
private Button buttonPlayBookmark = null;
private File path = Environment.getExternalStorageDirectory();
private String externalDirectory = path.toString() + "/otashu/";
private File musicSource = new File(externalDirectory + "otashu_bookmark.mid");
private MediaPlayer mediaPlayer = new MediaPlayer();
    
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

        List<String> allBookmarksData = new LinkedList<String>();
        BookmarksDataSource ds = new BookmarksDataSource(this);

        // get string version of returned bookmark list
        allBookmarksData = ds.getAllBookmarkListPreviews();

        // prevent crashes due to lack of database data
        if (allBookmarksData.isEmpty())
            allBookmarksData.add("empty");

        String[] allBookmarks = allBookmarksData
                .toArray(new String[allBookmarksData.size()]);

        // pass list data to adapter
        setListAdapter(new ArrayAdapter<String>(this, R.layout.list_bookmark,
                allBookmarks));

        ListView listView = getListView();
        listView.setTextFilterEnabled(true);
        
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
        selectedListPosition = info.position;
        
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
                Log.d("MYLOG", "selected row item: " + selectedListPosition);
                
                Bookmark bookmarkToDelete = getBookmarkFromListPosition(selectedListPosition);

                Log.d("MYLOG", "deleting bookmark: " + bookmarkToDelete.getId());
                deleteBookmark(bookmarkToDelete);
                
                // refresh activity to reflect changes
                finish();
                startActivity(getIntent());

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
        BookmarksDataSource ds = new BookmarksDataSource(this);

        // get string version of returned bookmark list
        allBookmarksData = ds.getAllBookmarkListDBTableIds();
        
        Log.d("MYLOG", allBookmarksData.toString());

        // prevent crashes due to lack of database data
        if (allBookmarksData.isEmpty())
            allBookmarksData.add((long) 0);
        
        Long[] allBookmarks = allBookmarksData
                .toArray(new Long[allBookmarksData.size()]);
        
        Log.d("MYLOG", "rowId: " + rowId);
        Log.d("MYLOG", "found bookmark data: " + allBookmarks[(int) bookmarkId]);
                
        Bookmark bookmark = ds.getBookmark(allBookmarks[(int) bookmarkId]);        
        
        ds.close();
        
        return bookmark;
    }
    
    public void deleteBookmark(Bookmark bookmark) {
        BookmarksDataSource ds = new BookmarksDataSource(this);
        ds.deleteBookmark(bookmark);
        ds.close();
    }
    
    @Override
    public void onResume() {
        super.onResume();

        List<String> allBookmarksData = new LinkedList<String>();
        BookmarksDataSource ds = new BookmarksDataSource(this);

        // get string version of returned bookmark list
        allBookmarksData = ds.getAllBookmarkListPreviews();

        // prevent crashes due to lack of database data
        if (allBookmarksData.isEmpty())
            allBookmarksData.add("empty");

        String[] allBookmarks = allBookmarksData
                .toArray(new String[allBookmarksData.size()]);

        // pass list data to adapter
        setListAdapter(new ArrayAdapter<String>(this, R.layout.list_bookmark,
                allBookmarks));

        ListView listView = getListView();
        listView.setTextFilterEnabled(true);
        
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
    
    void play_bookmark(long listId) {
        
        // TODO: get bookmark serialized value here
        
        int bookmarkId = (int) listId;
        
        Log.d("MYLOG", "bookmark id: " + bookmarkId);
        
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
        
        GenerateMusicActivity generateMusic = new GenerateMusicActivity();
        generateMusic.generateMusic(notes, musicSource);

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