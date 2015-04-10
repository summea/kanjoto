
package com.andrewsummers.otashu.model;

/**
 * Emotion is a model for Emotion objects.
 */
public class Emotion {
    private long id;
    private String name;
    private long labelId;
    private long apprenticeId;

    /**
     * getId gets Emotion id
     * 
     * @return <code>long</code> id value
     */
    public long getId() {
        return id;
    }

    /**
     * setId sets Emotion id
     * 
     * @param id New id value.
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * getName gets Emotion name.
     * 
     * @return <code>String</code> of Emotion name.
     */
    public String getName() {
        return name;
    }

    /**
     * setName sets Emotion name.
     * 
     * @param name New Emotion name.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * getLabelId gets related Label id
     * 
     * @return <code>long</code> of related Label id.
     */
    public long getLabelId() {
        return labelId;
    }

    /**
     * setLabelId sets Label id
     * 
     * @param id New id value.
     */
    public void setLabelId(long labelId) {
        this.labelId = labelId;
    }

    /**
     * getApprenticeId gets Apprentice id
     * 
     * @return <code>long</code> id value
     */
    public long getApprenticeId() {
        return apprenticeId;
    }

    /**
     * setApprenticeId sets Apprentice id
     * 
     * @param apprenticeId New apprenticeId value.
     */
    public void setApprenticeId(long apprenticeId) {
        this.apprenticeId = apprenticeId;
    }

    /**
     * toString override to return Emotion name.
     * 
     * @return <code>String</code> of Emotion name.
     */
    @Override
    public String toString() {
        return name;
    }
}
