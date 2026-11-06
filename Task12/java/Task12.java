/**
 * Task12 single-file source bundle.
 *
 * The canonical compilable implementation is in the root src directory.
 * This task file keeps all source code for this assignment stage together.
 */
public final class Task12 {
    private Task12() {}

    /* ===== Main.java =====
    package parking.app;
    
    import parking.model.*;
    import parking.service.ParkingService;
    import parking.exception.ParkingException;
    import parking.ui.LoginFrame;
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
            SwingUtilities.invokeLater(() -> new LoginFrame(service).setVisible(true));
        }
    }
    ===== end Main.java ===== */

    /* ===== ParkingServiceTest.java =====
    package parking;
    
    import org.junit.jupiter.api.Test;
    import parking.exception.*;
    import parking.model.*;
    import parking.pricing.FeeCalculator;
    import parking.service.ParkingService;
    import java.time.*;
    import static org.junit.jupiter.api.Assertions.*;
    
    class ParkingServiceTest {
        private final Student student = new Student("S1", "Asha");
    
        @Test void duplicateRegistrationIsRejected() throws Exception {
            ParkingService service = new ParkingService();
            service.registerVehicle(new Car("ABC-1", student));
            assertThrows(DuplicateVehicleException.class, () -> service.registerVehicle(new Car("ABC-1", student)));
        }
        @Test void vehicleCanEnterAndExit() throws Exception {
            ParkingService service = new ParkingService();
            service.registerVehicle(new Car("ABC-2", student));
            ParkingTicket ticket = service.park("ABC-2", LocalDateTime.of(2026, 1, 1, 10, 0));
            ParkingTicket receipt = service.exit(ticket.getId(), LocalDateTime.of(2026, 1, 1, 12, 0));
            assertEquals(80, receipt.getFee());
            assertEquals(0, service.occupiedSlots());
        }
        @Test void monthlyPassMakesFeeZero() throws Exception {
            ParkingService service = new ParkingService();
            service.registerVehicle(new Car("ABC-3", student));
            service.addPass(new MonthlyPass("M1", "ABC-3", LocalDate.of(2026, 12, 31)));
            ParkingTicket ticket = service.park("ABC-3", LocalDateTime.of(2026, 1, 1, 10, 0));
            assertEquals(0, service.exit(ticket.getId(), LocalDateTime.of(2026, 1, 1, 12, 0)).getFee());
        }
        @Test void evIncludesChargingFee() {
            ElectricCar car = new ElectricCar("EV-1", student);
            assertEquals(130, new FeeCalculator(car, LocalDateTime.of(2026, 1, 1, 8, 0), LocalDateTime.of(2026, 1, 1, 10, 0)).calculate());
        }
        @Test void deliveryVanPeakRateDoubles() {
            DeliveryVan van = new DeliveryVan("VAN-1", student);
            assertEquals(240, new FeeCalculator(van, LocalDateTime.of(2026, 1, 1, 10, 0), LocalDateTime.of(2026, 1, 1, 12, 0)).calculate());
        }
    
        @Test void oneDayPassIsValidOnlyOnItsDate() throws Exception {
            ParkingService service = new ParkingService();
            service.registerVehicle(new Car("ABC-4", new Visitor("V1", "Visitor")));
            service.addPass(new OneDayPass("D1", "ABC-4", LocalDate.of(2026, 1, 1)));
            ParkingTicket first = service.park("ABC-4", LocalDateTime.of(2026, 1, 1, 10, 0));
            assertEquals(0, service.exit(first.getId(), LocalDateTime.of(2026, 1, 1, 11, 0)).getFee());
            ParkingTicket second = service.park("ABC-4", LocalDateTime.of(2026, 1, 2, 10, 0));
            assertEquals(40, service.exit(second.getId(), LocalDateTime.of(2026, 1, 2, 11, 0)).getFee());
        }
    
        @Test void zeroMinuteParkingUsesMinimumOneHour() throws Exception {
            ParkingService service = new ParkingService();
            service.registerVehicle(new Car("ABC-5", student));
            ParkingTicket ticket = service.park("ABC-5", LocalDateTime.of(2026, 1, 1, 10, 0));
            assertEquals(40, service.exit(ticket.getId(), LocalDateTime.of(2026, 1, 1, 10, 0)).getFee());
        }
    
        @Test void dailyRevenueIncludesCompletedTickets() throws Exception {
            ParkingService service = new ParkingService();
            service.registerVehicle(new Car("ABC-6", student));
            ParkingTicket ticket = service.park("ABC-6", LocalDateTime.of(2026, 1, 1, 10, 0));
            service.exit(ticket.getId(), LocalDateTime.of(2026, 1, 1, 11, 0));
            assertEquals(40, service.dailyRevenue(LocalDate.of(2026, 1, 1)));
            assertEquals(0, service.dailyRevenue(LocalDate.of(2026, 1, 2)));
        }
    }
    ===== end ParkingServiceTest.java ===== */

}
