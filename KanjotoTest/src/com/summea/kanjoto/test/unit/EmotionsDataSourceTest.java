
package com.summea.kanjoto.test.unit;

import java.util.ArrayList;
import java.util.List;

import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;

import com.summea.kanjoto.data.EmotionsDataSource;
import com.summea.kanjoto.data.KanjotoDatabaseHelper;
import com.summea.kanjoto.model.Emotion;

public class EmotionsDataSourceTest extends AndroidTestCase {

    private EmotionsDataSource eds;
    private KanjotoDatabaseHelper db;
    private RenamingDelegatingContext context;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        context = new RenamingDelegatingContext(getContext(), "test_");
        db = new KanjotoDatabaseHelper(context);
        eds = new EmotionsDataSource(context);
    }

    @Override
    public void tearDown() throws Exception {
        db.close();
        super.tearDown();
    }

    public void test_createEmotion_paramEmotion() throws Throwable {
        Emotion emotion = new Emotion();
        emotion.setName("test emotion");
        emotion.setApprenticeId(1);
        Emotion result = eds.createEmotion(emotion);
        assertNotNull("created emotion is not null", result);
    }

    public void test_deleteEmotion_paramEmotion() throws Throwable {
        Emotion emotion = new Emotion();
        emotion.setId(1);
        emotion.setName("test emotion");
        emotion.setApprenticeId(1);
        Emotion result = eds.createEmotion(emotion);
        assertNotNull("created emotion is not null", result);
        eds.deleteEmotion(emotion);
        emotion = new Emotion();
        emotion = eds.getEmotion(1);
        assertTrue(emotion.getId() != 1);
    }

    public void test_getAllEmotions_paramApprenticeId() throws Throwable {
        test_createEmotion_paramEmotion();
        List<Emotion> emotions = new ArrayList<Emotion>();
        emotions = eds.getAllEmotions(1);
        assertNotNull("get all emotions is not null", emotions);
        assertFalse(emotions.isEmpty());
    }

    public void test_getAllEmotionIds_paramApprenticeId() throws Throwable {
        test_createEmotion_paramEmotion();
        List<Integer> emotionIds = eds.getAllEmotionIds(1);
        assertNotNull("get all emotion ids is not null", emotionIds);
        assertFalse(emotionIds.isEmpty());
    }

    public void test_getAllEmotionListDBTableIds_paramApprenticeId() throws Throwable {
        test_createEmotion_paramEmotion();
        List<Long> emotionTableIds = eds.getAllEmotionListDBTableIds(1);
        assertNotNull("get all emotion list db table ids is not null", emotionTableIds);
        assertFalse(emotionTableIds.isEmpty());
    }

    public void test_getEmotion_paramEmotionId() throws Throwable {
        test_createEmotion_paramEmotion();
        Emotion emotion = eds.getEmotion(1);
        assertNotNull("get emotion is not null", emotion);
    }

    public void test_updateEmotion_paramEmotion() throws Throwable {
        test_createEmotion_paramEmotion();
        Emotion emotion = eds.getEmotion(1);
        emotion.setName("another");
        eds.updateEmotion(emotion);
        Emotion emotion2 = eds.getEmotion(1);
        assertNotNull("update emotion is not null", emotion2);
        assertTrue(emotion.getName().equals("another"));
    }

    public void test_getRandomEmotion_paramApprenticeId() throws Throwable {
        test_createEmotion_paramEmotion();
        Emotion emotion = eds.getRandomEmotion(1);
        assertNotNull("get random emotion is not null", emotion);
    }

    public void test_getEmotionCount_paramApprenticeId() throws Throwable {
        test_createEmotion_paramEmotion();
        int count = eds.getEmotionCount(1);
        assertNotNull("get emotion count is not null", count);
    }
}
