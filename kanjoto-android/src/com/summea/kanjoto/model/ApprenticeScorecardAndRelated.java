
package com.summea.kanjoto.model;

import java.util.ArrayList;
import java.util.List;

public class ApprenticeScorecardAndRelated {
    private long id;
    private String takenAt;
    private List<ApprenticeScore> apprenticeScores = new ArrayList<ApprenticeScore>();

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
     * getApprenticeScores gets List<ApprenticeScore> scores
     * 
     * @return <code>List<ApprenticeScore></code> of List<ApprenticeScore> scores list
     */
    public List<ApprenticeScore> getApprenticeScores() {
        return apprenticeScores;
    }

    /**
     * setApprenticeScores sets List<ApprenticeScore> scores
     * 
     * @param apprenticeScores New List<ApprenticeScore> scores list
     */
    public void setApprenticeScores(List<ApprenticeScore> apprenticeScores) {
        this.apprenticeScores = apprenticeScores;
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
