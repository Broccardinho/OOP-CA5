package org.example.main;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.dao.MonzaPerformanceDAO;
import org.example.dto.MonzaPerformanceDTO;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class MainController {
    @FXML private TableView<MonzaPerformanceDTO> racersTable;
    @FXML private TableColumn<MonzaPerformanceDTO, Integer> idColumn;
    @FXML private TableColumn<MonzaPerformanceDTO, String> nameColumn;
    @FXML private TableColumn<MonzaPerformanceDTO, String> teamColumn;
    @FXML private TableColumn<MonzaPerformanceDTO, String> nationalityColumn;
    @FXML private TableColumn<MonzaPerformanceDTO, Double> fastestLapColumn;
    @FXML private TableColumn<MonzaPerformanceDTO, Integer> gridPosColumn;
    @FXML private TableColumn<MonzaPerformanceDTO, Integer> finalPosColumn;
    @FXML private TableColumn<MonzaPerformanceDTO, Integer> pointsColumn;

    @FXML private TextField idInput;
    @FXML private Label statusLabel;
    private MonzaPerformanceDAO racerDAO = new MonzaPerformanceDAO();

    @FXML
    private void initialize() {
        // Set up the table columns
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        teamColumn.setCellValueFactory(new PropertyValueFactory<>("team"));
        nationalityColumn.setCellValueFactory(new PropertyValueFactory<>("nationality"));
        fastestLapColumn.setCellValueFactory(new PropertyValueFactory<>("fastestLapTime"));
        gridPosColumn.setCellValueFactory(new PropertyValueFactory<>("gridPosition"));
        finalPosColumn.setCellValueFactory(new PropertyValueFactory<>("finalPosition"));
        pointsColumn.setCellValueFactory(new PropertyValueFactory<>("pointsEarned"));

        // Format the fastest lap time column
        fastestLapColumn.setCellFactory(column -> {
            return new TableCell<MonzaPerformanceDTO, Double>() {
                @Override
                protected void updateItem(Double item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        setText(String.format("%.3f", item));
                    }
                }
            };
        });

        refreshTable();
    }

    //all racers is the default, so refreshing it shows all of them
    private void refreshTable() {
        List<MonzaPerformanceDTO> racers = racerDAO.getAllRacers();
        racersTable.getItems().setAll(racers);
    }

    //the status displaying/error check display
    @FXML
    private void onFetchById() {
        try {
            int id = Integer.parseInt(idInput.getText());

            try (Socket socket = new Socket("localhost", 3306);
                 BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                 BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

                out.write("GET_BY_ID:" + id);
                out.newLine();
                out.flush();

                String json = in.readLine();
                if (json != null && !json.equals("null")) {
                    MonzaPerformanceDTO racer = parseJsonToDTO(json);
                    racersTable.getItems().setAll(racer);
                    statusLabel.setText("Racer found via server!");
                } else {
                    statusLabel.setText("No racer found with ID: " + id);
                }

            } catch (IOException e) {
                statusLabel.setText("Server error: " + e.getMessage());
            }

        } catch (NumberFormatException e) {
            statusLabel.setText("Invalid ID! Please enter a number.");
        }
    }

    private MonzaPerformanceDTO parseJsonToDTO(String json) {
        JSONObject obj = new JSONObject(json);

        return new MonzaPerformanceDTO(
                obj.getInt("id"),
                obj.getString("name"),
                obj.getString("team"),
                obj.getDouble("fastestLapTime"),
                obj.getInt("finalPosition"),
                obj.getInt("gridPosition"),
                obj.getInt("pointsEarned"),
                obj.getString("nationality")
        );
    }



    @FXML
    private void showAllRacers() {
        try (Socket socket = new Socket("localhost", 3306);
             BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            out.write("GET_ALL");
            out.newLine();
            out.flush();

            String jsonArray = in.readLine();
            if (jsonArray != null && jsonArray.startsWith("[")) {
                List<MonzaPerformanceDTO> racers = parseJsonArray(jsonArray);
                racersTable.getItems().setAll(racers);
                statusLabel.setText("All racers fetched from server.");
            } else {
                statusLabel.setText("Failed to fetch racers.");
            }

        } catch (IOException e) {
            statusLabel.setText("Error: " + e.getMessage());
        }
    }

    private List<MonzaPerformanceDTO> parseJsonArray(String json) {
        List<MonzaPerformanceDTO> racers = new ArrayList<>();
        JSONArray array = new JSONArray(json);

        for (int i = 0; i < array.length(); i++) {
            JSONObject obj = array.getJSONObject(i);

            MonzaPerformanceDTO racer = new MonzaPerformanceDTO(
                    obj.getInt("id"),
                    obj.getString("name"),
                    obj.getString("team"),
                    obj.getDouble("fastestLapTime"),
                    obj.getInt("finalPosition"),
                    obj.getInt("gridPosition"),
                    obj.getInt("pointsEarned"),
                    obj.getString("nationality")
            );
            racers.add(racer);
        }

        return racers;
    }
}