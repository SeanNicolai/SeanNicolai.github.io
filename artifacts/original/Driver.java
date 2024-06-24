import java.util.ArrayList;
import java.util.Scanner;

public class Driver {
    private static ArrayList<Dog> dogList = new ArrayList<Dog>();
    private static ArrayList<Monkey> monkeyList = new ArrayList<Monkey>();

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in); 
        boolean exit = false; 

        initializeDogList();
        initializeMonkeyList();

        while (!exit) {
            displayMenu();
            char option = input.next().charAt(0);

            switch (option) {
                case '1':
                    intakeNewDog(input);
                    break;
                case '2':
                    intakeNewMonkey(input);
                    break;
                case '3':
                    reserveAnimal(input);
                    break;
                case '4':
                    printAnimals("dogs");
                    break;
                case '5':
                    printAnimals("monkeys");
                    break;
                case '6':
                    printAnimals("not_reserved");
                    break;
                case 'q':
                    exit = true;
                    break;
                default:
                    System.out.println("Please enter valid input");
                    break;
            }
        }
    }

    public static void displayMenu() {
        System.out.println("\n\n");
        System.out.println("\t\t\t\tRescue Animal System Menu");
        System.out.println("[1] Intake a new dog");
        System.out.println("[2] Intake a new monkey");
        System.out.println("[3] Reserve an animal");
        System.out.println("[4] Print a list of all dogs");
        System.out.println("[5] Print a list of all monkeys");
        System.out.println("[6] Print a list of all animals that are not reserved");
        System.out.println("[q] Quit application");
        System.out.println();
        System.out.println("Enter a menu selection");
    }

    public static void initializeDogList() {
    
    }

    public static void initializeMonkeyList() {
     
    }

    public static void intakeNewDog(Scanner scanner) {
        System.out.println("Enter details for the new Dog");
        Dog newDog = new Dog(scanner.next(), scanner.next(), scanner.next(), scanner.next(), 
                             scanner.next(), scanner.next(), scanner.next(), scanner.next(), 
                             scanner.nextBoolean(), scanner.next());
        dogList.add(newDog);
    }

    public static void intakeNewMonkey(Scanner scanner) {
        System.out.println("Enter details for the new Monkey");
        Monkey newMonkey = new Monkey(scanner.next(), scanner.next(), scanner.next(), 
                                      scanner.next(), scanner.next(), scanner.next(), scanner.next(), 
                                      scanner.nextBoolean(), scanner.next(), scanner.nextInt(), 
                                      scanner.nextInt(), scanner.nextInt(), scanner.next());
        monkeyList.add(newMonkey);
    }

public static void reserveAnimal(Scanner scanner) {
    System.out.println("Enter the name of the animal you want to reserve: ");
    String name = scanner.next();
    for (Dog dog : dogList) {
        if (dog.getName().equals(name)) {
            if (!dog.getReserved() && dog.getTrainingStatus().equalsIgnoreCase("in service")) {
                dog.setReserved(true);
                System.out.println("The dog named " + name + " is now reserved.");
            } else if (dog.getReserved()) {
                System.out.println("The dog named " + name + " is already reserved.");
            } else {
                System.out.println("The dog named " + name + " is not in service and cannot be reserved.");
            }
            return;
        }
    }
    for (Monkey monkey : monkeyList) {
        if (monkey.getName().equals(name)) {
            if (!monkey.getReserved() && monkey.getTrainingStatus().equalsIgnoreCase("in service")) {
                monkey.setReserved(true);
                System.out.println("The monkey named " + name + " is now reserved.");
            } else if (monkey.getReserved()) {
                System.out.println("The monkey named " + name + " is already reserved.");
            } else {
                System.out.println("The monkey named " + name + " is not in service and cannot be reserved.");
            }
            return;
        }
    }
    System.out.println("The animal named " + name + " could not be found.");
}


    public static void printAnimals(String type) {
        switch (type) {
            case "dogs":
                for (Dog dog : dogList) {
                    System.out.println(dog);
                }
                break;
            case "monkeys":
                for (Monkey monkey : monkeyList) {
                    System.out.println(monkey);
                }
                break;
            case "not_reserved":
                for (Dog dog : dogList) {
                    if (!dog.getReserved()) {
                        System.out.println(dog);
                    }
                }
                for (Monkey monkey : monkeyList) {
                    if (!monkey.getReserved()) {
                        System.out.println(monkey);
                    }
                }
                break;
        }
    }
}