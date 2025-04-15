package org.example.main;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.dao.MonzaPerformanceDAO;
import org.example.dto.MonzaPerformanceDTO;
import org.example.Exceptions.DaoException;
import org.example.dto.JsonConverter;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

public class MainController {
    // TableView and Columns
    @FXML private TableView<MonzaPerformanceDTO> racersTable;
    @FXML private TableColumn<MonzaPerformanceDTO, Integer> idColumn;
    @FXML private TableColumn<MonzaPerformanceDTO, String> nameColumn;
    @FXML private TableColumn<MonzaPerformanceDTO, String> teamColumn;
    @FXML private TableColumn<MonzaPerformanceDTO, String> nationalityColumn;
    @FXML private TableColumn<MonzaPerformanceDTO, Double> fastestLapColumn;
    @FXML private TableColumn<MonzaPerformanceDTO, Integer> gridPosColumn;
    @FXML private TableColumn<MonzaPerformanceDTO, Integer> finalPosColumn;
    @FXML private TableColumn<MonzaPerformanceDTO, Integer> pointsColumn;

    // Form controls
    @FXML private TextField idInput;
    @FXML private Label statusLabel;

    // Constants
    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 3306;
    private static final int CONNECTION_TIMEOUT = 3000; // 3 seconds

    @FXML
    private void initialize() {
        setupTableColumns();
        checkServerConnection();
    }

    private void setupTableColumns() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        teamColumn.setCellValueFactory(new PropertyValueFactory<>("team"));
        nationalityColumn.setCellValueFactory(new PropertyValueFactory<>("nationality"));
        fastestLapColumn.setCellValueFactory(new PropertyValueFactory<>("fastestLapTime"));
        gridPosColumn.setCellValueFactory(new PropertyValueFactory<>("gridPosition"));
        finalPosColumn.setCellValueFactory(new PropertyValueFactory<>("finalPosition"));
        pointsColumn.setCellValueFactory(new PropertyValueFactory<>("pointsEarned"));

        // Format fastest lap time to 3 decimal places
        fastestLapColumn.setCellFactory(column -> new TableCell<MonzaPerformanceDTO, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : String.format("%.3f", item));
            }
        });
    }

    private void checkServerConnection() {
        new Thread(() -> {
            boolean connected = isServerAvailable();
            Platform.runLater(() -> {
                if (connected) {
                    refreshTable();
                    statusLabel.setText("Connected to server");
                } else {
                    statusLabel.setText("Server not available - running in offline mode");
                    loadLocalData();
                }
            });
        }).start();
    }

    @FXML
    private void onFetchById() {
        String inputText = idInput.getText().trim();
        if (inputText.isEmpty()) {
            statusLabel.setText("Please enter an ID");
            return;
        }

        try {
            int id = Integer.parseInt(inputText);
            new Thread(() -> fetchRacerById(id)).start();
        } catch (NumberFormatException e) {
            statusLabel.setText("Invalid ID format - must be a number");
        }
    }

    private void fetchRacerById(int id) {
        try (Socket socket = new Socket()) {
            socket.setSoTimeout(5000); // 5 second timeout
            socket.connect(new InetSocketAddress(SERVER_HOST, SERVER_PORT), CONNECTION_TIMEOUT);

            try (PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                 BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

                out.println("GET_RACER_BY_ID " + id);
                String jsonResponse = in.readLine();

                System.out.println("Raw server response: " + jsonResponse); // Debug log

                Platform.runLater(() -> {
                    try {
                        MonzaPerformanceDTO racer = parseRacerResponse(jsonResponse);
                        racersTable.getItems().setAll(racer);
                        statusLabel.setText("Racer found: " + racer.getName());
                    } catch (DaoException e) {
                        statusLabel.setText(e.getMessage());
                        racersTable.getItems().clear();
                    }
                });
            }
        } catch (SocketTimeoutException e) {
            Platform.runLater(() -> statusLabel.setText("Server timeout - try again later"));
        } catch (ConnectException e) {
            Platform.runLater(() -> statusLabel.setText("Cannot connect to server"));
        } catch (IOException e) {
            Platform.runLater(() -> statusLabel.setText("Network error: " + e.getMessage()));
        }
    }

    @FXML
    private void showAllRacers() {
        new Thread(() -> {
            try (Socket socket = new Socket()) {
                socket.connect(new InetSocketAddress(SERVER_HOST, SERVER_PORT), CONNECTION_TIMEOUT);

                try (PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                     BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

                    out.println("GET_ALL_RACERS");
                    String jsonResponse = in.readLine();

                    Platform.runLater(() -> {
                        try {
                            if (jsonResponse == null) {
                                statusLabel.setText("No response from server");
                                return;
                            }

                            List<MonzaPerformanceDTO> racers = JsonConverter.jsonToRacers(jsonResponse);
                            racersTable.getItems().setAll(racers);
                            statusLabel.setText("Loaded " + racers.size() + " racers from server");
                        } catch (DaoException e) {
                            statusLabel.setText(e.getMessage());
                            loadLocalData();
                        }
                    });
                }
            } catch (ConnectException | SocketTimeoutException e) {
                Platform.runLater(() -> {
                    statusLabel.setText("Server connection failed - showing local data");
                    loadLocalData();
                });
            } catch (IOException e) {
                Platform.runLater(() -> statusLabel.setText("Communication error: " + e.getMessage()));
            }
        }).start();
    }

    private void refreshTable() {
        showAllRacers();
    }

    private void loadLocalData() {
        MonzaPerformanceDAO dao = new MonzaPerformanceDAO();
        List<MonzaPerformanceDTO> racers = dao.getAllRacers();
        Platform.runLater(() -> {
            racersTable.getItems().setAll(racers);
            statusLabel.setText("Loaded " + racers.size() + " racers from local database");
        });
    }

    private boolean isServerAvailable() {
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress(SERVER_HOST, SERVER_PORT), CONNECTION_TIMEOUT);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    @FXML
    private void onRetryConnection() {
        statusLabel.setText("Attempting to reconnect...");
        new Thread(() -> {
            boolean connected = isServerAvailable();
            Platform.runLater(() -> {
                if (connected) {
                    statusLabel.setText("Reconnected to server");
                    refreshTable();
                } else {
                    statusLabel.setText("Still unable to connect to server");
                }
            });
        }).start();
    }

    private MonzaPerformanceDTO parseRacerResponse(String jsonResponse) throws DaoException {
        if (jsonResponse == null || jsonResponse.trim().isEmpty()) {
            throw new DaoException("Empty response from server");
        }

        // Check for JSON object start
        if (!jsonResponse.trim().startsWith("{")) {
            // Check if this might be an error message from server
            if (jsonResponse.toLowerCase().contains("error")) {
                throw new DaoException(jsonResponse);
            }
            throw new DaoException("Invalid JSON response: " + jsonResponse);
        }

        try {
            return JsonConverter.jsonToRacer(jsonResponse);
        } catch (Exception e) {
            throw new DaoException("JSON parsing failed: " + e.getMessage());
        }
    }

}