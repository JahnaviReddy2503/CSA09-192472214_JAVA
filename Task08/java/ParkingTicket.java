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
