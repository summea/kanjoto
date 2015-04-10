
package com.andrewsummers.otashu.activity;

import java.util.LinkedList;
import java.util.List;

import com.andrewsummers.otashu.R;
import com.andrewsummers.otashu.adapter.ApprenticeAdapter;
import com.andrewsummers.otashu.data.ApprenticesDataSource;
import com.andrewsummers.otashu.model.Apprentice;

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
 * View all Apprentices as a list.
 * <p>
 * This activity allows a user to view a list of all saved Apprentices. Apprentices are used to save
 * information "learned" by the Apprentice, in particular: noteset-emotion data and
 * noteset-transition data.
 * </p>
 */
public class ViewAllApprenticesActivity extends ListActivity {
    private ListView listView = null;
    private int selectedPositionInList = 0;
    private ApprenticeAdapter adapter = null;

    /**
     * onCreate override used to gather and display a list of all apprentices saved in database.
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
        headerText.setText(R.string.view_all_apprentices_list_header);
        listView.addHeaderView(listHeader, "", false);

        fillList();
    }

    public void fillList() {
        List<Apprentice> allApprentices = new LinkedList<Apprentice>();
        ApprenticesDataSource lds = new ApprenticesDataSource(this);
        allApprentices = lds.getAllApprentices();
        lds.close();

        // pass list data to adapter
        adapter = new ApprenticeAdapter(this, allApprentices);

        listView.setAdapter(adapter);

        // get individual apprentice details
        listView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {

                // launch details activity
                Intent intent = new Intent(view.getContext(),
                        ViewApprenticeDetailActivity.class);

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
        inflater.inflate(R.menu.menu_apprentices, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = null;

        // handle menu item selection
        switch (item.getItemId()) {
            case R.id.create_apprentice:
                intent = new Intent(this, CreateApprenticeActivity.class);
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
        inflater.inflate(R.menu.context_menu_apprentice, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
        Intent intent = null;

        switch (item.getItemId()) {
            case R.id.context_menu_view:
                intent = new Intent(this, ViewApprenticeDetailActivity.class);
                intent.putExtra("list_id", info.id);
                startActivity(intent);
                return true;
            case R.id.context_menu_edit:
                intent = new Intent(this, EditApprenticeActivity.class);
                intent.putExtra("list_id", info.id);
                startActivity(intent);
                return true;
            case R.id.context_menu_delete:
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
                // go ahead and delete apprentice

                // get correct apprentice id to delete
                Apprentice apprenticeToDelete = getApprenticeFromListPosition(selectedPositionInList);

                deleteApprentice(apprenticeToDelete);

                Context context = getApplicationContext();
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context,
                        context.getResources().getString(R.string.apprentice_deleted),
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

    public Apprentice getApprenticeFromListPosition(long rowId) {
        long apprenticeId = rowId;
        List<Long> allApprenticesData = new LinkedList<Long>();
        ApprenticesDataSource lds = new ApprenticesDataSource(this);

        // get string version of returned apprentice list
        allApprenticesData = lds.getAllApprenticeListDBTableIds();
        lds.close();

        // prevent crashes due to lack of database data
        if (allApprenticesData.isEmpty())
            allApprenticesData.add((long) 0);

        Long[] allApprentices = allApprenticesData
                .toArray(new Long[allApprenticesData.size()]);

        Apprentice apprentice = lds.getApprentice(allApprentices[(int) apprenticeId]);
        lds.close();

        return apprentice;
    }

    public void deleteApprentice(Apprentice apprentice) {
        ApprenticesDataSource lds = new ApprenticesDataSource(this);
        lds.deleteApprentice(apprentice);
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
