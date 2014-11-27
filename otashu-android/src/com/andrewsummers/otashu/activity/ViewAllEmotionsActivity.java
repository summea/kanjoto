package com.andrewsummers.otashu.activity;

import java.util.LinkedList;
import java.util.List;

import com.andrewsummers.com.otashu.adapter.EmotionAdapter;
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
import com.andrewsummers.otashu.model.EmotionAndRelated;

import android.app.AlertDialog;
import android.app.ListActivity;
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
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;

/**
 * View all emotions as a list.
 */
public class ViewAllEmotionsActivity extends ListActivity {
    
    private int selectedPositionInList = 0;
    private EmotionAdapter adapter = null;
    
    /**
     * onCreate override used to gather and display a list of all emotions saved
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
        List<Emotion> allEmotions = new LinkedList<Emotion>();
        
        List<EmotionAndRelated> allEmotionsAndRelated = new LinkedList<EmotionAndRelated>();
        
        EmotionsDataSource eds = new EmotionsDataSource(this);
        LabelsDataSource lds = new LabelsDataSource(this);
        
        allEmotions = eds.getAllEmotions();
        //allNotes = nds.getAllNotes();
        
        for (Emotion emotion : allEmotions) {
            relatedLabel = lds.getLabel(emotion.getLabelId());
            EmotionAndRelated emotionAndRelated = new EmotionAndRelated();
            emotionAndRelated.setEmotion(emotion);
            emotionAndRelated.setLabel(relatedLabel);
            allEmotionsAndRelated.add(emotionAndRelated);
        }

        // TODO: check if crash still happens when there is no database data...
        
        // prevent crashes due to lack of database data
        //if (allNotesetsData.isEmpty())
            //allNotesetsData.add("empty");

        // pass list data to adapter
        adapter = new EmotionAdapter(this, allEmotionsAndRelated);
        
        final ListView listView = getListView();
        listView.setTextFilterEnabled(true);
        listView.setAdapter(adapter);
        
        // get individual emotion details
        listView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
                
                Log.d("MYLOG", "list item id: " + id);
                
                // launch details activity
                Intent intent = new Intent(view.getContext(),
                        ViewEmotionDetailActivity.class);
                
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
        inflater.inflate(R.menu.menu_emotions, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = null;
        
        // handle menu item selection
        switch (item.getItemId()) {
        case R.id.create_emotion:
            intent = new Intent(this, CreateEmotionActivity.class);
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
        inflater.inflate(R.menu.context_menu_emotion, menu);
    }
    
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
        Intent intent = null;
        switch (item.getItemId()) {
            case R.id.context_menu_view:
                intent = new Intent(this, ViewEmotionDetailActivity.class);
                intent.putExtra("list_id", info.id);
                startActivity(intent);
                return true;
            case R.id.context_menu_edit:
                intent = new Intent(this, EditEmotionActivity.class);
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
                // go ahead and delete emotion
                
                // get correct emotion id to delete
                Log.d("MYLOG", "selected row item: " + selectedPositionInList);
                
                Emotion emotionToDelete = getEmotionFromListPosition(selectedPositionInList);

                Log.d("MYLOG", "deleting emotion: " + emotionToDelete.getId());
                deleteEmotion(emotionToDelete);
                
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
    
    public Emotion getEmotionFromListPosition(long rowId) {
        
        long emotionId = rowId;
        
        List<Long> allEmotionsData = new LinkedList<Long>();
        EmotionsDataSource ds = new EmotionsDataSource(this);

        // get string version of returned emotion list
        allEmotionsData = ds.getAllEmotionListDBTableIds();
        
        Log.d("MYLOG", allEmotionsData.toString());

        // prevent crashes due to lack of database data
        if (allEmotionsData.isEmpty())
            allEmotionsData.add((long) 0);
        
        Long[] allEmotions = allEmotionsData
                .toArray(new Long[allEmotionsData.size()]);
        
        Log.d("MYLOG", "rowId: " + rowId);
        Log.d("MYLOG", "found emotion data: " + allEmotions[(int) emotionId]);
                
        Emotion emotion = ds.getEmotion(allEmotions[(int) emotionId]);        
        
        ds.close();
        
        return emotion;
    }
    
    public void deleteEmotion(Emotion emotion) {
        EmotionsDataSource ds = new EmotionsDataSource(this);
        ds.deleteEmotion(emotion);
        ds.close();
    }
    
    @Override
    public void onResume() {
        super.onResume();
        
        // refresh list
        adapter.clear();
        fillList();
    }
}