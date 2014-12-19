
package com.andrewsummers.otashu.activity;

import java.util.LinkedList;
import java.util.List;

import com.andrewsummers.otashu.R;
import com.andrewsummers.otashu.adapter.LabelAdapter;
import com.andrewsummers.otashu.data.LabelsDataSource;
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
 * View all labels as a list.
 */
public class ViewAllLabelsActivity extends ListActivity {

    private int selectedPositionInList = 0;
    private LabelAdapter adapter = null;

    /**
     * onCreate override used to gather and display a list of all labels saved in database.
     * 
     * @param savedInstanceState Current application state data.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fillList();
    }

    public void fillList() {
        List<Label> allLabels = new LinkedList<Label>();
        LabelsDataSource lds = new LabelsDataSource(this);

        allLabels = lds.getAllLabels();

        lds.close();

        /*
         * // prevent crashes due to lack of database data if (allLabels.isEmpty())
         * allLabels.add("empty");
         */

        // pass list data to adapter
        adapter = new LabelAdapter(this, allLabels);

        final ListView listView = getListView();
        listView.setTextFilterEnabled(true);
        listView.setAdapter(adapter);

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
        selectedPositionInList = info.position;

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
        builder.setMessage(R.string.dialog_confirm_delete_message).setTitle(
                R.string.dialog_confirm_delete_title);
        builder.setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // user clicked ok
                // go ahead and delete label

                // get correct label id to delete
                Log.d("MYLOG", "selected row item: " + selectedPositionInList);

                Label labelToDelete = getLabelFromListPosition(selectedPositionInList);

                Log.d("MYLOG", "deleting label: " + labelToDelete.getId());
                deleteLabel(labelToDelete);

                Context context = getApplicationContext();
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context,
                        context.getResources().getString(R.string.label_deleted),
                        duration);
                toast.show();

                // refresh list
                adapter.removeItem(selectedPositionInList);
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
    }

    public Label getLabelFromListPosition(long rowId) {

        long labelId = rowId;

        List<Long> allLabelsData = new LinkedList<Long>();
        LabelsDataSource lds = new LabelsDataSource(this);

        // get string version of returned label list
        allLabelsData = lds.getAllLabelListDBTableIds();
        lds.close();

        Log.d("MYLOG", allLabelsData.toString());

        // prevent crashes due to lack of database data
        if (allLabelsData.isEmpty())
            allLabelsData.add((long) 0);

        Long[] allLabels = allLabelsData
                .toArray(new Long[allLabelsData.size()]);

        Log.d("MYLOG", "rowId: " + rowId);
        Log.d("MYLOG", "found label data: " + allLabels[(int) labelId]);

        Label label = lds.getLabel(allLabels[(int) labelId]);

        lds.close();

        return label;
    }

    public void deleteLabel(Label label) {
        LabelsDataSource lds = new LabelsDataSource(this);
        lds.deleteLabel(label);
        lds.close();
    }

    @Override
    public void onResume() {
        super.onResume();

        // refresh list
        adapter.clear();
        fillList();
    }
}
