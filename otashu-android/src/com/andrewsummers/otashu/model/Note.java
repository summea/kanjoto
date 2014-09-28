package com.andrewsummers.otashu.model;

/**
 * Note is a model for Note objects.
 */
public class Note {
    private long id;
    private long noteset_id;
    private int notevalue;
    private int velocity;
    private float length;
    private int position;

    /**
     * getId gets Note id
     * 
     * @return <code>long</code> id value
     */
    public long getId() {
        return id;
    }

    /**
     * setId sets Note id
     * 
     * @param id
     *            New id value.
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * getNotesetId gets Note parent noteset_id.
     * 
     * @return <code>long</code> of parent noteset_id.
     */
    public long getNotesetId() {
        return noteset_id;
    }

    /**
     * setNotesetId sets Note parent noteset_id.
     * 
     * @param notesetId
     *            New parent noteset_id.
     */
    public void setNotesetId(long noteset_id) {
        this.noteset_id = noteset_id;
    }
    
    /**
     * getNotevalue gets Note notevalue.
     * 
     * @return <code>int</code> of notevalue.
     */
    public int getNotevalue() {
        return notevalue;
    }

    /**
     * setNotevalue sets Note notevalue.
     * 
     * @param notevalue
     *            New notevalue.
     */
    public void setNotevalue(int notevalue) {
        this.notevalue = notevalue;
    }
    
    /**
     * getVelocity gets Note velocity.
     * 
     * @return <code>int</code> of velocity.
     */
    public int getVelocity() {
        return velocity;
    }

    /**
     * setVelocity sets Note velocity.
     * 
     * @param velocity
     *            New velocity.
     */
    public void setVelocity(int velocity) {
        this.velocity = velocity;
    }
    
    /**
     * getLength gets Note length.
     * 
     * @return <code>int</code> of length.
     */
    public float getLength() {
        return length;
    }

    /**
     * setLength sets Note length.
     * 
     * @param length
     *            New length.
     */
    public void setLength(float length) {
        this.length = length;
    }
    
    /**
     * getPosition gets Note position.
     * 
     * @return <code>int</code> of position.
     */
    public int getPosition() {
        return position;
    }

    /**
     * setPosition sets Note position.
     * 
     * @param position
     *            New position.
     */
    public void setPosition(int position) {
        this.position = position;
    }

    /**
     * toString override to return note values.
     * 
     * @return <code>String</code> of note values.
     */
    @Override
    public String toString() {
        return String.valueOf(notevalue);
    }
}