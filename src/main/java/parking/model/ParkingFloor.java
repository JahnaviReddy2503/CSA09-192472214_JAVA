package parking.model;

import java.util.ArrayList;
import java.util.List;

public final class ParkingFloor {
    private final int number;
    private final List<ParkingSlot> slots = new ArrayList<>();

    public ParkingFloor(int number, int twoWheelerSlots, int carSlots, int evSlots, int deliverySlots) {
        if (number < 1 || twoWheelerSlots < 0 || carSlots < 0 || evSlots < 0 || deliverySlots < 0) {
            throw new IllegalArgumentException("Floor number and capacities must be valid.");
        }
        this.number = number;
        addSlots("T", twoWheelerSlots, SlotType.TWO_WHEELER);
        addSlots("C", carSlots, SlotType.CAR);
        addSlots("E", evSlots, SlotType.EV_CHARGING);
        addSlots("D", deliverySlots, SlotType.DELIVERY);
    }
    private void addSlots(String prefix, int count, SlotType type) {
        for (int i = 1; i <= count; i++) slots.add(new ParkingSlot("F" + number + "-" + prefix + i, type));
    }
    public int getNumber() { return number; }
    public List<ParkingSlot> getSlots() { return List.copyOf(slots); }
}
