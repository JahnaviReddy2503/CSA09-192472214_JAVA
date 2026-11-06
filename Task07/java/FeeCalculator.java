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
