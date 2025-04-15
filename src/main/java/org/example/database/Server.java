package org.example.database;

import org.example.dao.MonzaPerformanceDAO;
import org.example.dto.MonzaPerformanceDTO;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

public class Server {
    public static void main(String[] args) {
        int port = 3306;
        MonzaPerformanceDAO dao = new MonzaPerformanceDAO();

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server started on port " + port);

            while (true) {
                try (Socket clientSocket = serverSocket.accept();
                     BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                     BufferedWriter out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()))) {

                    System.out.println("Client connected: " + clientSocket.getInetAddress());

                    String input = in.readLine();
                    System.out.println("Received command: " + input);

                    if (input.startsWith("GET_BY_ID")) {
                        String[] parts = input.split(":");
                        if (parts.length == 2) {
                            int id = Integer.parseInt(parts[1]);
                            MonzaPerformanceDTO racer = dao.getRacerById(id);
                            if (racer != null) {
                                String json = dtoToJson(racer);
                                out.write(json);
                            } else {
                                out.write("null");
                            }
                            out.newLine();
                            out.flush();
                        }
                    } else if (input.equals("GET_ALL")) {
                        List<MonzaPerformanceDTO> racers = dao.getAllRacers();
                        String jsonArray = listToJsonArray(racers);

                        System.out.println("Sending JSON array: " + jsonArray); // <-- ADD THIS

                        out.write(jsonArray);
                        out.newLine();
                        out.flush();
                    }
                    else {
                        out.write("Invalid command");
                        out.newLine();
                        out.flush();
                    }

                } catch (IOException e) {
                    System.err.println("Error handling client: " + e.getMessage());
                }
            }

        } catch (IOException e) {
            System.err.println("Could not start server on port " + port);
            e.printStackTrace();
        }
    }

    private static String dtoToJson(MonzaPerformanceDTO dto) {
        return "{" +
                "\"id\":" + dto.getId() + "," +
                "\"name\":\"" + dto.getName() + "\"," +
                "\"team\":\"" + dto.getTeam() + "\"," +
                "\"fastestLapTime\":" + dto.getFastestLapTime() + "," +
                "\"finalPosition\":" + dto.getFinalPosition() + "," +
                "\"gridPosition\":" + dto.getGridPosition() + "," +
                "\"pointsEarned\":" + dto.getPointsEarned() + "," +
                "\"nationality\":\"" + dto.getNationality() + "\"" +
                "}";
    }

    private static String listToJsonArray(List<MonzaPerformanceDTO> list) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");

        for (int i = 0; i < list.size(); i++) {
            MonzaPerformanceDTO dto = list.get(i);
            sb.append(dtoToJson(dto));
            if (i < list.size() - 1) {
                sb.append(",");
            }
        }

        sb.append("]");
        return sb.toString();
    }
}
