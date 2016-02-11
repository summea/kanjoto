
package com.andrewsummers.kanjoto.activity;

import com.andrewsummers.kanjoto.R;
import com.andrewsummers.kanjoto.data.AchievementsDataSource;
import com.andrewsummers.kanjoto.data.ApprenticesDataSource;
import com.andrewsummers.kanjoto.model.Apprentice;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

/**
 * The ApprenticeActivity class provides tests for the Apprentice with test results noted as judged
 * by the User.
 */
public class PlayModeApprenticeActivity extends Activity implements OnClickListener {
    private SharedPreferences sharedPref;
    private long apprenticeId = 0;
    private int programMode;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // get specific layout for content view
        setContentView(R.layout.activity_apprentice_detail);

        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        setProgramMode(Integer.parseInt(sharedPref.getString(
                "pref_program_mode", "1")));
        apprenticeId = Long.parseLong(sharedPref.getString(
                "pref_selected_apprentice", "1"));

        TextView apprenticeName = (TextView) findViewById(R.id.apprentice_name);
        Button buttonChooseApprentice = (Button) findViewById(R.id.button_choose_apprentice);

        ApprenticesDataSource ads = new ApprenticesDataSource(this);
        Apprentice apprentice = ads.getApprentice(apprenticeId);
        apprenticeName.setText(apprentice.getName());

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
        int achievementTransitionCount = acds.getAchievementCount(apprenticeId,
                "found_strong_transition");
        acds.close();

        apprenticeAchievement1.setText(String.valueOf(achievementEmotionCount));
        apprenticeAchievement2.setText(String.valueOf(achievementScaleCount));
        apprenticeAchievement3.setText(String.valueOf(achievementTransitionCount));

        try {
            // add listeners to buttons
            buttonChooseApprentice.setOnClickListener(this);
        } catch (Exception e) {
            Log.d("MYLOG", e.getStackTrace().toString());
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.button_choose_apprentice:
                intent = new Intent(this, ChooseApprenticeActivity.class);
                break;
        }
        if (intent != null) {
            startActivityForResult(intent, 1);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Intent refresh = new Intent(this, PlayModeApprenticeActivity.class);
            startActivity(refresh);
            this.finish();
        }
    }

    public int getProgramMode() {
        return programMode;
    }

    public void setProgramMode(int programMode) {
        this.programMode = programMode;
    }
}
