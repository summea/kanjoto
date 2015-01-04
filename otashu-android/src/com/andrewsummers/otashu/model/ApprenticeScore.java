
package com.andrewsummers.otashu.model;

import java.util.Locale;

/**
 * ApprenticeScore is a model for ApprenticeScore objects.
 */
public class ApprenticeScore {
    private long id;
    private int correct;
    private int total;
    private String takenAt;

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
     * getTotal gets ApprenticeScore total number of questions.
     * 
     * @return <code>int</code> of ApprenticeScore total number of questions.
     */
    public int getTotal() {
        return total;
    }

    /**
     * setTotal sets ApprenticeScore total number of questions.
     * 
     * @param total New ApprenticeScore total number of questions.
     */
    public void setTotal(int total) {
        this.total = total;
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
     * getPercentage gets a string-based percentage value of correct/total results.
     * 
     * @return <code>String</code> of ApprenticeScore percentage correct.
     */
    public String getPercentage() {
        double correctPercentage = 0.0;
        
        if (this.total > 0) {
            correctPercentage = ((double) this.correct / (double) this.total) * 100.0;
        }

        String correctPercentageString = String.format(Locale.getDefault(), "%.02f",
                correctPercentage);
        
        return correctPercentageString;
    }

    /**
     * toString override to return ApprenticeScore name.
     * 
     * @return <code>String</code> of ApprenticeScore name.
     */
    @Override
    public String toString() {
        return correct + "/" + total;
    }
}
