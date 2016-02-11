
package com.andrewsummers.otashu.adapter;

import java.util.List;

import com.andrewsummers.otashu.R;
import com.andrewsummers.otashu.model.NotevalueAndRelated;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class NotevalueAdapter extends BaseAdapter {

    private Context mContext;
    private List<NotevalueAndRelated> notevaluesAndRelated;

    public NotevalueAdapter(Context context, List<NotevalueAndRelated> allNotevaluesAndRelated) {
        mContext = context;
        notevaluesAndRelated = allNotevaluesAndRelated;
    }

    @Override
    public int getCount() {
        return notevaluesAndRelated.size();
    }

    @Override
    public Object getItem(int position) {
        return notevaluesAndRelated.get(position);
    }

    @Override
    public long getItemId(int position) {
        return notevaluesAndRelated.get(position).getNotevalue().getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = ((LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(
                    R.layout.row_notevalue, null);
        }

        TextView notevalue = (TextView) convertView.findViewById(R.id.notevalue);
        notevalue.setText(notevaluesAndRelated.get(position).getNotevalue().getNotelabel());

        // set relevant background color, if available
        if (notevaluesAndRelated.get(position).getLabel().getColor() != null) {
            notevalue.setBackgroundColor(Color.parseColor(notevaluesAndRelated.get(position)
                    .getLabel().getColor()));
        }

        return convertView;
    }

    public Object removeItem(int position) {
        return notevaluesAndRelated.remove(position);
    }

    public void clear() {
        notevaluesAndRelated.clear();
    }
}
