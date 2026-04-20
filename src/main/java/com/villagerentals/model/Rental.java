package com.villagerentals.model;

import java.time.LocalDate;

public class Rental {
    private int rentalId;
    private LocalDate date;
    private int customerId;
    private int equipmentId;
    private LocalDate rentalDate;
    private LocalDate returnDate;
    private double cost;

    public Rental() {}

    public Rental(int rentalId, LocalDate date, int customerId, int equipmentId,
                  LocalDate rentalDate, LocalDate returnDate, double cost) {
        this.rentalId = rentalId;
        this.date = date;
        this.customerId = customerId;
        this.equipmentId = equipmentId;
        this.rentalDate = rentalDate;
        this.returnDate = returnDate;
        this.cost = cost;
    }

    public int getRentalId() { return rentalId; }
    public void setRentalId(int rentalId) { this.rentalId = rentalId; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public int getCustomerId() { return customerId; }
    public void setCustomerId(int customerId) { this.customerId = customerId; }

    public int getEquipmentId() { return equipmentId; }
    public void setEquipmentId(int equipmentId) { this.equipmentId = equipmentId; }

    public LocalDate getRentalDate() { return rentalDate; }
    public void setRentalDate(LocalDate rentalDate) { this.rentalDate = rentalDate; }

    public LocalDate getReturnDate() { return returnDate; }
    public void setReturnDate(LocalDate returnDate) { this.returnDate = returnDate; }

    public double getCost() { return cost; }
    public void setCost(double cost) { this.cost = cost; }

    @Override
    public String toString() {
        return "Rental #" + rentalId + " - Customer: " + customerId + " Equipment: " + equipmentId;
    }
}
