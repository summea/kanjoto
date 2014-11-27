package com.andrewsummers.otashu.adapter;

import java.util.Arrays;
import java.util.List;

import com.andrewsummers.otashu.R;
import com.andrewsummers.otashu.model.NotesetAndRelated;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class NotesetAdapter extends BaseAdapter {

    private Context mContext;
    private List<NotesetAndRelated> notesetsAndRelated;
    
    public NotesetAdapter(Context context, List<NotesetAndRelated> allNotesetsAndNotes) {
        mContext = context;
        notesetsAndRelated = allNotesetsAndNotes;
    }
    
    @Override
    public int getCount() {
        return notesetsAndRelated.size();
    }

    @Override
    public Object getItem(int position) {
        return notesetsAndRelated.get(position);
    }

    @Override
    public long getItemId(int position) {
        return notesetsAndRelated.get(position).getNoteset().getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {        
        if (convertView == null) {
            convertView = ((LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.row_noteset, null);
        }
        
        TextView emotion = (TextView) convertView.findViewById(R.id.emotion);
        emotion.setText(notesetsAndRelated.get(position).getEmotion().getName());
        if (notesetsAndRelated.get(position).getLabel().getColor() != null) {
            emotion.setBackgroundColor(Color.parseColor(notesetsAndRelated.get(position).getLabel().getColor()));
        } else {
            emotion.setBackgroundColor(Color.parseColor("#dddddd"));
        }
        
        int[] noteItems = {R.id.note_1, R.id.note_2, R.id.note_3, R.id.note_4};
        TextView note = null;

        // fill in note names for each note in each row of this custom list
        for (int i = 0; i < noteItems.length; i++) {
            note = (TextView) convertView.findViewById(noteItems[i]);
            note.setText(getNoteName(notesetsAndRelated.get(position).getNotes().get(i).getNotevalue()));
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
    
    public void addItem(NotesetAndRelated notesetAndRelatedToBeAdded) {
        notesetsAndRelated.add(notesetAndRelatedToBeAdded);
    }
    
    public Object removeItem(int position) {
        return notesetsAndRelated.remove(position);
    }

    public void clear() {
        notesetsAndRelated.clear();
    }
}