
package com.andrewsummers.otashu.activity;

import java.util.LinkedList;
import java.util.List;

import com.andrewsummers.otashu.R;
import com.andrewsummers.otashu.adapter.NotesetAdapter;
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
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

/**
 * View all Notesets as a list.
 * <p>
 * This activity allows a user to view a list of all saved Notesets. Notesets are sequences of Note
 * objects (i.e. a list of musical notes grouped together in a specified order) that are tagged with
 * a specified Emotion.
 * </p>
 */
public class ViewAllNotesetsActivity extends ListActivity {
    private ListView listView = null;
    private int selectedPositionInList = 0;
    private NotesetAdapter adapter = null;
    List<NotesetAndRelated> allNotesetsAndNotes = new LinkedList<NotesetAndRelated>();
    private int currentOffset = 0;
    private int totalNotesetsAvailable = 0;
    private int limit = 25;
    private Boolean doneLoading = false;
    private SharedPreferences sharedPref;
    private long apprenticeId = 0;

    /**
     * onCreate override used to gather and display a list of all notesets saved in database.
     * 
     * @param savedInstanceState Current application state data.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        apprenticeId = Long.parseLong(sharedPref.getString(
                "pref_selected_apprentice", "1"));

        // initialize ListView
        listView = getListView();

        // set title for list activity
        ViewGroup listHeader = (ViewGroup) getLayoutInflater().inflate(R.layout.list_header,
                listView, false);
        TextView headerText = (TextView) listHeader.findViewById(R.id.list_header_title);
        headerText.setText(R.string.view_all_notesets_list_header);
        listView.addHeaderView(listHeader, "", false);

        NotesetsDataSource nds = new NotesetsDataSource(this);
        totalNotesetsAvailable = nds.getCount(apprenticeId);
        nds.close();

        fillList();
    }

    public void fillList() {
        Emotion relatedEmotion;
        Label relatedLabel;
        List<Noteset> allNotesets = new LinkedList<Noteset>();
        List<Note> relatedNotes = new LinkedList<Note>();

        EmotionsDataSource eds = new EmotionsDataSource(this);
        LabelsDataSource lds = new LabelsDataSource(this);
        NotesetsDataSource nsds = new NotesetsDataSource(this);
        NotesDataSource nds = new NotesDataSource(this);

        totalNotesetsAvailable = nsds.getCount(apprenticeId);

        if (currentOffset <= totalNotesetsAvailable && !doneLoading) {
            allNotesets = nsds.getAllNotesets(apprenticeId, limit, currentOffset);

            for (Noteset noteset : allNotesets) {
                relatedNotes = nds.getAllNotesByNotesetId(noteset.getId());
                relatedEmotion = eds.getEmotion(noteset.getEmotion());
                relatedLabel = lds.getLabel(relatedEmotion.getLabelId());
                NotesetAndRelated notesetAndRelated = new NotesetAndRelated();
                notesetAndRelated.setEmotion(relatedEmotion);
                notesetAndRelated.setLabel(relatedLabel);
                notesetAndRelated.setNoteset(noteset);
                notesetAndRelated.setNotes(relatedNotes);
                allNotesetsAndNotes.add(notesetAndRelated);
            }

            // pass list data to adapter
            adapter = new NotesetAdapter(this, allNotesetsAndNotes);

            listView.setAdapter(adapter);
            listView.setOnScrollListener(new AbsListView.OnScrollListener() {

                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {
                    // TODO Auto-generated method stub
                }

                @Override
                public void onScroll(AbsListView view, int firstVisibleItem,
                        int visibleItemCount, int totalItemCount) {
                    try {
                        // if we've reached the end of the visible list, get more items (if
                        // available) ... also note: the list header takes up 1 row, so we need to
                        // subtract 2 to get an accurate measurement of our current last row
                        // TODO: fix tablet issue...
                        if ((getListView().getLastVisiblePosition() == adapter.getCount() - 2)
                                && (getListView().getChildAt(getListView().getChildCount() - 2)
                                        .getBottom() <= getListView().getHeight())) {

                            // get more items for list
                            currentOffset += limit;
                            addToList();
                        }
                    } catch (Exception e) {
                        Log.d("MYLOG", e.getStackTrace().toString());
                    }
                }
            });

            // get individual noteset details
            listView.setOnItemClickListener(new OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view,
                        int position, long id) {

                    // the list header row takes up one row space... make sure to subtract it first
                    position = position - 1;

                    // launch details activity
                    Intent intent = new Intent(view.getContext(),
                            ViewNotesetDetailActivity.class);

                    intent.putExtra("list_id", adapter.getItemId(position));
                    startActivity(intent);
                }
            });

            // register context menu
            registerForContextMenu(listView);
        } else {
            // no more rows to load
            doneLoading = true;
        }

        eds.close();
        lds.close();
        nsds.close();
        nds.close();
    }

    public void addToList() {
        Emotion relatedEmotion;
        Label relatedLabel;
        List<Noteset> allNotesets = new LinkedList<Noteset>();
        List<Note> relatedNotes = new LinkedList<Note>();

        EmotionsDataSource eds = new EmotionsDataSource(this);
        LabelsDataSource lds = new LabelsDataSource(this);
        NotesetsDataSource nsds = new NotesetsDataSource(this);
        NotesDataSource nds = new NotesDataSource(this);

        totalNotesetsAvailable = nsds.getCount(apprenticeId);

        if (currentOffset <= totalNotesetsAvailable) {
            allNotesets = nsds.getAllNotesets(apprenticeId, limit, currentOffset);

            for (Noteset noteset : allNotesets) {
                relatedNotes = nds.getAllNotesByNotesetId(noteset.getId());
                relatedEmotion = eds.getEmotion(noteset.getEmotion());
                relatedLabel = lds.getLabel(relatedEmotion.getLabelId());
                NotesetAndRelated notesetAndRelated = new NotesetAndRelated();
                notesetAndRelated.setEmotion(relatedEmotion);
                notesetAndRelated.setLabel(relatedLabel);
                notesetAndRelated.setNoteset(noteset);
                notesetAndRelated.setNotes(relatedNotes);
                // allNotesetsAndNotes.add(notesetAndRelated);
                adapter.addItem(notesetAndRelated);
            }

            adapter.notifyDataSetChanged();
        } else {
            // no more rows to load
        }

        eds.close();
        lds.close();
        nsds.close();
        nds.close();
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
                // go ahead and delete noteset

                // get noteset id to delete (from chosen item in list)
                Noteset notesetToDelete = allNotesetsAndNotes.get(selectedPositionInList)
                        .getNoteset();

                deleteNoteset(notesetToDelete);

                Context context = getApplicationContext();
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context,
                        context.getResources().getString(R.string.noteset_deleted),
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

    public void deleteNoteset(Noteset noteset) {
        NotesetsDataSource nds = new NotesetsDataSource(this);
        nds.deleteNoteset(noteset);
        nds.close();
    }

    @Override
    public void onResume() {
        super.onResume();

        // refresh list
        currentOffset = 0;
        adapter.clear();
        adapter.notifyDataSetChanged();
        fillList();
    }
}
