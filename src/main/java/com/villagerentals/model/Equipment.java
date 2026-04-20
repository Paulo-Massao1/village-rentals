package com.villagerentals.model;

public class Equipment {
    private int equipmentId;
    private int categoryId;
    private String name;
    private String description;
    private double dailyRate;

    public Equipment() {}

    public Equipment(int equipmentId, int categoryId, String name, String description, double dailyRate) {
        this.equipmentId = equipmentId;
        this.categoryId = categoryId;
        this.name = name;
        this.description = description;
        this.dailyRate = dailyRate;
    }

    public int getEquipmentId() { return equipmentId; }
    public void setEquipmentId(int equipmentId) { this.equipmentId = equipmentId; }

    public int getCategoryId() { return categoryId; }
    public void setCategoryId(int categoryId) { this.categoryId = categoryId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public double getDailyRate() { return dailyRate; }
    public void setDailyRate(double dailyRate) { this.dailyRate = dailyRate; }

    @Override
    public String toString() {
        return equipmentId + " - " + name + " ($" + String.format("%.2f", dailyRate) + "/day)";
    }
}
