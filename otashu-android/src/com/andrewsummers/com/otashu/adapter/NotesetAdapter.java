package com.andrewsummers.com.otashu.adapter;

import java.util.Arrays;
import java.util.List;

import com.andrewsummers.otashu.R;
import com.andrewsummers.otashu.model.NotesetAndRelated;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class NotesetAdapter extends BaseAdapter {

    private Context mContext;
    private List<NotesetAndRelated> notesetsAndNotes;
    
    public NotesetAdapter(Context context, List<NotesetAndRelated> allNotesetsAndNotes) {
        mContext = context;
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
        
        TextView emotion = (TextView) convertView.findViewById(R.id.emotion);
        emotion.setText(notesetsAndNotes.get(position).getEmotion().getName());
        
        int[] noteItems = {R.id.note_1, R.id.note_2, R.id.note_3, R.id.note_4};
        TextView note = null;

        // fill in note names for each note in each row of this custom list
        for (int i = 0; i < noteItems.length; i++) {
            note = (TextView) convertView.findViewById(noteItems[i]);
            note.setText(getNoteName(notesetsAndNotes.get(position).getNotes().get(i).getNotevalue()));
        }

        return convertView;
    }
    
    public String getNoteName(int noteValue) {
        String[] noteNamesArray = mContext.getResources().getStringArray(R.array.note_labels_array);
        String[] noteValuesArray = mContext.getResources().getStringArray(R.array.note_values_array);
        
        // get array index position of note value (so we can get correct note name later)
        int noteIndex = Arrays.asList(noteValuesArray).indexOf(String.valueOf(noteValue));

        // return correct note name from note names array
        return noteNamesArray[noteIndex];
    }
}