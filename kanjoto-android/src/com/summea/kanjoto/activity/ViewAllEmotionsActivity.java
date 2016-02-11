
package com.summea.kanjoto.activity;

import java.util.LinkedList;
import java.util.List;

import com.summea.kanjoto.R;
import com.summea.kanjoto.adapter.EmotionAdapter;
import com.summea.kanjoto.data.EmotionsDataSource;
import com.summea.kanjoto.data.LabelsDataSource;
import com.summea.kanjoto.data.NotesetsDataSource;
import com.summea.kanjoto.model.Emotion;
import com.summea.kanjoto.model.EmotionAndRelated;
import com.summea.kanjoto.model.Label;
import com.summea.kanjoto.model.Noteset;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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
 * View all Emotions as a list.
 * <p>
 * This activity allows a user to view a list of all saved Emotions.
 * </p>
 */
public class ViewAllEmotionsActivity extends ListActivity {
    private ListView listView = null;
    private EmotionAdapter adapter = null;
    private SharedPreferences sharedPref;
    private long apprenticeId = 0;

    /**
     * onCreate override used to gather and display a list of all emotions saved in database.
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
        headerText.setText(R.string.view_all_emotions_list_header);
        listView.addHeaderView(listHeader, "", false);

        fillList();
    }

    public void fillList() {
        Label relatedLabel = new Label();
        List<Emotion> allEmotions = new LinkedList<Emotion>();
        List<EmotionAndRelated> allEmotionsAndRelated = new LinkedList<EmotionAndRelated>();

        EmotionsDataSource eds = new EmotionsDataSource(this);
        LabelsDataSource lds = new LabelsDataSource(this);

        allEmotions = eds.getAllEmotions(apprenticeId);

        for (Emotion emotion : allEmotions) {
            relatedLabel = lds.getLabel(emotion.getLabelId());
            EmotionAndRelated emotionAndRelated = new EmotionAndRelated();
            emotionAndRelated.setEmotion(emotion);
            emotionAndRelated.setLabel(relatedLabel);
            allEmotionsAndRelated.add(emotionAndRelated);
        }

        eds.close();
        lds.close();

        // pass list data to adapter
        adapter = new EmotionAdapter(this, allEmotionsAndRelated);

        listView.setAdapter(adapter);

        // get individual emotion details
        listView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {

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
                confirmDelete(info);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    public void confirmDelete(final AdapterContextMenuInfo info) {
        final EmotionsDataSource eds = new EmotionsDataSource(this);
        final NotesetsDataSource nds = new NotesetsDataSource(this);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.dialog_confirm_delete_message).setTitle(
                R.string.dialog_confirm_delete_title);
        builder.setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // user clicked ok, so go ahead and delete emotion
                // get correct emotion id to delete
                Emotion emotionToDelete = eds.getEmotion(info.id);
                deleteEmotion(emotionToDelete);

                // also, delete related notesets and notes
                List<Noteset> relatedNotesets = nds.getAllNotesetsByEmotion(info.id);
                for (Noteset noteset : relatedNotesets) {
                    nds.deleteNoteset(noteset);
                }

                Context context = getApplicationContext();
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context,
                        context.getResources().getString(R.string.emotion_deleted),
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
        eds.close();
    }

    public void deleteEmotion(Emotion emotion) {
        EmotionsDataSource eds = new EmotionsDataSource(this);
        eds.deleteEmotion(emotion);
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
