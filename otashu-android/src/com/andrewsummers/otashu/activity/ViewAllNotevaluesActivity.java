
package com.andrewsummers.otashu.activity;

import java.util.LinkedList;
import java.util.List;

import com.andrewsummers.otashu.R;
import com.andrewsummers.otashu.adapter.NotevalueAdapter;
import com.andrewsummers.otashu.data.LearningStylesDataSource;
import com.andrewsummers.otashu.data.NotevaluesDataSource;
import com.andrewsummers.otashu.data.LabelsDataSource;
import com.andrewsummers.otashu.model.Notevalue;
import com.andrewsummers.otashu.model.Label;
import com.andrewsummers.otashu.model.NotevalueAndRelated;

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
 * View all Notevalues as a list.
 * <p>
 * This activity allows a user to view a list of all saved Notevalues. Notevalues are conversion
 * objects between MIDI notevalues and their respective formatted note strings (e.g. A0:21, C8:108).
 * Notevalues are used to provide an easier-to-read format for musical notevalues.
 * </p>
 */
public class ViewAllNotevaluesActivity extends ListActivity {
    private ListView listView = null;
    private NotevalueAdapter adapter = null;

    /**
     * onCreate override used to gather and display a list of all notevalues saved in database.
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
        headerText.setText(R.string.view_all_notevalues_list_header);
        listView.addHeaderView(listHeader, "", false);

        fillList();
    }

    public void fillList() {
        Label relatedLabel = new Label();
        List<Notevalue> allNotevalues = new LinkedList<Notevalue>();
        List<NotevalueAndRelated> allNotevaluesAndRelated = new LinkedList<NotevalueAndRelated>();
        NotevaluesDataSource nvds = new NotevaluesDataSource(this);
        LabelsDataSource lds = new LabelsDataSource(this);

        allNotevalues = nvds.getAllNotevalues();

        for (Notevalue notevalue : allNotevalues) {
            relatedLabel = lds.getLabel(notevalue.getLabelId());
            NotevalueAndRelated notevalueAndRelated = new NotevalueAndRelated();
            notevalueAndRelated.setNotevalue(notevalue);
            notevalueAndRelated.setLabel(relatedLabel);
            allNotevaluesAndRelated.add(notevalueAndRelated);
        }

        nvds.close();
        lds.close();

        // pass list data to adapter
        adapter = new NotevalueAdapter(this, allNotevaluesAndRelated);

        listView.setAdapter(adapter);

        // get individual notevalue details
        listView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {

                // launch details activity
                Intent intent = new Intent(view.getContext(),
                        ViewNotevalueDetailActivity.class);

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
        inflater.inflate(R.menu.menu_notevalues, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = null;

        // handle menu item selection
        switch (item.getItemId()) {
            case R.id.create_notevalue:
                intent = new Intent(this, CreateNotevalueActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_menu_notevalue, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
        Intent intent = null;
        switch (item.getItemId()) {
            case R.id.context_menu_view:
                intent = new Intent(this, ViewNotevalueDetailActivity.class);
                intent.putExtra("list_id", info.id);
                startActivity(intent);
                return true;
            case R.id.context_menu_edit:
                intent = new Intent(this, EditNotevalueActivity.class);
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
        final NotevaluesDataSource nds = new NotevaluesDataSource(this);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.dialog_confirm_delete_message).setTitle(
                R.string.dialog_confirm_delete_title);
        builder.setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // user clicked ok
                // go ahead and delete notevalue

                // get correct notevalue id to delete
                Notevalue notevalueToDelete = nds.getNotevalue(info.id);
                deleteNotevalue(notevalueToDelete);

                Context context = getApplicationContext();
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context,
                        context.getResources().getString(R.string.notevalue_deleted),
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
        nds.close();
    }

    public void deleteNotevalue(Notevalue notevalue) {
        NotevaluesDataSource eds = new NotevaluesDataSource(this);
        eds.deleteNotevalue(notevalue);
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
