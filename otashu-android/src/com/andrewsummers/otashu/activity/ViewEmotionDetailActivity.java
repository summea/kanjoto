package com.andrewsummers.otashu.activity;

import java.util.LinkedList;
import java.util.List;

import com.andrewsummers.otashu.R;
import com.andrewsummers.otashu.data.EmotionsDataSource;
import com.andrewsummers.otashu.data.LabelsDataSource;
import com.andrewsummers.otashu.model.Emotion;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

/**
 * View details of a particular emotion.
 */
public class ViewEmotionDetailActivity extends Activity {
    
    private int emotionId = 0;
    
    /**
     * onCreate override used to get details view.
     * 
     * @param savedInstanceState
     *            Current application state data.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // get specific layout for content view
        setContentView(R.layout.activity_view_emotion_detail);
        
        Log.d("MYLOG", "got list item id: " + getIntent().getExtras().getLong("list_id"));
        emotionId = (int) getIntent().getExtras().getLong("list_id");
        
        List<Long> allEmotionsData = new LinkedList<Long>();
        EmotionsDataSource ds = new EmotionsDataSource(this);

        allEmotionsData = ds.getAllEmotionListDBTableIds();

        // prevent crashes due to lack of database data
        if (allEmotionsData.isEmpty())
            allEmotionsData.add((long) 0);

        
        Long[] allEmotions = allEmotionsData
                .toArray(new Long[allEmotionsData.size()]);
        
        Emotion emotion = new Emotion();
        emotion = ds.getEmotion(allEmotions[emotionId]);
        
        ds.close();
        LabelsDataSource lds = new LabelsDataSource(this);

        TextView emotionName = (TextView) findViewById(R.id.emotion_detail_name_value);
        emotionName.setText(emotion.getName());
        
        TextView emotionLabel = (TextView) findViewById(R.id.emotion_detail_label_value);
        emotionLabel.setText(lds.getLabel(emotion.getLabelId()).getName());
    }
}