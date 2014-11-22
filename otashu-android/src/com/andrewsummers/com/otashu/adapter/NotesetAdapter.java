package com.andrewsummers.com.otashu.adapter;

import java.util.List;

import com.andrewsummers.otashu.R;
import com.andrewsummers.otashu.model.NotesetNote;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class NotesetAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater inflater;
    private List<NotesetNote> notesetsAndNotes;
    
    public NotesetAdapter(Context context, List<NotesetNote> allNotesetsAndNotes) {
        mContext = context;
        inflater = LayoutInflater.from(context);
        notesetsAndNotes = allNotesetsAndNotes;
    }
    
    @Override
    public int getCount() {
        return notesetsAndNotes.size();
    }

    @Override
    public Object getItem(int position) {
        return notesetsAndNotes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return notesetsAndNotes.get(position).getNoteset().getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {        
        if (convertView == null) {
            convertView = ((LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.row_noteset, null);
        }
        TextView note1 = (TextView) convertView.findViewById(R.id.note_1);
        note1.setText(Long.toString(notesetsAndNotes.get(position).getNotes().get(0).getNotevalue()));
        TextView note2 = (TextView) convertView.findViewById(R.id.note_2);
        note2.setText(Long.toString(notesetsAndNotes.get(position).getNotes().get(1).getNotevalue()));
        TextView note3 = (TextView) convertView.findViewById(R.id.note_3);
        note3.setText(Long.toString(notesetsAndNotes.get(position).getNotes().get(2).getNotevalue()));
        TextView note4 = (TextView) convertView.findViewById(R.id.note_4);
        note4.setText(Long.toString(notesetsAndNotes.get(position).getNotes().get(3).getNotevalue()));
        
        return convertView;
    }
}