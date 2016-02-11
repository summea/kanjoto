
package com.andrewsummers.kanjoto.model;

/**
 * Graph is a model for Graph objects.
 */
public class Graph {
    private long id;
    private String name;
    private long label_id;

    /**
     * getId gets Graph id
     * 
     * @return <code>long</code> id value
     */
    public long getId() {
        return id;
    }

    /**
     * setId sets Graph id
     * 
     * @param id New id value.
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * getName gets Graph name.
     * 
     * @return <code>String</code> of Graph name.
     */
    public String getName() {
        return name;
    }

    /**
     * setName sets Graph name.
     * 
     * @param name New Graph name.
     */
    public void setName(String name) {
        this.name = name;
    }
    
    /**
     * getLabelId gets Graph label_id.
     * 
     * @return <code>long</code> of label_id.
     */
    public long getLabelId() {
        return label_id;
    }

    /**
     * setLabelId sets Graph label_id.
     * 
     * @param label_id New label_id.
     */
    public void setLabelId(long label_id) {
        this.label_id = label_id;
    }

    /**
     * toString override to return Graph id.
     * 
     * @return <code>String</code> of Graph id.
     */
    @Override
    public String toString() {
        return name;
    }
}
