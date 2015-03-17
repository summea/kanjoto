
package com.andrewsummers.otashu.model;

/**
 * KeyNote is a model for KeyNote objects.
 */
public class KeyNote {
    private long id;
    private long keySignatureId;
    private int notevalue;

    /**
     * getId gets KeyNote id
     * 
     * @return <code>long</code> id value
     */
    public long getId() {
        return id;
    }

    /**
     * setId sets KeyNote id
     * 
     * @param id New id value.
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * getKeySignatureId gets KeyNote keySignatureId.
     * 
     * @return <code>long</code> of KeyNote keySignatureId.
     */
    public long getKeySignatureId() {
        return keySignatureId;
    }

    /**
     * setKeySignatureId sets KeyNote keySignatureId.
     * 
     * @param keySignatureId New KeyNote keySignatureId.
     */
    public void setKeySignatureId(long keySignatureId) {
        this.keySignatureId = keySignatureId;
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
     * @param notevalue New notevalue.
     */
    public void setNotevalue(int notevalue) {
        this.notevalue = notevalue;
    }

    /**
     * toString override to return KeyNote keySignatureId.
     * 
     * @return <code>String</code> of KeyNote keySignatureId.
     */
    @Override
    public String toString() {
        return id + "";
    }
}
