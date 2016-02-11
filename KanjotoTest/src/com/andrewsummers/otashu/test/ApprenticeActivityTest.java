
package com.andrewsummers.otashu.test;

import com.andrewsummers.otashu.activity.ApprenticeActivity;
import com.andrewsummers.otashu.activity.ChooseApprenticeActivity;
import com.andrewsummers.otashu.model.Apprentice;
import com.andrewsummers.otashu.data.ApprenticesDataSource;
import com.andrewsummers.otashu.data.OtashuDatabaseHelper;

import android.app.Instrumentation.ActivityMonitor;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.test.ActivityInstrumentationTestCase2;
import android.test.RenamingDelegatingContext;
import android.test.TouchUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.Spinner;

public class ApprenticeActivityTest extends ActivityInstrumentationTestCase2<ApprenticeActivity> {

    private RenamingDelegatingContext context;
    private ApprenticeActivity apprenticeActivity;
    private Button buttonChooseApprentice;
    private int TIMEOUT_IN_MS = 10000;

    public ApprenticeActivityTest() {
        super(ApprenticeActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        context = new RenamingDelegatingContext(getInstrumentation().getTargetContext(), "test_");
        apprenticeActivity = (ApprenticeActivity) getActivity();
        buttonChooseApprentice = (Button) apprenticeActivity
                .findViewById(com.andrewsummers.otashu.R.id.button_choose_apprentice);
    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();
    }

    public void testChooseApprenticeA() throws Throwable {
        ActivityMonitor receiverActivityMonitor = getInstrumentation().addMonitor(
                ChooseApprenticeActivity.class.getName(), null, false);
        TouchUtils.clickView(this, buttonChooseApprentice);
        ChooseApprenticeActivity chooseApprenticeActivity = (ChooseApprenticeActivity) receiverActivityMonitor
                .waitForActivityWithTimeout(TIMEOUT_IN_MS);
        assertNotNull("ChooseApprenticeActivity is not null", chooseApprenticeActivity);
        getInstrumentation().removeMonitor(receiverActivityMonitor);

        receiverActivityMonitor = getInstrumentation().addMonitor(
                ApprenticeActivity.class.getName(), null, false);
        Button buttonChoose = (Button) chooseApprenticeActivity
                .findViewById(com.andrewsummers.otashu.R.id.button_choose);
        TouchUtils.clickView(this, buttonChoose);
        ApprenticeActivity apprenticeActivity = (ApprenticeActivity) receiverActivityMonitor
                .waitForActivityWithTimeout(TIMEOUT_IN_MS);
        assertNotNull("ApprenticeActivity is not null", apprenticeActivity);
        // get currently selected apprentice id
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        long apprenticeId = Long.parseLong(sharedPref.getString(
                "pref_selected_apprentice", "0"));
        Log.d("MYLOG", "apprentice id: " + apprenticeId);
        assertTrue(apprenticeId == 1);
        getInstrumentation().removeMonitor(receiverActivityMonitor);
    }
}
