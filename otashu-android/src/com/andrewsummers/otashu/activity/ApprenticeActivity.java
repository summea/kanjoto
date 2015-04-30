
package com.andrewsummers.otashu.activity;

import com.andrewsummers.otashu.R;
import com.andrewsummers.otashu.data.ApprenticesDataSource;
import com.andrewsummers.otashu.model.Apprentice;

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
 * <p>
 * In this activity, the Apprentice chooses a random emotion and chooses notesets (either randomly
 * or using previously-learned data) and presents the noteset-emotion combination to the User to
 * check for accuracy. If the noteset-emotion combination is correct (or passing), the Apprentice
 * will go and save this noteset-emotion combination information in a database graph table. This
 * noteset-emotion combination is also saved in the User's noteset collection (if enabled in the
 * program settings).
 * </p>
 * <p>
 * If the Apprentice incorrectly chooses a noteset-emotion combination (as determined by the User),
 * the noteset-emotion combination information is noted by raising the related path edge weights in
 * the database graph table. Also, the incorrect noteset-emotion combination is not saved in the
 * User's noteset collection.
 * </p>
 */
public class ApprenticeActivity extends Activity implements OnClickListener {
    private SharedPreferences sharedPref;
    private long apprenticeId = 0;
    private int programMode;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // get specific layout for content view
        setContentView(R.layout.activity_apprentice);
        
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        setProgramMode(Integer.parseInt(sharedPref.getString(
                "pref_program_mode", "1")));
        apprenticeId = Long.parseLong(sharedPref.getString(
                "pref_selected_apprentice", "1"));

        TextView apprenticeName = (TextView) findViewById(R.id.apprentice_name);
        TextView apprenticeText = (TextView) findViewById(R.id.apprentice_text);
        Button buttonChooseApprentice = (Button) findViewById(R.id.button_choose_apprentice);
        Button buttonApprenticeEmotionTest = (Button) findViewById(R.id.button_apprentice_emotion_test);
        Button buttonApprenticeTransitionTest = (Button) findViewById(R.id.button_apprentice_transition_test);
        Button buttonApprenticeScaleTest = (Button) findViewById(R.id.button_apprentice_scale_test);

        ApprenticesDataSource ads = new ApprenticesDataSource(this);
        Apprentice apprentice = ads.getApprentice(apprenticeId);
        apprenticeName.setText(apprentice.getName());
        apprenticeText.setText("Take a test?");

        try {
            // add listeners to buttons
            buttonChooseApprentice.setOnClickListener(this);
            buttonApprenticeEmotionTest.setOnClickListener(this);
            buttonApprenticeTransitionTest.setOnClickListener(this);
            buttonApprenticeScaleTest.setOnClickListener(this);
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
            case R.id.button_apprentice_emotion_test:
                intent = new Intent(this, ApprenticeEmotionTestActivity.class);
                break;
            case R.id.button_apprentice_transition_test:
                intent = new Intent(this, ApprenticeTransitionTestActivity.class);
                break;
            case R.id.button_apprentice_scale_test:
                intent = new Intent(this, ApprenticeScaleTestActivity.class);
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
            Intent refresh = new Intent(this, ApprenticeActivity.class);
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
