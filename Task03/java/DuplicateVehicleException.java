package parking.exception;

public final class DuplicateVehicleException extends ParkingException {
    public DuplicateVehicleException(String registration) { super("Vehicle already registered: " + registration); }
}
