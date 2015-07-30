
package com.andrewsummers.otashu.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Path is a model for Path objects.
 */
public class PathAndRelated {

    private Path path = new Path();
    private List<PathEdge> pathEdges = new ArrayList<PathEdge>();

    /**
     * getPath gets path
     * 
     * @return <code>Path</code> path object
     */
    public Path getPath() {
        return path;
    }

    /**
     * setPath sets path
     * 
     * @param path New Path object.
     */
    public void setPath(Path path) {
        this.path = path;
    }

    /**
     * getPathEdge gets related PathEdge
     * 
     * @return <code>List&lt;PathEdge&gt;</code> pathEdges object
     */
    public List<PathEdge> getPathEdge() {
        return pathEdges;
    }

    /**
     * setPathEdge sets pathEdges
     * 
     * @param pathEdges New List<PathEdge> object.
     */
    public void setPathEdge(List<PathEdge> pathEdges) {
        this.pathEdges = pathEdges;
    }

    /**
     * toString override to return path name.
     * 
     * @return <code>String</code> of path name.
     */
    @Override
    public String toString() {
        return "path: " + path.toString() + " path edges: " + pathEdges.toString();
    }
}
