package org.example.server;

import org.example.dao.MonzaPerformanceDAO;
import org.example.dto.JsonConverter;
import org.example.dto.MonzaPerformanceDTO;
import java.io.*;
        import java.net.*;
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
                            try {
                                // Trim any whitespace and parse ID
                                int deleteId = Integer.parseInt(parts[1].trim());
                                System.out.println("Attempting to delete racer with ID: " + deleteId);
                                boolean deleted = dao.deleteRacer(deleteId);
                                response = deleted ? "SUCCESS" : "ERROR: Racer not found";
                            } catch (NumberFormatException e) {
                                response = "ERROR: Invalid ID format";
                            } catch (Exception e) {
                                response = "ERROR: " + e.getMessage();
                                e.printStackTrace();
                            }
                            break;
                        case "ADD_RACER":
                            try {
                                if (parts.length < 2) {
                                    response = "ERROR: Missing racer data";
                                    break;
                                }
                                MonzaPerformanceDTO newRacer = JsonConverter.jsonStringToMonzaPerformance(parts[1]);
                                MonzaPerformanceDTO addedRacer = dao.addRacer(newRacer);
                                response = addedRacer != null ? "SUCCESS" : "ERROR: Failed to add racer";
                            } catch (Exception e) {
                                response = "ERROR: " + e.getMessage();
                                e.printStackTrace();
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