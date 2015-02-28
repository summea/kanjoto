
package com.andrewsummers.otashu.model;

/**
 * Apprentice is a model for Apprentice objects.
 */
public class Apprentice {
    private int state;

    public Apprentice() {
    }

    public Apprentice(int state) {
        this.state = state;
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
}
