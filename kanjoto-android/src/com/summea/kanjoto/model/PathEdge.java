
package com.summea.kanjoto.model;

import java.util.ArrayList;
import java.util.List;

/**
 * PathEdge is a model for PathEdge objects.
 */
public class PathEdge {

    private List<Edge> path = new ArrayList<Edge>();
    private long id;
    private long pathId;
    private int fromNodeId;
    private int toNodeId;
    private long apprenticeId;
    private long emotionId;
    private int position;
    private int rank;

    /**
     * getPathEdge gets path
     * 
     * @return <code>PathEdge</code> path object
     */
    public List<Edge> getPathEdge() {
        return path;
    }

    /**
     * setPathEdge sets path
     * 
     * @param List<Edge> New path object.
     */
    public void setPathEdge(List<Edge> path) {
        this.path = path;
    }

    /**
     * getId gets PathEdge id
     * 
     * @return <code>long</code> id value
     */
    public long getId() {
        return id;
    }

    /**
     * setId sets PathEdge id
     * 
     * @param id New id value.
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * getPathId gets PathEdge pathId
     * 
     * @return <code>long</code> pathId value
     */
    public long getPathId() {
        return pathId;
    }

    /**
     * setPathId sets PathEdge pathId
     * 
     * @param pathId New pathId value.
     */
    public void setPathId(long pathId) {
        this.pathId = pathId;
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
     * getEmotionId gets PathEdge emotionId
     * 
     * @return <code>long</code> emotionId value
     */
    public long getEmotionId() {
        return emotionId;
    }

    /**
     * setEmotionId sets PathEdge emotionId
     * 
     * @param emotionId New emotionId value.
     */
    public void setEmotionId(long emotionId) {
        this.emotionId = emotionId;
    }

    /**
     * getPosition gets PathEdge position.
     * 
     * @return <code>int</code> of PathEdge position.
     */
    public int getPosition() {
        return position;
    }

    /**
     * setPosition sets PathEdge position.
     * 
     * @param position New PathEdge position.
     */
    public void setPosition(int position) {
        this.position = position;
    }

    /**
     * getRank gets PathEdge rank.
     * 
     * @return <code>int</code> of PathEdge rank.
     */
    public int getRank() {
        return rank;
    }

    /**
     * setRank sets PathEdge rank.
     * 
     * @param position New PathEdge rank.
     */
    public void setRank(int rank) {
        this.rank = rank;
    }

    /**
     * toString override to return path name.
     * 
     * @return <code>String</code> of path name.
     */
    @Override
    public String toString() {
        return "id: " + id + ", pathId: " + pathId + ", fromNodeId: " + fromNodeId + ", toNodeId: "
                + toNodeId + ", apprenticeId: " + apprenticeId + ", emotionId: " + emotionId
                + ", position: " + position + ", rank: " + rank;
    }
}
