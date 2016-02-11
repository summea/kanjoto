
package com.andrewsummers.otashu.model;

/**
 * Path is a model for Path objects.
 * <p>
 * Paths are a way to keep track of strong paths in a "caching" sort of way. Paths were previously
 * found dynamically (path information found via database queries at user's request) but this
 * approach makes it harder to work with path information in activities and lists.
 * </p>
 */
public class Path {
    private long id;
    private String name;

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
     * getName gets Path name.
     * 
     * @return <code>String</code> of Path name.
     */
    public String getName() {
        return name;
    }

    /**
     * setName sets Path name.
     * 
     * @param name New Path name.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * toString override to return path name.
     * 
     * @return <code>String</code> of path name.
     */
    @Override
    public String toString() {
        return "id: " + id + ", name: " + name;
    }
}
