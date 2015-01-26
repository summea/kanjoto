
package com.andrewsummers.otashu.adapter;

import java.util.Arrays;
import java.util.List;

import com.andrewsummers.otashu.R;
import com.andrewsummers.otashu.data.LabelsDataSource;
import com.andrewsummers.otashu.data.NotevaluesDataSource;
import com.andrewsummers.otashu.model.Label;
import com.andrewsummers.otashu.model.Notevalue;
import com.andrewsummers.otashu.model.Path;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.StateListDrawable;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class PathAdapter extends BaseAdapter {

    private Context mContext;
    private List<Path> paths;
    SparseArray<Notevalue> notevalues = new SparseArray<Notevalue>();
    SparseArray<Label> labels = new SparseArray<Label>();

    public PathAdapter(Context context, List<Path> allPaths) {
        mContext = context;
        paths = allPaths;

        Log.d("MYLOG", "all paths: " + allPaths.toString());
        
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
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = ((LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(
                    R.layout.row_noteset, null);
        }

        TextView emotion = (TextView) convertView.findViewById(R.id.emotion);
        Log.d("MYLOG", "> get path data: " + paths.get(position).toString());
        emotion.setText(paths.get(position).getPath().get(0).getEmotionId() + "");

        String backgroundColor = "#dddddd";
        /*
        if (path.get(position).getLabel().getColor() != null) {
            backgroundColor = path.get(position).getLabel().getColor();
        }

        if (path.get(position).getNoteset().getEnabled() == 0) {
            backgroundColor = "#e8e8e8";
        }
        */

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

        Log.d("MYLOG", "note items length: " + noteItems.length + "");
        
        // fill in note names for each note in each row of this custom list
        for (int i = 0; i < noteItems.length-1; i++) {
            note = (TextView) convertView.findViewById(noteItems[i]);
            // note.setText(getNoteName(path.get(position).getNotes().get(i).getNotevalue()));
            
            note.setText(notevalues.get(
                    paths.get(position).getPath().get(i).getFromNodeId())
                    .getNotelabel());

            backgroundColor = "#dddddd";
            /*
            if (labels.get(
                    (int) notevalues.get(
                            paths.get(position).getNotes().get(i).getNotevalue())
                            .getLabelId()).getColor() != null)
                backgroundColor = labels.get(
                        (int) notevalues.get(
                                paths.get(position).getNotes().get(i).getNotevalue())
                                .getLabelId()).getColor();

            if (paths.get(position).getNoteset().getEnabled() == 0) {
                backgroundColor = "#e8e8e8";
            }
            */

            drawable = new StateListDrawable();
            drawable.addState(new int[] {
                    android.R.attr.state_pressed
            }, mContext.getResources().getDrawable(R.drawable.row_selector));
            drawable.addState(new int[] {
                    android.R.attr.state_selected
            }, mContext.getResources().getDrawable(R.drawable.row_selector));
            drawable.addState(new int[] {}, new ColorDrawable(Color.parseColor(backgroundColor)));
            note.setBackground(drawable);

            // make sure to add last note (because of the "from" and "to" differences)
            if (i == noteItems.length-2) {
            
                note = (TextView) convertView.findViewById(noteItems[i+1]);
                // note.setText(getNoteName(path.get(position).getNotes().get(i).getNotevalue()));
                
                note.setText(notevalues.get(
                        paths.get(position).getPath().get(i).getToNodeId())
                        .getNotelabel());

                backgroundColor = "#dddddd";
                /*
                if (labels.get(
                        (int) notevalues.get(
                                paths.get(position).getNotes().get(i).getNotevalue())
                                .getLabelId()).getColor() != null)
                    backgroundColor = labels.get(
                            (int) notevalues.get(
                                    paths.get(position).getNotes().get(i).getNotevalue())
                                    .getLabelId()).getColor();

                if (paths.get(position).getNoteset().getEnabled() == 0) {
                    backgroundColor = "#e8e8e8";
                }
                */

                drawable = new StateListDrawable();
                drawable.addState(new int[] {
                        android.R.attr.state_pressed
                }, mContext.getResources().getDrawable(R.drawable.row_selector));
                drawable.addState(new int[] {
                        android.R.attr.state_selected
                }, mContext.getResources().getDrawable(R.drawable.row_selector));
                drawable.addState(new int[] {}, new ColorDrawable(Color.parseColor(backgroundColor)));
                note.setBackground(drawable);
            
            }
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

    public void addItem(Path pathToBeAdded) {
        paths.add(pathToBeAdded);
    }

    public Object removeItem(int position) {
        return paths.remove(position);
    }

    public void clear() {
        paths.clear();
    }
}
