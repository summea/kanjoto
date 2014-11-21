package com.andrewsummers.com.otashu.adapter;

import java.util.List;

import com.andrewsummers.otashu.R;
import com.andrewsummers.otashu.model.Noteset;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class NotesetAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private List<Noteset> notesets;
    
    public NotesetAdapter(Context context, List<Noteset> incomingNotesets) {
        inflater = LayoutInflater.from(context);
        notesets = incomingNotesets;
    }
    
    @Override
    public int getCount() {
        return notesets.size();
    }

    @Override
    public Object getItem(int position) {
        return notesets.get(position);
    }

    @Override
    public long getItemId(int position) {
        return notesets.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.row_noteset, parent, false);
            TextView notesetId = (TextView) convertView.findViewById(R.id.noteset_id);
            notesetId.setText(Long.toString(notesets.get(position).getId()));
        }
        return convertView;
    }
}