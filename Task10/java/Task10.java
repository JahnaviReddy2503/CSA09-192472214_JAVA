/**
 * Task10 single-file source bundle.
 *
 * The canonical compilable implementation is in the root src directory.
 * This task file keeps all source code for this assignment stage together.
 */
public final class Task10 {
    private Task10() {}

    /* ===== DuplicateVehicleException.java =====
    package parking.exception;
    
    public final class DuplicateVehicleException extends ParkingException {
        public DuplicateVehicleException(String registration) { super("Vehicle already registered: " + registration); }
    }
    ===== end DuplicateVehicleException.java ===== */

    /* ===== NoAvailableSlotException.java =====
    package parking.exception;
    
    public final class NoAvailableSlotException extends ParkingException {
        public NoAvailableSlotException(String type) { super("No available slot for " + type + "."); }
    }
    ===== end NoAvailableSlotException.java ===== */

    /* ===== ParkingException.java =====
    package parking.exception;
    
    public class ParkingException extends Exception {
        public ParkingException(String message) { super(message); }
    }
    ===== end ParkingException.java ===== */

    /* ===== TicketNotFoundException.java =====
    package parking.exception;
    
    public final class TicketNotFoundException extends ParkingException {
        public TicketNotFoundException(String ticketId) { super("Active ticket not found: " + ticketId); }
    }
    ===== end TicketNotFoundException.java ===== */

}
