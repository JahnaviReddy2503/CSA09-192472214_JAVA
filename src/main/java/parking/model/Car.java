package parking.model;

public class Car extends Vehicle {
    public Car(String registrationNumber, User owner) { super(registrationNumber, owner, VehicleType.CAR); }
    @Override public SlotType preferredSlot() { return SlotType.CAR; }
    @Override public double hourlyRate() { return 40; }
}
