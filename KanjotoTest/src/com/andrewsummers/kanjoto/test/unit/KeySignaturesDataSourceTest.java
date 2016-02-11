
package com.andrewsummers.kanjoto.test.unit;

import java.util.ArrayList;
import java.util.List;

import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;

import com.andrewsummers.kanjoto.data.KeySignaturesDataSource;
import com.andrewsummers.kanjoto.data.KanjotoDatabaseHelper;
import com.andrewsummers.kanjoto.model.KeySignature;

public class KeySignaturesDataSourceTest extends AndroidTestCase {

    private KeySignaturesDataSource ksds;
    private KanjotoDatabaseHelper db;
    private RenamingDelegatingContext context;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        context = new RenamingDelegatingContext(getContext(), "test_");
        db = new KanjotoDatabaseHelper(context);
        ksds = new KeySignaturesDataSource(context);
    }

    @Override
    public void tearDown() throws Exception {
        db.close();
        super.tearDown();
    }

    public void test_createKeySignature_paramKeySignature() throws Throwable {
        KeySignature keySignature = new KeySignature();
        keySignature.setApprenticeId(1);
        keySignature.setEmotionId(1);
        KeySignature result = ksds.createKeySignature(keySignature);
        assertNotNull("created key signature is not null", result);
    }

    public void test_deleteKeySignature_paramKeySignature() throws Throwable {
        KeySignature keySignature = new KeySignature();
        keySignature.setId(1);
        keySignature.setApprenticeId(1);
        keySignature.setEmotionId(1);
        KeySignature result = ksds.createKeySignature(keySignature);
        assertNotNull("created key signature is not null", result);
        ksds.deleteKeySignature(keySignature);
        keySignature = new KeySignature();
        keySignature = ksds.getKeySignature(1);
        assertTrue(keySignature.getId() != 1);
    }

    public void test_getAllKeySignatures() throws Throwable {
        test_createKeySignature_paramKeySignature();
        List<KeySignature> keySignatures = new ArrayList<KeySignature>();
        keySignatures = ksds.getAllKeySignatures(1);
        assertNotNull("get all key signatures is not null", keySignatures);
        assertFalse(keySignatures.isEmpty());
    }

    public void test_getKeySignature_paramKeySignatureId() throws Throwable {
        test_createKeySignature_paramKeySignature();
        KeySignature keySignature = ksds.getKeySignature(1);
        assertNotNull("get key signature is not null", keySignature);
    }

    public void test_updateKeySignature_paramKeySignature() throws Throwable {
        test_createKeySignature_paramKeySignature();
        KeySignature keySignature = ksds.getKeySignature(1);
        keySignature.setEmotionId(2);
        ksds.updateKeySignature(keySignature);
        KeySignature keySignature2 = ksds.getKeySignature(1);
        assertNotNull("update key signature is not null", keySignature2);
        assertTrue(keySignature.getEmotionId() == 2);
    }
}
