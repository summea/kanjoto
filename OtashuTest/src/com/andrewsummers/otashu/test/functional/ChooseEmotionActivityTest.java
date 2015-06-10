
package com.andrewsummers.otashu.test.functional;

import com.andrewsummers.otashu.activity.GenerateMusicActivity;
import com.andrewsummers.otashu.activity.ChooseEmotionActivity;

import android.app.Instrumentation.ActivityMonitor;
import android.media.AudioManager;
import android.test.ActivityInstrumentationTestCase2;
import android.test.RenamingDelegatingContext;
import android.test.TouchUtils;
import android.widget.Button;

public class ChooseEmotionActivityTest extends
        ActivityInstrumentationTestCase2<ChooseEmotionActivity> {

    private RenamingDelegatingContext context;
    private GenerateMusicActivity generateMusicActivity;
    private int TIMEOUT_IN_MS = 7000;

    public ChooseEmotionActivityTest() {
        super(ChooseEmotionActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        // context = new RenamingDelegatingContext(getInstrumentation().getTargetContext(),
        // "test_");
    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();
    }

    public void testChooseEmotionAndGenerateMusic() throws Throwable {
        ActivityMonitor receiverActivityMonitor = getInstrumentation().addMonitor(
                GenerateMusicActivity.class.getName(), null, false);
        ChooseEmotionActivity chooseEmotionActivity = (ChooseEmotionActivity) getActivity();
        Button buttonGo = (Button) chooseEmotionActivity
                .findViewById(com.andrewsummers.otashu.R.id.button_go);
        TouchUtils.clickView(this, buttonGo);
        GenerateMusicActivity generateMusicActivity = (GenerateMusicActivity) receiverActivityMonitor
                .waitForActivityWithTimeout(TIMEOUT_IN_MS);
        assertNotNull("GenerateMusicActivity is not null", generateMusicActivity);
        getInstrumentation().removeMonitor(receiverActivityMonitor);

        AudioManager am = (AudioManager) getActivity().getSystemService(context.AUDIO_SERVICE);
        assertTrue("music is playing", am.isMusicActive());

        generateMusicActivity.finish();
        chooseEmotionActivity.finish();
    }
}
