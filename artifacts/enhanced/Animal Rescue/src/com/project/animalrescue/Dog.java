package com.project.animalrescue;

public class Dog extends RescueAnimal {

    // Instance variables
    private String breed;

    // Constructor
    public Dog(String name, String breed, String gender, String age,
               String weight, String acquisitionDate, String acquisitionCountry,
               String trainingStatus, boolean reserved, String inServiceCountry, String lastCheckup) {
        super(name, "dog", gender, age, weight, acquisitionDate, acquisitionCountry, trainingStatus, reserved, inServiceCountry, lastCheckup);
        this.breed = breed;
    }

    // Accessor Methods
    public String getBreed() {
        return breed;
    }

    // Mutator Methods
    public void setBreed(String breed) {
        this.breed = breed;
    }

    // Overriding toString method to display all relevant information
    @Override
    public String toString() {
        return "Dog [name=" + getName() + ", breed=" + breed + ", gender=" + getGender() + ", age=" + getAge() + 
                ", weight=" + getWeight() + ", acquisitionDate=" + getAcquisitionDate() + ", acquisitionCountry=" + getAcquisitionLocation() + 
                ", trainingStatus=" + getTrainingStatus() + ", reserved=" + getReserved() + ", inServiceCountry=" + getInServiceLocation() +
                ", lastCheckup=" + getLastCheckup() + ", healthStatus=" + getHealthStatus() + "]";
    }
}
