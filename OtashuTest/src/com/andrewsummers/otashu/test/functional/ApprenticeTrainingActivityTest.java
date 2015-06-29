
package com.andrewsummers.otashu.test.functional;

import com.andrewsummers.otashu.activity.ApprenticeEmotionTestActivity;
import com.andrewsummers.otashu.activity.ApprenticeScaleTestActivity;
import com.andrewsummers.otashu.activity.ApprenticeTrainingActivity;
import com.andrewsummers.otashu.activity.ApprenticeTransitionTestActivity;

import android.app.Instrumentation.ActivityMonitor;
import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.widget.Button;

public class ApprenticeTrainingActivityTest extends
        ActivityInstrumentationTestCase2<ApprenticeTrainingActivity> {

    private int TIMEOUT_IN_MS = 7000;

    public ApprenticeTrainingActivityTest() {
        super(ApprenticeTrainingActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();
    }

    public void testApprenticeEmotionTestYes() throws Throwable {
        ActivityMonitor receiverActivityMonitor = getInstrumentation().addMonitor(
                ApprenticeEmotionTestActivity.class.getName(), null, false);
        ApprenticeTrainingActivity apprenticeTrainingActivity = (ApprenticeTrainingActivity) getActivity();
        Button buttonApprenticeEmotionTest = (Button) apprenticeTrainingActivity
                .findViewById(com.andrewsummers.otashu.R.id.button_apprentice_emotion_test);
        TouchUtils.clickView(this, buttonApprenticeEmotionTest);
        ApprenticeEmotionTestActivity apprenticeEmotionTestActivity = (ApprenticeEmotionTestActivity) receiverActivityMonitor
                .waitForActivityWithTimeout(TIMEOUT_IN_MS);
        assertNotNull("ApprenticeEmotionTestActivity is not null", apprenticeEmotionTestActivity);

        Thread.sleep(2000);

        Button buttonYes = (Button) apprenticeEmotionTestActivity
                .findViewById(com.andrewsummers.otashu.R.id.button_yes);
        TouchUtils.clickView(this, buttonYes);
        assertNotNull("ApprenticeEmotionTestActivity is not null", apprenticeEmotionTestActivity);

        getInstrumentation().removeMonitor(receiverActivityMonitor);

        apprenticeEmotionTestActivity.finish();
        apprenticeTrainingActivity.finish();
    }

    public void testApprenticeEmotionTestNo() throws Throwable {
        ActivityMonitor receiverActivityMonitor = getInstrumentation().addMonitor(
                ApprenticeEmotionTestActivity.class.getName(), null, false);
        ApprenticeTrainingActivity apprenticeTrainingActivity = (ApprenticeTrainingActivity) getActivity();
        Button buttonApprenticeEmotionTest = (Button) apprenticeTrainingActivity
                .findViewById(com.andrewsummers.otashu.R.id.button_apprentice_emotion_test);
        TouchUtils.clickView(this, buttonApprenticeEmotionTest);
        ApprenticeEmotionTestActivity apprenticeEmotionTestActivity = (ApprenticeEmotionTestActivity) receiverActivityMonitor
                .waitForActivityWithTimeout(TIMEOUT_IN_MS);
        assertNotNull("ApprenticeEmotionTestActivity is not null", apprenticeEmotionTestActivity);

        Thread.sleep(1500);

        Button buttonNo = (Button) apprenticeEmotionTestActivity
                .findViewById(com.andrewsummers.otashu.R.id.button_no);
        TouchUtils.clickView(this, buttonNo);
        assertNotNull("ApprenticeEmotionTestActivity is not null", apprenticeEmotionTestActivity);

        getInstrumentation().removeMonitor(receiverActivityMonitor);

        apprenticeEmotionTestActivity.finish();
        apprenticeTrainingActivity.finish();
    }

    public void testApprenticeScaleTestYes() throws Throwable {
        ActivityMonitor receiverActivityMonitor = getInstrumentation().addMonitor(
                ApprenticeScaleTestActivity.class.getName(), null, false);
        ApprenticeTrainingActivity apprenticeTrainingActivity = (ApprenticeTrainingActivity) getActivity();
        Button buttonApprenticeScaleTest = (Button) apprenticeTrainingActivity
                .findViewById(com.andrewsummers.otashu.R.id.button_apprentice_scale_test);
        TouchUtils.clickView(this, buttonApprenticeScaleTest);
        ApprenticeScaleTestActivity apprenticeScaleTestActivity = (ApprenticeScaleTestActivity) receiverActivityMonitor
                .waitForActivityWithTimeout(TIMEOUT_IN_MS);
        assertNotNull("ApprenticeScaleTestActivity is not null", apprenticeScaleTestActivity);
        getInstrumentation().removeMonitor(receiverActivityMonitor);

        Thread.sleep(2000);

        Button buttonYes = (Button) apprenticeScaleTestActivity
                .findViewById(com.andrewsummers.otashu.R.id.button_yes);
        TouchUtils.clickView(this, buttonYes);
        assertNotNull("ApprenticeScaleTestActivity is not null", apprenticeScaleTestActivity);

        // Thread.sleep(1000);

        apprenticeScaleTestActivity.finish();
        apprenticeTrainingActivity.finish();
    }

    public void testApprenticeScaleTestNo() throws Throwable {
        ActivityMonitor receiverActivityMonitor = getInstrumentation().addMonitor(
                ApprenticeScaleTestActivity.class.getName(), null, false);
        ApprenticeTrainingActivity apprenticeTrainingActivity = (ApprenticeTrainingActivity) getActivity();
        Button buttonApprenticeScaleTest = (Button) apprenticeTrainingActivity
                .findViewById(com.andrewsummers.otashu.R.id.button_apprentice_scale_test);
        TouchUtils.clickView(this, buttonApprenticeScaleTest);
        ApprenticeScaleTestActivity apprenticeScaleTestActivity = (ApprenticeScaleTestActivity) receiverActivityMonitor
                .waitForActivityWithTimeout(TIMEOUT_IN_MS);
        assertNotNull("ApprenticeScaleTestActivity is not null", apprenticeScaleTestActivity);

        Thread.sleep(2000);

        Button buttonNo = (Button) apprenticeScaleTestActivity
                .findViewById(com.andrewsummers.otashu.R.id.button_no);
        TouchUtils.clickView(this, buttonNo);
        assertNotNull("ApprenticeScaleTestActivity is not null", apprenticeScaleTestActivity);

        getInstrumentation().removeMonitor(receiverActivityMonitor);

        apprenticeScaleTestActivity.finish();
        apprenticeTrainingActivity.finish();
    }

    public void testApprenticeTransitionTestYes() throws Throwable {
        ActivityMonitor receiverActivityMonitor = getInstrumentation().addMonitor(
                ApprenticeTransitionTestActivity.class.getName(), null, false);
        ApprenticeTrainingActivity apprenticeTrainingActivity = (ApprenticeTrainingActivity) getActivity();
        Button buttonApprenticeTransitionTest = (Button) apprenticeTrainingActivity
                .findViewById(com.andrewsummers.otashu.R.id.button_apprentice_transition_test);
        TouchUtils.clickView(this, buttonApprenticeTransitionTest);
        ApprenticeTransitionTestActivity apprenticeTransitionTestActivity = (ApprenticeTransitionTestActivity) receiverActivityMonitor
                .waitForActivityWithTimeout(TIMEOUT_IN_MS);
        assertNotNull("ApprenticeTransitionTestActivity is not null",
                apprenticeTransitionTestActivity);

        Thread.sleep(1500);

        Button buttonYes = (Button) apprenticeTransitionTestActivity
                .findViewById(com.andrewsummers.otashu.R.id.button_yes);
        TouchUtils.clickView(this, buttonYes);
        assertNotNull("ApprenticeTransitionTestActivity is not null",
                apprenticeTransitionTestActivity);

        getInstrumentation().removeMonitor(receiverActivityMonitor);

        apprenticeTransitionTestActivity.finish();
        apprenticeTrainingActivity.finish();
    }

    public void testApprenticeTransitionTestNo() throws Throwable {
        ActivityMonitor receiverActivityMonitor = getInstrumentation().addMonitor(
                ApprenticeTransitionTestActivity.class.getName(), null, false);
        ApprenticeTrainingActivity apprenticeTrainingActivity = (ApprenticeTrainingActivity) getActivity();
        Button buttonApprenticeTransitionTest = (Button) apprenticeTrainingActivity
                .findViewById(com.andrewsummers.otashu.R.id.button_apprentice_transition_test);
        TouchUtils.clickView(this, buttonApprenticeTransitionTest);
        ApprenticeTransitionTestActivity apprenticeTransitionTestActivity = (ApprenticeTransitionTestActivity) receiverActivityMonitor
                .waitForActivityWithTimeout(TIMEOUT_IN_MS);
        assertNotNull("ApprenticeTransitionTestActivity is not null",
                apprenticeTransitionTestActivity);

        Thread.sleep(1000);

        Button buttonNo = (Button) apprenticeTransitionTestActivity
                .findViewById(com.andrewsummers.otashu.R.id.button_no);
        TouchUtils.clickView(this, buttonNo);
        assertNotNull("ApprenticeTransitionTestActivity is not null",
                apprenticeTransitionTestActivity);

        getInstrumentation().removeMonitor(receiverActivityMonitor);

        apprenticeTransitionTestActivity.finish();
        apprenticeTrainingActivity.finish();
    }
}
