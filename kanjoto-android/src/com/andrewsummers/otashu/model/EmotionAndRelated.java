
package com.andrewsummers.otashu.model;

/**
 * Emotion is a model for Emotion objects.
 */
public class EmotionAndRelated {

    private Emotion emotion = new Emotion();
    private Label label = new Label();
    private float certainty = 0.0f;
    private String method;

    /**
     * getEmotion gets emotion
     * 
     * @return <code>Emotion</code> emotion object
     */
    public Emotion getEmotion() {
        return emotion;
    }

    /**
     * setEmotion sets emotion
     * 
     * @param emotion New Emotion object.
     */
    public void setEmotion(Emotion emotion) {
        this.emotion = emotion;
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
     * getCertainty gets related Certainty
     * 
     * @return <code>float</code> certainty value
     */
    public float getCertainty() {
        return certainty;
    }

    /**
     * setCertainty sets certainty
     * 
     * @param float New Certainty object.
     */
    public void setCertainty(float certainty) {
        this.certainty = certainty;
    }
    
    /**
     * getMethod gets related selection Method
     * 
     * @return <code>String</code> selection method
     */
    public String getMethod() {
        return method;
    }

    /**
     * setMethod sets selection method
     * 
     * @param String new selection method
     */
    public void setMethod(String method) {
        this.method = method;
    }

    /**
     * toString override to return emotion name.
     * 
     * @return <code>String</code> of emotion name.
     */
    @Override
    public String toString() {
        return "emotion and related items";
    }
}
