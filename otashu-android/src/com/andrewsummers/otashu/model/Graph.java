
package com.andrewsummers.otashu.model;

/**
 * Graph is a model for Graph objects.
 */
public class Graph {
    private long id;
    private String name;

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
     * toString override to return Graph id.
     * 
     * @return <code>String</code> of Graph id.
     */
    @Override
    public String toString() {
        return name;
    }
}
