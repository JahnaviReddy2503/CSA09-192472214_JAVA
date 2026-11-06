package parking.exception;

public final class TicketNotFoundException extends ParkingException {
    public TicketNotFoundException(String ticketId) { super("Active ticket not found: " + ticketId); }
}
