package com.example.yanghanwen.subbook;

/**
 * Created by yanghanwen on 2018-01-29.
 *
 * Subscription class
 */


/**
 * Information of a subscription including
 * name, date, monthly charge and comment
 * along with setters and getters
 */
public class Subscription {
    public String name;
    public String date;
    public float charge;
    public String comment;

    /**
     * constructor for initializing
     * @param name
     * @param date
     * @param charge
     * @param comment
     */
    public Subscription(String name, String date, float charge, String comment) {
        this.name = name;
        this.date = date;
        this.charge = charge;
        this.comment = comment;
    }


    /**
     * getter
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * setter
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public float getCharge() {
        return charge;
    }

    public void setCharge(float charge) {
        this.charge = charge;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    /**
     * return all information in a certain form
     * with every entry in a single line
     * @return
     */
    @Override
    public String toString() {
        String all = name + '\n' + date + '\n' + charge + '\n'+ comment;
        return all;
    }
}
