package org.example.dao;
import org.example.database.DatabaseConnection;
import org.example.dto.monzaPerformanceDTO;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class MonzaPerformanceDAO {

    public void addRacer(monzaPerformanceDTO racer) {
        String sql = "INSERT INTO MonzaPerformance (name, team, fastestLapTime, finalPosition, gridPosition, pointsEarned, nationality) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, racer.getName());
            stmt.setString(2, racer.getTeam());
            stmt.setDouble(3, racer.getFastestLapTime());
            stmt.setInt(4, racer.getFinalPosition());
            stmt.setInt(5, racer.getGridPosition());
            stmt.setInt(6, racer.getPointsEarned());
            stmt.setString(7, racer.getNationality());
            stmt.executeUpdate();
            System.out.println("Racer added successfully.");
        } catch (SQLException e) {
            System.out.println("Error adding racer: " + e.getMessage());
        }
    }

    public void deleteRacer(int id) {
        String sql = "DELETE FROM MonzaPerformance WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            int rowsDeleted = stmt.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Racer deleted successfully.");
            } else {
                System.out.println("No racer found with ID: " + id);
            }
        } catch (SQLException e) {
            System.out.println("Error deleting racer: " + e.getMessage());
        }
    }

    public List<monzaPerformanceDTO> getAllRacers() {
        List<monzaPerformanceDTO> racers = new ArrayList<>();
        String sql = "SELECT * FROM MonzaPerformance";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                racers.add(new monzaPerformanceDTO(
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
}
