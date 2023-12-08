package main;

import connection.Connection;
import repository.Repository;
import repository.TeamRepository;
import repository.UserRepository;
import models.Model;
import models.Team;
import models.User;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
    	menu();
    }
    private static void menu() {
//    	Connection connection1 = new Connection("/Users/jasontimothy/Desktop/database/teams.csv");
//    	Connection connection2 = new Connection("/Users/jasontimothy/Desktop/database/user.csv");
//    	Connection connection = new Connection("/Users/jasontimothy/Desktop/database.zip");
//        Repository userRepository = new UserRepository();
//        Repository teamRepository = new TeamRepository();

        int choice;

        do {
            System.out.println("1. Menu Utama");
            System.out.println("2. Insert Data");
            System.out.println("3. Show");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt(); 
            scanner.nextLine();

            switch (choice) {
                case 1:
                    showMainMenu();
                    break;
                case 2:
                    showInsertDataMenu();
                    break;
                case 3:
                    showShowMenu();
                    break;
                case 4:
                    System.out.println("Exiting the program.");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 4);
    }
    private static void showMainMenu() {
        System.out.println("This is the Main Menu.");
    }

    private static void showInsertDataMenu() {
        System.out.println("Which table to insert? 1. User, 2. Team.");
        int tableChoice = scanner.nextInt();
        scanner.nextLine(); 

        switch (tableChoice) {
            case 1:
                insertUser();
                break;
            case 2:
            	CSVDataInsertTeam();
                break;
            default:
                System.out.println("Invalid table choice.");
        }
    }
    private static void insertUser() {
        System.out.println("Enter user details:");

        System.out.print("Name: ");
        String name = scanner.nextLine();

        System.out.print("NIM: ");
        String nim = scanner.nextLine();

        System.out.print("Team: ");
        String teamName = scanner.nextLine();
        
        insertTeam2(teamName);
        
        try (PrintWriter writer = new PrintWriter(new FileWriter("user.csv", true))) {
            writer.println(name + "," + nim + "," + teamName);
            System.out.println("User added successfully.");
        } catch (IOException e) {
            System.out.println("An error occurred while writing to the file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void insertTeam2(String teamName) {
        ArrayList<String> teams = new ArrayList<>();

        try (Scanner fileScanner = new Scanner(new File("team.csv"))) {
            while (fileScanner.hasNextLine()) {
                teams.add(fileScanner.nextLine());
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + e.getMessage());
        }

        boolean teamFound = false;

        for (int i = 0; i < teams.size(); i++) {
            String line = teams.get(i);
            String[] parts = line.split(",");
            if (parts.length > 1 && parts[1].equals(teamName)) {
                int counter = Integer.parseInt(parts[0]);
                if (counter < 3) {
                    counter++;
                    teams.set(i, counter + "," + teamName);
                    teamFound = true;
                } else {
                    System.out.println("Team is full (max 3 users).");
                    return;
                }
                break;
            }
        }

        if (!teamFound) {
            teams.add("1," + teamName);
        }

        try (PrintWriter writer = new PrintWriter(new FileWriter("team.csv"))) {
            for (String team : teams) {
                writer.println(team);
            }
            System.out.println("Team added successfully.");
        } catch (IOException e) {
            System.out.println("An error occurred while writing to the file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void showShowMenu() {
        // Implement Show logic
        System.out.println("Which table to show? 1. User, 2. Team.");
        int tableChoice = scanner.nextInt();
        scanner.nextLine(); // Consume the newline character

        switch (tableChoice) {
            case 1:
                showUsers();
                break;
            case 2:
                // Show Team
                showTeams();
                break;
            default:
                System.out.println("Invalid table choice.");
        }
    }

    private static void showUsers() {
        System.out.println("Users in user.csv:");

        try (Scanner fileScanner = new Scanner(new File("user.csv"))) {
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    String name = parts[0];
                    String nim = parts[1];
                    String team = parts[2];
                    System.out.println("Name: " + name + ", NIM: " + nim + ", Team: " + team);
                } else {
                    System.out.println("Invalid line in user.csv: " + line);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + e.getMessage());
        }
    }
    
    private static void showTeams() {

        try (Scanner fileScanner = new Scanner(new File("team.csv"))) {
            int flag = 0;
            while (fileScanner.hasNextLine()) {
                String teamName = fileScanner.nextLine();
                System.out.println(teamName);
                flag = 1;
            }
            if(flag == 0) {
            	System.out.println("there is no team yet");
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + e.getMessage());
        }
    }
    
        public static void CSVDataInsertTeam(){
            System.out.println("Masukkan nama team: ");
            String teamName = scanner.nextLine();
            insertTeam(teamName);
        }

        public static void insertTeam(String teamName) {
            if (isTeamNameDuplicate(teamName)) {
                System.out.println("Duplicate team name. Team not inserted.");
                menu();
                return;
            }

            try {
                File myObj = new File("team.csv");
                if (myObj.createNewFile()) {
                    System.out.println("File created: " + myObj.getName());
                } else {
                    System.out.println("File already exists.");
                }
            } catch (IOException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }
            int teamCounter = 0;
            try (PrintWriter writer = new PrintWriter(new FileWriter("team.csv", true))) {
                writer.println(teamCounter + "," + teamName);
                teamCounter++;
                System.out.println("Team added successfully.");
            } catch (IOException e) {
                System.out.println("An error occurred while writing to the file: " + e.getMessage());
                e.printStackTrace();
            }

            menu();
        }

        private static boolean isTeamNameDuplicate(String teamName) {
            try (Scanner fileScanner = new Scanner(new File("team.csv"))) {
                while (fileScanner.hasNextLine()) {
                    String line = fileScanner.nextLine();
                    if (line.equals(teamName)) {
                        return true; 
                    }
                }
            } catch (FileNotFoundException e) {
                System.out.println("File not found: " + e.getMessage());
            }
            return false; 
        }
}

