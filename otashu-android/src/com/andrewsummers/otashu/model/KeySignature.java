
package com.andrewsummers.otashu.model;

/**
 * KeySignature is a model for KeySignature objects.
 */
public class KeySignature {
    private long id;
    private long emotionId;

    /**
     * getId gets KeySignature id
     * 
     * @return <code>long</code> id value
     */
    public long getId() {
        return id;
    }

    /**
     * setId sets KeySignature id
     * 
     * @param id New id value.
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * getEmotionId gets KeySignature emotionId.
     * 
     * @return <code>long</code> of KeySignature emotionId.
     */
    public long getEmotionId() {
        return emotionId;
    }

    /**
     * setEmotionId sets KeySignature emotionId.
     * 
     * @param emotionId New KeySignature emotionId.
     */
    public void setEmotionId(long emotionId) {
        this.emotionId = emotionId;
    }

    /**
     * toString override to return KeySignature emotionId.
     * 
     * @return <code>String</code> of KeySignature emotionId.
     */
    @Override
    public String toString() {
        return id + "";
    }
}
