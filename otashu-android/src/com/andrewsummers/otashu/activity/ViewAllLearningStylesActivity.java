
package com.andrewsummers.otashu.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.andrewsummers.otashu.R;
import com.andrewsummers.otashu.adapter.LearningStyleAdapter;
import com.andrewsummers.otashu.data.LearningStylesDataSource;
import com.andrewsummers.otashu.model.LearningStyle;
import com.andrewsummers.otashu.model.Note;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
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
 * View all LearningStyles as a list.
 * <p>
 * This activity allows a user to view a list of all saved LearningStyles.
 * </p>
 */
public class ViewAllLearningStylesActivity extends ListActivity {
    private ListView listView = null;
    private int selectedPositionInList = 0;
    private LearningStyleAdapter adapter = null;

    /**
     * onCreate override used to gather and display a list of all learningStyles saved in database.
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
        headerText.setText(R.string.view_all_learning_styles_list_header);
        listView.addHeaderView(listHeader, "", false);

        fillList();
    }

    public void fillList() {
        List<LearningStyle> allLearningStyles = new LinkedList<LearningStyle>();
        LearningStylesDataSource lsds = new LearningStylesDataSource(this);
        allLearningStyles = lsds.getAllLearningStyles();
        lsds.close();

        // pass list data to adapter
        adapter = new LearningStyleAdapter(this, allLearningStyles);

        listView.setAdapter(adapter);

        // get individual learningStyle details
        listView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {

                // launch details activity
                Intent intent = new Intent(view.getContext(),
                        ViewLearningStyleDetailActivity.class);

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
        inflater.inflate(R.menu.menu_learning_styles, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = null;

        // handle menu item selection
        switch (item.getItemId()) {
            case R.id.view_settings:
                intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
            case R.id.create_learning_style:
                intent = new Intent(this, CreateLearningStyleActivity.class);
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
        inflater.inflate(R.menu.context_menu_learning_style, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
        Intent intent = null;
        switch (item.getItemId()) {
            case R.id.context_menu_view:
                intent = new Intent(this, ViewLearningStyleDetailActivity.class);
                intent.putExtra("list_id", info.id);
                startActivity(intent);
                return true;
            case R.id.context_menu_edit:
                intent = new Intent(this, EditLearningStyleActivity.class);
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
                // go ahead and delete learningStyle

                // get correct learningStyle id to delete
                LearningStyle learningStyleToDelete = getLearningStyleFromListPosition(selectedPositionInList);

                deleteLearningStyle(learningStyleToDelete);

                Context context = getApplicationContext();
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context,
                        context.getResources().getString(R.string.learning_style_deleted),
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

    public LearningStyle getLearningStyleFromListPosition(long rowId) {
        long learningStyleId = rowId;

        List<Long> allLearningStylesData = new LinkedList<Long>();
        LearningStylesDataSource lsds = new LearningStylesDataSource(this);

        // get string version of returned learningStyle list
        allLearningStylesData = lsds.getAllLearningStyleListDBTableIds();
        lsds.close();

        // prevent crashes due to lack of database data
        if (allLearningStylesData.isEmpty())
            allLearningStylesData.add((long) 0);

        Long[] allLearningStyles = allLearningStylesData
                .toArray(new Long[allLearningStylesData.size()]);

        LearningStyle learningStyle = lsds
                .getLearningStyle(allLearningStyles[(int) learningStyleId]);
        lsds.close();

        return learningStyle;
    }

    public void deleteLearningStyle(LearningStyle learningStyle) {
        LearningStylesDataSource lsds = new LearningStylesDataSource(this);
        lsds.deleteLearningStyle(learningStyle);
        lsds.close();
    }

    @Override
    public void onResume() {
        super.onResume();

        // refresh list
        adapter.clear();
        fillList();
    }
}
