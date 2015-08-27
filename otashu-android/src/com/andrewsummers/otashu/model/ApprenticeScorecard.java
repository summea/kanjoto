
package com.andrewsummers.otashu.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class ApprenticeScorecard {
    private long id;
    private String takenAt;
    private int total;
    private int correct;
    private long apprenticeId;
    private long graphId;
    private long scaleId;

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
        // get formatted string of test "taken at" datestamp
        // based on examples from:
        // http://stackoverflow.com/questions/17692863/converting-string-in-t-z-format-to-date
        TimeZone timezone = TimeZone.getTimeZone("UTC");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'", Locale.getDefault());
        sdf.setTimeZone(timezone);
        Date date = new Date();
        try {
            date = sdf.parse(takenAt);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date.toString();
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
     * getApprenticeId gets Apprentice id
     * 
     * @return <code>long</code> id value
     */
    public long getApprenticeId() {
        return apprenticeId;
    }

    /**
     * setApprenticeId sets Apprentice id
     * 
     * @param apprenticeId New apprenticeId value.
     */
    public void setApprenticeId(long apprenticeId) {
        this.apprenticeId = apprenticeId;
    }

    /**
     * getGraphId gets Graph id
     * 
     * @return <code>long</code> graphId value
     */
    public long getGraphId() {
        return graphId;
    }

    /**
     * setGraphId sets Graph id
     * 
     * @param graphId New graphId value.
     */
    public void setGraphId(long graphId) {
        this.graphId = graphId;
    }

    /**
     * getScaleId gets Scale id
     * 
     * @return <code>long</code> scaleId value
     */
    public long getScaleId() {
        return scaleId;
    }

    /**
     * setScaleId sets Scale id
     * 
     * @param scaleId New scaleId value.
     */
    public void setScaleId(long scaleId) {
        this.scaleId = scaleId;
    }

    /**
     * toString override to return ApprenticeScorecard id.
     * 
     * @return <code>String</code> of ApprenticeScorecard id.
     */
    @Override
    public String toString() {
        double percentage = 0d;

        if (this.total > 0) {
            percentage = ((double) this.correct / (double) this.total) * 100;
        }
        String guessesCorrectPercentageString = String.format(Locale.getDefault(), "%.02f",
                percentage);

        return "" + this.getTakenAt() + " " + this.correct + "/" + this.total + " ("
                + guessesCorrectPercentageString + "%)";
    }
}
