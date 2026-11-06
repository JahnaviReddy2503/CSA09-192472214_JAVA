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
}
