package com.project.animalrescue;

import java.util.Date;

public class AdoptionData {
    private double adoptionRate;
    private Date date;

    public AdoptionData(double adoptionRate, Date date) {
        this.adoptionRate = adoptionRate;
        this.date = date;
    }

    public double getAdoptionRate() {
        return adoptionRate;
    }

    public void setAdoptionRate(double adoptionRate) {
        this.adoptionRate = adoptionRate;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "AdoptionData{" +
                "adoptionRate=" + adoptionRate +
                ", date=" + date +
                '}';
    }
}
