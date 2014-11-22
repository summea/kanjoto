package com.andrewsummers.otashu.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Noteset is a model for Noteset objects.
 */
public class NotesetNote {
    
    private Noteset noteset = new Noteset();
    private List<Note> notes = new ArrayList<Note>();
    
    /**
     * getNoteset gets noteset
     * 
     * @return <code>Noteset</code> noteset object
     */
    public Noteset getNoteset() {
        return noteset;
    }

    /**
     * setNoteset sets noteset
     * 
     * @param noteset
     *            New noteset object.
     */
    public void setNoteset(Noteset noteset) {
        this.noteset = noteset;
    }
    
    /**
     * getNotes gets related notes
     * 
     * @return <code>List<Note></code> notes list
     */
    public List<Note> getNotes() {
        return notes;
    }

    /**
     * setNotes sets related notes
     * 
     * @param 
     *            New notes list value.
     */
    public void setNotes(List<Note> notes) {
        this.notes = notes;
    }
    
    /**
     * toString override to return noteset name.
     * 
     * @return <code>String</code> of noteset name.
     */
    @Override
    public String toString() {
        return "noteset and notes";
    }
}