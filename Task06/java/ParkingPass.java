package parking.model;

import java.time.LocalDate;

public abstract class ParkingPass {
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
