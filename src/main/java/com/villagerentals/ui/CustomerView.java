package com.villagerentals.ui;

import com.villagerentals.dao.CustomerDAO;
import com.villagerentals.model.Customer;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class CustomerView {

    private final CustomerDAO customerDAO = new CustomerDAO();
    private final TableView<Customer> customerTable = new TableView<>();

    private final TextField idField = new TextField();
    private final TextField firstNameField = new TextField();
    private final TextField lastNameField = new TextField();
    private final TextField phoneField = new TextField();
    private final TextField emailField = new TextField();

    public Parent createView(Runnable goBack) {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(20));

        Label title = new Label("Customer Management");
        title.setStyle("-fx-font-size: 22px; -fx-font-weight: bold;");

        setupTable();
        loadCustomers();

        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(10);

        idField.setPromptText("Customer ID");
        firstNameField.setPromptText("First Name");
        lastNameField.setPromptText("Last Name");
        phoneField.setPromptText("Phone");
        emailField.setPromptText("Email");

        form.add(new Label("Customer ID:"), 0, 0);
        form.add(idField, 1, 0);

        form.add(new Label("First Name:"), 0, 1);
        form.add(firstNameField, 1, 1);

        form.add(new Label("Last Name:"), 0, 2);
        form.add(lastNameField, 1, 2);

        form.add(new Label("Phone:"), 0, 3);
        form.add(phoneField, 1, 3);

        form.add(new Label("Email:"), 0, 4);
        form.add(emailField, 1, 4);

        Button addButton = new Button("Add Customer");
        Button deleteButton = new Button("Delete Selected");
        Button refreshButton = new Button("Refresh");
        Button backButton = new Button("Back");

        addButton.setOnAction(e -> addCustomer());
        deleteButton.setOnAction(e -> deleteSelectedCustomer());
        refreshButton.setOnAction(e -> loadCustomers());
        backButton.setOnAction(e -> goBack.run());

        HBox buttonBar = new HBox(10, addButton, deleteButton, refreshButton, backButton);

        VBox topSection = new VBox(15, title, form, buttonBar);

        root.setTop(topSection);
        root.setCenter(customerTable);

        return root;
    }

    private void setupTable() {
        TableColumn<Customer, Number> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(cellData ->
                new SimpleIntegerProperty(cellData.getValue().getCustomerId()));

        TableColumn<Customer, String> firstNameCol = new TableColumn<>("First Name");
        firstNameCol.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getFirstName()));

        TableColumn<Customer, String> lastNameCol = new TableColumn<>("Last Name");
        lastNameCol.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getLastName()));

        TableColumn<Customer, String> phoneCol = new TableColumn<>("Phone");
        phoneCol.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getContactPhone()));

        TableColumn<Customer, String> emailCol = new TableColumn<>("Email");
        emailCol.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getEmail()));

        customerTable.getColumns().add(idCol);
        customerTable.getColumns().add(firstNameCol);
        customerTable.getColumns().add(lastNameCol);
        customerTable.getColumns().add(phoneCol);
        customerTable.getColumns().add(emailCol);

        customerTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    private void loadCustomers() {
        customerTable.setItems(FXCollections.observableArrayList(customerDAO.getAll()));
    }

    private void addCustomer() {
        try {
            int id = Integer.parseInt(idField.getText().trim());
            String firstName = firstNameField.getText().trim();
            String lastName = lastNameField.getText().trim();
            String phone = phoneField.getText().trim();
            String email = emailField.getText().trim();

            if (firstName.isEmpty() || lastName.isEmpty() || phone.isEmpty() || email.isEmpty()) {
                showMessage(Alert.AlertType.ERROR, "Validation Error", "All fields are required.");
                return;
            }

            Customer customer = new Customer(id, lastName, firstName, phone, email);
            boolean added = customerDAO.add(customer);

            if (added) {
                clearFields();
                loadCustomers();
                showMessage(Alert.AlertType.INFORMATION, "Success", "Customer added successfully.");
            } else {
                showMessage(Alert.AlertType.ERROR, "Error", "Could not add customer.");
            }

        } catch (NumberFormatException ex) {
            showMessage(Alert.AlertType.ERROR, "Validation Error", "Customer ID must be a number.");
        } catch (Exception ex) {
            showMessage(Alert.AlertType.ERROR, "Error", "Something went wrong: " + ex.getMessage());
        }
    }

    private void deleteSelectedCustomer() {
        Customer selected = customerTable.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showMessage(Alert.AlertType.ERROR, "No Selection", "Please select a customer to delete.");
            return;
        }

        boolean deleted = customerDAO.delete(selected.getCustomerId());

        if (deleted) {
            loadCustomers();
            showMessage(Alert.AlertType.INFORMATION, "Success", "Customer deleted successfully.");
        } else {
            showMessage(Alert.AlertType.ERROR, "Error", "Could not delete customer.");
        }
    }

    private void clearFields() {
        idField.clear();
        firstNameField.clear();
        lastNameField.clear();
        phoneField.clear();
        emailField.clear();
    }

    private void showMessage(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}