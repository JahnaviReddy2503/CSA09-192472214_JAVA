/**
 * Task04 single-file source bundle.
 *
 * The canonical compilable implementation is in the root src directory.
 * This task file keeps all source code for this assignment stage together.
 */
public final class Task04 {
    private Task04() {}

    /* ===== NoAvailableSlotException.java =====
    package parking.exception;
    
    public final class NoAvailableSlotException extends ParkingException {
        public NoAvailableSlotException(String type) { super("No available slot for " + type + "."); }
    }
    ===== end NoAvailableSlotException.java ===== */

    /* ===== ParkingFloor.java =====
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
    ===== end ParkingFloor.java ===== */

    /* ===== ParkingSlot.java =====
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
    ===== end ParkingSlot.java ===== */

    /* ===== SlotType.java =====
    package parking.model;
    
    public enum SlotType {
        TWO_WHEELER, CAR, EV_CHARGING, DELIVERY
    }
    ===== end SlotType.java ===== */

}
