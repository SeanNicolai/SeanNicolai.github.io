package com.project.animalrescue;

public class Monkey extends RescueAnimal {

    private int bodyLength;
    private int tailLength;
    private int height;
    private String species;

    // Constructor
    public Monkey(String name, String gender, String age, String weight, String acquisitionDate, String acquisitionCountry,
                  String trainingStatus, boolean reserved, String inServiceCountry, String lastCheckup,
                  int bodyLength, int tailLength, int height, String species) {
        super(name, "monkey", gender, age, weight, acquisitionDate, acquisitionCountry, trainingStatus, reserved, inServiceCountry, lastCheckup);
        this.bodyLength = bodyLength;
        this.tailLength = tailLength;
        this.height = height;
        this.species = species;
    }

    // Accessor and Mutator Methods
    public void setBodyLength(int bodyLength) {
        this.bodyLength = bodyLength;
    }

    public void setTailLength(int tailLength) {
        this.tailLength = tailLength;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public int getBodyLength() {
        return bodyLength;
    }

    public int getTailLength() {
        return tailLength;
    }

    public int getHeight() {
        return height;
    }

    public String getSpecies() {
        return species;
    }

    // Overriding toString method to display all relevant information
    @Override
    public String toString() {
        return "Monkey [name=" + getName() + ", species=" + species + ", gender=" + getGender() + ", age=" + getAge() + 
                ", weight=" + getWeight() + ", acquisitionDate=" + getAcquisitionDate() + ", acquisitionCountry=" + getAcquisitionLocation() + 
                ", trainingStatus=" + getTrainingStatus() + ", reserved=" + getReserved() + ", inServiceCountry=" + getInServiceLocation() +
                ", lastCheckup=" + getLastCheckup() + ", healthStatus=" + getHealthStatus() + ", bodyLength=" + bodyLength +
                ", tailLength=" + tailLength + ", height=" + height + "]";
    }
}
