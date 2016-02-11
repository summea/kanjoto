
package com.summea.kanjoto.model;

/**
 * Apprentice is a model for Apprentice objects.
 */
public class Apprentice {
    private long id;
    private String name;
    private int state;
    private long learningStyleId;

    public Apprentice() {
    }

    public Apprentice(int state) {
        this.state = state;
    }
    
    /**
     * getId gets Apprentice id
     * 
     * @return <code>long</code> id value
     */
    public long getId() {
        return id;
    }

    /**
     * setId sets Apprentice id
     * 
     * @param id New id value.
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * getName gets Apprentice name.
     * 
     * @return <code>String</code> of Apprentice name.
     */
    public String getName() {
        return name;
    }

    /**
     * setName sets Apprentice name.
     * 
     * @param name New Apprentice name.
     */
    public void setName(String name) {
        this.name = name;
    }


    /**
     * getState gets Apprentice's current state
     * 
     * @return <code>int</code> state
     */
    public int getState() {
        return state;
    }

    /**
     * setState sets Apprentice's current state.
     * 
     * @param state New int state.
     */
    public void setState(int state) {
        this.state = state;
    }
    
    /**
     * getLearningStyleId gets Apprentice's learningStyleId
     * 
     * @return <code>long</code> learningStyleId value
     */
    public long getLearningStyleId() {
        return learningStyleId;
    }

    /**
     * setLearningStyleId sets Apprentice's learningStyleId
     * 
     * @param learningStyleId New learningStyleId value.
     */
    public void setLearningStyleId(long learningStyleId) {
        this.learningStyleId = learningStyleId;
    }
    
    /**
     * toString override to return Apprentice name.
     * 
     * @return <code>String</code> of Apprentice name.
     */
    @Override
    public String toString() {
        return this.name;
    }
}
