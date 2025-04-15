package org.example.main;

import org.example.dao.MonzaPerformanceDAO;
import org.example.dto.JsonConverter;
import org.example.dto.MonzaPerformanceDTO;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.List;

public class OldMainApp {

    private static int getIntegerInput(String prompt, int currentValue, Scanner scanner) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine();
            if (input.isEmpty()) return currentValue;
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a whole number.");
            }
        }
    }

    public static void main(String[] args) {
        MonzaPerformanceDAO racerDAO = new MonzaPerformanceDAO();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\nF1 Tracker Menu:");
            System.out.println("1. List All Racer info");
            System.out.println("2. Add Racer");
            System.out.println("3. Delete Racer");
            System.out.println("4. Find Racer By ID");
            System.out.println("5. Update Racer");
            System.out.println("6. Find Racers By Team");
            System.out.println("7. Convert List of Entities to a JSON String");
            System.out.println("8. Convert a single Entity by Key into a JSON String");
            System.out.println("14. Exit");

            try {
                System.out.print("Enter choice: ");
                int choice = scanner.nextInt();
                scanner.nextLine();

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
                            scanner.nextLine();
                            System.out.print("Enter racer's nationality: ");
                            String nationality = scanner.nextLine();

                            MonzaPerformanceDTO newRacer = racerDAO.addRacer(
                                    new MonzaPerformanceDTO(0, name, team, fastestLapTime,
                                            finalPosition, gridPosition, pointsEarned, nationality));

                            if(newRacer != null) {
                                System.out.println("Successfully added new Racer with ID: " + newRacer.getId());
                            } else {
                                System.out.println("Failed to add new Racer");
                            }
                        } catch (InputMismatchException e) {
                            System.out.println("Invalid input, ensure that you have given valid numbers only.");
                            scanner.nextLine();
                        }
                    }

                    case 3 -> {
                        try {
                            System.out.print("Enter the ID of the racer you wish to delete: ");
                            int id = scanner.nextInt();
                            scanner.nextLine();
                            if (racerDAO.deleteRacer(id)) {
                                System.out.println("Successfully deleted");
                            } else {
                                System.out.println("Failed to delete");
                            }
                        } catch (InputMismatchException e) {
                            System.out.println("Invalid input, please enter a valid ID.");
                            scanner.nextLine();
                        }
                    }

                    case 4 -> {
                        System.out.print("Enter Racer ID: ");
                        int id = scanner.nextInt();
                        scanner.nextLine();
                        MonzaPerformanceDTO racer = racerDAO.getRacerById(id);
                        if(racer != null) {
                            System.out.println("Found: " + racer);
                        } else {
                            System.out.println("No Racer with ID " + id + " found");
                        }
                    }

                    case 5 -> {
                        try {
                            System.out.print("Enter Racer ID to update: ");
                            int id = scanner.nextInt();
                            scanner.nextLine();

                            MonzaPerformanceDTO existingRacer = racerDAO.getRacerById(id);
                            if (existingRacer == null) {
                                System.out.println("No Racer with ID " + id + " found");
                                break;
                            }
                            System.out.println("Current Detail: " + existingRacer);

                            System.out.print("New name (leave blank to keep current): ");
                            String name = scanner.nextLine();
                            if (name.isEmpty()) name = existingRacer.getName();

                            System.out.print("New team (leave blank to keep current): ");
                            String team = scanner.nextLine();
                            if (team.isEmpty()) team = existingRacer.getTeam();

                            double fastestLapTime;
                            while (true) {
                                System.out.print("New fastest lap time (current: " + existingRacer.getFastestLapTime() + "): ");
                                String lapInput = scanner.nextLine();
                                if (lapInput.isEmpty()) {
                                    fastestLapTime = existingRacer.getFastestLapTime();
                                    break;
                                }
                                try {
                                    fastestLapTime = Double.parseDouble(lapInput);
                                    break;
                                } catch (NumberFormatException e) {
                                    System.out.println("Invalid format! Use decimal numbers (e.g., 87.452)");
                                }
                            }

                            int finalPosition = getIntegerInput(
                                    "New final position (current: " + existingRacer.getFinalPosition() + "): ",
                                    existingRacer.getFinalPosition(),
                                    scanner
                            );

                            int gridPosition = getIntegerInput(
                                    "New grid position (current: " + existingRacer.getGridPosition() + "): ",
                                    existingRacer.getGridPosition(),
                                    scanner
                            );

                            int pointsEarned = getIntegerInput(
                                    "New points earned (current: " + existingRacer.getPointsEarned() + "): ",
                                    existingRacer.getPointsEarned(),
                                    scanner
                            );

                            System.out.print("New nationality (current: " + existingRacer.getNationality() + "): ");
                            String nationality = scanner.nextLine();
                            if (nationality.isEmpty()) nationality = existingRacer.getNationality();

                            MonzaPerformanceDTO updatedRacer = new MonzaPerformanceDTO(
                                    id,
                                    name,
                                    team,
                                    fastestLapTime,
                                    finalPosition,
                                    gridPosition,
                                    pointsEarned,
                                    nationality
                            );

                            if (racerDAO.updateRacer(id, updatedRacer)) {
                                System.out.println("Racer updated successfully!");
                            } else {
                                System.out.println("Update failed. Please check the ID and try again.");
                            }
                        } catch(InputMismatchException e) {
                            System.out.println("Invalid input! Please enter a valid ID number.");
                            scanner.nextLine();
                        }
                    }

                    case 6 -> {
                        System.out.print("Enter team name to filter: ");
                        String teamFilter = scanner.nextLine();
                        List<MonzaPerformanceDTO> teamRacers = racerDAO.getRacersByTeam(teamFilter);

                        if (teamRacers.isEmpty()) {
                            System.out.println("No racers found in team: " + teamFilter);
                        } else {
                            System.out.println("Racers in team " + teamFilter + ":");
                            teamRacers.forEach(System.out::println);
                        }
                    }

                    case 7 -> {
                        try {
                            List<MonzaPerformanceDTO> allRacers = racerDAO.getAllRacers();
                            if (allRacers.isEmpty()) {
                                System.out.println("No racers found in the database");
                            } else {
//                                String jsonOutput = JsonConverter.monzaPerformanceListToJsonString(allRacers);
                                System.out.println("JSON Output:");
//                                System.out.println(formatJson(jsonOutput));
                            }
                        } catch (Exception e) {
                            System.out.println("Error: " + e.getMessage());
                        }
                    }

                    case 8 -> {
                        try {
                            System.out.print("Enter Racer ID: ");
                            int id = scanner.nextInt();
                            scanner.nextLine();
                            MonzaPerformanceDTO racer = racerDAO.getRacerById(id);
                            if (racer == null) {
                                System.out.println("No racer found with ID: " + id);
                            } else {
//                                String jsonOutput = JsonConverter.monzaPerformanceToJsonString(racer);
                                System.out.println("JSON Output:");
//                                System.out.println(formatJson(jsonOutput));
                            }
                        } catch (InputMismatchException e) {
                            System.out.println("Invalid input! Please enter a number.");
                            scanner.nextLine();
                        } catch (Exception e) {
                            System.out.println("Error: " + e.getMessage());
                        }
                    }

                    case 14 -> {
                        System.out.println("Exiting...");
                        System.exit(0);
                    }

                    default -> System.out.println("Invalid choice! Please enter a number from the menu.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input! Please enter a number.");
                scanner.nextLine();
            }
        }
    }

    private static String formatJson(String jsonString) {
        try {
            if (jsonString.startsWith("[")) {
                return new JSONArray(jsonString).toString(2);
            } else {
                return new JSONObject(jsonString).toString(2);
            }
        } catch (Exception e) {
            return jsonString;
        }
    }
}