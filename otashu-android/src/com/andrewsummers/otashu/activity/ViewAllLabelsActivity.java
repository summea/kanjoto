package com.andrewsummers.otashu.activity;

import java.util.LinkedList;
import java.util.List;

import com.andrewsummers.otashu.R;
import com.andrewsummers.otashu.data.LabelsDataSource;
import com.andrewsummers.otashu.model.Label;

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
 * View all labels as a list.
 */
public class ViewAllLabelsActivity extends ListActivity {
    
    private int selectedListPosition = 0;
    
    /**
     * onCreate override used to gather and display a list of all labels saved
     * in database.
     * 
     * @param savedInstanceState
     *            Current application state data.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        List<String> allLabelsData = new LinkedList<String>();
        LabelsDataSource ds = new LabelsDataSource(this);

        // get string version of returned label list
        allLabelsData = ds.getAllLabelListPreviews();

        // prevent crashes due to lack of database data
        if (allLabelsData.isEmpty())
            allLabelsData.add("empty");

        String[] allLabels = allLabelsData
                .toArray(new String[allLabelsData.size()]);

        // pass list data to adapter
        setListAdapter(new ArrayAdapter<String>(this, R.layout.list_label,
                allLabels));

        ListView listView = getListView();
        listView.setTextFilterEnabled(true);
        
        // get individual label details
        listView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
                
                Log.d("MYLOG", "list item id: " + id);
                
                // launch details activity
                Intent intent = new Intent(view.getContext(),
                        ViewLabelDetailActivity.class);
                
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
        inflater.inflate(R.menu.menu_labels, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = null;
        
        // handle menu item selection
        switch (item.getItemId()) {
        case R.id.create_label:
            intent = new Intent(this, CreateLabelActivity.class);
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
        inflater.inflate(R.menu.context_menu_label, menu);
    }
    
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
        Intent intent = null;

        switch (item.getItemId()) {
            case R.id.context_menu_view:
                intent = new Intent(this, ViewLabelDetailActivity.class);
                intent.putExtra("list_id", info.id);
                startActivity(intent);
                return true;
            case R.id.context_menu_edit:
                intent = new Intent(this, EditLabelActivity.class);
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
                // go ahead and delete label
                
                // get correct label id to delete
                Log.d("MYLOG", "selected row item: " + selectedListPosition);
                
                Label labelToDelete = getLabelFromListPosition(selectedListPosition);

                Log.d("MYLOG", "deleting label: " + labelToDelete.getId());
                deleteLabel(labelToDelete);
                
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
    
    public Label getLabelFromListPosition(long rowId) {
        
        long labelId = rowId;
        
        List<Long> allLabelsData = new LinkedList<Long>();
        LabelsDataSource ds = new LabelsDataSource(this);

        // get string version of returned label list
        allLabelsData = ds.getAllLabelListDBTableIds();
        
        Log.d("MYLOG", allLabelsData.toString());

        // prevent crashes due to lack of database data
        if (allLabelsData.isEmpty())
            allLabelsData.add((long) 0);
        
        Long[] allLabels = allLabelsData
                .toArray(new Long[allLabelsData.size()]);
        
        Log.d("MYLOG", "rowId: " + rowId);
        Log.d("MYLOG", "found label data: " + allLabels[(int) labelId]);
                
        Label label = ds.getLabel(allLabels[(int) labelId]);        
        
        ds.close();
        
        return label;
    }
    
    public void deleteLabel(Label label) {
        LabelsDataSource ds = new LabelsDataSource(this);
        ds.deleteLabel(label);
        ds.close();
    }
    
    @Override
    public void onResume() {
        super.onResume();

        List<String> allLabelsData = new LinkedList<String>();
        LabelsDataSource ds = new LabelsDataSource(this);

        // get string version of returned label list
        allLabelsData = ds.getAllLabelListPreviews();

        // prevent crashes due to lack of database data
        if (allLabelsData.isEmpty())
            allLabelsData.add("empty");

        String[] allLabels = allLabelsData
                .toArray(new String[allLabelsData.size()]);

        // pass list data to adapter
        setListAdapter(new ArrayAdapter<String>(this, R.layout.list_label,
                allLabels));

        ListView listView = getListView();
        listView.setTextFilterEnabled(true);
        
        // get individual label details
        listView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
                
                Log.d("MYLOG", "list item id: " + id);
                
                // launch details activity
                Intent intent = new Intent(view.getContext(),
                        ViewLabelDetailActivity.class);
                
                intent.putExtra("list_id", id);
                startActivity(intent);
            }
        });

        // register context menu
        registerForContextMenu(listView);
    }
}