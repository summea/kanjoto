package com.andrewsummers.otashu.activity;

import java.util.LinkedList;
import java.util.List;

import com.andrewsummers.otashu.R;
import com.andrewsummers.otashu.data.EmotionsDataSource;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.AdapterContextMenuInfo;

/**
 * View all emotions as a list.
 */
public class ViewAllEmotionsActivity extends ListActivity {
    
    //private int selectedListPosition = 0;
    
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

        List<String> allEmotionsData = new LinkedList<String>();
        EmotionsDataSource ds = new EmotionsDataSource(this);

        // get string version of returned emotion list
        allEmotionsData = ds.getAllEmotionListPreviews();

        // prevent crashes due to lack of database data
        if (allEmotionsData.isEmpty())
            allEmotionsData.add("empty");

        String[] allEmotions = allEmotionsData
                .toArray(new String[allEmotionsData.size()]);

        // pass list data to adapter
        setListAdapter(new ArrayAdapter<String>(this, R.layout.list_emotion,
                allEmotions));

        ListView listView = getListView();
        listView.setTextFilterEnabled(true);
        
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
        
        //AdapterContextMenuInfo info = (AdapterContextMenuInfo) menuInfo;
        //selectedListPosition = info.position;
        
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_menu_emotion, menu);
    }
    
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
        Intent intent = null;
        switch (item.getItemId()) {
            case R.id.context_menu_view:
                /*
                intent = new Intent(this, ViewEmotionDetailActivity.class);
                intent.putExtra("list_id", info.id);
                startActivity(intent);
                */
                return true;
            case R.id.context_menu_edit:
                intent = new Intent(this, EditEmotionActivity.class);
                intent.putExtra("list_id", info.id);
                startActivity(intent);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }
    
    // refresh noteset after save or update
    // TODO: combine onCreate and onResume duplicated code
    @Override
    public void onResume() {
        super.onResume();
        
        List<String> allEmotionsData = new LinkedList<String>();
        EmotionsDataSource ds = new EmotionsDataSource(this);

        // get string version of returned emotion list
        allEmotionsData = ds.getAllEmotionListPreviews();

        // prevent crashes due to lack of database data
        if (allEmotionsData.isEmpty())
            allEmotionsData.add("empty");

        String[] allEmotions = allEmotionsData
                .toArray(new String[allEmotionsData.size()]);

        // pass list data to adapter
        setListAdapter(new ArrayAdapter<String>(this, R.layout.list_emotion,
                allEmotions));

        ListView listView = getListView();
        listView.setTextFilterEnabled(true);
    }
}