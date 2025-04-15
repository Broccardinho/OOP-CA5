package org.example.database;

import org.example.dao.MonzaPerformanceDAO;
import org.example.dto.MonzaPerformanceDTO;
import org.example.Exceptions.DaoException;
import org.example.dto.JsonConverter;
import org.json.JSONObject;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

public class Server {
    private static final int PORT = 8888;
    private static boolean running = true;

    public static void main(String[] args) {
        System.out.println("Starting F1 Racer Tracker Server on port " + PORT);

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server started. Waiting for connections...");

            while (running) {
                try (Socket clientSocket = serverSocket.accept();
                     BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                     PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

                    System.out.println("Client connected: " + clientSocket.getInetAddress());
                    handleClientRequest(in, out);

                } catch (IOException e) {
                    System.err.println("Client connection error: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("Could not start server: " + e.getMessage());
        } finally {
            System.out.println("Server shutting down");
        }
    }

    private static void handleClientRequest(BufferedReader in, PrintWriter out) {
        try {
            String command = in.readLine();
            if (command == null || command.trim().isEmpty()) {
                sendJsonResponse(out, "error", "Empty command received", null);
                return;
            }

            command = command.trim();
            System.out.println("[SERVER] Received command: " + command);

            try {
                if (command.startsWith("GET_RACER_BY_ID")) {
                    handleGetRacerById(command, out);
                } else if (command.equals("GET_ALL_RACERS")) {
                    handleGetAllRacers(out);
                } else {
                    sendJsonResponse(out, "error", "Unknown command: " + command, null);
                }
            } catch (Exception e) {
                sendJsonResponse(out, "error", "Server error: " + e.getMessage(), null);
                e.printStackTrace();
            }
        } catch (IOException e) {
            System.err.println("[SERVER] Communication error: " + e.getMessage());
        }
    }

    private static void sendJsonResponse(PrintWriter out, String status, String message, Object data) {
        JSONObject response = new JSONObject();
        response.put("status", status);
        if (message != null) response.put("message", message);
        if (data != null) response.put("data", data);

        String jsonResponse = response.toString();
        System.out.println("[SERVER] Sending response: " + jsonResponse);
        out.println(jsonResponse);
        out.flush(); // Ensure response is sent immediately
    }

    private static void sendError(PrintWriter out, String message) {
        String errorJson = JsonConverter.createErrorResponse(message);
        System.err.println("Sending error: " + errorJson);
        out.println(errorJson);
    }


    private static void handleGetRacerById(String command, PrintWriter out) {
        try {
            String[] parts = command.split(" ");
            if (parts.length != 2) {
                out.println(JsonConverter.createErrorResponse("Invalid command format"));
                return;
            }

            int id = Integer.parseInt(parts[1]);
            MonzaPerformanceDAO dao = new MonzaPerformanceDAO();
            MonzaPerformanceDTO racer = dao.getRacerById(id);

            if (racer != null) {
                out.println(JsonConverter.createSuccessResponse(racer));
                System.out.println("Sent racer data for ID: " + id);
            } else {
                out.println(JsonConverter.createErrorResponse("Racer not found with ID: " + id));
            }
        } catch (NumberFormatException e) {
            out.println(JsonConverter.createErrorResponse("Invalid ID format"));
        }
    }

    private static void handleGetAllRacers(PrintWriter out) {
        MonzaPerformanceDAO dao = new MonzaPerformanceDAO();
        List<MonzaPerformanceDTO> racers = dao.getAllRacers();

        out.println(JsonConverter.createSuccessResponse(racers));
        System.out.println("Sent all racers data (" + racers.size() + " records)");
    }

//    private static void handleShutdown(PrintWriter out) {
//        out.println(JsonConverter.createSuccessResponse("Server shutting down"));
//        System.out.println("Received shutdown command");
//        running = false;
//    }
}