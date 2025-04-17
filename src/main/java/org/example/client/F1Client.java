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
}