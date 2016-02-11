package com.summea.kanjoto.test.unit;

import com.summea.kanjoto.activity.ViewAllNotesetsActivity;

import android.content.Intent;
import android.test.ActivityUnitTestCase;
import android.test.suitebuilder.annotation.MediumTest;

public class ViewAllNotesetsActivityTest extends ActivityUnitTestCase<ViewAllNotesetsActivity> {
    
    private Intent mLaunchIntent;

    public ViewAllNotesetsActivityTest() {
        super(ViewAllNotesetsActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mLaunchIntent = new Intent(getInstrumentation().getTargetContext(), ViewAllNotesetsActivity.class);
    }
    
    @MediumTest
    public void testViewAllNotesetsActivityOnCreate() {
        startActivity(mLaunchIntent, null, null);
    }
}
