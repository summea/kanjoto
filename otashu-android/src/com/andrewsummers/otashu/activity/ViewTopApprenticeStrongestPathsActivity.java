
package com.andrewsummers.otashu.activity;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.andrewsummers.otashu.R;
import com.andrewsummers.otashu.adapter.PathAdapter;
import com.andrewsummers.otashu.data.EdgesDataSource;
import com.andrewsummers.otashu.data.EmotionsDataSource;
import com.andrewsummers.otashu.data.PathsDataSource;
import com.andrewsummers.otashu.model.Edge;
import com.andrewsummers.otashu.model.Emotion;
import com.andrewsummers.otashu.model.Path;

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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;

/**
 * View all Apprentice strongest emotion-noteset paths as a list.
 * <p>
 * This activity allows a user to see a list of the strongest emotion-noteset paths that the
 * Apprentice has learned through means of various tests (such as the Emotion Test or Transition
 * Test). This data can be used in a variety of ways, but providing a visual view into the
 * Apprentice's memory allows the user to observe the Apprentice's current strengths.
 * </p>
 */
public class ViewTopApprenticeStrongestPathsActivity extends ListActivity {
    private ListView listView = null;
    private int selectedPositionInList = 0;
    private PathAdapter adapter = null;
    private long emotionId = 0;
    private SharedPreferences sharedPref;
    private long apprenticeId = 0;
    private List<Path> topPaths = new ArrayList<Path>();

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
        emotionId = getIntent().getExtras().getLong("list_id");
        
        // initialize ListView
        listView = getListView();

        // set title for list activity
        ViewGroup listHeader = (ViewGroup) getLayoutInflater().inflate(R.layout.list_header,
                listView, false);
        TextView headerText = (TextView) listHeader.findViewById(R.id.list_header_title);
        headerText.setText(R.string.top_apprentice_strongest_paths_list_header);
        listView.addHeaderView(listHeader, "", false);

        // update Paths table in database first time around
        //updatePathsTable();

        // TODO: then pull from Paths table for the fillList()
        fillList();
    }

    public void updatePathsTable() {
        // fill list with Top 3 strongest Apprentice paths (if available)
        topPaths = new ArrayList<Path>();
        EdgesDataSource eds = new EdgesDataSource(this);

        // select a given graph
        long graphId = 1;        

        // select a given weight limit
        float weightLimit = 0.5f;

        // select an edge position
        int position = 1;

        // select all position one edges for given emotion with given threshold (e.g. all rows that
        // have a weight less than 0.5)
        List<Edge> p1Edges = eds.getAllEdges(apprenticeId, graphId, emotionId, weightLimit,
                position);

        position = 2;
        // select all position two edges for given emotion with given threshold (e.g. all rows that
        // have a weight less than 0.5)
        List<Edge> p2Edges = eds.getAllEdges(apprenticeId, graphId, emotionId, weightLimit,
                position);

        position = 3;
        // select all position three edges for given emotion with given threshold (e.g. all rows
        // that have a weight less than 0.5)
        List<Edge> p3Edges = eds.getAllEdges(apprenticeId, graphId, emotionId, weightLimit,
                position);

        List<Long> usedOnce = new ArrayList<Long>();

        // delete rows that have old emotion data
        PathsDataSource pds = new PathsDataSource(this);
        List<Path> pathsToDelete = pds.getAllPathsByEmotion(emotionId);
        for (Path pathToDelete : pathsToDelete) {
            pds.deletePath(pathToDelete);
            Log.d("MYLOG", "deleting old path for emotion: " + emotionId);
        }

        // get top 3
        for (int i = 0; i < 3; i++) {
            List<Edge> bestMatch = new ArrayList<Edge>();
            boolean edge1To2Match = false;
            boolean edge2To3Match = false;
            // check to see if any of the lowest-weight edges are related nodes (i.e. do they
            // connect in the graph?)
            outerloop: for (Edge edge1 : p1Edges) {
                if (!usedOnce.contains(edge1.getId())) {
                    for (Edge edge2 : p2Edges) {
                        if (!usedOnce.contains(edge2.getId())) {
                            if (edge1.getToNodeId() == edge2.getFromNodeId()) {
                                // edge1 to edge2 match!
                                edge1To2Match = true;
                            }
                            for (Edge edge3 : p3Edges) {
                                if (!usedOnce.contains(edge3.getId())) {
                                    if (edge2.getToNodeId() == edge3.getFromNodeId()) {
                                        // edge2 to edge3 match!
                                        edge2To3Match = true;

                                        if (edge1To2Match && edge2To3Match) {
                                            Log.d("MYLOG", "> cross section match found!");
                                            bestMatch.add(edge1);
                                            bestMatch.add(edge2);
                                            bestMatch.add(edge3);
                                            break outerloop;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // TODO: possibly add other ranks for less-strong paths
            int rank = 1;

            try {
                Path path;
                Log.d("MYLOG", "creating new emotion data");

                for (int j = 0; j < bestMatch.size(); j++) {
                    // add current path data into database
                    path = new Path();
                    path.setApprenticeId(apprenticeId);
                    path.setEmotionId(bestMatch.get(j).getEmotionId());
                    path.setFromNodeId(bestMatch.get(j).getFromNodeId());
                    path.setToNodeId(bestMatch.get(j).getToNodeId());
                    path.setPosition(j + 1);
                    path.setRank(rank);
                    pds.createPath(path);
                }

                // keep track of what edges have been used already
                for (int j = 0; j < 3; j++) {
                    if (!usedOnce.contains(bestMatch.get(j).getId())) {
                        usedOnce.add(bestMatch.get(j).getId());
                    }
                }

                Log.d("MYLOG", usedOnce.toString());
            } catch (Exception e) {
                Log.d("MYLOG", e.getStackTrace().toString());
            }

            pds.close();

            if (!bestMatch.isEmpty()) {
                // add path for list
                Path path = new Path();
                path.setPath(bestMatch);
                Log.d("MYLOG", "adding best match: " + bestMatch.toString());
                topPaths.add(path);
                Log.d("MYLOG", "current state of topPaths: " + topPaths.toString());
            }
        }

        Log.d("MYLOG", "top paths being passed to adapter: " + topPaths.toString());

        // pass list data to adapter
        adapter = new PathAdapter(this, topPaths);

        listView.setAdapter(adapter);

        // get individual emotion details
        listView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {

                // launch details activity
                Intent intent = new Intent(view.getContext(),
                        ViewApprenticeStrongestPathDetailActivity.class);

                intent.putExtra("emotion_id", emotionId);
                intent.putExtra("list_id", id);
                startActivity(intent);
            }
        });

        // register context menu
        registerForContextMenu(listView);
    }

    public void fillList() {
        // fill list with Top 3 strongest Apprentice paths (if available)
        topPaths = new ArrayList<Path>();
        
        /*
        EdgesDataSource eds = new EdgesDataSource(this);

        // select a given graph
        long graphId = 1;

        // select a given emotion
        emotionId = getIntent().getExtras().getLong("list_id");

        // select a given weight limit
        float weightLimit = 0.5f;

        // select an edge position
        int position = 1;

        // select all position one edges for given emotion with given threshold (e.g. all rows that
        // have a weight less than 0.5)
        List<Edge> p1Edges = eds.getAllEdges(apprenticeId, graphId, emotionId, weightLimit,
                position);

        position = 2;
        // select all position two edges for given emotion with given threshold (e.g. all rows that
        // have a weight less than 0.5)
        List<Edge> p2Edges = eds.getAllEdges(apprenticeId, graphId, emotionId, weightLimit,
                position);

        position = 3;
        // select all position three edges for given emotion with given threshold (e.g. all rows
        // that have a weight less than 0.5)
        List<Edge> p3Edges = eds.getAllEdges(apprenticeId, graphId, emotionId, weightLimit,
                position);

        List<Long> usedOnce = new ArrayList<Long>();

        // get top 3
        for (int i = 0; i < 3; i++) {

            List<Edge> bestMatch = new ArrayList<Edge>();

            boolean edge1To2Match = false;
            boolean edge2To3Match = false;
            // check to see if any of the lowest-weight edges are related nodes (i.e. do they
            // connect in the graph?)
            outerloop: for (Edge edge1 : p1Edges) {
                if (!usedOnce.contains(edge1.getId())) {
                    for (Edge edge2 : p2Edges) {
                        if (!usedOnce.contains(edge2.getId())) {
                            if (edge1.getToNodeId() == edge2.getFromNodeId()) {
                                // edge1 to edge2 match!
                                edge1To2Match = true;
                            }
                            for (Edge edge3 : p3Edges) {
                                if (!usedOnce.contains(edge3.getId())) {
                                    if (edge2.getToNodeId() == edge3.getFromNodeId()) {
                                        // edge2 to edge3 match!
                                        edge2To3Match = true;

                                        if (edge1To2Match && edge2To3Match) {
                                            Log.d("MYLOG", "cross section match found!");
                                            bestMatch.add(edge1);
                                            bestMatch.add(edge2);
                                            bestMatch.add(edge3);
                                            break outerloop;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            try {
                // return results
                Log.d("MYLOG", "best match results: " + bestMatch.toString());

                // keep track of what edges have been used already
                for (int j = 0; j < 3; j++) {
                    if (!usedOnce.contains(bestMatch.get(j).getId())) {
                        usedOnce.add(bestMatch.get(j).getId());
                    }
                }

                Log.d("MYLOG", usedOnce.toString());
            } catch (Exception e) {
                Log.d("MYLOG", e.getStackTrace().toString());
            }

            if (!bestMatch.isEmpty()) {
                // add path for list
                Path path = new Path();
                path.setPath(bestMatch);
                Log.d("MYLOG", "adding best match: " + bestMatch.toString());
                topPaths.add(path);
                Log.d("MYLOG", "current state of topPaths: " + topPaths.toString());
            }
        }

        Log.d("MYLOG", "top paths being passed to adapter: " + topPaths.toString());
        */
        
        PathsDataSource pds = new PathsDataSource(this);
        topPaths = pds.getAllPathsByEmotion(emotionId);
        pds.close();
        
        for (Path path : topPaths) {
            Log.d("MYLOG", "top path: " + path.toString());
        }
        
        // pass list data to adapter
        adapter = new PathAdapter(this, topPaths);

        listView.setAdapter(adapter);

        // get individual emotion details
        listView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {

                // launch details activity
                Intent intent = new Intent(view.getContext(),
                        ViewApprenticeStrongestPathDetailActivity.class);

                intent.putExtra("emotion_id", emotionId);
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
                intent = new Intent(this, ViewApprenticeStrongestPathDetailActivity.class);
                intent.putExtra("emotion_id", emotionId);
                intent.putExtra("list_id", info.id);
                startActivity(intent);
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
                // go ahead and delete emotion

                // get correct emotion id to delete
                Emotion emotionToDelete = getEmotionFromListPosition(selectedPositionInList);

                deleteEmotion(emotionToDelete);

                Context context = getApplicationContext();
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context,
                        context.getResources().getString(R.string.emotion_deleted),
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

    public Emotion getEmotionFromListPosition(long rowId) {

        long emotionId = rowId;

        List<Long> allEmotionsData = new LinkedList<Long>();
        EmotionsDataSource eds = new EmotionsDataSource(this);

        // get string version of returned emotion list
        allEmotionsData = eds.getAllEmotionListDBTableIds(apprenticeId);
        eds.close();

        // prevent crashes due to lack of database data
        if (allEmotionsData.isEmpty())
            allEmotionsData.add((long) 0);

        Long[] allEmotions = allEmotionsData
                .toArray(new Long[allEmotionsData.size()]);

        Emotion emotion = eds.getEmotion(allEmotions[(int) emotionId]);
        eds.close();

        return emotion;
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
