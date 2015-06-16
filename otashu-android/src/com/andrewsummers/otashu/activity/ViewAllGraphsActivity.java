
package com.andrewsummers.otashu.activity;

import java.util.LinkedList;
import java.util.List;

import com.andrewsummers.otashu.R;
import com.andrewsummers.otashu.adapter.GraphAdapter;
import com.andrewsummers.otashu.data.GraphsDataSource;
import com.andrewsummers.otashu.model.Graph;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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
 * View all Graphs as a list.
 * <p>
 * This activity allows a user to view a list of all saved Graphs. Graphs are used to save
 * information "learned" by the Apprentice, in particular: noteset-emotion data and
 * noteset-transition data.
 * </p>
 */
public class ViewAllGraphsActivity extends ListActivity {
    private ListView listView = null;
    private int selectedPositionInList = 0;
    private GraphAdapter adapter = null;

    /**
     * onCreate override used to gather and display a list of all graphs saved in database.
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
        headerText.setText(R.string.view_all_graphs_list_header);
        listView.addHeaderView(listHeader, "", false);

        fillList();
    }

    public void fillList() {
        List<Graph> allGraphs = new LinkedList<Graph>();
        GraphsDataSource lds = new GraphsDataSource(this);
        allGraphs = lds.getAllGraphs();
        lds.close();

        // pass list data to adapter
        adapter = new GraphAdapter(this, allGraphs);

        listView.setAdapter(adapter);

        // get individual graph details
        listView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {

                // launch details activity
                Intent intent = new Intent(view.getContext(),
                        ViewGraphDetailActivity.class);

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
        inflater.inflate(R.menu.menu_graphs, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = null;

        // handle menu item selection
        switch (item.getItemId()) {
            case R.id.create_graph:
                intent = new Intent(this, CreateGraphActivity.class);
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
        inflater.inflate(R.menu.context_menu_graph, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
        Intent intent = null;

        switch (item.getItemId()) {
            case R.id.context_menu_view:
                intent = new Intent(this, ViewGraphDetailActivity.class);
                intent.putExtra("list_id", info.id);
                startActivity(intent);
                return true;
            case R.id.context_menu_edit:
                intent = new Intent(this, EditGraphActivity.class);
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
        final GraphsDataSource gds = new GraphsDataSource(this);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.dialog_confirm_delete_message).setTitle(
                R.string.dialog_confirm_delete_title);
        builder.setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // user clicked ok, so go ahead and delete graph
                // get correct graph id to delete
                Graph graphToDelete = gds.getGraph(info.id);
                deleteGraph(graphToDelete);

                Context context = getApplicationContext();
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context,
                        context.getResources().getString(R.string.graph_deleted),
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
    }

    public void deleteGraph(Graph graph) {
        GraphsDataSource lds = new GraphsDataSource(this);
        lds.deleteGraph(graph);
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
