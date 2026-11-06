/** Single-file source for Task03. */

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.time.*;
import java.util.*;
import javax.swing.*;
public final class Task03 {
    private Task03() {}
}

final class Main {
    private Main() {}
    public static void main(String[] args) {
        ParkingService service = new ParkingService();
        try {
            Student demoUser = new Student("STU001", "Demo Student");
            service.registerVehicle(new Car("DEMO-001", demoUser));
        } catch (ParkingException | IllegalArgumentException ignored) {
            // Demo data is optional; the UI remains usable if it cannot be loaded.
        }
        SwingUtilities.invokeLater(() -> new LoginFrame(service).setVisible(true));
    }
}


final class DuplicateVehicleException extends ParkingException {
    public DuplicateVehicleException(String registration) { super("Vehicle already registered: " + registration); }
}


final class NoAvailableSlotException extends ParkingException {
    public NoAvailableSlotException(String type) { super("No available slot for " + type + "."); }
}


class ParkingException extends Exception {
    public ParkingException(String message) { super(message); }
}


final class TicketNotFoundException extends ParkingException {
    public TicketNotFoundException(String ticketId) { super("Active ticket not found: " + ticketId); }
}


class Car extends Vehicle {
    public Car(String registrationNumber, User owner) { super(registrationNumber, owner, VehicleType.CAR); }
    @Override public SlotType preferredSlot() { return SlotType.CAR; }
    @Override public double hourlyRate() { return 40; }
}


final class DeliveryVan extends Vehicle {
    public DeliveryVan(String registrationNumber, User owner) { super(registrationNumber, owner, VehicleType.DELIVERY_VAN); }
    @Override public SlotType preferredSlot() { return SlotType.DELIVERY; }
    @Override public double hourlyRate() { return 60; }
}


final class EBike extends Vehicle {
    public EBike(String registrationNumber, User owner) { super(registrationNumber, owner, VehicleType.E_BIKE); }
    @Override public SlotType preferredSlot() { return SlotType.EV_CHARGING; }
    @Override public double hourlyRate() { return 30; }
    @Override public boolean needsCharging() { return true; }
}


final class ElectricCar extends Car {
    public ElectricCar(String registrationNumber, User owner) { super(registrationNumber, owner); }
    @Override public SlotType preferredSlot() { return SlotType.EV_CHARGING; }
    @Override public double hourlyRate() { return 50; }
    @Override public boolean needsCharging() { return true; }
}


final class MonthlyPass extends ParkingPass {
    public MonthlyPass(String id, String registrationNumber, LocalDate validUntil) { super(id, registrationNumber, validUntil); }
}


final class OneDayPass extends ParkingPass {
    public OneDayPass(String id, String registrationNumber, LocalDate date) { super(id, registrationNumber, date); }
}


final class ParkingFloor {
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


abstract class ParkingPass {
    private final String id;
    private final String registrationNumber;
    private final LocalDate validUntil;

    protected ParkingPass(String id, String registrationNumber, LocalDate validUntil) {
        this.id = id; this.registrationNumber = registrationNumber.toUpperCase(); this.validUntil = validUntil;
    }
    public boolean isValid(LocalDate date) { return !date.isAfter(validUntil); }
    public String getRegistrationNumber() { return registrationNumber; }
    public String getId() { return id; }
}


final class ParkingSlot {
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


final class ParkingTicket {
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


enum SlotType {
    TWO_WHEELER, CAR, EV_CHARGING, DELIVERY
}


final class Staff extends User {
    public Staff(String id, String name) { super(id, name, UserType.STAFF); }
    @Override public boolean canUseMonthlyPass() { return true; }
}


final class Student extends User {
    public Student(String id, String name) { super(id, name, UserType.STUDENT); }
    @Override public boolean canUseMonthlyPass() { return true; }
}


final class TwoWheeler extends Vehicle {
    public TwoWheeler(String registrationNumber, User owner) { super(registrationNumber, owner, VehicleType.TWO_WHEELER); }
    @Override public SlotType preferredSlot() { return SlotType.TWO_WHEELER; }
    @Override public double hourlyRate() { return 20; }
}


abstract class User {
    private final String id;
    private final String name;
    private final UserType type;

    protected User(String id, String name, UserType type) {
        if (id == null || id.isBlank() || name == null || name.isBlank()) {
            throw new IllegalArgumentException("User ID and name are required.");
        }
        this.id = id.trim();
        this.name = name.trim();
        this.type = Objects.requireNonNull(type, "User type is required.");
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public UserType getType() { return type; }
    public abstract boolean canUseMonthlyPass();

    @Override public String toString() { return name + " (" + type + ", " + id + ")"; }
}


enum UserType {
    STUDENT, STAFF, VISITOR
}


abstract class Vehicle {
    private final String registrationNumber;
    private final User owner;
    private final VehicleType type;

    protected Vehicle(String registrationNumber, User owner, VehicleType type) {
        if (registrationNumber == null || !registrationNumber.matches("[A-Za-z0-9-]{3,15}")) {
            throw new IllegalArgumentException("Registration must contain 3-15 letters, digits or hyphens.");
        }
        this.registrationNumber = registrationNumber.toUpperCase();
        this.owner = Objects.requireNonNull(owner, "Owner is required.");
        this.type = Objects.requireNonNull(type, "Vehicle type is required.");
    }

    public String getRegistrationNumber() { return registrationNumber; }
    public User getOwner() { return owner; }
    public VehicleType getType() { return type; }
    public boolean needsCharging() { return false; }
    public abstract SlotType preferredSlot();
    public abstract double hourlyRate();

    @Override public String toString() { return registrationNumber + " - " + type + " - " + owner.getName(); }
    @Override public boolean equals(Object other) {
        return other instanceof Vehicle v && registrationNumber.equals(v.registrationNumber);
    }
    @Override public int hashCode() { return registrationNumber.hashCode(); }
}


enum VehicleType {
    TWO_WHEELER, CAR, ELECTRIC_CAR, E_BIKE, DELIVERY_VAN
}


final class Visitor extends User {
    public Visitor(String id, String name) { super(id, name, UserType.VISITOR); }
    @Override public boolean canUseMonthlyPass() { return false; }
}


interface Chargeable {
    double calculateCharge(long minutes);
}


final class FeeCalculator implements Chargeable {
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


final class ParkingService {
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


final class DashboardFrame extends JFrame {
    private final ParkingService service;
    private final JLabel summary = new JLabel();
    private final JTextArea output = new JTextArea();
    private final JPanel slotPanel = new JPanel(new GridLayout(0, 5, 6, 6));

    public DashboardFrame(ParkingService service) {
        this.service = service;
        setTitle("Campus Smart-Parking System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);
        output.setEditable(false);
        output.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));
        JPanel top = new JPanel(new BorderLayout());
        top.add(new JLabel("  Campus Smart-Parking System", SwingConstants.LEFT), BorderLayout.WEST);
        top.add(summary, BorderLayout.EAST);
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton register = new JButton("Register vehicle");
        JButton entry = new JButton("Vehicle entry");
        JButton exit = new JButton("Vehicle exit");
        JButton pass = new JButton("Add pass");
        JButton reports = new JButton("Reports");
        JButton vehicles = new JButton("Registered vehicles");
        JButton slots = new JButton("Refresh");
        register.addActionListener(e -> registerVehicle());
        entry.addActionListener(e -> vehicleEntry());
        exit.addActionListener(e -> vehicleExit());
        pass.addActionListener(e -> addPass());
        reports.addActionListener(e -> showReports());
        vehicles.addActionListener(e -> showVehicles());
        slots.addActionListener(e -> refresh());
        actions.add(register); actions.add(entry); actions.add(exit); actions.add(pass);
        actions.add(reports); actions.add(vehicles); actions.add(slots);
        JPanel center = new JPanel(new BorderLayout(8, 8));
        center.add(actions, BorderLayout.NORTH);
        center.add(new JScrollPane(slotPanel), BorderLayout.CENTER);
        center.add(new JScrollPane(output), BorderLayout.SOUTH);
        add(top, BorderLayout.NORTH); add(center, BorderLayout.CENTER);
        ((BorderLayout) getContentPane().getLayout()).setVgap(8);
        refresh();
    }
    private void registerVehicle() {
        JTextField reg = new JTextField();
        JTextField id = new JTextField();
        JTextField name = new JTextField();
        JComboBox<String> user = new JComboBox<>(new String[]{"Student", "Staff", "Visitor"});
        JComboBox<String> type = new JComboBox<>(new String[]{"Car", "Two-wheeler", "Electric car", "E-bike", "Delivery van"});
        Object[] fields = {"Registration:", reg, "User ID:", id, "Name:", name, "User type:", user, "Vehicle type:", type};
        if (JOptionPane.showConfirmDialog(this, fields, "Register vehicle", JOptionPane.OK_CANCEL_OPTION) != JOptionPane.OK_OPTION) return;
        try {
            User u = switch ((String) user.getSelectedItem()) {
                case "Staff" -> new Staff(id.getText(), name.getText());
                case "Visitor" -> new Visitor(id.getText(), name.getText());
                default -> new Student(id.getText(), name.getText());
            };
            Vehicle v = switch ((String) type.getSelectedItem()) {
                case "Two-wheeler" -> new TwoWheeler(reg.getText(), u);
                case "Electric car" -> new ElectricCar(reg.getText(), u);
                case "E-bike" -> new EBike(reg.getText(), u);
                case "Delivery van" -> new DeliveryVan(reg.getText(), u);
                default -> new Car(reg.getText(), u);
            };
            service.registerVehicle(v); output.append("Registered: " + v + "\n"); refresh();
        } catch (Exception ex) { showError(ex); }
    }
    private void vehicleEntry() {
        String reg = JOptionPane.showInputDialog(this, "Registration number:");
        if (reg == null) return;
        try { output.append("Entry ticket: " + service.park(reg, LocalDateTime.now()).getId() + "\n"); refresh(); }
        catch (Exception ex) { showError(ex); }
    }
    private void vehicleExit() {
        String id = JOptionPane.showInputDialog(this, "Ticket number:");
        if (id == null) return;
        try { ParkingTicket t = service.exit(id, LocalDateTime.now()); output.append("Receipt " + t.getId() + ": fee Rs. " + t.getFee() + "\n"); refresh(); }
        catch (Exception ex) { showError(ex); }
    }
    private void addPass() {
        JTextField reg = new JTextField();
        JComboBox<String> type = new JComboBox<>(new String[]{"Monthly pass", "One-day visitor pass"});
        Object[] fields = {"Registration:", reg, "Pass type:", type};
        if (JOptionPane.showConfirmDialog(this, fields, "Add parking pass",
                JOptionPane.OK_CANCEL_OPTION) != JOptionPane.OK_OPTION) return;
        try {
            Vehicle vehicle = service.getVehicles().stream()
                    .filter(v -> v.getRegistrationNumber().equalsIgnoreCase(reg.getText().trim()))
                    .findFirst().orElseThrow(() -> new IllegalArgumentException("Vehicle is not registered."));
            if (type.getSelectedIndex() == 0 && !vehicle.getOwner().canUseMonthlyPass()) {
                throw new IllegalArgumentException("Only students and staff can use monthly passes.");
            }
            String id = "P-" + System.currentTimeMillis();
            if (type.getSelectedIndex() == 0) {
                service.addPass(new MonthlyPass(id, vehicle.getRegistrationNumber(), LocalDate.now().plusMonths(1)));
            } else {
                service.addPass(new OneDayPass(id, vehicle.getRegistrationNumber(), LocalDate.now()));
            }
            output.append("Pass added for " + vehicle.getRegistrationNumber() + "\n");
        } catch (Exception ex) { showError(ex); }
    }
    private void showVehicles() {
        StringBuilder text = new StringBuilder("REGISTERED VEHICLES\n===================\n");
        service.getVehicles().forEach(v -> text.append(v).append('\n'));
        output.setText(text.toString());
    }
    private void showReports() {
        StringBuilder text = new StringBuilder("PARKING REPORT\n==============\n");
        text.append("Total slots: ").append(service.totalSlots()).append('\n');
        text.append("Occupied slots: ").append(service.occupiedSlots()).append('\n');
        text.append("Available slots: ").append(service.totalSlots() - service.occupiedSlots()).append('\n');
        text.append("Active tickets: ").append(service.getActiveTickets().size()).append('\n');
        text.append("Today's revenue: Rs. ")
                .append(String.format("%.2f", service.dailyRevenue(LocalDate.now()))).append('\n');
        text.append("\nVEHICLE-WISE FEES\n=================\n");
        service.feesByVehicle().forEach((registration, fee) ->
                text.append(String.format("%-15s Rs. %.2f%n", registration, fee)));
        output.setText(text.toString());
    }
    private void refresh() {
        summary.setText("Occupied: " + service.occupiedSlots() + " / " + service.totalSlots() + "   ");
        slotPanel.removeAll();
        StringBuilder text = new StringBuilder("ACTIVE TICKETS\n==============\n");
        service.getActiveTickets().forEach(t -> text.append(t.getId()).append(" | ")
                .append(t.getVehicle().getRegistrationNumber()).append(" | ")
                .append(t.getSlot().getId()).append('\n'));
        for (ParkingFloor floor : service.getFloors()) {
            JLabel floorLabel = new JLabel("Floor " + floor.getNumber());
            floorLabel.setBorder(BorderFactory.createEmptyBorder(8, 4, 2, 4));
            slotPanel.add(floorLabel);
            floor.getSlots().forEach(s -> {
                JButton button = new JButton("<html>" + s.getId() + "<br>" +
                        (s.isAvailable() ? "AVAILABLE" : s.getVehicle().getRegistrationNumber()) + "</html>");
                button.setOpaque(true);
                button.setBackground(s.isAvailable() ? new Color(190, 245, 190)
                        : (s.getType() == SlotType.EV_CHARGING ? new Color(165, 205, 255) : new Color(255, 180, 180)));
                button.setToolTipText(s.getType().toString());
                slotPanel.add(button);
            });
        }
        output.setText(text.toString());
        slotPanel.revalidate();
        slotPanel.repaint();
    }
    private void showError(Exception ex) { JOptionPane.showMessageDialog(this, ex.getMessage(), "Operation failed", JOptionPane.ERROR_MESSAGE); }
}


final class LoginFrame extends JFrame {
    public LoginFrame(ParkingService service) {
        setTitle("Administrator Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(360, 220);
        setLocationRelativeTo(null);

        JPanel form = new JPanel(new GridLayout(3, 2, 8, 8));
        form.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        JTextField username = new JTextField();
        JPasswordField password = new JPasswordField();
        JButton login = new JButton("Sign in");
        form.add(new JLabel("Username:"));
        form.add(username);
        form.add(new JLabel("Password:"));
        form.add(password);
        form.add(new JLabel());
        form.add(login);
        login.addActionListener(event -> {
            if ("admin".equals(username.getText().trim())
                    && "admin123".equals(new String(password.getPassword()))) {
                dispose();
                new DashboardFrame(service).setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Use the administrator credentials from the README.",
                        "Login failed", JOptionPane.ERROR_MESSAGE);
            }
        });
        add(form);
    }
}
