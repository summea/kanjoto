
package com.summea.kanjoto.model;

/**
 * Edge is a model for Edge objects.
 */
public class EdgeAndRelated {

    private Edge edge = new Edge();
    private Label label = new Label();

    /**
     * getEdge gets edge
     * 
     * @return <code>Edge</code> edge object
     */
    public Edge getEdge() {
        return edge;
    }

    /**
     * setEdge sets edge
     * 
     * @param edge New Edge object.
     */
    public void setEdge(Edge edge) {
        this.edge = edge;
    }

    /**
     * getLabel gets related Label
     * 
     * @return <code>Label</code> label object
     */
    public Label getLabel() {
        return label;
    }

    /**
     * setLabel sets label
     * 
     * @param label New label object.
     */
    public void setLabel(Label label) {
        this.label = label;
    }

    /**
     * toString override to return edge name.
     * 
     * @return <code>String</code> of edge name.
     */
    @Override
    public String toString() {
        return "edge and related items";
    }
}
