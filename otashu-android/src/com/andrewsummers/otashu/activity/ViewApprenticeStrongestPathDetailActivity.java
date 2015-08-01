
package com.andrewsummers.otashu.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.TextView;

import com.andrewsummers.otashu.R;
import com.andrewsummers.otashu.data.EdgesDataSource;
import com.andrewsummers.otashu.data.EmotionsDataSource;
import com.andrewsummers.otashu.data.LabelsDataSource;
import com.andrewsummers.otashu.data.PathEdgesDataSource;
import com.andrewsummers.otashu.data.PathsDataSource;
import com.andrewsummers.otashu.model.Edge;
import com.andrewsummers.otashu.model.Emotion;
import com.andrewsummers.otashu.model.Label;
import com.andrewsummers.otashu.model.Path;
import com.andrewsummers.otashu.model.PathEdge;

/**
 * View details of a particular Apprentice strongest path.
 * <p>
 * As the Apprentice learns more about which notesets fit well with certain emotions, this
 * information is saved in a graph in the database. As this data is "learned" by the Apprentice, the
 * Apprentice becomes more confident in making an educated guess towards certain noteset-emotion
 * combinations. This confidence comes from lower weights for particular edges in the graph (the
 * lower the weight, the less effort it takes to follow that path for the Apprentice when making
 * decisions). This activity displays the strongest paths (i.e. the paths with the lowest weights)
 * learned up to this point by the Apprentice.
 * </p>
 */
public class ViewApprenticeStrongestPathDetailActivity extends Activity {
    private SharedPreferences sharedPref;
    private long apprenticeId = 0;
    private long pathId;
    private List<PathEdge> pathEdges = new ArrayList<PathEdge>();

    /**
     * onCreate override that provides emotion-choose view to user.
     * 
     * @param savedInstanceState Current application state data.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // get specific layout for content view
        setContentView(R.layout.activity_view_apprentice_strongest_path_detail);

        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        apprenticeId = Long.parseLong(sharedPref.getString(
                "pref_selected_apprentice", "1"));
        pathId = getIntent().getExtras().getLong("path_id");

        Log.d("MYLOG", "detail activity received pathId: " + pathId);
        
        TextView pathText = (TextView) findViewById(R.id.view_strongest_path_detail_title);

        try {
            PathEdgesDataSource peds = new PathEdgesDataSource(this);
            pathEdges = peds.getPathEdgesByPath(pathId);
            peds.close();

            EmotionsDataSource eds = new EmotionsDataSource(this);
            Emotion emotion = eds.getEmotion(pathEdges.get(0).getEmotionId());
            eds.close();

            LabelsDataSource lds = new LabelsDataSource(this);
            Label emotionLabel = lds.getLabel(emotion.getLabelId());
            lds.close();
            
            Log.d("MYLOG", "found emotion for detail activity: " + emotion.getName());

            TextView emotionName = (TextView) findViewById(R.id.strongest_path_detail_emotion_value);
            emotionName.setText(emotion.getName());
            String backgroundColor = "#ffffff";
            if (emotionLabel != null) {
                backgroundColor = emotionLabel.getColor();
            }
            emotionName.setBackgroundColor(Color.parseColor(backgroundColor));

        } catch (Exception e) {
            Log.d("MYLOG", e.getStackTrace().toString());
        }
    }
}
