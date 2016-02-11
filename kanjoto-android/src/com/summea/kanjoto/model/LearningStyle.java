
package com.summea.kanjoto.model;

/**
 * LearningStyle is a model for LearningStyle objects.
 */
public class LearningStyle {
    private long id;
    private String name;

    /**
     * getId gets LearningStyle id
     * 
     * @return <code>long</code> id value
     */
    public long getId() {
        return id;
    }

    /**
     * setId sets LearningStyle id
     * 
     * @param id New id value.
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * getName gets LearningStyle name.
     * 
     * @return <code>String</code> of LearningStyle name.
     */
    public String getName() {
        return name;
    }

    /**
     * setName sets LearningStyle name.
     * 
     * @param name New LearningStyle name.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * toString override to return LearningStyle name.
     * 
     * @return <code>String</code> of LearningStyle name.
     */
    @Override
    public String toString() {
        return name;
    }
}
