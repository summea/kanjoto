package com.andrewsummers.otashu.activity;

import java.util.LinkedList;
import java.util.List;

import com.andrewsummers.com.otashu.adapter.NotesetAdapter;
import com.andrewsummers.otashu.R;
import com.andrewsummers.otashu.data.EmotionsDataSource;
import com.andrewsummers.otashu.data.LabelsDataSource;
import com.andrewsummers.otashu.data.NotesDataSource;
import com.andrewsummers.otashu.data.NotesetsDataSource;
import com.andrewsummers.otashu.model.Emotion;
import com.andrewsummers.otashu.model.Label;
import com.andrewsummers.otashu.model.Note;
import com.andrewsummers.otashu.model.Noteset;
import com.andrewsummers.otashu.model.NotesetAndRelated;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

/**
 * View all notesets as a list.
 */
public class ViewAllNotesetsActivity extends ListActivity {
    
    private int selectedPositionInList = 0;
    private NotesetAdapter adapter = null;
    List<NotesetAndRelated> allNotesetsAndNotes = new LinkedList<NotesetAndRelated>();
    
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
        fillList();
    }
    
    public void fillList() {
        Emotion relatedEmotion;
        Label relatedLabel;
        List<Noteset> allNotesets = new LinkedList<Noteset>();        
        List<Note> relatedNotes = new LinkedList<Note>();
        
        EmotionsDataSource eds = new EmotionsDataSource(this);
        LabelsDataSource lds = new LabelsDataSource(this);
        NotesetsDataSource ds = new NotesetsDataSource(this);
        NotesDataSource nds = new NotesDataSource(this);
        
        allNotesets = ds.getAllNotesets();
        
        for (Noteset noteset : allNotesets) {            
            relatedNotes = nds.getAllNotes(noteset.getId());
            relatedEmotion = eds.getEmotion(noteset.getEmotion());
            relatedLabel = lds.getLabel(relatedEmotion.getLabelId());
            NotesetAndRelated notesetAndRelated = new NotesetAndRelated();
            notesetAndRelated.setEmotion(relatedEmotion);
            notesetAndRelated.setLabel(relatedLabel);
            notesetAndRelated.setNoteset(noteset);
            notesetAndRelated.setNotes(relatedNotes);
            allNotesetsAndNotes.add(notesetAndRelated);
        }

        // TODO: check if crash still happens when there is no database data...
        
        // prevent crashes due to lack of database data
        //if (allNotesetsData.isEmpty())
            //allNotesetsData.add("empty");

        // pass list data to adapter
        adapter = new NotesetAdapter(this, allNotesetsAndNotes);
        
        final ListView listView = getListView();
        listView.setTextFilterEnabled(true);
        listView.setAdapter(adapter);
        
        
        // get individual noteset details
        listView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {

                // launch details activity
                Intent intent = new Intent(view.getContext(),
                        ViewNotesetDetailActivity.class);
 
                intent.putExtra("list_id", adapter.getItemId(position));
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
        selectedPositionInList = info.position;
        
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

                // get noteset id to delete (from chosen item in list)
                Noteset notesetToDelete = allNotesetsAndNotes.get(selectedPositionInList).getNoteset();  

                Log.d("MYLOG", "deleting noteset: " + notesetToDelete.getId());
                deleteNoteset(notesetToDelete);
                
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
    
    public void deleteNoteset(Noteset noteset) {
        NotesetsDataSource ds = new NotesetsDataSource(this);
        ds.deleteNoteset(noteset);
        ds.close();
    }
    
    @Override
    public void onResume() {
        super.onResume();
        
        // refresh list
        adapter.clear();
        fillList();

/*
        List<String> allNotesetsData = new LinkedList<String>();
        NotesetsDataSource ds = new NotesetsDataSource(this);

        String[] noteLabelsArray = getResources().getStringArray(R.array.note_labels_array);
        String[] noteValuesArray = getResources().getStringArray(R.array.note_values_array);
        
        // get string version of returned noteset list
        allNotesetsData = ds.getAllNotesetListPreviews(noteLabelsArray, noteValuesArray);
        
        // prevent crashes due to lack of database data
        if (allNotesetsData.isEmpty())
            allNotesetsData.add("empty");

        // pass list data to adapter
        setListAdapter(new ArrayAdapter<String>(this, R.layout.list_noteset,
                allNotesetsData));

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
        */
    }    
}