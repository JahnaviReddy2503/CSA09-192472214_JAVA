package parking.model;

public final class DeliveryVan extends Vehicle {
    public DeliveryVan(String registrationNumber, User owner) { super(registrationNumber, owner, VehicleType.DELIVERY_VAN); }
    @Override public SlotType preferredSlot() { return SlotType.DELIVERY; }
    @Override public double hourlyRate() { return 60; }
}
