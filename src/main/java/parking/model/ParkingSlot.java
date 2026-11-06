package parking.model;

import java.util.Objects;

public final class ParkingSlot {
    private final String id;
    private final SlotType type;
    private Vehicle vehicle;

    public ParkingSlot(String id, SlotType type) {
        this.id = Objects.requireNonNull(id);
        this.type = Objects.requireNonNull(type);
    }
    public String getId() { return id; }
    public SlotType getType() { return type; }
    public Vehicle getVehicle() { return vehicle; }
    public boolean isAvailable() { return vehicle == null; }
    public void occupy(Vehicle vehicle) {
        if (!isAvailable()) throw new IllegalStateException("Slot " + id + " is occupied.");
        this.vehicle = Objects.requireNonNull(vehicle);
    }
    public void release() { vehicle = null; }
    @Override public String toString() { return id + " [" + type + "] - " + (isAvailable() ? "AVAILABLE" : vehicle.getRegistrationNumber()); }
}
