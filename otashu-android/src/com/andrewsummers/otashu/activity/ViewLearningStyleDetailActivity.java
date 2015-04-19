
package com.andrewsummers.otashu.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.andrewsummers.otashu.R;
import com.andrewsummers.otashu.data.LearningStylesDataSource;
import com.andrewsummers.otashu.model.LearningStyle;
import com.andrewsummers.otashu.model.Note;

import android.app.Activity;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

/**
 * View details of a particular LearningStyle.
 * <p>
 * This activity allows a user to see more information about a particular LearningStyle.
 * </p>
 */
public class ViewLearningStyleDetailActivity extends Activity implements OnClickListener {
    private int learningStyleId = 0;

    /**
     * onCreate override used to get details view.
     * 
     * @param savedInstanceState Current application state data.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // get specific layout for content view
        setContentView(R.layout.activity_view_learning_style_detail);

        learningStyleId = (int) getIntent().getExtras().getLong("list_id");

        LearningStyle learningStyle = new LearningStyle();
        LearningStylesDataSource bds = new LearningStylesDataSource(this);
        learningStyle = bds.getLearningStyle(learningStyleId);
        bds.close();

        // fill in form data
        TextView learningStyleName = (TextView) findViewById(R.id.learning_style_detail_name_value);
        learningStyleName.setText(learningStyle.getName());
    }

    /**
     * onClick override that acts as a router to start desired activities.
     * 
     * @param view Incoming view.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        }
    }
}
