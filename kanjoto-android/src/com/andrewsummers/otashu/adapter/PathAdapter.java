
package com.andrewsummers.otashu.adapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import com.andrewsummers.otashu.R;
import com.andrewsummers.otashu.data.EmotionsDataSource;
import com.andrewsummers.otashu.data.LabelsDataSource;
import com.andrewsummers.otashu.data.NotevaluesDataSource;
import com.andrewsummers.otashu.model.Emotion;
import com.andrewsummers.otashu.model.Label;
import com.andrewsummers.otashu.model.Notevalue;
import com.andrewsummers.otashu.model.Path;
import com.andrewsummers.otashu.model.PathAndRelated;
import com.andrewsummers.otashu.model.PathEdge;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.StateListDrawable;
import android.preference.PreferenceManager;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class PathAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<PathAndRelated> paths;
    private SharedPreferences sharedPref;
    private long apprenticeId = 0;
    SparseArray<Notevalue> notevalues = new SparseArray<Notevalue>();
    SparseArray<Label> labels = new SparseArray<Label>();

    public PathAdapter(Context context, ArrayList<PathAndRelated> topPaths) {
        mContext = context;
        paths = topPaths;

        sharedPref = PreferenceManager.getDefaultSharedPreferences(mContext);
        apprenticeId = Long.parseLong(sharedPref.getString(
                "pref_selected_apprentice", "1"));

        NotevaluesDataSource nvds = new NotevaluesDataSource(mContext);
        List<Notevalue> allNotevalues = nvds.getAllNotevalues();
        nvds.close();

        for (Notevalue notevalue : allNotevalues) {
            notevalues.put(notevalue.getNotevalue(), notevalue);
        }

        LabelsDataSource lds = new LabelsDataSource(mContext);
        List<Label> allLabels = lds.getAllLabels();
        lds.close();

        for (Label label : allLabels) {
            labels.put((int) label.getId(), label);
        }
    }

    @Override
    public int getCount() {
        return paths.size();
    }

    @Override
    public Object getItem(int position) {
        return paths.get(position);
    }

    @Override
    public long getItemId(int position) {
        return paths.get(position).getPath().getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = ((LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(
                    R.layout.row_noteset, null);
        }

        // TODO: keep all path edges in same row
        try {
            EmotionsDataSource eds = new EmotionsDataSource(mContext);
            List<Emotion> allEmotions = eds.getAllEmotions(apprenticeId);
            eds.close();

            HashMap<Long, Emotion> allEmotionsMap = new HashMap<Long, Emotion>();
            for (Emotion emotion : allEmotions) {
                allEmotionsMap.put(emotion.getId(), emotion);
            }

            LabelsDataSource lds = new LabelsDataSource(mContext);
            List<Label> allLabels = lds.getAllLabels();
            lds.close();

            HashMap<Long, Label> allLabelsMap = new HashMap<Long, Label>();
            for (Label label : allLabels) {
                allLabelsMap.put(label.getId(), label);
            }

            TextView emotion = (TextView) convertView.findViewById(R.id.emotion);
            emotion.setText(allEmotionsMap.get(
                    paths.get(position).getPathEdge().get(0).getEmotionId()).getName()
                    + "");

            String backgroundColor = "#dddddd";

            // TODO: get correct path data... might have to rethink the Path cache, though
            // because how should each path be grouped when paths are made up of individual
            // entries...? just needed a place to cache path info, but this is getting
            // unnecessarily complex...

            // TODO: refactor this in the future...
            // get background color of related label
            // TODO: make dynamic
            if (allLabelsMap.get(
                    allEmotionsMap.get(paths.get(position).getPathEdge().get(0).getEmotionId())
                            .getLabelId()).getColor() != null) {
                backgroundColor = allLabelsMap.get(
                        allEmotionsMap.get(paths.get(position).getPathEdge().get(0).getEmotionId())
                                .getLabelId()).getColor();
            }

            // add correct color to background (but maintain default state "pressed" and "selected"
            // effects)
            StateListDrawable drawable = new StateListDrawable();
            drawable.addState(new int[] {
                    android.R.attr.state_pressed
            }, mContext.getResources().getDrawable(R.drawable.row_selector));
            drawable.addState(new int[] {
                    android.R.attr.state_selected
            }, mContext.getResources().getDrawable(R.drawable.row_selector));
            drawable.addState(new int[] {}, new ColorDrawable(Color.parseColor(backgroundColor)));
            emotion.setBackground(drawable);

            int[] noteItems = {
                    R.id.note_1, R.id.note_2, R.id.note_3, R.id.note_4
            };
            TextView note = null;

            // fill in note names for each note in each row of this custom list
            for (int i = 0; i < noteItems.length - 1; i++) {
                note = (TextView) convertView.findViewById(noteItems[i]);

                note.setText(notevalues.get(
                        paths.get(position).getPathEdge().get(i).getFromNodeId())
                        .getNotelabel());

                backgroundColor = "#dddddd";

                // TODO: refactor this in the future...

                // get background color of related label
                if (allLabelsMap
                        .get(notevalues.get(
                                paths.get(position).getPathEdge().get(i).getFromNodeId())
                                .getLabelId()).getColor() != null) {
                    backgroundColor = allLabelsMap.get(
                            notevalues
                                    .get(paths.get(position).getPathEdge().get(i).getFromNodeId())
                                    .getLabelId()).getColor();
                }

                drawable = new StateListDrawable();
                drawable.addState(new int[] {
                        android.R.attr.state_pressed
                }, mContext.getResources().getDrawable(R.drawable.row_selector));
                drawable.addState(new int[] {
                        android.R.attr.state_selected
                }, mContext.getResources().getDrawable(R.drawable.row_selector));
                drawable.addState(new int[] {},
                        new ColorDrawable(Color.parseColor(backgroundColor)));
                note.setBackground(drawable);

                // make sure to add last note (because of the "from" and "to" differences)
                if (i == noteItems.length - 2) {

                    note = (TextView) convertView.findViewById(noteItems[i + 1]);

                    note.setText(notevalues.get(
                            paths.get(position).getPathEdge().get(i).getToNodeId())
                            .getNotelabel());

                    backgroundColor = "#dddddd";

                    if (allLabelsMap
                            .get(notevalues.get(
                                    paths.get(position).getPathEdge().get(i).getToNodeId())
                                    .getLabelId()).getColor() != null) {
                        backgroundColor = allLabelsMap.get(
                                notevalues.get(
                                        paths.get(position).getPathEdge().get(i).getToNodeId())
                                        .getLabelId()).getColor();
                    }

                    drawable = new StateListDrawable();
                    drawable.addState(new int[] {
                            android.R.attr.state_pressed
                    }, mContext.getResources().getDrawable(R.drawable.row_selector));
                    drawable.addState(new int[] {
                            android.R.attr.state_selected
                    }, mContext.getResources().getDrawable(R.drawable.row_selector));
                    drawable.addState(new int[] {},
                            new ColorDrawable(Color.parseColor(backgroundColor)));
                    note.setBackground(drawable);

                }
            }
        } catch (Exception e) {
            Log.d("MYLOG", e.getStackTrace().toString());
        }

        return convertView;
    }

    public String getNoteName(int noteValue) {
        String[] noteNamesArray = mContext.getResources().getStringArray(R.array.note_labels_array);
        String[] noteValuesArray = mContext.getResources()
                .getStringArray(R.array.note_values_array);

        // get array index position of note value (so we can get correct note name later)
        int noteIndex = Arrays.asList(noteValuesArray).indexOf(String.valueOf(noteValue));

        // return correct note name from note names array
        return noteNamesArray[noteIndex];
    }

    public void addItem(Long pathId, ArrayList<PathEdge> pathEdges) {
        PathAndRelated par = new PathAndRelated();
        Path path = new Path();
        path.setId(pathId);
        par.setPath(path);
        par.setPathEdge(pathEdges);
        paths.add(par);
    }

    public Object removeItem(int position) {
        return paths.remove(position);
    }

    public void clear() {
        paths.clear();
    }
}
