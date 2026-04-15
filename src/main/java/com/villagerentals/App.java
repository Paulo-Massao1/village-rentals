package com.villagerentals;

import com.villagerentals.dao.DatabaseManager;
import com.villagerentals.util.SampleDataLoader;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Initialize database and load sample data
        DatabaseManager.initialize();
        SampleDataLoader.loadIfEmpty();

        // Main layout - teammates will replace this with actual screens
        BorderPane root = new BorderPane();
        Label placeholder = new Label("Village Rentals System - Ready for GUI screens");
        placeholder.setStyle("-fx-font-size: 18px; -fx-padding: 40px;");
        root.setCenter(placeholder);

        Scene scene = new Scene(root, 900, 600);
        primaryStage.setTitle("Village Rentals System");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
