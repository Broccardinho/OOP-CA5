package org.example.main;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.example.client.F1Client;
import org.example.dto.MonzaPerformanceDTO;
import java.io.IOException;
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
    @FXML private TextField addName;
    @FXML private TextField addTeam;
    @FXML private TextField addNationality;
    @FXML private TextField addFastestLap;
    @FXML private TextField addGridPos;
    @FXML private TextField addFinalPos;
    @FXML private TextField addPoints;
    @FXML private TextField addImageLink;
    @FXML private ImageView racerImageView;
    @FXML private TextField deleteIdInput;
    @FXML private TextField idInput;
    @FXML private Label statusLabel;

    private F1Client f1Client;

    @FXML
    private void initialize() {
        setupTableColumns();
        connectToServer();
        refreshTable();

        // Add selection listener for images
        racersTable.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> {
                    if (newSelection != null) {
                        updateRacerImage(newSelection.getImageLink());
                    } else {
                        updateRacerImage(null);
                    }
                });
    }

    private void updateRacerImage(String imageUrl) {
        if (imageUrl != null && !imageUrl.isEmpty()) {
            try {
                Image image = new Image(imageUrl, true); // Async loading
                racerImageView.setImage(image);

                // Log loading errors
                image.errorProperty().addListener((obs, wasError, isNowError) -> {
                    if (isNowError) {
                        racerImageView.setImage(null);
                        statusLabel.setText("Failed to load image: Invalid URL");
                    }
                });
            } catch (Exception e) {
                racerImageView.setImage(null);
                statusLabel.setText("Error loading image: " + e.getMessage());
            }
        } else {
            racerImageView.setImage(null);
            statusLabel.setText("No image available for this racer");
        }
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

        fastestLapColumn.setCellFactory(column -> new TableCell<MonzaPerformanceDTO, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(String.format("%.3f", item));
                }
            }
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

            // Select first racer by default to show their image
            if (!racers.isEmpty()) {
                racersTable.getSelectionModel().selectFirst();
            } else {
                updateRacerImage(null);
            }
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
                updateRacerImage(racer.getImageLink());
                statusLabel.setText("Found racer: " + racer.getName());
            } else {
                racersTable.getItems().clear();
                updateRacerImage(null);
                statusLabel.setText("No racer found with ID: " + id);
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

    public void shutdown() throws IOException {
        if (f1Client != null) {
            f1Client.disconnect();
        }
    }

    @FXML
    private void onDeleteRacer() {
        String inputText = deleteIdInput.getText().trim();
        if (inputText.isEmpty()) {
            statusLabel.setText("Please enter an ID to delete");
            return;
        }

        try {
            int id = Integer.parseInt(inputText);

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm Deletion");
            alert.setHeaderText("Delete Racer");
            alert.setContentText("Are you sure you want to delete racer with ID: " + id + "?");

            if (alert.showAndWait().get() == ButtonType.OK) {
                boolean deleted = f1Client.deleteRacer(id);

                if (deleted) {
                    statusLabel.setText("Successfully deleted racer with ID: " + id);
                    refreshTable();
                } else {
                    statusLabel.setText("No racer found with ID: " + id);
                }
            }
        } catch (NumberFormatException e) {
            statusLabel.setText("Invalid ID! Please enter a number.");
        } catch (IOException e) {
            statusLabel.setText("Error deleting racer: " + e.getMessage());
            showErrorAlert("Delete Error", "Failed to delete racer", e.getMessage());
        }
    }

    @FXML
    private void onAddRacer() {
        try {
            // Validate inputs
            if (addName.getText().isEmpty()) {
                statusLabel.setText("Name cannot be empty");
                return;
            }
            String imageLink = addImageLink.getText().trim();
            if (imageLink.isEmpty()) {
                imageLink = "https://example.com/default_racer.png";
            }

            MonzaPerformanceDTO newRacer = new MonzaPerformanceDTO(
                    0,
                    addName.getText(),
                    addTeam.getText(),
                    parseLapTime(addFastestLap.getText()),
                    Integer.parseInt(addFinalPos.getText()),
                    Integer.parseInt(addGridPos.getText()),
                    Integer.parseInt(addPoints.getText()),
                    addNationality.getText(),
                    imageLink
            );

            if (f1Client.addRacer(newRacer)) {
                statusLabel.setText("Racer added successfully!");
                refreshTable();
                clearAddForm();
            } else {
                statusLabel.setText("Failed to add racer (server error)");
            }
        } catch (NumberFormatException e) {
            statusLabel.setText("Invalid number in positions/points");
        } catch (IllegalArgumentException e) {
            statusLabel.setText(e.getMessage());
        } catch (IOException e) {
            statusLabel.setText("Error: " + e.getMessage());
        }
    }

    private double parseLapTime(String lapTime) throws IllegalArgumentException {
        try {
            if (lapTime.contains(":")) {
                String[] parts = lapTime.split(":");
                if (parts.length != 2) throw new IllegalArgumentException();
                int minutes = Integer.parseInt(parts[0]);
                double seconds = Double.parseDouble(parts[1]);
                return minutes * 60 + seconds;
            } else {
                return Double.parseDouble(lapTime);
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Lap time must be in SS.sss format");
        }
    }

    private void clearAddForm() {
        addName.clear();
        addTeam.clear();
        addNationality.clear();
        addFastestLap.clear();
        addGridPos.clear();
        addFinalPos.clear();
        addPoints.clear();
        addImageLink.clear();
    }
    @FXML
    private void onExit() {
        try {
            shutdown();

            Stage stage = (Stage) racersTable.getScene().getWindow();
            stage.close();
        } catch (IOException e) {
            showErrorAlert("Exit Error", "Failed to shutdown properly", e.getMessage());
        }
    }
}