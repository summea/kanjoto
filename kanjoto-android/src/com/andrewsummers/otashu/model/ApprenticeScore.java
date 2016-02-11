
package com.andrewsummers.otashu.model;

/**
 * ApprenticeScore is a model for ApprenticeScore objects.
 */
public class ApprenticeScore {
    private long id;
    private long scorecardId;
    private int questionNumber;
    private int correct;
    private long notevalue;

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
     * getQuestionNumber gets ApprenticeScore question number
     * 
     * @return <code>int</code> questionNumber value
     */
    public int getQuestionNumber() {
        return questionNumber;
    }

    /**
     * setQuestionNumber sets ApprenticeScore question number
     * 
     * @param questionNumber New questionNumber value.
     */
    public void setQuestionNumber(int questionNumber) {
        this.questionNumber = questionNumber;
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
     * getNotevalue gets ApprenticeScore edge that connects the two related nodes (notes)
     * 
     * @return <code>long</code> of ApprenticeScore notevalue
     */
    public long getNotevalue() {
        return this.notevalue;
    }

    /**
     * setNotevalue sets ApprenticeScore edge that connects the two related nodes (notes)
     * 
     * @param notevalue New ApprenticeScore notevalue
     */
    public void setNotevalue(long notevalue) {
        this.notevalue = notevalue;
    }

    /**
     * toString override to return ApprenticeScore name.
     * 
     * @return <code>String</code> of ApprenticeScore name.
     */
    @Override
    public String toString() {
        return "id: " + id + " scorecard_id: " + scorecardId + " question_number: "
                + questionNumber + " correct: " + correct + " notevalue: " + notevalue + "\n";
    }
}
