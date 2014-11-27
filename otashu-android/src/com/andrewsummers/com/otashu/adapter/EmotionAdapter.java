package com.andrewsummers.com.otashu.adapter;

import java.util.List;

import com.andrewsummers.otashu.R;
import com.andrewsummers.otashu.model.EmotionAndRelated;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class EmotionAdapter extends BaseAdapter {

    private Context mContext;
    private List<EmotionAndRelated> emotionsAndRelated;
    
    public EmotionAdapter(Context context, List<EmotionAndRelated> allEmotionsAndRelated) {
        mContext = context;
        emotionsAndRelated = allEmotionsAndRelated;
    }
    
    @Override
    public int getCount() {
        return emotionsAndRelated.size();
    }

    @Override
    public Object getItem(int position) {
        return emotionsAndRelated.get(position);
    }

    @Override
    public long getItemId(int position) {
        return emotionsAndRelated.get(position).getEmotion().getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {        
        if (convertView == null) {
            convertView = ((LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.row_emotion, null);
        }
        
        TextView emotion = (TextView) convertView.findViewById(R.id.emotion);
        emotion.setText(emotionsAndRelated.get(position).getEmotion().getName());
        if (emotionsAndRelated.get(position).getLabel().getColor() != null) {
            emotion.setBackgroundColor(Color.parseColor(emotionsAndRelated.get(position).getLabel().getColor()));
        }

        return convertView;
    }
    
    public Object removeItem(int position) {
        return emotionsAndRelated.remove(position);
    }

    public void clear() {
        emotionsAndRelated.clear();
    }
}