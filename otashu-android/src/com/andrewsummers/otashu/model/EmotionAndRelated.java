package com.andrewsummers.otashu.model;

/**
 * Emotion is a model for Emotion objects.
 */
public class EmotionAndRelated {
    
    private Emotion emotion = new Emotion();
    private Label label = new Label();
    
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
     * @param emotion
     *            New Emotion object.
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
     * @param label
     *            New label object.
     */
    public void setLabel(Label label) {
        this.label = label;
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