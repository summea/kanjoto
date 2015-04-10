
package com.andrewsummers.otashu.model;

import java.util.Locale;

public class ApprenticeScorecard {
    private long id;
    private String takenAt;
    private int total;
    private int correct;
    private long apprenticeId;

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
     * @throws java.text.ParseException
     */
    public String getTakenAt() {
        /*
         * String formattedDate = ""; SimpleDateFormat sdf = new
         * SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'", Locale.getDefault()); try { Date date =
         * sdf.parse(takenAt); formattedDate = new SimpleDateFormat("yyyy-MM-dd HH:mm",
         * Locale.getDefault()) .format(date); } catch (ParseException e) { Log.d("MYLOG",
         * e.toString()); } catch (java.text.ParseException e) { Log.d("MYLOG", e.toString()); }
         * return formattedDate;
         */
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
