
package com.andrewsummers.otashu.model;

/**
 * Achievement is a model for Achievement objects.
 */
public class Achievement {
    private long id;
    private String name;
    private long apprenticeId;
    private String earnedOn;
    private String key;

    /**
     * getId gets Achievement id
     * 
     * @return <code>long</code> id value
     */
    public long getId() {
        return id;
    }

    /**
     * setId sets Achievement id
     * 
     * @param id New id value.
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * getName gets Achievement name.
     * 
     * @return <code>String</code> of Achievement name.
     */
    public String getName() {
        return name;
    }

    /**
     * setName sets Achievement name.
     * 
     * @param name New Achievement name.
     */
    public void setName(String name) {
        this.name = name;
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
     * getEarnedOn gets ApprenticeScore earned-at date-timestamp.
     * 
     * @return <code>String</code> of ApprenticeScore earned-at date-timestamp.
     * @throws java.text.ParseException
     */
    public String getEarnedOn() {
        return earnedOn;
    }

    /**
     * setEarnedOn sets ApprenticeScore earned-at date-timestamp.
     * 
     * @param earnedOn New ApprenticeScore earned-at date-timestamp.
     */
    public void setEarnedOn(String earnedOn) {
        this.earnedOn = earnedOn;
    }

    /**
     * getKey gets Achievement key.
     * 
     * @return <code>String</code> of Achievement key.
     */
    public String getKey() {
        return key;
    }

    /**
     * setKey sets Achievement key.
     * 
     * @param name New Achievement key.
     */
    public void setKey(String key) {
        this.key = key;
    }

    /**
     * toString override to return Achievement name.
     * 
     * @return <code>String</code> of Achievement name.
     */
    @Override
    public String toString() {
        return name;
    }
}
