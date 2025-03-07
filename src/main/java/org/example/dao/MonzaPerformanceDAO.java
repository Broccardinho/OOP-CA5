package org.example.dao;
import org.example.database.DatabaseConnection;
import org.example.dto.MonzaPerformanceDTO;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class MonzaPerformanceDAO {

    public MonzaPerformanceDTO addRacer(MonzaPerformanceDTO racer) {
        String sql = "INSERT INTO MonzaPerformance (name, team, fastestLapTime, finalPosition, gridPosition, pointsEarned, nationality) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            // Bind parameters
            stmt.setString(1, racer.getName());
            stmt.setString(2, racer.getTeam());
            stmt.setDouble(3, racer.getFastestLapTime());
            stmt.setInt(4, racer.getFinalPosition());
            stmt.setInt(5, racer.getGridPosition());
            stmt.setInt(6, racer.getPointsEarned());
            stmt.setString(7, racer.getNationality());

            stmt.executeUpdate();  // Execute AFTER setting parameters

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    int newId = rs.getInt(1);
                    return new MonzaPerformanceDTO(
                            newId,
                            racer.getName(),
                            racer.getTeam(),
                            racer.getFastestLapTime(),
                            racer.getFinalPosition(),
                            racer.getGridPosition(),
                            racer.getPointsEarned(),
                            racer.getNationality()
                    );
                }
            }
        } catch (SQLException e) {
            System.out.println("Error adding racer: " + e.getMessage());
        }
        return null;
    }

    public MonzaPerformanceDTO getRacerById(int id) {
        String sql = "SELECT * FROM MonzaPerformance WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id); // Bind the ID parameter
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    // Build and return the DTO
                    return new MonzaPerformanceDTO(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("team"),
                            rs.getDouble("fastestLapTime"),
                            rs.getInt("finalPosition"),
                            rs.getInt("gridPosition"),
                            rs.getInt("pointsEarned"),
                            rs.getString("nationality")
                    );
                } else {
                    return null; // No racer found
                }
            }
        } catch (SQLException e) {
            System.out.println("Error fetching racer: " + e.getMessage());
            return null;
        }
    }

    public boolean deleteRacer(int id) {
        String sql = "DELETE FROM MonzaPerformance WHERE id = ?";
        int rowsDeleted = 0;
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            rowsDeleted = stmt.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Racer deleted successfully.");
            } else {
                System.out.println("No racer found with ID: " + id);
            }
        } catch (SQLException e) {
            System.out.println("Error deleting racer: " + e.getMessage());
        }
        return rowsDeleted > 0;
    }

    public List<MonzaPerformanceDTO> getAllRacers() {
        List<MonzaPerformanceDTO> racers = new ArrayList<>();
        String sql = "SELECT * FROM MonzaPerformance";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                racers.add(new MonzaPerformanceDTO(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("team"),
                        rs.getDouble("fastestLapTime"),
                        rs.getInt("finalPosition"),
                        rs.getInt("gridPosition"),
                        rs.getInt("pointsEarned"),
                        rs.getString("nationality")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving racers: " + e.getMessage());
        }
        return racers;
    }
    public boolean updateRacer(int id, MonzaPerformanceDTO updatedRacer) {
        // Previously had typos like "MonzaPerfprmance" and "fatestLapTime"
        String sql = "UPDATE MonzaPerformance SET name=?, team=?, fastestLapTime=?, "
                + "finalPosition=?, gridPosition=?, pointsEarned=?, nationality=? WHERE id=?";
        try(Connection conn = DatabaseConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setString(1, updatedRacer.getName());
            stmt.setString(2, updatedRacer.getTeam());
            stmt.setDouble(3, updatedRacer.getFastestLapTime());
            stmt.setInt(4, updatedRacer.getFinalPosition());
            stmt.setInt(5, updatedRacer.getGridPosition());
            stmt.setInt(6, updatedRacer.getPointsEarned());
            stmt.setString(7, updatedRacer.getNationality());
            stmt.setInt(8, id);

            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;
        }catch (SQLException e){
            System.out.println("Error updating racer: " + e.getMessage());
            return false;
        }
    }
    public List<MonzaPerformanceDTO> getRacersByTeam(String team) {
        List<MonzaPerformanceDTO> filteredRacers = new ArrayList<>();
        String sql = "SELECT * FROM MonzaPerformance WHERE team = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, team);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                filteredRacers.add(new MonzaPerformanceDTO(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("team"),
                        rs.getDouble("fastestLapTime"),
                        rs.getInt("finalPosition"),
                        rs.getInt("gridPosition"),
                        rs.getInt("pointsEarned"),
                        rs.getString("nationality")
                ));
            }

        } catch (SQLException e) {
            System.out.println("Filter error: " + e.getMessage());
        }
        return filteredRacers;
    }
}
