/**
 * Task05 single-file source bundle.
 *
 * The canonical compilable implementation is in the root src directory.
 * This task file keeps all source code for this assignment stage together.
 */
public final class Task05 {
    private Task05() {}

    /* ===== Chargeable.java =====
    package parking.pricing;
    
    public interface Chargeable {
        double calculateCharge(long minutes);
    }
    ===== end Chargeable.java ===== */

    /* ===== FeeCalculator.java =====
    package parking.pricing;
    
    import parking.model.*;
    import java.time.LocalDateTime;
    
    public final class FeeCalculator implements Chargeable {
        private static final double EV_CHARGING_PER_HOUR = 15;
        private Vehicle vehicle;
        private LocalDateTime entry;
        private LocalDateTime exit;
    
        public FeeCalculator() {}
        public FeeCalculator(Vehicle vehicle, LocalDateTime entry, LocalDateTime exit) {
            this.vehicle = vehicle; this.entry = entry; this.exit = exit;
        }
        @Override public double calculateCharge(long minutes) {
            if (vehicle == null) throw new IllegalStateException("Vehicle is required.");
            long hours = Math.max(1, (minutes + 59) / 60);
            double charge = Math.min(hours * vehicle.hourlyRate(), vehicle.hourlyRate() * 8);
            if (vehicle.needsCharging()) charge += hours * EV_CHARGING_PER_HOUR;
            if (vehicle.getType() == VehicleType.DELIVERY_VAN && entry != null && exit != null
                    && entry.toLocalDate().equals(exit.toLocalDate())) {
                boolean overlapsPeak = entry.toLocalTime().isBefore(java.time.LocalTime.of(17, 0))
                        && exit.toLocalTime().isAfter(java.time.LocalTime.of(9, 0));
                if (overlapsPeak) charge *= 2;
            }
            return charge;
        }
        public double calculate() { return calculateCharge(java.time.Duration.between(entry, exit).toMinutes()); }
    }
    ===== end FeeCalculator.java ===== */

    /* ===== ParkingTicket.java =====
    package parking.model;
    
    import java.time.Duration;
    import java.time.LocalDateTime;
    
    public final class ParkingTicket {
        private final String id;
        private final Vehicle vehicle;
        private final ParkingSlot slot;
        private final LocalDateTime entryTime;
        private LocalDateTime exitTime;
        private double fee;
    
        public ParkingTicket(String id, Vehicle vehicle, ParkingSlot slot, LocalDateTime entryTime) {
            this.id = id; this.vehicle = vehicle; this.slot = slot; this.entryTime = entryTime;
        }
        public String getId() { return id; }
        public Vehicle getVehicle() { return vehicle; }
        public ParkingSlot getSlot() { return slot; }
        public LocalDateTime getEntryTime() { return entryTime; }
        public LocalDateTime getExitTime() { return exitTime; }
        public double getFee() { return fee; }
        public void close(LocalDateTime exitTime, double fee) { this.exitTime = exitTime; this.fee = fee; }
        public long durationMinutes(LocalDateTime end) { return Math.max(0, Duration.between(entryTime, end).toMinutes()); }
    }
    ===== end ParkingTicket.java ===== */

    /* ===== TicketNotFoundException.java =====
    package parking.exception;
    
    public final class TicketNotFoundException extends ParkingException {
        public TicketNotFoundException(String ticketId) { super("Active ticket not found: " + ticketId); }
    }
    ===== end TicketNotFoundException.java ===== */

}
