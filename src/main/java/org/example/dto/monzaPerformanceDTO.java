package org.example.dto;

public class monzaPerformanceDTO {
    private int id;
    private String name;
    private String team;
    private double fastestLapTime;
    private int finalPosition;
    private int gridPosition;
    private int pointsEarned;
    private String nationality;

    public monzaPerformanceDTO(int id, String name, String team, double fastestLapTime, int finalPosition, int gridPosition, int pointsEarned, String nationality) {
        this.id = id;
        this.name = name;
        this.team = team;
        this.fastestLapTime = fastestLapTime;
        this.finalPosition = finalPosition;
        this.gridPosition = gridPosition;
        this.pointsEarned = pointsEarned;
        this.nationality = nationality;
    }

    // Getters
    public int getId() { return id; }
    public String getName() { return name; }
    public String getTeam() { return team; }
    public double getFastestLapTime() { return fastestLapTime; }
    public int getFinalPosition() { return finalPosition; }
    public int getGridPosition() { return gridPosition; }
    public int getPointsEarned() { return pointsEarned; }
    public String getNationality() { return nationality; }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setTeam(String team) { this.team = team; }
    public void setFastestLapTime(double fastestLapTime) { this.fastestLapTime = fastestLapTime; }
    public void setFinalPosition(int finalPosition) { this.finalPosition = finalPosition; }
    public void setGridPosition(int gridPosition) { this.gridPosition = gridPosition; }
    public void setPointsEarned(int pointsEarned) { this.pointsEarned = pointsEarned; }
    public void setNationality(String nationality) { this.nationality = nationality; }

    @Override
    public String toString() {
        return "Racer{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", team='" + team + '\'' +
                ", fastestLapTime=" + fastestLapTime +
                ", finalPosition=" + finalPosition +
                ", gridPosition=" + gridPosition +
                ", pointsEarned=" + pointsEarned +
                ", nationality='" + nationality + '\'' +
                '}';
    }
}
