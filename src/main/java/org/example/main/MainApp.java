package org.example.main;

import org.example.dao.MonzaPerformanceDAO;
import org.example.dto.monzaPerformanceDTO;
import java.util.InputMismatchException;
import java.util.Scanner;

public class MainApp {
    public static void main(String[] args) {
        MonzaPerformanceDAO racerDAO = new MonzaPerformanceDAO();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\nF1 Tracker Menu:");
            System.out.println("1. List All Racer info");
            System.out.println("2. Add Racer");
            System.out.println("3. Delete Racer");
            System.out.println("7. Exit");

            try {
                System.out.print("Enter choice: ");
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                switch (choice) {
                    case 1 -> racerDAO.getAllRacers().forEach(System.out::println);

                    case 2 -> {
                        try {
                            System.out.print("Enter name of racer: ");
                            String name = scanner.nextLine();
                            System.out.print("Enter Team Name: ");
                            String team = scanner.nextLine();
                            System.out.print("Enter the fastest lap time: ");
                            double fastestLapTime = scanner.nextDouble();
                            System.out.print("Enter racer's final position: ");
                            int finalPosition = scanner.nextInt();
                            System.out.print("Enter racer's grid position: ");
                            int gridPosition = scanner.nextInt();
                            System.out.print("Enter racer's points earned: ");
                            int pointsEarned = scanner.nextInt();
                            scanner.nextLine(); // Consume newline
                            System.out.print("Enter racer's nationality: ");
                            String nationality = scanner.nextLine();

                            racerDAO.addRacer(new monzaPerformanceDTO(0, name, team, fastestLapTime, finalPosition, gridPosition, pointsEarned, nationality));
                            System.out.println("Successfully added racer");
                        } catch (InputMismatchException e) {
                            System.out.println("Invalid input, ensure that you have given valid numbers only.");
                            scanner.nextLine(); // Consume the invalid input
                        }
                    }

                    case 3 -> {
                        try {
                            System.out.print("Enter the ID of the racer you wish to delete: ");
                            int id = scanner.nextInt();
                            scanner.nextLine(); // Consume newline
                            racerDAO.deleteRacer(id);
                            System.out.println("Successfully deleted");
                        } catch (InputMismatchException e) {
                            System.out.println("Invalid input, please enter a valid ID.");
                            scanner.nextLine(); // Consume the invalid input
                        }
                    }

                    case 7 -> {
                        System.out.println("Exiting...");
                        System.exit(0);
                    }

                    default -> System.out.println("Invalid choice! Please enter a number from the menu.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input! Please enter a number.");
                scanner.nextLine(); // Consume the invalid input
            }
        }
    }
}
