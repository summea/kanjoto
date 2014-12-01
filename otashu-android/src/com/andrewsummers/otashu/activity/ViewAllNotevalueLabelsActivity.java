package com.andrewsummers.otashu.activity;

import java.util.LinkedList;
import java.util.List;

import com.andrewsummers.otashu.R;
import com.andrewsummers.otashu.adapter.NotevalueLabelAdapter;
import com.andrewsummers.otashu.data.NotevalueLabelsDataSource;
import com.andrewsummers.otashu.data.LabelsDataSource;
import com.andrewsummers.otashu.model.NotevalueLabel;
import com.andrewsummers.otashu.model.Label;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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

/**
 * View all noteLabels as a list.
 */
public class ViewAllNotevalueLabelsActivity extends ListActivity {
    
    private int selectedPositionInList = 0;
    private NotevalueLabelAdapter adapter = null;
    
    /**
     * onCreate override used to gather and display a list of all noteLabels saved
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
        Label relatedLabel = new Label();
        List<NotevalueLabel> allNoteLabels = new LinkedList<NotevalueLabel>();
        
        List<NotevalueLabel> allNoteLabelsAndRelated = new LinkedList<NotevalueLabel>();
        
        NotevalueLabelsDataSource eds = new NotevalueLabelsDataSource(this);
        LabelsDataSource lds = new LabelsDataSource(this);
        
        allNoteLabels = eds.getAllNoteLabels();
        
        for (NotevalueLabel noteLabel : allNoteLabels) {
            /*relatedLabel = lds.getLabel(noteLabel.getLabelId());
            NoteLabel noteLabel = new NoteLabel();
            noteLabel.setNoteLabel(noteLabel);
            noteLabel.setLabel(relatedLabel);
            */
            allNoteLabelsAndRelated.add(noteLabel);
        }
        
        eds.close();
        lds.close();

        // TODO: check if crash still happens when there is no database data...
        
        // prevent crashes due to lack of database data
        //if (allNotesetsData.isEmpty())
            //allNotesetsData.add("empty");

        // pass list data to adapter
        adapter = new NotevalueLabelAdapter(this, allNoteLabelsAndRelated);
        
        final ListView listView = getListView();
        listView.setTextFilterEnabled(true);
        listView.setAdapter(adapter);
        
        // get individual noteLabel details
        listView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
                
                Log.d("MYLOG", "list item id: " + id);
              
                /*
                // launch details activity
                Intent intent = new Intent(view.getContext(),
                        ViewNoteLabelDetailActivity.class);
                
                intent.putExtra("list_id", id);
                startActivity(intent);
                */
            }
        });

        // register context menu
        registerForContextMenu(listView);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_notevalue_labels, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = null;
        
        // handle menu item selection
        switch (item.getItemId()) {
        case R.id.create_notevalue_label:
            intent = new Intent(this, CreateNotevalueLabelActivity.class);
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
        //inflater.inflate(R.menu.context_menu_noteLabel, menu);
    }
    
    /*
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
        Intent intent = null;
        switch (item.getItemId()) {
            case R.id.context_menu_view:
                intent = new Intent(this, ViewNoteLabelDetailActivity.class);
                intent.putExtra("list_id", info.id);
                startActivity(intent);
                return true;
            case R.id.context_menu_edit:
                intent = new Intent(this, EditNoteLabelActivity.class);
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
    */
    
    public void confirmDelete() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.dialog_confirm_delete_message).setTitle(R.string.dialog_confirm_delete_title);
        builder.setPositiveButton(R.string.button_ok,  new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // user clicked ok
                // go ahead and delete noteLabel
                
                // get correct noteLabel id to delete
                Log.d("MYLOG", "selected row item: " + selectedPositionInList);
                
                NotevalueLabel noteLabelToDelete = getNoteLabelFromListPosition(selectedPositionInList);

                Log.d("MYLOG", "deleting noteLabel: " + noteLabelToDelete.getId());
                deleteNoteLabel(noteLabelToDelete);
                
                Context context = getApplicationContext();
                int duration = Toast.LENGTH_SHORT;

                /*
                Toast toast = Toast.makeText(context,
                        context.getResources().getString(R.string.noteLabel_deleted),
                        duration);
                toast.show();
                */
                
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
    
    public NotevalueLabel getNoteLabelFromListPosition(long rowId) {
        
        long noteLabelId = rowId;
        
        List<Long> allNoteLabelsData = new LinkedList<Long>();
        NotevalueLabelsDataSource eds = new NotevalueLabelsDataSource(this);

        // get string version of returned noteLabel list
        //allNoteLabelsData = eds.getAllNoteLabelListDBTableIds();
        eds.close();
        
        Log.d("MYLOG", allNoteLabelsData.toString());

        // prevent crashes due to lack of database data
        if (allNoteLabelsData.isEmpty())
            allNoteLabelsData.add((long) 0);
        
        Long[] allNoteLabels = allNoteLabelsData
                .toArray(new Long[allNoteLabelsData.size()]);
        
        Log.d("MYLOG", "rowId: " + rowId);
        Log.d("MYLOG", "found noteLabel data: " + allNoteLabels[(int) noteLabelId]);
                
        NotevalueLabel noteLabel = eds.getNoteLabel(allNoteLabels[(int) noteLabelId]);        
        
        eds.close();
        
        return noteLabel;
    }
    
    public void deleteNoteLabel(NotevalueLabel noteLabel) {
        NotevalueLabelsDataSource eds = new NotevalueLabelsDataSource(this);
        eds.deleteNoteLabel(noteLabel);
        eds.close();
    }
    
    @Override
    public void onResume() {
        super.onResume();
        
        // refresh list
        adapter.clear();
        fillList();
    }
}