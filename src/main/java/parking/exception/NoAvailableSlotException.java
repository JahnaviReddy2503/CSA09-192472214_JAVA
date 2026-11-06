package parking.exception;

public final class NoAvailableSlotException extends ParkingException {
    public NoAvailableSlotException(String type) { super("No available slot for " + type + "."); }
}
