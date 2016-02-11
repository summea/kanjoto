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
    public void testLaunchDataModeMainMenuActivity() {
        startActivity(mLaunchIntent, null, null);
        final GridView gridview = (GridView) getActivity().findViewById(R.id.gridview);
        
        int gridItemTotal = gridview.getAdapter().getCount();
        Log.d("MYLOG", "" + gridItemTotal);
        
        gridview.performItemClick(gridview, 0, 0);
        
        final Intent launchIntent = getStartedActivityIntent();
        assertNotNull("Intent was null", launchIntent);
    }
    
    @MediumTest
    public void testLaunchPlayModeMainMenuActivity() {
        startActivity(mLaunchIntent, null, null);
        final GridView gridView = (GridView) getActivity().findViewById(R.id.gridview);
        
        int gridItemTotal = gridView.getAdapter().getCount();
        Log.d("MYLOG", "" + gridItemTotal);
        
        gridView.performItemClick(gridView, 1, 0);
        
        final Intent launchIntent = getStartedActivityIntent();
        assertNotNull("Intent was null", launchIntent);
    }

    /*
    @MediumTest
    public void testLaunchSettings() {
        startActivity(mLaunchIntent, null, null);
        MainActivity mMainActivity = getActivity();
        
        // final View viewSettingsMenuItem = (View) mMainActivity.findViewById(R.id.view_settings);
        
        // Set up an ActivityMonitor
        
        ActivityMonitor settingsActivityMonitor = getInstrumentation().addMonitor(SettingsActivity.class.getName(), null, false);        
        
        getInstrumentation().sendKeyDownUpSync(KeyEvent.KEYCODE_MENU);
        getInstrumentation().invokeMenuActionSync(mMainActivity, R.id.view_settings, 0);
        
        // Activity settings = getInstrumentation().waitForMonitorWithTimeout(settingsActivityMonitor, 5000);
        // assertEquals(true, getInstrumentation().checkMonitorHit(settingsActivityMonitor, 1));
        // settings.finish();
        
        // Validate that SettingsActivity has started
        // TouchUtils.clickView(this, viewSettingsMenuItem);
        // SettingsActivity settingsActivity = (SettingsActivity) settingsActivityMonitor.waitForActivityWithTimeout(5000);
        // assertNotNull("SettingsActivity is null", settingsActivity);
        // assertEquals("Monitor for SettingsActivity has not been called", 1, settingsActivityMonitor.getHits());
        // assertEquals("Wrong type of Activity", SettingsActivity.class, settingsActivity.getClass());
        
        
        // Remove ActivityMonitor
        // getInstrumentation().removeMonitor(settingsActivityMonitor);
    }*/
}