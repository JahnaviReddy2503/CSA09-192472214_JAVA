package parking.model;

public final class EBike extends Vehicle {
    public EBike(String registrationNumber, User owner) { super(registrationNumber, owner, VehicleType.E_BIKE); }
    @Override public SlotType preferredSlot() { return SlotType.EV_CHARGING; }
    @Override public double hourlyRate() { return 30; }
    @Override public boolean needsCharging() { return true; }
}
