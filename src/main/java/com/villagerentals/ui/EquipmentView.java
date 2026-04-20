package com.villagerentals.ui;

import com.villagerentals.dao.CategoryDAO;
import com.villagerentals.dao.EquipmentDAO;
import com.villagerentals.model.Category;
import com.villagerentals.model.Equipment;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class EquipmentView {

    private final EquipmentDAO equipmentDAO = new EquipmentDAO();
    private final CategoryDAO categoryDAO = new CategoryDAO();
    private final TableView<Equipment> equipmentTable = new TableView<>();

    private final TextField idField = new TextField();
    private final TextField nameField = new TextField();
    private final TextField descriptionField = new TextField();
    private final TextField dailyRateField = new TextField();
    private final ComboBox<Category> categoryComboBox = new ComboBox<>();

    public Parent createView(Runnable goBack) {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(20));

        Label title = new Label("Equipment Management");
        title.setStyle("-fx-font-size: 22px; -fx-font-weight: bold;");

        setupTable();
        loadEquipment();
        loadCategories();

        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(10);

        idField.setPromptText("Equipment ID");
        nameField.setPromptText("Name");
        descriptionField.setPromptText("Description");
        dailyRateField.setPromptText("Daily Rate (e.g. 19.99)");
        categoryComboBox.setPromptText("Select Category");

        form.add(new Label("Equipment ID:"), 0, 0);
        form.add(idField, 1, 0);

        form.add(new Label("Name:"), 0, 1);
        form.add(nameField, 1, 1);

        form.add(new Label("Description:"), 0, 2);
        form.add(descriptionField, 1, 2);

        form.add(new Label("Daily Rate ($):"), 0, 3);
        form.add(dailyRateField, 1, 3);

        form.add(new Label("Category:"), 0, 4);
        form.add(categoryComboBox, 1, 4);

        Button addButton = new Button("Add Equipment");
        Button deleteButton = new Button("Delete Selected");
        Button refreshButton = new Button("Refresh");
        Button backButton = new Button("Back");

        addButton.setOnAction(e -> addEquipment());
        deleteButton.setOnAction(e -> deleteSelectedEquipment());
        refreshButton.setOnAction(e -> loadEquipment());
        backButton.setOnAction(e -> goBack.run());

        HBox buttonBar = new HBox(10, addButton, deleteButton, refreshButton, backButton);

        VBox topSection = new VBox(15, title, form, buttonBar);

        root.setTop(topSection);
        root.setCenter(equipmentTable);

        return root;
    }

    private void setupTable() {
        TableColumn<Equipment, Number> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(cellData ->
                new SimpleIntegerProperty(cellData.getValue().getEquipmentId()));

        TableColumn<Equipment, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getName()));

        TableColumn<Equipment, String> descriptionCol = new TableColumn<>("Description");
        descriptionCol.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getDescription()));

        TableColumn<Equipment, Number> dailyRateCol = new TableColumn<>("Daily Rate ($)");
        dailyRateCol.setCellValueFactory(cellData ->
                new SimpleDoubleProperty(cellData.getValue().getDailyRate()));

        TableColumn<Equipment, Number> categoryIdCol = new TableColumn<>("Category ID");
        categoryIdCol.setCellValueFactory(cellData ->
                new SimpleIntegerProperty(cellData.getValue().getCategoryId()));

        equipmentTable.getColumns().add(idCol);
        equipmentTable.getColumns().add(nameCol);
        equipmentTable.getColumns().add(descriptionCol);
        equipmentTable.getColumns().add(dailyRateCol);
        equipmentTable.getColumns().add(categoryIdCol);

        equipmentTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
    }

    private void loadEquipment() {
        equipmentTable.setItems(FXCollections.observableArrayList(equipmentDAO.getAll()));
    }

    private void loadCategories() {
        categoryComboBox.setItems(FXCollections.observableArrayList(categoryDAO.getAll()));
    }

    private void addEquipment() {
        try {
            int id = Integer.parseInt(idField.getText().trim());
            String name = nameField.getText().trim();
            String description = descriptionField.getText().trim();
            double dailyRate = Double.parseDouble(dailyRateField.getText().trim());
            Category selectedCategory = categoryComboBox.getValue();

            if (name.isEmpty() || description.isEmpty()) {
                showMessage(Alert.AlertType.ERROR, "Validation Error", "Name and description are required.");
                return;
            }

            if (selectedCategory == null) {
                showMessage(Alert.AlertType.ERROR, "Validation Error", "Please select a category.");
                return;
            }

            if (dailyRate <= 0) {
                showMessage(Alert.AlertType.ERROR, "Validation Error", "Daily rate must be greater than 0.");
                return;
            }

            Equipment equipment = new Equipment(id, selectedCategory.getCategoryId(), name, description, dailyRate);
            boolean added = equipmentDAO.add(equipment);

            if (added) {
                clearFields();
                loadEquipment();
                showMessage(Alert.AlertType.INFORMATION, "Success", "Equipment added successfully.");
            } else {
                showMessage(Alert.AlertType.ERROR, "Error", "Could not add equipment. ID may already exist.");
            }

        } catch (NumberFormatException ex) {
            showMessage(Alert.AlertType.ERROR, "Validation Error", "Equipment ID must be a whole number and Daily Rate must be a valid number.");
        } catch (Exception ex) {
            showMessage(Alert.AlertType.ERROR, "Error", "Something went wrong: " + ex.getMessage());
        }
    }

    private void deleteSelectedEquipment() {
        Equipment selected = equipmentTable.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showMessage(Alert.AlertType.ERROR, "No Selection", "Please select an equipment item to delete.");
            return;
        }

        boolean deleted = equipmentDAO.delete(selected.getEquipmentId());

        if (deleted) {
            loadEquipment();
            showMessage(Alert.AlertType.INFORMATION, "Success", "Equipment deleted successfully.");
        } else {
            showMessage(Alert.AlertType.ERROR, "Error", "Could not delete equipment.");
        }
    }

    private void clearFields() {
        idField.clear();
        nameField.clear();
        descriptionField.clear();
        dailyRateField.clear();
        categoryComboBox.setValue(null);
    }

    private void showMessage(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
