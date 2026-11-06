package parking.ui;

import parking.model.*;
import parking.service.ParkingService;
import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

public final class DashboardFrame extends JFrame {
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
