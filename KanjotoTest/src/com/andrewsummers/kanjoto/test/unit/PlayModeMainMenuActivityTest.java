
package com.andrewsummers.kanjoto.test.unit;

import com.andrewsummers.kanjoto.R;
import com.andrewsummers.kanjoto.activity.PlayModeMainMenuActivity;

import android.content.Intent;
import android.test.ActivityUnitTestCase;
import android.test.suitebuilder.annotation.MediumTest;
import android.util.Log;
import android.widget.GridView;

public class PlayModeMainMenuActivityTest extends ActivityUnitTestCase<PlayModeMainMenuActivity> {

    private Intent mLaunchIntent;

    public PlayModeMainMenuActivityTest() {
        super(PlayModeMainMenuActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mLaunchIntent = new Intent(getInstrumentation().getTargetContext(),
                PlayModeMainMenuActivityTest.class);
    }

    @MediumTest
    public void testLaunchPlayModeMainMenuActivity() {
        startActivity(mLaunchIntent, null, null);
        final GridView gridView = (GridView) getActivity().findViewById(R.id.gridview);

        int gridItemTotal = gridView.getAdapter().getCount();
        Log.d("MYLOG", "" + gridItemTotal);

        gridView.performItemClick(gridView, 0, 0);

        final Intent launchIntent = getStartedActivityIntent();
        assertNotNull("Intent was null", launchIntent);
    }

    @MediumTest
    public void testLaunchGenerateMusicActivity() {
        startActivity(mLaunchIntent, null, null);
        final GridView gridView = (GridView) getActivity().findViewById(R.id.gridview);

        int gridItemTotal = gridView.getAdapter().getCount();
        Log.d("MYLOG", "" + gridItemTotal);

        gridView.performItemClick(gridView, 1, 0);

        final Intent launchIntent = getStartedActivityIntent();
        assertNotNull("Intent was null", launchIntent);
    }

    @MediumTest
    public void testLaunchViewAllEmotionsActivity() {
        startActivity(mLaunchIntent, null, null);
        final GridView gridView = (GridView) getActivity().findViewById(R.id.gridview);

        int gridItemTotal = gridView.getAdapter().getCount();
        Log.d("MYLOG", "" + gridItemTotal);

        gridView.performItemClick(gridView, 2, 0);

        final Intent launchIntent = getStartedActivityIntent();
        assertNotNull("Intent was null", launchIntent);
    }

    @MediumTest
    public void testLaunchApprenticeActivity() {
        startActivity(mLaunchIntent, null, null);
        final GridView gridView = (GridView) getActivity().findViewById(R.id.gridview);

        int gridItemTotal = gridView.getAdapter().getCount();
        Log.d("MYLOG", "" + gridItemTotal);

        gridView.performItemClick(gridView, 3, 0);

        final Intent launchIntent = getStartedActivityIntent();
        assertNotNull("Intent was null", launchIntent);
    }
}
