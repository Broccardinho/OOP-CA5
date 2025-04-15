package org.example.main;
import javafx.application.Application;
import javafx.fxml.*;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApp extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Main.fxml"));
        Parent root = loader.load();
        stage.setScene(new Scene(root, 800, 600));
        stage.setTitle("F1 Racer Tracker");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}