
package com.andrewsummers.kanjoto.activity;

import java.io.File;

import com.andrewsummers.kanjoto.R;
import com.andrewsummers.kanjoto.data.AchievementsDataSource;
import com.andrewsummers.kanjoto.data.ApprenticesDataSource;
import com.andrewsummers.kanjoto.data.LearningStylesDataSource;
import com.andrewsummers.kanjoto.model.Apprentice;
import com.andrewsummers.kanjoto.model.LearningStyle;
import com.andrewsummers.kanjoto.task.UploadFileTask;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

/**
 * View details of a particular Apprentice.
 * <p>
 * This activity allows a user to see more information about a particular Apprentice.
 * </p>
 */
public class ViewApprenticeDetailActivity extends Activity implements OnClickListener {
    private Button buttonSendApprentice = null;
    private SharedPreferences sharedPref;
    private long apprenticeId = 0;
    private String apprenticeUploadUrl;
    private File path = Environment.getExternalStorageDirectory();
    private String externalDirectory = path.toString() + "/kanjoto/";
    private String fullPathString = externalDirectory + "ota.db";

    /**
     * onCreate override used to get details view.
     * 
     * @param savedInstanceState Current application state data.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // get specific layout for content view
        setContentView(R.layout.activity_view_apprentice_detail);

        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        apprenticeUploadUrl = sharedPref.getString("pref_apprentice_upload_url", "");

        apprenticeId = getIntent().getExtras().getLong("list_id");

        Apprentice apprentice = new Apprentice();
        ApprenticesDataSource ads = new ApprenticesDataSource(this);
        apprentice = ads.getApprentice(apprenticeId);
        ads.close();

        try {
            // fill in form data
            TextView apprenticeName = (TextView) findViewById(R.id.apprentice_name_value);
            apprenticeName.setText(apprentice.getName());

            buttonSendApprentice = (Button) findViewById(R.id.button_send_apprentice);

            LearningStylesDataSource lsds = new LearningStylesDataSource(this);
            LearningStyle learningStyle = lsds.getLearningStyle(apprentice.getLearningStyleId());

            TextView apprenticeLearningStyle = (TextView) findViewById(R.id.apprentice_learning_style_value);
            apprenticeLearningStyle.setText(learningStyle.getName());

            TextView apprenticeAchievementLabel1 = (TextView) findViewById(R.id.apprentice_achievement_label_1);
            apprenticeAchievementLabel1.setText("Emotion");

            TextView apprenticeAchievementLabel2 = (TextView) findViewById(R.id.apprentice_achievement_label_2);
            apprenticeAchievementLabel2.setText("Scale");

            TextView apprenticeAchievementLabel3 = (TextView) findViewById(R.id.apprentice_achievement_label_3);
            apprenticeAchievementLabel3.setText("Transition");
            
            TextView apprenticeAchievement1 = (TextView) findViewById(R.id.apprentice_achievement_1);
            TextView apprenticeAchievement2 = (TextView) findViewById(R.id.apprentice_achievement_2);
            TextView apprenticeAchievement3 = (TextView) findViewById(R.id.apprentice_achievement_3);
            
            AchievementsDataSource acds = new AchievementsDataSource(this);
            int achievementEmotionCount = acds.getAchievementCount(apprenticeId, "found_strong_path");
            int achievementScaleCount = acds.getAchievementCount(apprenticeId, "completed_scale");
            int achievementTransitionCount = acds.getAchievementCount(apprenticeId, "found_strong_transition");
            acds.close();
            
            apprenticeAchievement1.setText(String.valueOf(achievementEmotionCount));
            apprenticeAchievement2.setText(String.valueOf(achievementScaleCount));
            apprenticeAchievement3.setText(String.valueOf(achievementTransitionCount));
            
            // add listeners to buttons
            buttonSendApprentice.setOnClickListener(this);
        } catch (Exception e) {
            Log.d("MYLOG", e.getStackTrace().toString());
        }        
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_send_apprentice:
                // disable button to avoid multiple sends for same emotion
                buttonSendApprentice = (Button) findViewById(R.id.button_send_apprentice);
                buttonSendApprentice.setClickable(false);

                new UploadFileTask().execute(apprenticeUploadUrl, fullPathString, "file");
        }
    }
}
