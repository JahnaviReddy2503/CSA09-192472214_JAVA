package parking.service;

import parking.exception.*;
import parking.model.*;
import parking.pricing.FeeCalculator;
import java.time.*;
import java.util.*;

public final class ParkingService {
    private final Map<String, Vehicle> vehicles = new LinkedHashMap<>();
    private final Map<String, ParkingTicket> activeTickets = new LinkedHashMap<>();
    private final List<ParkingTicket> completedTickets = new ArrayList<>();
    private final List<ParkingFloor> floors = new ArrayList<>();
    private final Map<String, ParkingPass> passes = new HashMap<>();
    private int ticketSequence = 1000;

    public ParkingService() {
        floors.add(new ParkingFloor(1, 4, 4, 2, 1));
        floors.add(new ParkingFloor(2, 5, 6, 2, 1));
        floors.add(new ParkingFloor(3, 6, 8, 2, 2));
    }
    public void registerVehicle(Vehicle vehicle) throws DuplicateVehicleException {
        if (vehicles.containsKey(vehicle.getRegistrationNumber())) throw new DuplicateVehicleException(vehicle.getRegistrationNumber());
        vehicles.put(vehicle.getRegistrationNumber(), vehicle);
    }
    public void addPass(ParkingPass pass) { passes.put(pass.getRegistrationNumber(), pass); }
    public boolean hasValidPass(String registration, LocalDate date) {
        ParkingPass pass = passes.get(registration.toUpperCase());
        return pass != null && pass.isValid(date);
    }
    public Collection<Vehicle> getVehicles() { return List.copyOf(vehicles.values()); }
    public Collection<ParkingTicket> getActiveTickets() { return List.copyOf(activeTickets.values()); }
    public List<ParkingTicket> getCompletedTickets() { return List.copyOf(completedTickets); }
    public List<ParkingFloor> getFloors() { return List.copyOf(floors); }
    public long occupiedSlots() { return floors.stream().flatMap(f -> f.getSlots().stream()).filter(s -> !s.isAvailable()).count(); }
    public long totalSlots() { return floors.stream().mapToLong(f -> f.getSlots().size()).sum(); }
    public ParkingTicket park(String registration, LocalDateTime entry) throws ParkingException {
        if (registration == null || registration.isBlank() || entry == null) {
            throw new ParkingException("Registration and entry time are required.");
        }
        Vehicle vehicle = vehicles.get(registration.toUpperCase());
        if (vehicle == null) throw new ParkingException("Vehicle is not registered.");
        if (activeTickets.values().stream().anyMatch(t -> t.getVehicle().equals(vehicle))) throw new ParkingException("Vehicle is already parked.");
        ParkingSlot slot = floors.stream().flatMap(f -> f.getSlots().stream())
                .filter(s -> s.isAvailable() && s.getType() == vehicle.preferredSlot()).findFirst()
                .orElseThrow(() -> new NoAvailableSlotException(vehicle.getType().name()));
        slot.occupy(vehicle);
        ParkingTicket ticket = new ParkingTicket("T-" + (++ticketSequence), vehicle, slot, entry);
        activeTickets.put(ticket.getId(), ticket);
        return ticket;
    }
    public ParkingTicket exit(String ticketId, LocalDateTime exit) throws TicketNotFoundException {
        if (ticketId == null || ticketId.isBlank() || exit == null) {
            throw new TicketNotFoundException(String.valueOf(ticketId));
        }
        ParkingTicket ticket = activeTickets.remove(ticketId);
        if (ticket == null) throw new TicketNotFoundException(ticketId);
        double fee = hasValidPass(ticket.getVehicle().getRegistrationNumber(), exit.toLocalDate())
                ? 0 : new FeeCalculator(ticket.getVehicle(), ticket.getEntryTime(), exit).calculate();
        ticket.close(exit, fee);
        ticket.getSlot().release();
        completedTickets.add(ticket);
        return ticket;
    }
    public double dailyRevenue(LocalDate date) { return completedTickets.stream().filter(t -> t.getExitTime() != null && t.getExitTime().toLocalDate().equals(date)).mapToDouble(ParkingTicket::getFee).sum(); }
    public Map<String, Double> feesByVehicle() {
        Map<String, Double> result = new LinkedHashMap<>();
        completedTickets.forEach(ticket -> result.merge(ticket.getVehicle().getRegistrationNumber(),
                ticket.getFee(), Double::sum));
        return result;
    }
    public List<ParkingSlot> allSlots() { return floors.stream().flatMap(f -> f.getSlots().stream()).toList(); }
}
