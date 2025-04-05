package org.example.main;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.example.dao.MonzaPerformanceDAO;
import org.example.dto.MonzaPerformanceDTO;
import java.util.List;

public class MainController {
    @FXML private ListView<MonzaPerformanceDTO> racersList;
    @FXML private TextField idInput;
    @FXML private Label statusLabel;
    private MonzaPerformanceDAO racerDAO = new MonzaPerformanceDAO();

    // Called when FXML loads
    @FXML
    private void initialize() {
        refreshTable();
    }

    private void refreshTable() {
        List<MonzaPerformanceDTO> racers = racerDAO.getAllRacers();
        racersList.getItems().setAll(racers);
    }

    @FXML
    private void onFetchById() {
        try {
            int id = Integer.parseInt(idInput.getText());
            MonzaPerformanceDTO racer = racerDAO.getRacerById(id);
            if (racer != null) {
                racersList.getItems().setAll(racer);
                statusLabel.setText("Racer found!");
            }
        } catch (NumberFormatException e) {
            statusLabel.setText("Invalid ID!");
        }
    }
}