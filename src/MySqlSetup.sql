-- Create Database
CREATE DATABASE IF NOT EXISTS F1_2024;
USE F1_2024;

-- Create MonzaPerformance Table
CREATE TABLE IF NOT EXISTS MonzaPerformance (
                                                id INT AUTO_INCREMENT PRIMARY KEY,
                                                name VARCHAR(100) NOT NULL,
    team VARCHAR(100) NOT NULL,
    fastestLapTime FLOAT NOT NULL,
    finalPosition INT NOT NULL,
    gridPosition INT NOT NULL,
    pointsEarned INT NOT NULL,
    nationality VARCHAR(50) NOT NULL
    );

-- Insert Sample Data
INSERT INTO MonzaPerformance (name, team, fastestLapTime, finalPosition, gridPosition, pointsEarned, nationality) VALUE
    ('Lewis Hamilton', 'Mercedes', 87.452, 1, 2, 25, 'British'),
    ('Max Verstappen', 'Red Bull', 87.123, 2, 1, 18, 'Dutch'),
    ('Charles Leclerc', 'Ferrari', 87.789, 3, 3, 15, 'Monegasque'),
    ('Lando Norris', 'McLaren', 88.012, 4, 5, 12, 'British'),
    ('George Russell', 'Mercedes', 88.345, 5, 4, 10, 'British'),
    ('Carlos Sainz', 'Ferrari', 88.567, 6, 6, 8, 'Spanish'),
    ('Sergio Perez', 'Red Bull', 88.789, 7, 7, 6, 'Mexican'),
    ('Fernando Alonso', 'Aston Martin', 89.012, 8, 8, 4, 'Spanish'),
    ('Oscar Piastri', 'McLaren', 89.345, 9, 9, 2, 'Australian'),
    ('Pierre Gasly', 'Alpine', 89.567, 10, 10, 1, 'French');