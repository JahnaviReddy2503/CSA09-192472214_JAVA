package parking.model;

public final class ElectricCar extends Car {
    public ElectricCar(String registrationNumber, User owner) { super(registrationNumber, owner); }
    @Override public SlotType preferredSlot() { return SlotType.EV_CHARGING; }
    @Override public double hourlyRate() { return 50; }
    @Override public boolean needsCharging() { return true; }
}
