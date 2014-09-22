package com.andrewsummers.otashu.test;

import android.test.ActivityInstrumentationTestCase2;

import com.andrewsummers.otashu.activity.MainActivity;

public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity>{

    private MainActivity activity;
    
    public MainActivityTest() {
        super(MainActivity.class);
    }
    
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        activity = getActivity();
    }
    
    public void testLayout() {
        assertNotNull("View All Button Exists", activity.findViewById(com.andrewsummers.otashu.R.id.button_view_all_notesets));
        assertNotNull("Settings Button Exists", activity.findViewById(com.andrewsummers.otashu.R.id.button_settings));
    }
}