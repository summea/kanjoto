
package com.andrewsummers.otashu.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Path is a model for Path objects.
 * <p>
 * Paths are a way to keep track of strong paths in a "caching" sort of way. Paths were previously
 * found dynamically (path information found via database queries at user's request) but this
 * approach makes it harder to work with path information in activities and lists.
 * </p>
 */
public class Path {

    private List<Edge> path = new ArrayList<Edge>();
    private long id;
    private int fromNodeId;
    private int toNodeId;
    private long apprenticeId;
    private int position;
    private int rank;

    /**
     * getPath gets path
     * 
     * @return <code>Path</code> path object
     */
    public List<Edge> getPath() {
        return path;
    }

    /**
     * setPath sets path
     * 
     * @param List<Edge> New path object.
     */
    public void setPath(List<Edge> path) {
        this.path = path;
    }

    /**
     * getId gets Path id
     * 
     * @return <code>long</code> id value
     */
    public long getId() {
        return id;
    }

    /**
     * setId sets Path id
     * 
     * @param id New id value.
     */
    public void setId(long id) {
        this.id = id;
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
     * getPosition gets Path position.
     * 
     * @return <code>int</code> of Path position.
     */
    public int getPosition() {
        return position;
    }

    /**
     * setPosition sets Path position.
     * 
     * @param position New Path position.
     */
    public void setPosition(int position) {
        this.position = position;
    }

    /**
     * getRank gets Path rank.
     * 
     * @return <code>int</code> of Path rank.
     */
    public int getRank() {
        return rank;
    }

    /**
     * setRank sets Path rank.
     * 
     * @param position New Path rank.
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
        return this.path.toString();
    }
}
