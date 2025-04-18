package org.example.client;

import org.example.dto.JsonConverter;
import org.example.dto.MonzaPerformanceDTO;
import java.io.*;
import java.net.*;
import java.util.List;

public class F1Client {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private String serverAddress;
    private int serverPort;

    public F1Client(String serverAddress, int serverPort) {
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
    }

    public void connect() throws IOException {
        socket = new Socket(serverAddress, serverPort);
        out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    public void disconnect() throws IOException {
        if (in != null) in.close();
        if (out != null) out.close();
        if (socket != null) socket.close();
    }

    public List<MonzaPerformanceDTO> getAllRacers() throws IOException {
        out.println("GET_ALL_RACERS");
        String response = in.readLine();
        return JsonConverter.jsonStringToMonzaPerformanceList(response);
    }

    public MonzaPerformanceDTO getRacerById(int id) throws IOException {
        out.println("GET_RACER_BY_ID " + id);
        String response = in.readLine();
        if (response.startsWith("ERROR")) {
            throw new IOException(response);
        }
        return JsonConverter.jsonStringToMonzaPerformance(response);
    }
    public boolean deleteRacer(int id) throws IOException {
        // Validate input
        if (id <= 0) {
            throw new IllegalArgumentException("Invalid racer ID");
        }

        // Check if we need to reconnect
        if (socket == null || socket.isClosed() || !socket.isConnected()) {
            connect(); // Re-establish connection if needed
        }

        try {
            // Send delete command
            out.println("DELETE_RACER " + id);
            out.flush(); // Ensure command is sent immediately

            // Set read timeout (5 seconds)
            socket.setSoTimeout(5000);

            // Read server response
            String response = in.readLine();

            if (response == null) {
                throw new IOException("Server disconnected during operation");
            }

            // Handle different response cases
            if (response.startsWith("ERROR:")) {
                String errorMsg = response.substring(6).trim();
                if (errorMsg.contains("NOT_FOUND")) {
                    return false; // Racer not found is not an error condition
                }
                throw new IOException("Server error: " + errorMsg);
            }

            return "DELETED".equals(response) || "OK".equals(response);

        } catch (SocketTimeoutException e) {
            throw new IOException("Server response timeout", e);
        } catch (IOException e) {
            // Clean up broken connection
            try {
                disconnect();
            } catch (IOException ex) {
                e.addSuppressed(ex);
            }
            throw new IOException("Failed to delete racer: " + e.getMessage(), e);
        }
    }

    public boolean addRacer(MonzaPerformanceDTO racer) throws IOException {
        try {
            // Convert to JSON
            String racerJson = JsonConverter.monzaPerformanceToJsonString(racer);

            // Send command
            out.println("ADD_RACER " + racerJson);
            out.flush();

            // Read response
            String response = in.readLine();

            if (response == null) {
                throw new IOException("No response from server");
            }

            if (response.startsWith("ERROR:")) {
                throw new IOException(response.substring(6).trim());
            }

            // Return true if we got back valid JSON (success)
            JsonConverter.jsonStringToMonzaPerformance(response);
            return true;
        } catch (IllegalArgumentException e) {
            throw new IOException("Invalid server response: " + e.getMessage());
        }
    }
}