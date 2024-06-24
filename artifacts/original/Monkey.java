public class Monkey extends RescueAnimal {

    public int bodyLength;
    public int tailLength;
    public int height;
    public String species;

    public Monkey(String name, String gender, String age, String weight, String acquisitionDate, String acquisitionCountry,
                  String trainingStatus, boolean reserved, String inServiceCountry,
                  int bodyLength, int tailLength, int height, String species) {
        
        super(name, "monkey", gender, age, weight, acquisitionDate, acquisitionCountry, trainingStatus, reserved, inServiceCountry);
        this.bodyLength = bodyLength;
        this.tailLength = tailLength;
        this.height = height;
        this.species = species;
    }

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
}
