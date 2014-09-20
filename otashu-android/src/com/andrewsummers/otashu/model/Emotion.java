package com.andrewsummers.otashu.model;

/**
 * Emotion is a model for Emotion objects.
 */
public class Emotion {
    private long id;
    private String name;

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
     * @param id
     *            New id value.
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
     * @param name
     *            New Emotion name.
     */
    public void setName(String name) {
        this.name = name;
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