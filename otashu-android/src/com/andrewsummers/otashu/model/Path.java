
package com.andrewsummers.otashu.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Path is a model for Path objects.
 * 
 * <p>
 * Paths are a way to keep track of strong paths in a "caching"
 * sort of way. Paths were previously found dynamically (path information found via database queries
 * at user's request) but this approach makes it harder to work with path information in activities
 * and lists.
 * </p>
 */
public class Path {

    private List<Edge> path = new ArrayList<Edge>();
    private long id;
    private long edgeId;
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
     * getEdgeId gets Edge id
     * 
     * @return <code>long</code> id value
     */
    public long getEdgeId() {
        return edgeId;
    }

    /**
     * setEdgeId sets Edge id
     * 
     * @param edgeId New id value.
     */
    public void setEdgeId(long edgeId) {
        this.edgeId = edgeId;
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
