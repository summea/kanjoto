
package com.andrewsummers.otashu.model;

/**
 * Notevalue is a model for Notevalue objects.
 */
public class Notevalue {
    private long id;
    private int notevalue;
    private String notelabel;
    private long label_id;

    /**
     * getId gets Notevalue id
     * 
     * @return <code>long</code> id value
     */
    public long getId() {
        return id;
    }

    /**
     * setId sets Notevalue id
     * 
     * @param id New id value.
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * getNotevalue gets Notevalue notevalue.
     * 
     * @return <code>int</code> of notevalue.
     */
    public int getNotevalue() {
        return notevalue;
    }

    /**
     * setNotevalue sets Notevalue notevalue.
     * 
     * @param notevalue New notevalue.
     */
    public void setNotevalue(int notevalue) {
        this.notevalue = notevalue;
    }

    /**
     * getNotelabel gets Notevalue notelabel.
     * 
     * @return <code>String</code> of notelabel.
     */
    public String getNotelabel() {
        return notelabel;
    }

    /**
     * setNotelabel sets Notevalue notelabel.
     * 
     * @param notelabel New notelabel.
     */
    public void setNotelabel(String notelabel) {
        this.notelabel = notelabel;
    }

    /**
     * getLabelId gets Notevalue label_id.
     * 
     * @return <code>long</code> of label_id.
     */
    public long getLabelId() {
        return label_id;
    }

    /**
     * setLabelId sets Notevalue label_id.
     * 
     * @param label_id New label_id.
     */
    public void setLabelId(long label_id) {
        this.label_id = label_id;
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
