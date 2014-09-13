package com.andrewsummers.otashu;

/**
 * Noteset is a model for Noteset objects.
 */
public class Noteset {
    private long id;
    private String name;
    private int emotion_id;

    /**
     * getId gets Noteset id
     * 
     * @return <code>long</code> id value
     */
    public long getId() {
        return id;
    }

    /**
     * setId sets Noteset id
     * 
     * @param id
     *            New id value.
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * getName gets Noteset name.
     * 
     * @return <code>String</code> of noteset name.
     */
    public String getName() {
        return name;
    }

    /**
     * setName sets Noteset name.
     * 
     * @param name
     *            New noteset name.
     */
    public void setName(String name) {
        this.name = name;
    }
    
    /**
     * getEmotion gets Noteset emotion_id.
     * 
     * @return <code>int</code> of noteset emotion_id.
     */
    public int getEmotion() {
        return emotion_id;
    }

    /**
     * setEmotion sets Noteset emotion_id.
     * 
     * @param name
     *            New noteset emotion_id.
     */
    public void setEmotion(int emotion_id) {
        this.emotion_id = emotion_id;
    }

    /**
     * toString override to return noteset name.
     * 
     * @return <code>String</code> of noteset name.
     */
    @Override
    public String toString() {
        return name + " " + emotion_id;
    }
}