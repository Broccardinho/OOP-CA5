package org.example.main;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.client.F1Client;
import org.example.dto.MonzaPerformanceDTO;
import java.io.IOException;
import java.util.List;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;

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

    private F1Client f1Client;

    @FXML
    private void initialize() {
        // Set up the table columns
        setupTableColumns();
        addDeleteButtonColumn();

        // Connect to server
        connectToServer();

        // Load initial data
        refreshTable();
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
    }

    private void connectToServer() {
        try {
            f1Client = new F1Client("localhost", 8888);
            f1Client.connect();
            statusLabel.setText("Connected to server");
        } catch (IOException e) {
            statusLabel.setText("Error connecting to server: " + e.getMessage());
            showErrorAlert("Connection Error", "Failed to connect to server", e.getMessage());
        }
    }

    private void refreshTable() {
        try {
            List<MonzaPerformanceDTO> racers = f1Client.getAllRacers();
            racersTable.getItems().setAll(racers);
            statusLabel.setText("Loaded " + racers.size() + " racers from server");
        } catch (IOException e) {
            statusLabel.setText("Error loading data: " + e.getMessage());
            showErrorAlert("Data Load Error", "Failed to load racers", e.getMessage());
        }
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
            MonzaPerformanceDTO racer = f1Client.getRacerById(id);

            if (racer != null) {
                racersTable.getItems().setAll(racer);
                statusLabel.setText("Found racer: " + racer.getName());
            } else {
                statusLabel.setText("No racer found with ID: " + id);
                racersTable.getItems().clear();
            }
        } catch (NumberFormatException e) {
            statusLabel.setText("Invalid ID! Please enter a number.");
        } catch (IOException e) {
            statusLabel.setText("Error fetching racer: " + e.getMessage());
            showErrorAlert("Fetch Error", "Failed to fetch racer", e.getMessage());
        }
    }

    @FXML
    private void showAllRacers() {
        refreshTable();
    }

    private void showErrorAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    // Clean up resources when controller is no longer needed
    public void shutdown() {
        try {
            if (f1Client != null) {
                f1Client.disconnect();
            }
        } catch (IOException e) {
            System.err.println("Error disconnecting from server: " + e.getMessage());
        }
    }

    private void addDeleteButtonColumn() {
        TableColumn<MonzaPerformanceDTO, Void> deleteColumn = new TableColumn<>("Action");

        deleteColumn.setCellFactory(param -> new TableCell<>() {
            private final Button deleteButton = new Button("Delete");

            {
                deleteButton.setOnAction(event -> {
                    MonzaPerformanceDTO racer = getTableView().getItems().get(getIndex());
                    try {
                        boolean success = f1Client.deleteRacer(racer.getId());
                        if (success) {
                            statusLabel.setText("Successfully deleted racer: " + racer.getName());
                            refreshTable();
                        } else {
                            statusLabel.setText("Failed to delete racer: " + racer.getName());
                        }
                    } catch (IOException e) {
                        statusLabel.setText("Error deleting racer: " + e.getMessage());
                        showErrorAlert("Delete Error", "Failed to delete racer", e.getMessage());
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(deleteButton);
                }
            }
        });

        racersTable.getColumns().add(deleteColumn);
    }
}