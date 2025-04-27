package org.example.server;

import org.example.dao.MonzaPerformanceDAO;
import org.example.dto.JsonConverter;
import org.example.dto.MonzaPerformanceDTO;
import java.io.*;
import java.net.*;
import java.sql.SQLException;
import java.util.List;

public class F1Server {
    private static final int PORT = 8888;
    private ServerSocket serverSocket;
    private boolean running;
    private MonzaPerformanceDAO dao = new MonzaPerformanceDAO();

    public void start() {
        try {
            serverSocket = new ServerSocket(PORT);
            running = true;
            System.out.println("F1 Server started on port " + PORT);

            while (running) {
                Socket clientSocket = serverSocket.accept();
                new ClientHandler(clientSocket).start();
            }
        } catch (IOException e) {
            System.out.println("Server error: " + e.getMessage());
        }
    }

    private class ClientHandler extends Thread {
        private Socket clientSocket;
        private PrintWriter out;
        private BufferedReader in;

        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        public void run() {
            try {
                out = new PrintWriter(clientSocket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    System.out.println("Received command: " + inputLine);
                    String[] parts = inputLine.split(" ", 2);
                    String command = parts[0];
                    String response = "";

                    switch (command) {
                        case "GET_ALL_RACERS":
                            List<MonzaPerformanceDTO> racers = dao.getAllRacers();
                            response = JsonConverter.monzaPerformanceListToJsonString(racers);
                            break;
                        case "GET_RACER_BY_ID":
                            int id = Integer.parseInt(parts[1]);
                            MonzaPerformanceDTO racer = dao.getRacerById(id);
                            response = racer != null ?
                                    JsonConverter.monzaPerformanceToJsonString(racer) :
                                    "ERROR: Racer not found";
                            break;
                        case "DELETE_RACER":
                            id = Integer.parseInt(parts[1]);
                            dao.deleteRacer(id);
                            response = "DELETED";
                            break;
                        case "ADD_RACER":
                            try {
                                MonzaPerformanceDTO newRacer = JsonConverter.jsonStringToMonzaPerformance(parts[1]);

                                // Additional validation
                                if (newRacer.getName() == null || newRacer.getName().trim().isEmpty()) {
                                    response = "ERROR: Racer name cannot be empty";
                                    break;
                                }

                                MonzaPerformanceDTO addedRacer = dao.addRacer(newRacer);

                                if (addedRacer != null) {
                                    response = JsonConverter.monzaPerformanceToJsonString(addedRacer);
                                } else {
                                    response = "ERROR: Database failed to add racer (check constraints)";
                                }
                            } catch (IllegalArgumentException e) {
                                response = "ERROR: Invalid data format - " + e.getMessage();
                            } catch (Exception e) {
                                response = "ERROR: Unexpected error - " + e.getMessage();
                            }
                            break;
                        default:
                            response = "ERROR: Unknown command";
                    }

                    out.println(response);
                }

                closeConnection();
            } catch (IOException e) {
                System.out.println("Client handler error: " + e.getMessage());
            }
        }

        private void closeConnection() {
            try {
                in.close();
                out.close();
                clientSocket.close();
            } catch (IOException e) {
                System.out.println("Error closing connection: " + e.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        F1Server server = new F1Server();
        server.start();
    }
}