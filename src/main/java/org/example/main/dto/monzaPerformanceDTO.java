package org.example.main.dto;

public class monzaPerformanceDTO {
    private int id;
    private String name;
    private String team;
    private float fastestLapTime;
    private int finalPosition;
    private int gridPosition;
    private int pointsEarned;
    private String nationality;


    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public int getPointsEarned() {
        return pointsEarned;
    }

    public void setPointsEarned(int pointsEarned) {
        this.pointsEarned = pointsEarned;
    }

    public int getGridPosition() {
        return gridPosition;
    }

    public void setGridPosition(int gridPosition) {
        this.gridPosition = gridPosition;
    }

    public int getFinalPosition() {
        return finalPosition;
    }

    public void setFinalPosition(int finalPosition) {
        this.finalPosition = finalPosition;
    }

    public float getFastestLapTime() {
        return fastestLapTime;
    }

    public void setFastestLapTime(float fastestLapTime) {
        this.fastestLapTime = fastestLapTime;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return "monzaperformanceDTO{" +
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