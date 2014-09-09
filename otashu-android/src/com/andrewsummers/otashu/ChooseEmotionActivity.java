package com.andrewsummers.otashu;

import android.app.Activity;
import android.os.Bundle;
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
        
        Spinner spinner = null;
        ArrayAdapter<CharSequence> adapter = null;

        // locate next spinner in layout
        spinner = (Spinner) findViewById(R.id.spinner_emotion);
        // create an ArrayAdapter using the string array in the related XML file
        // and use the default spinner layout
        adapter = ArrayAdapter.createFromResource(this,
                R.array.emotion_values_array,
                android.R.layout.simple_spinner_item);
        // specify the default layout when list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // apply this adapter to the spinner
        spinner.setAdapter(adapter);
    }

    @Override
    public void onClick(View arg0) {
        // TODO Auto-generated method stub
        
    }
}
