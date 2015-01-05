package com.andrewsummers.otashu.model;

public class ApprenticeScorecard {
    private long id;
    private String takenAt;

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
     * toString override to return ApprenticeScorecard id.
     * 
     * @return <code>String</code> of ApprenticeScorecard id.
     */
    @Override
    public String toString() {
        return "" + this.id;
    }
}
