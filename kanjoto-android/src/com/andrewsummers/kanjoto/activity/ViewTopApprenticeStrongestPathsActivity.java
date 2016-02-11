
package com.andrewsummers.kanjoto.activity;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import com.andrewsummers.kanjoto.R;
import com.andrewsummers.kanjoto.adapter.PathAdapter;
import com.andrewsummers.kanjoto.data.EdgesDataSource;
import com.andrewsummers.kanjoto.data.EmotionsDataSource;
import com.andrewsummers.kanjoto.data.PathEdgesDataSource;
import com.andrewsummers.kanjoto.data.PathsDataSource;
import com.andrewsummers.kanjoto.model.Edge;
import com.andrewsummers.kanjoto.model.Emotion;
import com.andrewsummers.kanjoto.model.Path;
import com.andrewsummers.kanjoto.model.PathAndRelated;
import com.andrewsummers.kanjoto.model.PathEdge;

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
    private ArrayList<PathAndRelated> topPaths = new ArrayList<PathAndRelated>();
    private int resetAutoIncrementLimit = 1000;
    private long topApprenticePathsLastCachedAt = 0;
    private boolean useCache = true;

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
        topApprenticePathsLastCachedAt = Long.parseLong(sharedPref.getString(
                "pref_top_apprentice_paths_last_cached_at", "0"));

        // 1000000000 nanoseconds = 1 second
        long seconds = 30;
        if (topApprenticePathsLastCachedAt <= 0) {
            topApprenticePathsLastCachedAt = System.nanoTime();
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("pref_top_apprentice_paths_last_cached_at",
                    Long.toString(topApprenticePathsLastCachedAt));
            editor.apply();
            useCache = false;
        }

        // check if cache is expired
        if (System.nanoTime() > (topApprenticePathsLastCachedAt + (1000000000L * seconds))) {
            // update cached-at time
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("pref_top_apprentice_paths_last_cached_at",
                    Long.toString(System.nanoTime()));
            editor.apply();
            useCache = false;
            Log.d("MYLOG", "cache expired... time to refresh");
        } else {
            useCache = true;
        }

        // initialize ListView
        listView = getListView();

        // set title for list activity
        ViewGroup listHeader = (ViewGroup) getLayoutInflater().inflate(R.layout.list_header,
                listView, false);
        TextView headerText = (TextView) listHeader.findViewById(R.id.list_header_title);
        headerText.setText(R.string.top_apprentice_strongest_paths_list_header);
        listView.addHeaderView(listHeader, "", false);

        if (!useCache) {
            // update Paths table in database first time around
            updatePathsTable();
        } else {
            // use cached data
            fillList();
        }
    }

    public void updatePathsTable() {
        // use cached data for the next time around
        useCache = true;

        // fill list with Top 3 strongest Apprentice paths (if available)
        topPaths.clear();
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
        PathEdgesDataSource peds = new PathEdgesDataSource(this);

        Path currentLastPath = pds.getLastPath();
        if (currentLastPath.getId() > resetAutoIncrementLimit) {
            pds.resetAutoIncrement();
        }

        PathEdge currentLastPathEdge = peds.getLastPathEdge();
        if (currentLastPathEdge.getId() > resetAutoIncrementLimit) {
            peds.resetAutoIncrement();
        }

        Set<Long> pathIdsToDelete = new TreeSet<Long>();

        List<PathEdge> pathEdgesToDelete = peds.getAllPathEdgesByEmotion(emotionId);
        for (PathEdge pathEdgeToDelete : pathEdgesToDelete) {
            // add path ids that are related to current path edge
            // so that these can be deleted from the `paths` table later
            pathIdsToDelete.add(pathEdgeToDelete.getPathId());
            peds.deletePathEdge(pathEdgeToDelete);
        }

        Path path;

        // delete related paths in `paths` database table
        for (long pathId : pathIdsToDelete) {
            path = new Path();
            path.setId(pathId);
            pds.deletePath(path);
        }

        // get top 3
        // TODO: change back to top 3
        for (int i = 0; i < 1; i++) {
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
                // TODO: get a non-zero id...
                if (bestMatch.size() > 0) {
                    path = new Path();
                    path = pds.createPath(path);

                    PathEdge pathEdge;
                    List<PathEdge> pathEdges = new ArrayList<PathEdge>();
                    PathAndRelated par = new PathAndRelated();
                    par.setPath(path);

                    for (int j = 0; j < bestMatch.size(); j++) {
                        // add current path data into database
                        pathEdge = new PathEdge();
                        pathEdge.setPathId(path.getId());
                        pathEdge.setApprenticeId(apprenticeId);
                        pathEdge.setEmotionId(bestMatch.get(j).getEmotionId());
                        pathEdge.setFromNodeId(bestMatch.get(j).getFromNodeId());
                        pathEdge.setToNodeId(bestMatch.get(j).getToNodeId());
                        pathEdge.setPosition(j + 1);
                        pathEdge.setRank(rank);
                        peds.createPathEdge(pathEdge);

                        // TODO: add to topPaths
                        // ArrayList<PathEdge> al = new ArrayList<PathEdge>();
                        // check if a list exists for current path

                        pathEdges.add(pathEdge);
                    }

                    par.setPathEdge(pathEdges);
                    topPaths.add(par);
                }

                // keep track of what edges have been used already
                for (int j = 0; j < 3; j++) {
                    if (!usedOnce.contains(bestMatch.get(j).getId())) {
                        usedOnce.add(bestMatch.get(j).getId());
                    }
                }
            } catch (Exception e) {
                Log.d("MYLOG", e.getStackTrace().toString());
            }

            pds.close();
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

    public void fillList() {
        // fill list with Top 3 strongest Apprentice paths (if available)
        topPaths.clear();

        List<PathEdge> allPathEdges;
        PathEdgesDataSource peds = new PathEdgesDataSource(this);
        allPathEdges = peds.getAllPathEdgesByEmotion(emotionId);
        peds.close();

        List<PathEdge> pathEdges = new ArrayList<PathEdge>();

        PathAndRelated par = new PathAndRelated();
        pathEdges.clear();

        long lastPathId = 0;
        boolean firstLoop = true;
        for (PathEdge pathEdge : allPathEdges) {
            if (firstLoop) {
                lastPathId = pathEdge.getPathId();
                firstLoop = false;
            }
            if (pathEdge.getPathId() != lastPathId) {
                Path path = new Path();
                path.setId(lastPathId);
                par.setPath(path);
                par.setPathEdge(pathEdges);
            }
            pathEdges.add(pathEdge);
            lastPathId = pathEdge.getPathId();
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
