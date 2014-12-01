package com.andrewsummers.otashu.model;

/**
 * NoteLabel is a model for NoteLabel objects.
 */
public class NotevalueLabel {
    private long id;
    private int notevalue;
    private long label_id;
    
    /**
     * getId gets NoteLabel id
     * 
     * @return <code>long</code> id value
     */
    public long getId() {
        return id;
    }

    /**
     * setId sets NoteLabel id
     * 
     * @param id
     *            New id value.
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * getNotevalue gets NoteLabel notevalue.
     * 
     * @return <code>int</code> of notevalue.
     */
    public int getNotevalue() {
        return notevalue;
    }

    /**
     * setNotevalue sets NoteLabel notevalue.
     * 
     * @param notevalue
     *            New notevalue.
     */
    public void setNotevalue(int notevalue) {
        this.notevalue = notevalue;
    }
    
    /**
     * getLabelId gets NoteLabel label_id.
     * 
     * @return <code>long</code> of label_id.
     */
    public long getLabelId() {
        return label_id;
    }

    /**
     * setLabelId sets NoteLabel label_id.
     * 
     * @param label_id
     *            New label_id.
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