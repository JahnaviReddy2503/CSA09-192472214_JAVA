package parking.model;

import java.time.LocalDate;

public final class OneDayPass extends ParkingPass {
    public OneDayPass(String id, String registrationNumber, LocalDate date) { super(id, registrationNumber, date); }
}
