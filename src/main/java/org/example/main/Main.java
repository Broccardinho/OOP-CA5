package Main;
import dao.UserDaoInterface;
import dao.monzaPerformanceDAO;
import dto.monzaPerformanceDTO;
import java.sql.Date;
import java.util.InputMismatchException;
import java.util.Scanner;
import exceptions.DaoException;

public class ExpenseTrackerApp {
    public static void main(String[] args) {
        MonzaPerformanceDAO Racer = new MonzaPerformanceDAO();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\nF1 Tracker Menu:");
            System.out.println("1. List All Racer info");
            System.out.println("2. Add Racer");
            System.out.println("3. Delete Racer");
            System.out.println("7. Exit");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> monzaPerformanceDAO.findAllRacers().forEach(System.out::println);
                case 2 -> {try{
                    System.out.print("Enter name of racer: ");
                    String name = scanner.nextLine();
                    System.out.print("Enter Team Name: ");
                    String team = scanner.nextLine();
                    System.out.print("Enter the fastest lap time: ");
                    double fastestLapTime = scanner.nextDouble();
                    System.out.println("Enter racer's final position");
                    int finalPosition = scanner.nextInt();
                    System.out.println("Enter racer's grid position");
                    int gridPosition = scanner.nextInt();
                    System.out.println("Enter racer's points earned");
                    int pointsEarned = scanner.nextInt();
                    System.out.println("Enter racer's nationality");
                    String nationality = scanner.nextLine();
                    monzaPerformanceDAO.addRacer(new Racer(0, name, team, fastestLapTime, finalPosition, gridPosition, pointsEarned, nationality));
                    System.out.println("Successfully added racer");
                }catch (InputMismatchException e) {
                    System.out.println("Invalid input, ensure that you have given a valid number only");
                    main(null);
                } }
                case 3 -> {try{
                    System.out.print("Enter the ID of the racer you wish to delete: ");
                    int id = scanner.nextInt();
                    monzaPerformanceDAO.deleteRacer(id);
                    System.out.println("Successfully deleted");
                }catch (InputMismatchException e) {
                    System.out.println("Invalid input, Please enter a valid ID");
                    main(null);
                } }

                case 4 -> System.exit(0);
                default -> System.out.println("Invalid choice!");
            }
        }
    }
}
