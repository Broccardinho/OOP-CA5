package org.example.main;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.dao.MonzaPerformanceDAO;
import org.example.dto.MonzaPerformanceDTO;
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
            MonzaPerformanceDTO racer = racerDAO.getRacerById(id);
            if (racer != null) {
                racersTable.getItems().setAll(racer);
                statusLabel.setText("Racer found!");
            } else {
                statusLabel.setText("No racer found with ID: " + id);
            }
        } catch (NumberFormatException e) {
            statusLabel.setText("Invalid ID! Please enter a number.");
        }
    }

    @FXML
    private void showAllRacers() {
        refreshTable();
        statusLabel.setText("Showing all racers");
    }
}