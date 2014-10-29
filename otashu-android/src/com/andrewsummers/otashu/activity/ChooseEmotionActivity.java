package com.andrewsummers.otashu.activity;

import java.util.ArrayList;
import java.util.List;

import com.andrewsummers.otashu.R;
import com.andrewsummers.otashu.data.EmotionsDataSource;
import com.andrewsummers.otashu.model.Emotion;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

public class ChooseEmotionActivity extends Activity implements OnClickListener {

    private Button buttonGo = null;
    
    /**
     * onCreate override that provides emotion-choose view to user.
     * 
     * @param savedInstanceState
     *            Current application state data.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // get specific layout for content view
        setContentView(R.layout.activity_choose_emotion);

        // add listeners to buttons
        // have to cast to Button in this case
        buttonGo = (Button) findViewById(R.id.button_go);
        buttonGo.setOnClickListener(this);
        
        EmotionsDataSource emotionsDataSource = new EmotionsDataSource(this);
        emotionsDataSource.open();

        List<Emotion> allEmotions = new ArrayList<Emotion>();
        allEmotions = emotionsDataSource.getAllEmotions();
        
        emotionsDataSource.close();
        
        Spinner spinner = null;
        
        // locate next spinner in layout
        spinner = (Spinner) findViewById(R.id.spinner_emotion);

        // create array adapter for list of emotions
        ArrayAdapter<Emotion> emotionsAdapter = new ArrayAdapter<Emotion>(this, android.R.layout.simple_spinner_item);
        emotionsAdapter.addAll(allEmotions);
        
        // specify the default layout when list of choices appears
        emotionsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        
        // apply this adapter to the spinner
        spinner.setAdapter(emotionsAdapter);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;

        switch (v.getId()) {
        case R.id.button_go:
            
            EmotionsDataSource emotionsDataSource = new EmotionsDataSource(this);
            emotionsDataSource.open();

            List<Integer> allEmotionIds = new ArrayList<Integer>();
            allEmotionIds = emotionsDataSource.getAllEmotionIds();
            
            Spinner emotionSpinner = (Spinner) findViewById(R.id.spinner_emotion);
            int selectedEmotionValue = allEmotionIds.get(emotionSpinner.getSelectedItemPosition());
            
            emotionsDataSource.close();
            
            Log.d("MYLOG", "selected emotion value: " + selectedEmotionValue);

            Bundle bundle = new Bundle();
            bundle.putInt("emotion_id", selectedEmotionValue);

            intent = new Intent(this, GenerateMusicActivity.class);
            intent.putExtras(bundle);            
            startActivity(intent);
            break;
        }
    }
}