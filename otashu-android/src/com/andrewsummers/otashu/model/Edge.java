
package com.andrewsummers.otashu.model;

/**
 * Edge is a model for Edge objects.
 */
public class Edge {
    private long id;
    private int fromId;
    private int toId;
    private float weight;

    /**
     * getId gets Edge id
     * 
     * @return <code>long</code> id value
     */
    public long getId() {
        return id;
    }

    /**
     * setId sets Edge id
     * 
     * @param id New id value.
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * getFromId gets Edge fromId.
     * 
     * @return <code>int</code> of Edge fromId.
     */
    public int getFromId() {
        return fromId;
    }

    /**
     * setFromId sets Edge fromId.
     * 
     * @param fromId New Edge fromId.
     */
    public void setfromId(int fromId) {
        this.fromId = fromId;
    }
    
    /**
     * getToId gets Edge toId.
     * 
     * @return <code>int</code> of Edge toId.
     */
    public int getToId() {
        return toId;
    }

    /**
     * setToId sets Edge toId.
     * 
     * @param toId New Edge toId.
     */
    public void setToId(int toId) {
        this.toId = toId;
    }

    /**
     * getWeight gets Edge weight.
     * 
     * @return <code>float</code> of Edge weight.
     */
    public float getWeight() {
        return weight;
    }

    /**
     * setWeight sets Edge weight.
     * 
     * @param weight New Edge weight.
     */
    public void setWeight(float weight) {
        this.weight = weight;
    }
    
    /**
     * toString override to return Edge id.
     * 
     * @return <code>String</code> of Edge id.
     */
    @Override
    public String toString() {
        return String.valueOf(id);
    }
}
