
package com.andrewsummers.otashu.model;

/**
 * Bookmark is a model for Bookmark objects. serializedValue is a serialized value of notesets (with
 * the following properties): notevalue:velocity:length:position|
 */
public class Bookmark {
    private long id;
    private String name;
    private String serializedValue;

    /**
     * getId gets Bookmark id
     * 
     * @return <code>long</code> id value
     */
    public long getId() {
        return id;
    }

    /**
     * setId sets Bookmark id
     * 
     * @param id New id value.
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * getName gets Bookmark name.
     * 
     * @return <code>String</code> of Bookmark name.
     */
    public String getName() {
        return name;
    }

    /**
     * setName sets Bookmark name.
     * 
     * @param name New Bookmark name.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * getName gets Bookmark serializedValue.
     * 
     * @return <code>String</code> of Bookmark serializedValue.
     */
    public String getSerializedValue() {
        return serializedValue;
    }

    /**
     * setName sets Bookmark serializedValue.
     * 
     * @param name New Bookmark serializedValue.
     */
    public void setSerializedValue(String serializedValue) {
        this.serializedValue = serializedValue;
    }

    /**
     * toString override to return Bookmark name.
     * 
     * @return <code>String</code> of Bookmark name.
     */
    @Override
    public String toString() {
        return name;
    }
}
