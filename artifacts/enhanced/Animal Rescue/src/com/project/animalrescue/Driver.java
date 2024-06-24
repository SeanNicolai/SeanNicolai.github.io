// Login using username: Admin and password: password

package com.project.animalrescue;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class Driver {
    private static final ArrayList<Dog> dogList = new ArrayList<>();
    private static final ArrayList<Monkey> monkeyList = new ArrayList<>();
    private static final ScheduleManager scheduleManager = new ScheduleManager(16); // Assuming 8 hours a day with 30-min slots
    private static final List<String> scheduledCheckups = new ArrayList<>(); // List to store scheduled checkups
    private static final String DB_URL = "jdbc:sqlite:database/rescue_animals.db"; // Local SQLite database
    private static String currentUsername = null;

    public static void main(String[] args) {
        // Load SQLite JDBC driver
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            System.out.println("Failed to load SQLite JDBC driver");
            return;
        }

        loadDogsFromDatabase();
        loadMonkeysFromDatabase();

        try (Scanner input = new Scanner(System.in)) {
            boolean exit = false;

            if (!authenticateUser(input)) {
                System.out.println("Authentication failed. Exiting...");
                return;
            }

            while (!exit) {
                displayMenu();
                char option = input.next().charAt(0);

                switch (option) {
                    case '1' -> intakeNewAnimal(input);
                    case '2' -> reserveAnimal(input);
                    case '3' -> printAnimals(input);
                    case '4' -> editAnimalInfo(input);
                    case '5' -> printScheduledCheckups();
                    case '6' -> scheduleCheckupForAnimal(input);
                    case '7' -> editScheduledCheckup(input);
                    case '8' -> performDatabaseAnalytics();
                    case '9' -> manageUsers(input);
                    case 'p' -> changePassword(input, currentUsername);
                    case 'q' -> exit = true;
                    default -> System.out.println("Please enter valid input");
                }
            }
        }
    }

    public static void displayMenu() {
        System.out.println("\n\n");
        System.out.println("\t\t\t\tRescue Animal System Menu");
        System.out.println("[1] Intake a new animal");
        System.out.println("[2] Reserve an animal");
        System.out.println("[3] Print animals");
        System.out.println("[4] Edit animal information");
        System.out.println("[5] Show scheduled checkups");
        System.out.println("[6] Schedule a checkup for an animal");
        System.out.println("[7] Edit scheduled checkup");
        System.out.println("[8] Perform data analytics on adoption trends");
        System.out.println("[9] Manage users");
        System.out.println("[p] Change your password");
        System.out.println("[q] Quit application");
        System.out.println();
        System.out.println("Enter a menu selection");
    }

    public static void loadDogsFromDatabase() {
        String sql = "SELECT * FROM dog";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Dog dog = new Dog(
                        rs.getString("name"),
                        rs.getString("breed"),
                        rs.getString("gender"),
                        rs.getString("age"),
                        rs.getString("weight"),
                        rs.getString("acquisition_date"),
                        rs.getString("acquisition_country"),
                        rs.getString("training_status"),
                        rs.getBoolean("reserved"),
                        rs.getString("in_service_country"),
                        rs.getString("last_checkup")
                );
                dogList.add(dog);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void loadMonkeysFromDatabase() {
        String sql = "SELECT * FROM monkey";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Monkey monkey = new Monkey(
                        rs.getString("name"),
                        rs.getString("gender"),
                        rs.getString("age"),
                        rs.getString("weight"),
                        rs.getString("acquisition_date"),
                        rs.getString("acquisition_country"),
                        rs.getString("training_status"),
                        rs.getBoolean("reserved"),
                        rs.getString("in_service_country"),
                        rs.getString("last_checkup"),
                        rs.getInt("body_length"),
                        rs.getInt("tail_length"),
                        rs.getInt("height"),
                        rs.getString("species")
                );
                monkeyList.add(monkey);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void intakeNewAnimal(Scanner scanner) {
        System.out.println("Enter the type of animal (dog/monkey): ");
        String type = scanner.next().toLowerCase();
        scanner.nextLine(); // Consume newline
        switch (type) {
            case "dog" -> {
                System.out.println("Enter details for the new Dog (name, breed, gender, age, weight, acquisition date, acquisition country, training status, reserved, in service country, last checkup):");
                String details = scanner.nextLine();
                String[] parts = details.split(", ");
                Dog newDog = new Dog(parts[0], parts[1], parts[2], parts[3], parts[4], parts[5], parts[6], parts[7], Boolean.parseBoolean(parts[8]), parts[9], parts[10]);
                dogList.add(newDog);
                saveDogToDatabase(newDog);
            }
            case "monkey" -> {
                System.out.println("Enter details for the new Monkey (name, gender, age, weight, acquisition date, acquisition country, training status, reserved, in service country, last checkup, body length, tail length, height, species):");
                String details = scanner.nextLine();
                String[] parts = details.split(", ");
                Monkey newMonkey = new Monkey(parts[0], parts[1], parts[2], parts[3], parts[4], parts[5], parts[6], Boolean.parseBoolean(parts[7]), parts[8], parts[9], Integer.parseInt(parts[10]), Integer.parseInt(parts[11]), Integer.parseInt(parts[12]), parts[13]);
                monkeyList.add(newMonkey);
                saveMonkeyToDatabase(newMonkey);
            }
            default -> System.out.println("Unknown animal type.");
        }
    }

    public static void saveDogToDatabase(Dog dog) {
        String sql = "INSERT INTO dog(name, breed, gender, age, weight, acquisition_date, acquisition_country, training_status, reserved, in_service_country, last_checkup) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, dog.getName());
            pstmt.setString(2, dog.getBreed());
            pstmt.setString(3, dog.getGender());
            pstmt.setString(4, dog.getAge());
            pstmt.setString(5, dog.getWeight());
            pstmt.setString(6, dog.getAcquisitionDate());
            pstmt.setString(7, dog.getAcquisitionLocation());
            pstmt.setString(8, dog.getTrainingStatus());
            pstmt.setBoolean(9, dog.getReserved());
            pstmt.setString(10, dog.getInServiceLocation());
            pstmt.setString(11, dog.getLastCheckup());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void saveMonkeyToDatabase(Monkey monkey) {
        String sql = "INSERT INTO monkey(name, gender, age, weight, acquisition_date, acquisition_country, training_status, reserved, in_service_country, last_checkup, body_length, tail_length, height, species) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, monkey.getName());
            pstmt.setString(2, monkey.getGender());
            pstmt.setString(3, monkey.getAge());
            pstmt.setString(4, monkey.getWeight());
            pstmt.setString(5, monkey.getAcquisitionDate());
            pstmt.setString(6, monkey.getAcquisitionLocation());
            pstmt.setString(7, monkey.getTrainingStatus());
            pstmt.setBoolean(8, monkey.getReserved());
            pstmt.setString(9, monkey.getInServiceLocation());
            pstmt.setString(10, monkey.getLastCheckup());
            pstmt.setInt(11, monkey.getBodyLength());
            pstmt.setInt(12, monkey.getTailLength());
            pstmt.setInt(13, monkey.getHeight());
            pstmt.setString(14, monkey.getSpecies());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void performDatabaseAnalytics() {
        String sql = "SELECT adoption_rate, date FROM animal_adoption_trends ORDER BY date DESC";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (!rs.isBeforeFirst()) { // Check if ResultSet is empty
                System.out.println("No adoption data available.");
                return;
            }

            List<AdoptionData> adoptionDataList = new ArrayList<>();
            while (rs.next()) {
                try {
                    AdoptionData data = new AdoptionData(
                            rs.getDouble("adoption_rate"),
                            sdf.parse(rs.getString("date"))
                    );
                    adoptionDataList.add(data);
                } catch (ParseException e) {
                    System.out.println("Error parsing date: " + rs.getString("date"));
                }
            }
            analyzeData(adoptionDataList);
        } catch (SQLException e) {
            System.out.println("Failed to perform database analytics: " + e.getMessage());
        }
    }

    public static void analyzeData(List<AdoptionData> data) {
        // Perform analysis - this is a placeholder for real analytics
        double totalAdoptionRate = 0;
        int count = 0;

        for (AdoptionData ad : data) {
            totalAdoptionRate += ad.getAdoptionRate();
            count++;
        }

        double averageAdoptionRate = (count != 0) ? totalAdoptionRate / count : 0;

        System.out.println("Total adoption records: " + count);
        System.out.println("Average adoption rate: " + averageAdoptionRate);
        System.out.println("Recent adoption trends: " + data);
        System.out.println("Analysis completed.");
    }

    public static void reserveAnimal(Scanner scanner) {
        System.out.println("Enter the name of the animal you want to reserve: ");
        String name = scanner.next();
        boolean animalFound = false;

        for (Dog dog : dogList) {
            if (dog.getName().equalsIgnoreCase(name)) {
                animalFound = true;
                if (!dog.getReserved() && dog.getTrainingStatus().equalsIgnoreCase("in_service")) {
                    dog.setReserved(true);
                    System.out.println("The dog named " + name + " is now reserved.");
                    updateDogReservationInDatabase(dog);
                } else if (dog.getReserved()) {
                    System.out.println("The dog named " + name + " is already reserved.");
                } else {
                    System.out.println("The dog named " + name + " is not in service and cannot be reserved.");
                }
                return;
            }
        }
        for (Monkey monkey : monkeyList) {
            if (monkey.getName().equalsIgnoreCase(name)) {
                animalFound = true;
                if (!monkey.getReserved() && monkey.getTrainingStatus().equalsIgnoreCase("in_service")) {
                    monkey.setReserved(true);
                    System.out.println("The monkey named " + name + " is now reserved.");
                    updateMonkeyReservationInDatabase(monkey);
                } else if (monkey.getReserved()) {
                    System.out.println("The monkey named " + name + " is already reserved.");
                } else {
                    System.out.println("The monkey named " + name + " is not in service and cannot be reserved.");
                }
                return;
            }
        }
        if (!animalFound) {
            System.out.println("The animal named " + name + " could not be found.");
        }
    }

    public static void updateDogReservationInDatabase(Dog dog) {
        String sql = "UPDATE dog SET reserved = ? WHERE name = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setBoolean(1, dog.getReserved());
            pstmt.setString(2, dog.getName());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void updateMonkeyReservationInDatabase(Monkey monkey) {
        String sql = "UPDATE monkey SET reserved = ? WHERE name = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setBoolean(1, monkey.getReserved());
            pstmt.setString(2, monkey.getName());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void printAnimals(Scanner scanner) {
        System.out.println("Enter the type of animals to print (dogs/monkeys/all): ");
        String type = scanner.next().toLowerCase();
        System.out.println("Do you want to print only reserved animals? (yes/no): ");
        boolean reservedOnly = scanner.next().equalsIgnoreCase("yes");

        switch (type) {
            case "dogs" -> dogList.stream().filter(dog -> !reservedOnly || dog.getReserved()).forEach(System.out::println);
            case "monkeys" -> monkeyList.stream().filter(monkey -> !reservedOnly || monkey.getReserved()).forEach(System.out::println);
            case "all" -> {
                dogList.stream().filter(dog -> !reservedOnly || dog.getReserved()).forEach(System.out::println);
                monkeyList.stream().filter(monkey -> !reservedOnly || monkey.getReserved()).forEach(System.out::println);
            }
            default -> System.out.println("Invalid animal type");
        }
    }

    public static void editAnimalInfo(Scanner scanner) {
        System.out.println("Enter the name of the animal you want to edit: ");
        String name = scanner.next();
        boolean animalFound = false;

        for (Dog dog : dogList) {
            if (dog.getName().equalsIgnoreCase(name)) {
                animalFound = true;
                System.out.println("Editing information for dog: " + dog);
                System.out.println("Enter new breed (current: " + dog.getBreed() + "): ");
                dog.setBreed(scanner.next());
                System.out.println("Enter new age (current: " + dog.getAge() + "): ");
                dog.setAge(scanner.next());
                System.out.println("Enter new weight (current: " + dog.getWeight() + "): ");
                dog.setWeight(scanner.next());
                System.out.println("Enter new training status (current: " + dog.getTrainingStatus() + "): ");
                dog.setTrainingStatus(scanner.next());
                System.out.println("Enter new last checkup date (current: " + dog.getLastCheckup() + "): ");
                dog.setLastCheckup(scanner.next());
                System.out.println("Dog information updated.");
                updateDogInfoInDatabase(dog);
                return;
            }
        }

        for (Monkey monkey : monkeyList) {
            if (monkey.getName().equalsIgnoreCase(name)) {
                animalFound = true;
                System.out.println("Editing information for monkey: " + monkey);
                System.out.println("Enter new species (current: " + monkey.getSpecies() + "): ");
                monkey.setSpecies(scanner.next());
                System.out.println("Enter new age (current: " + monkey.getAge() + "): ");
                monkey.setAge(scanner.next());
                System.out.println("Enter new weight (current: " + monkey.getWeight() + "): ");
                monkey.setWeight(scanner.next());
                System.out.println("Enter new training status (current: " + monkey.getTrainingStatus() + "): ");
                monkey.setTrainingStatus(scanner.next());
                System.out.println("Enter new last checkup date (current: " + monkey.getLastCheckup() + "): ");
                monkey.setLastCheckup(scanner.next());
                System.out.println("Monkey information updated.");
                updateMonkeyInfoInDatabase(monkey);
                return;
            }
        }

        if (!animalFound) {
            System.out.println("The animal named " + name + " could not be found.");
        }
    }

    public static void updateDogInfoInDatabase(Dog dog) {
        String sql = "UPDATE dog SET breed = ?, age = ?, weight = ?, training_status = ?, last_checkup = ? WHERE name = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, dog.getBreed());
            pstmt.setString(2, dog.getAge());
            pstmt.setString(3, dog.getWeight());
            pstmt.setString(4, dog.getTrainingStatus());
            pstmt.setString(5, dog.getLastCheckup());
            pstmt.setString(6, dog.getName());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void updateMonkeyInfoInDatabase(Monkey monkey) {
        String sql = "UPDATE monkey SET species = ?, age = ?, weight = ?, training_status = ?, last_checkup = ? WHERE name = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, monkey.getSpecies());
            pstmt.setString(2, monkey.getAge());
            pstmt.setString(3, monkey.getWeight());
            pstmt.setString(4, monkey.getTrainingStatus());
            pstmt.setString(5, monkey.getLastCheckup());
            pstmt.setString(6, monkey.getName());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void printScheduledCheckups() {
        System.out.println("\nScheduled Checkups:");
        String sql = "SELECT * FROM checkups";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (!rs.isBeforeFirst()) { // Check if ResultSet is empty
                System.out.println("No checkups scheduled.");
                return;
            }

            while (rs.next()) {
                System.out.println("Animal: " + rs.getString("animal_name") +
                        ", Date: " + rs.getString("checkup_date") +
                        ", TimeSlot: " + rs.getString("timeslot"));
            }
        } catch (SQLException e) {
            System.out.println("Failed to fetch scheduled checkups: " + e.getMessage());
        }
    }

    public static void scheduleCheckupForAnimal(Scanner scanner) {
        System.out.println("Enter the name of the animal you want to schedule a checkup for: ");
        String name = scanner.next();
        System.out.println("Enter the desired checkup date (MM/dd/yyyy): ");
        String dateStr = scanner.next();
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");

        boolean animalFound = false;
        boolean scheduled = false;

        for (Dog dog : dogList) {
            if (dog.getName().equalsIgnoreCase(name)) {
                animalFound = true;
                try {
                    Date checkupDate = sdf.parse(dateStr);
                    TimeSlot slot = scheduleManager.getNextAvailableSlot();
                    if (slot != null) {
                        dog.setLastCheckup(sdf.format(checkupDate));
                        String checkupInfo = "Scheduled checkup for dog: " + dog.getName() + " on " + sdf.format(checkupDate) + " at " + slot;
                        scheduledCheckups.add(checkupInfo);
                        saveCheckupToDatabase(dog.getName(), sdf.format(checkupDate), slot.toString());
                        System.out.println(checkupInfo);
                        scheduled = true;
                    } else {
                        System.out.println("No available slots for dog: " + dog.getName());
                    }
                } catch (ParseException e) {
                    System.out.println("Invalid date format. Please use MM/dd/yyyy.");
                }
                break;
            }
        }

        if (!scheduled) {
            for (Monkey monkey : monkeyList) {
                if (monkey.getName().equalsIgnoreCase(name)) {
                    animalFound = true;
                    try {
                        Date checkupDate = sdf.parse(dateStr);
                        TimeSlot slot = scheduleManager.getNextAvailableSlot();
                        if (slot != null) {
                            monkey.setLastCheckup(sdf.format(checkupDate));
                            String checkupInfo = "Scheduled checkup for monkey: " + monkey.getName() + " on " + sdf.format(checkupDate) + " at " + slot;
                            scheduledCheckups.add(checkupInfo);
                            saveCheckupToDatabase(monkey.getName(), sdf.format(checkupDate), slot.toString());
                            System.out.println(checkupInfo);
                            scheduled = true;
                        } else {
                            System.out.println("No available slots for monkey: " + monkey.getName());
                        }
                    } catch (ParseException e) {
                        System.out.println("Invalid date format. Please use MM/dd/yyyy.");
                    }
                    break;
                }
            }
        }

        if (!animalFound) {
            System.out.println("The animal named " + name + " could not be found.");
        }
    }

    public static void saveCheckupToDatabase(String animalName, String checkupDate, String timeSlot) {
        String sql = "INSERT INTO checkups(animal_name, checkup_date, timeslot) VALUES(?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, animalName);
            pstmt.setString(2, checkupDate);
            pstmt.setString(3, timeSlot);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Failed to save checkup: " + e.getMessage());
        }
    }

    public static void editScheduledCheckup(Scanner scanner) {
        System.out.println("Enter the name of the animal whose checkup you want to edit: ");
        String name = scanner.next();
        System.out.println("Enter the current checkup date (MM/dd/yyyy): ");
        String currentDateStr = scanner.next();
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");

        boolean checkupFound = false;

        for (int i = 0; i < scheduledCheckups.size(); i++) {
            String checkup = scheduledCheckups.get(i);
            if (checkup.contains(name) && checkup.contains(currentDateStr)) {
                checkupFound = true;
                System.out.println("Found checkup: " + checkup);
                System.out.println("Enter the new checkup date (MM/dd/yyyy): ");
                String newDateStr = scanner.next();

                try {
                    Date newDate = sdf.parse(newDateStr);
                    TimeSlot slot = scheduleManager.getNextAvailableSlot();
                    if (slot != null) {
                        String newCheckupInfo = "Scheduled checkup for " + name + " on " + sdf.format(newDate) + " at " + slot;
                        scheduledCheckups.set(i, newCheckupInfo);
                        updateCheckupInDatabase(name, currentDateStr, sdf.format(newDate), slot.toString());
                        System.out.println("Checkup updated: " + newCheckupInfo);
                    } else {
                        System.out.println("No available slots for the new date.");
                    }
                } catch (ParseException e) {
                    System.out.println("Invalid date format. Please use MM/dd/yyyy.");
                }
                break;
            }
        }

        if (!checkupFound) {
            System.out.println("No checkup found for " + name + " on " + currentDateStr);
        }
    }

    public static void updateCheckupInDatabase(String animalName, String currentCheckupDate, String newCheckupDate, String newTimeSlot) {
        String sql = "UPDATE checkups SET checkup_date = ?, timeslot = ? WHERE animal_name = ? AND checkup_date = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, newCheckupDate);
            pstmt.setString(2, newTimeSlot);
            pstmt.setString(3, animalName);
            pstmt.setString(4, currentCheckupDate);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Failed to update checkup: " + e.getMessage());
        }
    }

    public static boolean authenticateUser(Scanner scanner) {
        System.out.println("Enter username: ");
        String username = scanner.next();
        System.out.println("Enter password: ");
        String password = scanner.next();

        String sql = "SELECT COUNT(*) FROM users WHERE username = ? AND password = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                currentUsername = username;
                return true;
            } else {
                System.out.println("Invalid username or password.");
            }
        } catch (SQLException e) {
            System.out.println("Failed to authenticate user: " + e.getMessage());
        }
        return false;
    }

    public static void manageUsers(Scanner scanner) {
        if (!"admin".equals(currentUsername)) {
            System.out.println("Access denied. Only admin can manage users.");
            return;
        }

        boolean back = false;
        while (!back) {
            System.out.println("\nManage Users Menu");
            System.out.println("[1] Add new user");
            System.out.println("[2] Delete user");
            System.out.println("[3] View all users");
            System.out.println("[b] Back to main menu");

            char option = scanner.next().charAt(0);

            switch (option) {
                case '1' -> addNewUser(scanner);
                case '2' -> deleteUser(scanner);
                case '3' -> viewAllUsers();
                case 'b' -> back = true;
                default -> System.out.println("Please enter valid input");
            }
        }
    }

    public static void addNewUser(Scanner scanner) {
        System.out.println("Enter new username: ");
        String username = scanner.next();
        System.out.println("Enter new password: ");
        String password = scanner.next();

        String sql = "INSERT INTO users(username, password) VALUES(?, ?)";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.executeUpdate();
            System.out.println("User added successfully.");
        } catch (SQLException e) {
            System.out.println("Failed to add user: " + e.getMessage());
        }
    }

    public static void changePassword(Scanner scanner, String username) {
        if (username == null) {
            System.out.println("Enter username: ");
            username = scanner.next();
        }
        System.out.println("Enter new password: ");
        String newPassword = scanner.next();

        String sql = "UPDATE users SET password = ? WHERE username = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, newPassword);
            pstmt.setString(2, username);
            pstmt.executeUpdate();
            System.out.println("Password updated successfully.");
        } catch (SQLException e) {
            System.out.println("Failed to update password: " + e.getMessage());
        }
    }

    public static void deleteUser(Scanner scanner) {
        System.out.println("Enter username to delete: ");
        String username = scanner.next();

        String sql = "DELETE FROM users WHERE username = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.executeUpdate();
            System.out.println("User deleted successfully.");
        } catch (SQLException e) {
            System.out.println("Failed to delete user: " + e.getMessage());
        }
    }

    public static void viewAllUsers() {
        String sql = "SELECT username FROM users";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            System.out.println("\nAll Users:");
            while (rs.next()) {
                System.out.println(rs.getString("username"));
            }
        } catch (SQLException e) {
            System.out.println("Failed to fetch users: " + e.getMessage());
        }
    }
}
