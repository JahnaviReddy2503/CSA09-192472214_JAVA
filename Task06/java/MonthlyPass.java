package parking.model;

import java.time.LocalDate;

public final class MonthlyPass extends ParkingPass {
    public MonthlyPass(String id, String registrationNumber, LocalDate validUntil) { super(id, registrationNumber, validUntil); }
}
