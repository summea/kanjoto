
package com.andrewsummers.otashu.model;

/**
 * Notevalue is a model for Notevalue objects.
 */
public class NotevalueAndRelated {

    private Notevalue notevalue = new Notevalue();
    private Label label = new Label();

    /**
     * getNotevalue gets notevalue
     * 
     * @return <code>Notevalue</code> notevalue object
     */
    public Notevalue getNotevalue() {
        return notevalue;
    }

    /**
     * setNotevalue sets notevalue
     * 
     * @param notevalue New Notevalue object.
     */
    public void setNotevalue(Notevalue notevalue) {
        this.notevalue = notevalue;
    }

    /**
     * getLabel gets related Label
     * 
     * @return <code>Label</code> label object
     */
    public Label getLabel() {
        return label;
    }

    /**
     * setLabel sets label
     * 
     * @param label New label object.
     */
    public void setLabel(Label label) {
        this.label = label;
    }

    /**
     * toString override to return notevalue name.
     * 
     * @return <code>String</code> of notevalue name.
     */
    @Override
    public String toString() {
        return "notevalue and related items";
    }
}
