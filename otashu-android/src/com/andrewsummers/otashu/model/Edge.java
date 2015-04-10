
package com.andrewsummers.otashu.model;

/**
 * Edge is a model for Edge objects.
 */
public class Edge {
    private long id;
    private long graphId;
    private long emotionId;
    private int fromNodeId;
    private int toNodeId;
    private float weight;
    private int position;
    private long apprenticeId;

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
     * getGraphId gets Edge graphId
     * 
     * @return <code>long</code> graphId value
     */
    public long getGraphId() {
        return graphId;
    }

    /**
     * setGraphId sets Edge graphId
     * 
     * @param graphId New graphId value.
     */
    public void setGraphId(long graphId) {
        this.graphId = graphId;
    }

    /**
     * getEmotionId gets Edge emotionId
     * 
     * @return <code>long</code> emotionId value
     */
    public long getEmotionId() {
        return emotionId;
    }

    /**
     * setEmotionId sets Edge emotionId
     * 
     * @param emotionId New emotionId value.
     */
    public void setEmotionId(long emotionId) {
        this.emotionId = emotionId;
    }

    /**
     * getFromNodeId gets Edge fromNodeId.
     * 
     * @return <code>int</code> of Edge fromNodeId.
     */
    public int getFromNodeId() {
        return fromNodeId;
    }

    /**
     * setFromNodeId sets Edge fromNodeId.
     * 
     * @param fromNodeId New Edge fromNodeId.
     */
    public void setFromNodeId(int fromNodeId) {
        this.fromNodeId = fromNodeId;
    }

    /**
     * getToNodeId gets Edge toNodeId.
     * 
     * @return <code>int</code> of Edge toNodeId.
     */
    public int getToNodeId() {
        return toNodeId;
    }

    /**
     * setToNodeId sets Edge toNodeId.
     * 
     * @param toNodeId New Edge toNodeId.
     */
    public void setToNodeId(int toNodeId) {
        this.toNodeId = toNodeId;
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
     * getPosition gets Edge position.
     * 
     * @return <code>int</code> of Edge position.
     */
    public int getPosition() {
        return position;
    }

    /**
     * setPosition sets Edge position.
     * 
     * @param position New Edge position.
     */
    public void setPosition(int position) {
        this.position = position;
    }

    /**
     * getApprenticeId gets Apprentice id
     * 
     * @return <code>long</code> id value
     */
    public long getApprenticeId() {
        return apprenticeId;
    }

    /**
     * setApprenticeId sets Apprentice id
     * 
     * @param apprenticeId New apprenticeId value.
     */
    public void setApprenticeId(long apprenticeId) {
        this.apprenticeId = apprenticeId;
    }

    /**
     * toString override to return Edge id.
     * 
     * @return <code>String</code> of Edge id.
     */
    @Override
    public String toString() {
        return String.valueOf(id) + "|" + graphId + "|" + emotionId + "|" + fromNodeId
                + "|" + toNodeId + "|" + weight + "|" + position;
    }
}
