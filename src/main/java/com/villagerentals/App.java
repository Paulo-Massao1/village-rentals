package com.villagerentals;

import com.villagerentals.dao.DatabaseManager;
import com.villagerentals.ui.CustomerView;
import com.villagerentals.ui.EquipmentView;
import com.villagerentals.ui.MainMenuView;
import com.villagerentals.util.SampleDataLoader;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

    private Stage primaryStage;

    @Override
    public void start(Stage stage) {
        this.primaryStage = stage;

        DatabaseManager.initialize();
        SampleDataLoader.loadIfEmpty();

        showMainMenu();

        primaryStage.setTitle("Village Rentals");
        primaryStage.show();
    }

    private void showMainMenu() {
        MainMenuView mainMenuView = new MainMenuView();

        Scene scene = new Scene(
                mainMenuView.createView(new MainMenuView.NavigationHandler() {
                    @Override
                    public void goToCustomers() {
                        showCustomerView();
                    }

                    @Override
                    public void goToEquipment() {

                        showEquipmentView();

                    }

                    @Override
                    public void goToRentals() {
                        // Person 4 area
                    }
                }),
                900,
                600
        );

        primaryStage.setScene(scene);
    }

    private void showCustomerView() {
        CustomerView customerView = new CustomerView();
        Scene scene = new Scene(customerView.createView(this::showMainMenu), 900, 600);
        primaryStage.setScene(scene);
    }


    private void showEquipmentView() {
        EquipmentView equipmentView = new EquipmentView();
        Scene scene = new Scene(equipmentView.createView(this::showMainMenu), 900, 600);
        primaryStage.setScene(scene);
    }


    public static void main(String[] args) {
        launch(args);
    }
}
