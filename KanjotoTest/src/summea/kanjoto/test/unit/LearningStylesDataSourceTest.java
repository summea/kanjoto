
package summea.kanjoto.test.unit;

import java.util.ArrayList;
import java.util.List;

import summea.kanjoto.data.KanjotoDatabaseHelper;
import summea.kanjoto.data.LearningStylesDataSource;
import summea.kanjoto.model.LearningStyle;
import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;

public class LearningStylesDataSourceTest extends AndroidTestCase {

    private LearningStylesDataSource lsds;
    private KanjotoDatabaseHelper db;
    private RenamingDelegatingContext context;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        context = new RenamingDelegatingContext(getContext(), "test_");
        db = new KanjotoDatabaseHelper(context);
        lsds = new LearningStylesDataSource(context);
    }

    @Override
    public void tearDown() throws Exception {
        db.close();
        super.tearDown();
    }

    public void test_createLearningStyle_paramLearningStyle() throws Throwable {
        LearningStyle learningStyle = new LearningStyle();
        learningStyle.setName("learningstyle");
        LearningStyle result = lsds.createLearningStyle(learningStyle);
        assertNotNull("created learning style is not null", result);
    }

    public void test_deleteLearningStyle_paramLearningStyle() throws Throwable {
        LearningStyle learningStyle = new LearningStyle();
        learningStyle.setId(1);
        learningStyle.setName("learningstyle");
        LearningStyle result = lsds.createLearningStyle(learningStyle);
        assertNotNull("created learning style is not null", result);
        lsds.deleteLearningStyle(learningStyle);
        learningStyle = new LearningStyle();
        learningStyle = lsds.getLearningStyle(1);
        assertTrue(learningStyle.getId() != 1);
    }

    public void test_getAllLearningStyles() throws Throwable {
        test_createLearningStyle_paramLearningStyle();
        List<LearningStyle> learningStyles = new ArrayList<LearningStyle>();
        learningStyles = lsds.getAllLearningStyles();
        assertNotNull("get all learning styles is not null", learningStyles);
        assertFalse(learningStyles.isEmpty());
    }

    public void test_getAllLearningStyleListDBTableIds() throws Throwable {
        test_createLearningStyle_paramLearningStyle();
        List<Long> learningStyles = lsds.getAllLearningStyleListDBTableIds();
        assertNotNull("get all learning style list db table ids is not null", learningStyles);
        assertFalse(learningStyles.isEmpty());
    }

    public void test_getLearningStyle_paramLearningStyleId() throws Throwable {
        test_createLearningStyle_paramLearningStyle();
        LearningStyle learningStyle = lsds.getLearningStyle(1);
        assertNotNull("get learning style is not null", learningStyle);
    }

    public void test_updateLearningStyle_paramLearningStyle() throws Throwable {
        test_createLearningStyle_paramLearningStyle();
        LearningStyle learningStyle = lsds.getLearningStyle(1);
        learningStyle.setName("another");
        lsds.updateLearningStyle(learningStyle);
        LearningStyle learningStyle2 = lsds.getLearningStyle(1);
        assertNotNull("update learning style is not null", learningStyle2);
        assertTrue(learningStyle.getName().equals("another"));
    }
}
