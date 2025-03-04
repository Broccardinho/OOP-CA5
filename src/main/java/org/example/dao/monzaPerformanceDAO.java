package dao;

import database.DatabaseConnection;
import dto.monzaPerformanceDTO;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class monzaPerformanceDAO {
    public void addRacer(Racer racer) {
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
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void deleteRacer(int id) {
        String sql = "DELETE FROM monzaperformance WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Racer> getAllRacers() {
        List<Racer> racer = new ArrayList<>();
        String sql = "SELECT * FROM monzaperformance";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                racers.add(new Racer(
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
            e.printStackTrace();
        }
        return racers;
    }
}
