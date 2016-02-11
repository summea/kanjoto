
package com.summea.kanjoto.model;

/**
 * Label is a model for Label objects.
 */
public class Label {
    private long id;
    private String name;
    private String color;

    /**
     * getId gets Label id
     * 
     * @return <code>long</code> id value
     */
    public long getId() {
        return id;
    }

    /**
     * setId sets Label id
     * 
     * @param id New id value.
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * getName gets Label name.
     * 
     * @return <code>String</code> of Label name.
     */
    public String getName() {
        return name;
    }

    /**
     * setName sets Label name.
     * 
     * @param name New Label name.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * getColor gets Label name.
     * 
     * @return <code>String</code> of Label color.
     */
    public String getColor() {
        String colorToReturn = "#dddddd";
        if (!color.isEmpty())
            colorToReturn = color;
        return colorToReturn;
    }

    /**
     * setColor sets Label color.
     * 
     * @param color New Label color.
     */
    public void setColor(String color) {
        this.color = color;
    }

    /**
     * toString override to return Label name.
     * 
     * @return <code>String</code> of Label name.
     */
    @Override
    public String toString() {
        return name;
    }
}
