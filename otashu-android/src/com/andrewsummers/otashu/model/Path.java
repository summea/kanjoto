
package com.andrewsummers.otashu.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Path is a model for Path objects.
 */
public class Path {

    private List<Edge> path = new ArrayList<Edge>();

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
     * toString override to return path name.
     * 
     * @return <code>String</code> of path name.
     */
    @Override
    public String toString() {
        return this.path.toString();
    }
}
