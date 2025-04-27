-- Create Database
DROP DATABASE IF EXISTS F1_2024;
CREATE DATABASE IF NOT EXISTS F1_2024;
USE F1_2024;

-- Create MonzaPerformance Table
DROP TABLE IF EXISTS MonzaPerformance;
CREATE TABLE IF NOT EXISTS MonzaPerformance (
                                                id INT AUTO_INCREMENT PRIMARY KEY,
                                                name VARCHAR(100) NOT NULL,
    team VARCHAR(100) NOT NULL,
    fastestLapTime FLOAT NOT NULL,
    finalPosition INT NOT NULL,
    gridPosition INT NOT NULL,
    pointsEarned INT NOT NULL,
    nationality VARCHAR(50) NOT NULL,
    imageLink VARCHAR(150) NOT NULL
    );

-- Insert Sample Data
INSERT INTO MonzaPerformance (name, team, fastestLapTime, finalPosition, gridPosition, pointsEarned, nationality, imageLink) VALUES
                                                                                                                                 ('Lewis Hamilton', 'Mercedes', 87.452, 1, 2, 25, 'British','https://media.formula1.com/image/upload/f_auto,c_limit,q_auto,w_1320/content/dam/fom-website/drivers/2025Drivers/hamilton'),
                                                                                                                                 ('Max Verstappen', 'Red Bull', 87.123, 2, 1, 18, 'Dutch','https://media.formula1.com/image/upload/f_auto,c_limit,q_auto,w_1320/content/dam/fom-website/drivers/2025Drivers/verstappen'),
                                                                                                                                 ('Charles Leclerc', 'Ferrari', 87.789, 3, 3, 15, 'Monegasque','https://media.formula1.com/image/upload/f_auto,c_limit,q_auto,w_1320/content/dam/fom-website/drivers/2025Drivers/leclerc'),
                                                                                                                                 ('Lando Norris', 'McLaren', 88.012, 4, 5, 12, 'British','https://media.formula1.com/image/upload/f_auto,c_limit,q_auto,w_1320/content/dam/fom-website/drivers/2025Drivers/norris'),
                                                                                                                                 ('George Russell', 'Mercedes', 88.345, 5, 4, 10, 'British','https://media.formula1.com/image/upload/f_auto,c_limit,q_auto,w_1320/content/dam/fom-website/drivers/2025Drivers/russell'),
                                                                                                                                 ('Carlos Sainz', 'Ferrari', 88.567, 6, 6, 8, 'Spanish','https://media.formula1.com/image/upload/f_auto,c_limit,q_auto,w_1320/content/dam/fom-website/drivers/2025Drivers/sainz'),
                                                                                                                                 ('Sergio Perez', 'Red Bull', 88.789, 7, 7, 6, 'Mexican','https://media.formula1.com/image/upload/v1712849107/content/dam/fom-website/drivers/2024Drivers/perez.png'),
                                                                                                                                 ('Fernando Alonso', 'Aston Martin', 89.012, 8, 8, 4, 'Spanish','https://media.formula1.com/image/upload/f_auto/q_auto/v1741268869/content/dam/fom-website/drivers/2025Drivers/alonso.png'),
                                                                                                                                 ('Oscar Piastri', 'McLaren', 89.345, 9, 9, 2, 'Australian','https://media.formula1.com/image/upload/f_auto,c_limit,q_auto,w_1320/content/dam/fom-website/drivers/2025Drivers/piastri'),
                                                                                                                                 ('Pierre Gasly', 'Alpine', 89.567, 10, 10, 1, 'French','https://media.formula1.com/image/upload/f_auto,c_limit,q_auto,w_1320/content/dam/fom-website/drivers/2025Drivers/gasly');