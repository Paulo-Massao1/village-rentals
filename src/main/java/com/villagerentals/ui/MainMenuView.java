package com.villagerentals.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class MainMenuView {

    public interface NavigationHandler {
        void goToCustomers();
        void goToEquipment();
        void goToRentals();
    }

    public Parent createView(NavigationHandler nav) {
        VBox root = new VBox(20);
        root.setPadding(new Insets(30));
        root.setAlignment(Pos.CENTER);

        Label title = new Label("Village Rentals");
        title.setStyle("-fx-font-size: 28px; -fx-font-weight: bold;");

        Button customersBtn = new Button("Customers");
        Button equipmentBtn = new Button("Equipment");
        Button rentalsBtn = new Button("Rentals");

        customersBtn.setPrefWidth(220);
        equipmentBtn.setPrefWidth(220);
        rentalsBtn.setPrefWidth(220);

        customersBtn.setOnAction(e -> nav.goToCustomers());

        equipmentBtn.setOnAction(e -> nav.goToEquipment());

        rentalsBtn.setOnAction(e -> {
            // Person 4 screen placeholder
        });

        root.getChildren().addAll(title, customersBtn, equipmentBtn, rentalsBtn);
        return root;
    }
}
