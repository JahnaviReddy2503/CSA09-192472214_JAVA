package parking.ui;

import parking.exception.ParkingException;
import parking.model.*;
import parking.service.ParkingService;
import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;

public final class DashboardFrame extends JFrame {
    private final ParkingService service;
    private final JLabel summary = new JLabel();
    private final JTextArea output = new JTextArea();

    public DashboardFrame(ParkingService service) {
        this.service = service;
        setTitle("Campus Smart-Parking System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
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
        JButton slots = new JButton("Refresh slots");
        register.addActionListener(e -> registerVehicle());
        entry.addActionListener(e -> vehicleEntry());
        exit.addActionListener(e -> vehicleExit());
        slots.addActionListener(e -> refresh());
        actions.add(register); actions.add(entry); actions.add(exit); actions.add(slots);
        add(top, BorderLayout.NORTH); add(actions, BorderLayout.CENTER); add(new JScrollPane(output), BorderLayout.SOUTH);
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
    private void refresh() {
        summary.setText("Occupied: " + service.occupiedSlots() + " / " + service.totalSlots() + "   ");
        StringBuilder text = new StringBuilder("PARKING SLOTS\n=============\n");
        for (ParkingFloor floor : service.getFloors()) {
            text.append("Floor ").append(floor.getNumber()).append("\n");
            floor.getSlots().forEach(s -> text.append(String.format("%-12s %-13s %s%n", s.getId(), s.getType(), s.isAvailable() ? "AVAILABLE" : s.getVehicle().getRegistrationNumber())));
        }
        output.setText(text.toString());
    }
    private void showError(Exception ex) { JOptionPane.showMessageDialog(this, ex.getMessage(), "Operation failed", JOptionPane.ERROR_MESSAGE); }
}
