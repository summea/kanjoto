package com.andrewsummers.otashu.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.andrewsummers.otashu.R;
import com.andrewsummers.otashu.model.Emotion;
import com.andrewsummers.otashu.model.Note;
import com.andrewsummers.otashu.model.Noteset;

import android.app.Activity;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class ApprenticeTransitionTestActivity extends Activity implements OnClickListener {
    private File path = Environment.getExternalStorageDirectory();
    private String externalDirectory = path.toString() + "/otashu/";
    private File musicSource = new File(externalDirectory + "otashu_preview.mid");
    private static MediaPlayer mediaPlayer;
    private List<Note> notesToInsert = new ArrayList<Note>();
    private Noteset newlyInsertedNoteset = new Noteset();
    private Noteset notesetToInsert = new Noteset();
    private Emotion chosenEmotion = new Emotion();
    private Button buttonYes = null;
    private Button buttonNo = null;
    private Button buttonPlayNoteset = null;
    private SharedPreferences sharedPref;
    private long emotionGraphId;
    private long emotionId;
    private int guessesCorrect = 0;
    private int guessesIncorrect = 0;
    private double guessesCorrectPercentage = 0.0;
    private int totalGuesses = 0;
    private long scorecardId = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // get specific layout for content view
        setContentView(R.layout.activity_apprentice);

        buttonNo = (Button) findViewById(R.id.button_yes);
        buttonYes = (Button) findViewById(R.id.button_no);

        // get emotion graph id for Apprentice's note relationships graph
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        emotionGraphId = Long.parseLong(sharedPref.getString(
                "pref_emotion_graph_for_apprentice", "2"));

        try {
            // add listeners to buttons
            buttonNo.setOnClickListener(this);
            buttonYes.setOnClickListener(this);

            Button buttonPlayNoteset = (Button) findViewById(R.id.button_play_noteset);
            buttonPlayNoteset.setOnClickListener(this);
        } catch (Exception e) {
            Log.d("MYLOG", e.getStackTrace().toString());
        }

        // disable buttons while playing
        buttonYes.setClickable(false);
        buttonNo.setClickable(false);
        buttonPlayNoteset = (Button) findViewById(R.id.button_play_noteset);
        buttonPlayNoteset.setClickable(false);

        // apprenticeAskProcess();

        mediaPlayer.setOnCompletionListener(new OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer aMediaPlayer) {
                // enable play button again
                buttonYes.setClickable(true);
                buttonNo.setClickable(true);
                buttonPlayNoteset.setClickable(true);
            }
        });
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        
    }
}
