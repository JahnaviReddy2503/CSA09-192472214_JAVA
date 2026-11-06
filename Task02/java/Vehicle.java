package parking.model;

import java.util.Objects;

public abstract class Vehicle {
    private final String registrationNumber;
    private final User owner;
    private final VehicleType type;

    protected Vehicle(String registrationNumber, User owner, VehicleType type) {
        if (registrationNumber == null || !registrationNumber.matches("[A-Za-z0-9-]{3,15}")) {
            throw new IllegalArgumentException("Registration must contain 3-15 letters, digits or hyphens.");
        }
        this.registrationNumber = registrationNumber.toUpperCase();
        this.owner = Objects.requireNonNull(owner, "Owner is required.");
        this.type = Objects.requireNonNull(type, "Vehicle type is required.");
    }

    public String getRegistrationNumber() { return registrationNumber; }
    public User getOwner() { return owner; }
    public VehicleType getType() { return type; }
    public boolean needsCharging() { return false; }
    public abstract SlotType preferredSlot();
    public abstract double hourlyRate();

    @Override public String toString() { return registrationNumber + " - " + type + " - " + owner.getName(); }
    @Override public boolean equals(Object other) {
        return other instanceof Vehicle v && registrationNumber.equals(v.registrationNumber);
    }
    @Override public int hashCode() { return registrationNumber.hashCode(); }
}
