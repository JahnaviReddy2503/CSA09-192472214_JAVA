package parking.app;

import parking.model.*;
import parking.service.ParkingService;
import parking.exception.ParkingException;
import parking.ui.DashboardFrame;
import javax.swing.*;

public final class Main {
    private Main() {}
    public static void main(String[] args) {
        ParkingService service = new ParkingService();
        try {
            Student demoUser = new Student("STU001", "Demo Student");
            service.registerVehicle(new Car("DEMO-001", demoUser));
        } catch (ParkingException | IllegalArgumentException ignored) {
            // Demo data is optional; the UI remains usable if it cannot be loaded.
        }
        SwingUtilities.invokeLater(() -> new DashboardFrame(service).setVisible(true));
    }
}
