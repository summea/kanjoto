
package com.andrewsummers.otashu.model;

/**
 * ApprenticeScore is a model for ApprenticeScore objects.
 */
public class ApprenticeScore {
    private long id;
    private long scorecardId;
    private int correct;
    private int emotionId;
    private String noteset;

    /**
     * getId gets ApprenticeScore id
     * 
     * @return <code>long</code> id value
     */
    public long getId() {
        return id;
    }

    /**
     * setId sets ApprenticeScore id
     * 
     * @param id New id value.
     */
    public void setId(long id) {
        this.id = id;
    }
    
    /**
     * getScorecardId gets ApprenticeScore scorecardId
     * 
     * @return <code>long</code> scorecardId value
     */
    public long getScorecardId() {
        return scorecardId;
    }

    /**
     * setScorecardId sets ApprenticeScore scorecardId
     * 
     * @param scorecardId New scorecardId value.
     */
    public void setScorecardId(long scorecardId) {
        this.scorecardId = scorecardId;
    }

    /**
     * getCorrect gets ApprenticeScore number of correct questions.
     * 
     * @return <code>int</code> of ApprenticeScore number of correct questions.
     */
    public int getCorrect() {
        return correct;
    }

    /**
     * setCorrect sets ApprenticeScore number of correct questions.
     * 
     * @param correct New ApprenticeScore number of correct questions.
     */
    public void setCorrect(int correct) {
        this.correct = correct;
    }
    
    /**
     * getEmotionId gets related ApprenticeScore emotion id
     * 
     * @return <code>int</code> of ApprenticeScore emotion id
     */
    public int getEmotionId() {
        return emotionId;
    }

    /**
     * setEmotionId sets related ApprenticeScore emotion id
     * 
     * @param emotionId New ApprenticeScore emotion id
     */
    public void setEmotionId(int emotionId) {
        this.emotionId = emotionId;
    }
    
    /**
     * getNoteset gets ApprenticeScore noteset that was used for this question
     * 
     * @return <code>String</code> of ApprenticeScore noteset
     */
    public String getNoteset() {
        return this.noteset;
    }

    /**
     * setNoteset sets ApprenticeScore noteset that was used for this question
     * 
     * @param noteset New ApprenticeScore noteset
     */
    public void setNoteset(String noteset) {
        this.noteset = noteset;
    }

    /**
     * toString override to return ApprenticeScore name.
     * 
     * @return <code>String</code> of ApprenticeScore name.
     */
    @Override
    public String toString() {
        return noteset;
    }
}
