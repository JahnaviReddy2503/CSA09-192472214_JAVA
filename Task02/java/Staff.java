package parking.model;

public final class Staff extends User {
    public Staff(String id, String name) { super(id, name, UserType.STAFF); }
    @Override public boolean canUseMonthlyPass() { return true; }
}
