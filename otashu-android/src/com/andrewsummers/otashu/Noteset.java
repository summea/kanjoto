package com.andrewsummers.otashu;

/**
 * Noteset is a model for Noteset objects.
 */
public class Noteset {
    private long id;
    private String name;

    /**
     * getId gets Noteset id
     * 
     * @return <code>long</code> id value
     */
    public long getId() {
        return id;
    }

    /**
     * setID sets Noteset id
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
     * toString override to return noteset name.
     * 
     * @return <code>String</code> of noteset name.
     */
    @Override
    public String toString() {
        return name;
    }
}