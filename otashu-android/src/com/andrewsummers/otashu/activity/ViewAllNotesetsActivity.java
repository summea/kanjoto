package com.andrewsummers.otashu.activity;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import com.andrewsummers.otashu.R;
import com.andrewsummers.otashu.data.NotesetsDataSource;
import com.andrewsummers.otashu.model.Note;
import com.andrewsummers.otashu.model.Noteset;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

/**
 * View all notesets as a list.
 */
public class ViewAllNotesetsActivity extends ListActivity {
    
    private int selectedListPosition = 0;
    
    /**
     * onCreate override used to gather and display a list of all notesets saved
     * in database.
     * 
     * @param savedInstanceState
     *            Current application state data.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        List<String> allNotesetsData = new LinkedList<String>();
        NotesetsDataSource ds = new NotesetsDataSource(this);

        String[] noteLabelsArray = getResources().getStringArray(R.array.note_labels_array);
        String[] noteValuesArray = getResources().getStringArray(R.array.note_values_array);
        
        // get string version of returned noteset list
        allNotesetsData = ds.getAllNotesetListPreviews(noteLabelsArray, noteValuesArray);
        
        Log.d("MYLOG", allNotesetsData.toString() + "current");
        
        // prevent crashes due to lack of database data
        if (allNotesetsData.isEmpty())
            allNotesetsData.add("empty");

        String[] allNotesets = allNotesetsData
                .toArray(new String[allNotesetsData.size()]);

        // pass list data to adapter
        setListAdapter(new ArrayAdapter<String>(this, R.layout.list_noteset,
                allNotesets));

        ListView listView = getListView();
        listView.setTextFilterEnabled(true);

        // get individual noteset details
        listView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
                
                Log.d("MYLOG", "list item id: " + id);
                
                // launch details activity
                Intent intent = new Intent(view.getContext(),
                        ViewNotesetDetailActivity.class);
                
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
        inflater.inflate(R.menu.menu_notesets, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = null;
        
        // handle menu item selection
        switch (item.getItemId()) {
        case R.id.create_noteset:
            intent = new Intent(this, CreateNotesetActivity.class);
            startActivity(intent);
            return true;
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
        inflater.inflate(R.menu.context_menu_noteset, menu);
    }
    
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
        Intent intent = null;
        switch (item.getItemId()) {
            case R.id.context_menu_view:
                intent = new Intent(this, ViewNotesetDetailActivity.class);
                intent.putExtra("list_id", info.id);
                startActivity(intent);
                return true;
            case R.id.context_menu_edit:
                intent = new Intent(this, EditNotesetActivity.class);
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
                // go ahead and delete noteset
                
                // get correct noteset id to delete
                Log.d("MYLOG", "selected row item: " + selectedListPosition);
                
                Noteset notesetToDelete = getNotesetFromListPosition(selectedListPosition);

                Log.d("MYLOG", "deleting noteset: " + notesetToDelete.getId());
                deleteNoteset(notesetToDelete);
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
    
    public Noteset getNotesetFromListPosition(long rowId) {
        
        long notesetId = rowId;
        
        List<Long> allNotesetsData = new LinkedList<Long>();
        NotesetsDataSource ds = new NotesetsDataSource(this);

        // get string version of returned noteset list
        allNotesetsData = ds.getAllNotesetListDBTableIds();
        
        Log.d("MYLOG", allNotesetsData.toString());

        // prevent crashes due to lack of database data
        if (allNotesetsData.isEmpty())
            allNotesetsData.add((long) 0);

        Log.d("MYLOG", "notesetId:: " + notesetId);
        
        Long[] allNotesets = allNotesetsData
                .toArray(new Long[allNotesetsData.size()]);
        
        Log.d("MYLOG", "found noteset data: " + allNotesets[(int) notesetId]);
        
        // get noteset and notes information
        HashMap<Integer, List<Note>> notesetBundle = new HashMap<Integer, List<Note>>();
        notesetBundle = ds.getNotesetBundle(allNotesets[(int) notesetId]);
        
        Log.d("MYLOG", "noteset bundle: " + notesetBundle);
        Log.d("MYLOG", "notesetId::: " + allNotesets[(int) notesetId]);
        
        Noteset noteset = ds.getNoteset(allNotesets[(int) notesetId]);        
        
        ds.close();
        
        return noteset;
    }
    
    public void deleteNoteset(Noteset noteset) {
        NotesetsDataSource ds = new NotesetsDataSource(this);
        ds.deleteNoteset(noteset);
        ds.close();
    }
    
    // refresh noteset after save or update
    // TODO: combine onCreate and onResume duplicated code
    @Override
    public void onResume() {
        super.onResume();
        
        List<String> allNotesetsData = new LinkedList<String>();
        NotesetsDataSource ds = new NotesetsDataSource(this);

        // get string version of returned noteset list
        allNotesetsData = ds.getAllNotesetListPreviews();

        // prevent crashes due to lack of database data
        if (allNotesetsData.isEmpty())
            allNotesetsData.add("empty");

        String[] allNotesets = allNotesetsData
                .toArray(new String[allNotesetsData.size()]);

        // pass list data to adapter
        setListAdapter(new ArrayAdapter<String>(this, R.layout.list_noteset,
                allNotesets));

        ListView listView = getListView();
        listView.setTextFilterEnabled(true);

        // get individual noteset details
        listView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
                
                Log.d("MYLOG", "list item id: " + id);
                
                // launch details activity
                Intent intent = new Intent(view.getContext(),
                        ViewNotesetDetailActivity.class);
                
                intent.putExtra("list_id", id);
                startActivity(intent);
            }
        });
        
        // register context menu
        registerForContextMenu(listView);
    }
}