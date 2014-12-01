package com.andrewsummers.otashu.adapter;

import java.util.List;

import com.andrewsummers.otashu.R;
import com.andrewsummers.otashu.model.NotevalueLabel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class NotevalueLabelAdapter extends BaseAdapter {

    private Context mContext;
    private List<NotevalueLabel> noteLabels;
    
    public NotevalueLabelAdapter(Context context, List<NotevalueLabel> allNoteLabelsAndRelated) {
        mContext = context;
        noteLabels = allNoteLabelsAndRelated;
    }
    
    @Override
    public int getCount() {
        return noteLabels.size();
    }

    @Override
    public Object getItem(int position) {
        return noteLabels.get(position);
    }

    @Override
    public long getItemId(int position) {
        return noteLabels.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {        
        if (convertView == null) {
            convertView = ((LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.row_notevalue_label, null);
        }
        
        TextView noteLabel = (TextView) convertView.findViewById(R.id.note_label);
        noteLabel.setText(noteLabels.get(position).getNotevalue());

        return convertView;
    }
    
    public Object removeItem(int position) {
        return noteLabels.remove(position);
    }

    public void clear() {
        noteLabels.clear();
    }
}