package com.villagerentals.ui;

import com.villagerentals.dao.CustomerDAO;
import com.villagerentals.dao.EquipmentDAO;
import com.villagerentals.dao.RentalDAO;
import com.villagerentals.model.Customer;
import com.villagerentals.model.Equipment;
import com.villagerentals.model.Rental;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class RentalView {

    // DAOs to talk to the database
    private final RentalDAO rentalDAO = new RentalDAO();
    private final CustomerDAO customerDAO = new CustomerDAO();
    private final EquipmentDAO equipmentDAO = new EquipmentDAO();

    // Table that shows all existing rentals
    private final TableView<Rental> rentalTable = new TableView<>();

    // Form fields
    private final ComboBox<Customer> customerComboBox = new ComboBox<>();
    private final ComboBox<Equipment> equipmentComboBox = new ComboBox<>();
    private final DatePicker rentalDatePicker = new DatePicker();
    private final DatePicker returnDatePicker = new DatePicker();
    private final TextField costField = new TextField(); // read-only, auto-calculated

    public Parent createView(Runnable goBack) {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(20));

        Label title = new Label("Rental Processing");
        title.setStyle("-fx-font-size: 22px; -fx-font-weight: bold;");

        setupTable();
        loadRentals();
        loadDropdowns();

        // Set placeholder text for each field
        customerComboBox.setPromptText("Select Customer");
        equipmentComboBox.setPromptText("Select Equipment");
        rentalDatePicker.setPromptText("Rental Date");
        returnDatePicker.setPromptText("Return Date");
        costField.setPromptText("Auto-calculated");
        costField.setEditable(false);

        // Recalculate cost whenever the user changes equipment or dates
        equipmentComboBox.setOnAction(e -> recalculateCost());
        rentalDatePicker.setOnAction(e -> recalculateCost());
        returnDatePicker.setOnAction(e -> recalculateCost());

        // Build the form layout
        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(10);

        form.add(new Label("Customer:"), 0, 0);
        form.add(customerComboBox, 1, 0);

        form.add(new Label("Equipment:"), 0, 1);
        form.add(equipmentComboBox, 1, 1);

        form.add(new Label("Rental Date:"), 0, 2);
        form.add(rentalDatePicker, 1, 2);

        form.add(new Label("Return Date:"), 0, 3);
        form.add(returnDatePicker, 1, 3);

        form.add(new Label("Total Cost ($):"), 0, 4);
        form.add(costField, 1, 4);

        Button processButton = new Button("Process Rental");
        Button refreshButton = new Button("Refresh");
        Button backButton = new Button("Back");

        processButton.setOnAction(e -> processRental());
        refreshButton.setOnAction(e -> loadRentals());
        backButton.setOnAction(e -> goBack.run());

        HBox buttonBar = new HBox(10, processButton, refreshButton, backButton);
        VBox topSection = new VBox(15, title, form, buttonBar);

        root.setTop(topSection);
        root.setCenter(rentalTable);

        return root;
    }

    private void setupTable() {
        TableColumn<Rental, Number> idCol = new TableColumn<>("Rental ID");
        idCol.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getRentalId()));

        TableColumn<Rental, String> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDate().toString()));

        TableColumn<Rental, Number> customerCol = new TableColumn<>("Customer ID");
        customerCol.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getCustomerId()));

        TableColumn<Rental, Number> equipmentCol = new TableColumn<>("Equipment ID");
        equipmentCol.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getEquipmentId()));

        TableColumn<Rental, String> rentalDateCol = new TableColumn<>("Rental Date");
        rentalDateCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getRentalDate().toString()));

        TableColumn<Rental, String> returnDateCol = new TableColumn<>("Return Date");
        returnDateCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getReturnDate().toString()));

        TableColumn<Rental, Number> costCol = new TableColumn<>("Cost ($)");
        costCol.setCellValueFactory(data -> new SimpleDoubleProperty(data.getValue().getCost()));

        rentalTable.getColumns().add(idCol);
        rentalTable.getColumns().add(dateCol);
        rentalTable.getColumns().add(customerCol);
        rentalTable.getColumns().add(equipmentCol);
        rentalTable.getColumns().add(rentalDateCol);
        rentalTable.getColumns().add(returnDateCol);
        rentalTable.getColumns().add(costCol);
        rentalTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
    }

    private void loadRentals() {
        rentalTable.setItems(FXCollections.observableArrayList(rentalDAO.getAll()));
    }

    private void loadDropdowns() {
        customerComboBox.setItems(FXCollections.observableArrayList(customerDAO.getAll()));
        equipmentComboBox.setItems(FXCollections.observableArrayList(equipmentDAO.getAll()));
    }

    // Calculates cost live as the user picks dates and equipment
    private void recalculateCost() {
        Equipment equipment = equipmentComboBox.getValue();
        LocalDate start = rentalDatePicker.getValue();
        LocalDate end = returnDatePicker.getValue();

        // Can't calculate if anything is missing or dates are wrong
        if (equipment == null || start == null || end == null || !end.isAfter(start)) {
            costField.clear();
            return;
        }

        long days = ChronoUnit.DAYS.between(start, end);
        double cost = Math.round(days * equipment.getDailyRate() * 100.0) / 100.0;
        costField.setText(String.format("%.2f", cost));
    }

    private void processRental() {
        Customer customer = customerComboBox.getValue();
        Equipment equipment = equipmentComboBox.getValue();
        LocalDate start = rentalDatePicker.getValue();
        LocalDate end = returnDatePicker.getValue();

        if (customer == null || equipment == null || start == null || end == null) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Please fill in all fields.");
            return;
        }

        if (!end.isAfter(start)) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Return date must be after the rental date.");
            return;
        }

        long days = ChronoUnit.DAYS.between(start, end);
        double cost = Math.round(days * equipment.getDailyRate() * 100.0) / 100.0;

        // Save to the database — today's date is used as the transaction date
        Rental rental = new Rental(0, LocalDate.now(), customer.getCustomerId(),
                equipment.getEquipmentId(), start, end, cost);

        boolean saved = rentalDAO.add(rental);

        if (saved) {
            clearForm();
            loadRentals();
            showAlert(Alert.AlertType.INFORMATION, "Success",
                    String.format("Rental saved!\nTotal cost: $%.2f", cost));
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "Something went wrong saving the rental.");
        }
    }

    private void clearForm() {
        customerComboBox.setValue(null);
        equipmentComboBox.setValue(null);
        rentalDatePicker.setValue(null);
        returnDatePicker.setValue(null);
        costField.clear();
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
