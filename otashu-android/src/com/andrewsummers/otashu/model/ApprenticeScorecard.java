
package com.andrewsummers.otashu.model;

public class ApprenticeScorecard {
    private long id;
    private String takenAt;
    private int total;
    private int correct;

    /**
     * getId gets ApprenticeScorecard id
     * 
     * @return <code>long</code> id value
     */
    public long getId() {
        return id;
    }

    /**
     * setId sets ApprenticeScorecard id
     * 
     * @param id New id value.
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * getTakenAt gets ApprenticeScore taken-at date-timestamp.
     * 
     * @return <code>String</code> of ApprenticeScore taken-at date-timestamp.
     */
    public String getTakenAt() {
        return takenAt;
    }

    /**
     * setTakenAt sets ApprenticeScore taken-at date-timestamp.
     * 
     * @param takenAt New ApprenticeScore taken-at date-timestamp.
     */
    public void setTakenAt(String takenAt) {
        this.takenAt = takenAt;
    }

    /**
     * getTotal gets ApprenticeScorecard total number of test questions
     * 
     * @return <code>int</code> total number of test questions
     */
    public int getTotal() {
        return total;
    }

    /**
     * setTotal sets ApprenticeScorecard total number of test questions
     * 
     * @param total New total value.
     */
    public void setTotal(int total) {
        this.total = total;
    }

    /**
     * getTotal gets ApprenticeScorecard total number of correct test questions
     * 
     * @return <code>int</code> total number of correct test questions
     */
    public int getCorrect() {
        return correct;
    }

    /**
     * setTotal sets ApprenticeScorecard total number of correct test questions
     * 
     * @return <code>int</code> total number of correct test questions
     */
    public void setCorrect(int correct) {
        this.correct = correct;
    }

    /**
     * toString override to return ApprenticeScorecard id.
     * 
     * @return <code>String</code> of ApprenticeScorecard id.
     */
    @Override
    public String toString() {
        return "" + this.id + " " + this.correct + "/" + this.total + " ("
                + ((this.correct / this.total) * 100) + "%)";
    }
}
