package com.andrewsummers.otashu.test;

import com.andrewsummers.otashu.R;
import com.andrewsummers.otashu.activity.MainActivity;

import android.content.Intent;
import android.test.ActivityUnitTestCase;
import android.test.suitebuilder.annotation.MediumTest;
import android.util.Log;
import android.widget.GridView;

public class MainActivityTest extends ActivityUnitTestCase<MainActivity> {

    private Intent mLaunchIntent;

    public MainActivityTest() {
        super(MainActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mLaunchIntent = new Intent(getInstrumentation().getTargetContext(), MainActivity.class);
    }
    
    @MediumTest
    public void testLaunchViewAllNotesetsActivity() {
        startActivity(mLaunchIntent, null, null);
        final GridView gridview = (GridView) getActivity().findViewById(R.id.gridview);
        
        int gridItemTotal = gridview.getAdapter().getCount();
        Log.d("MYLOG", "" + gridItemTotal);
        
        gridview.performItemClick(gridview, 0, 0);
        
        final Intent launchIntent = getStartedActivityIntent();
        assertNotNull("Intent was null", launchIntent);
    }
    
    @MediumTest
    public void testLaunchGenerateMusicActivity() {
        startActivity(mLaunchIntent, null, null);
        final GridView gridview = (GridView) getActivity().findViewById(R.id.gridview);
        
        int gridItemTotal = gridview.getAdapter().getCount();
        Log.d("MYLOG", "" + gridItemTotal);
        
        gridview.performItemClick(gridview, 1, 0);
        
        final Intent launchIntent = getStartedActivityIntent();
        assertNotNull("Intent was null", launchIntent);
    }
}