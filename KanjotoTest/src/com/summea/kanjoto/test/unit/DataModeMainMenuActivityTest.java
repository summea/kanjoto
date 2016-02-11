
package com.summea.kanjoto.test.unit;

import com.summea.kanjoto.R;
import com.summea.kanjoto.activity.DataModeMainMenuActivity;

import android.content.Intent;
import android.test.ActivityUnitTestCase;
import android.test.suitebuilder.annotation.MediumTest;
import android.util.Log;
import android.widget.GridView;

public class DataModeMainMenuActivityTest extends ActivityUnitTestCase<DataModeMainMenuActivity> {

    private Intent mLaunchIntent;

    public DataModeMainMenuActivityTest() {
        super(DataModeMainMenuActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mLaunchIntent = new Intent(getInstrumentation().getTargetContext(),
                DataModeMainMenuActivityTest.class);
    }

    @MediumTest
    public void testLaunchDataModeMainMenuActivity() {
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
