package com.andrewsummers.otashu;

/**
 * Noteset is a model for Noteset objects.
 */
public class Noteset {
    private long id;
    private String notevalues;

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
     * getNotevalues gets Noteset note values.
     * 
     * @return <code>String</code> of note values.
     */
    public String getNotevalues() {
        return notevalues;
    }

    /**
     * setNotevalues sets Noteset note values.
     * 
     * @param notevalues
     *            New note values.
     */
    public void setNotevalues(String notevalues) {
        this.notevalues = notevalues;
    }

    /**
     * toString override to return note values.
     * 
     * @return <code>String</code> of note values.
     */
    @Override
    public String toString() {
        return notevalues;
    }
}