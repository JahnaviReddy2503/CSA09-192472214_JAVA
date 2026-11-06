package parking.model;

public final class TwoWheeler extends Vehicle {
    public TwoWheeler(String registrationNumber, User owner) { super(registrationNumber, owner, VehicleType.TWO_WHEELER); }
    @Override public SlotType preferredSlot() { return SlotType.TWO_WHEELER; }
    @Override public double hourlyRate() { return 20; }
}
