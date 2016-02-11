
package com.summea.kanjoto.activity;

import java.util.List;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.TextView;

import com.summea.kanjoto.R;
import com.summea.kanjoto.data.AchievementsDataSource;
import com.summea.kanjoto.data.KanjotoDatabaseHelper;
import com.summea.kanjoto.model.Achievement;

public class DatabaseDumperAchievementsActivity extends Activity {
    private SharedPreferences sharedPref;
    private long apprenticeId = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // get specific layout for content view
        setContentView(R.layout.activity_database_dumper);

        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        apprenticeId = Long.parseLong(sharedPref.getString(
                "pref_selected_apprentice", "1"));

        AchievementsDataSource ads = new AchievementsDataSource(this);
        List<Achievement> allAchievements = ads.getAllAchievements(apprenticeId);
        ads.close();

        TextView debugText = (TextView) findViewById(R.id.debug_text);

        debugText.setText(debugText.getText().toString() + "Table: Achievements\n"
                + KanjotoDatabaseHelper.COLUMN_ID + "|" + KanjotoDatabaseHelper.COLUMN_NAME + "|"
                + KanjotoDatabaseHelper.COLUMN_APPRENTICE_ID + "|"
                + KanjotoDatabaseHelper.COLUMN_EARNED_ON + "|"
                + KanjotoDatabaseHelper.COLUMN_KEY + "\n");

        for (Achievement achievement : allAchievements) {

            String newText = debugText.getText().toString();
            newText += achievement.getId() + "|" + achievement.getName() + "|"
                    + achievement.getApprenticeId()
                    + "|" + achievement.getEarnedOn() + "|" + achievement.getKey() + "\n";

            debugText.setText(newText);
        }
    }
}
