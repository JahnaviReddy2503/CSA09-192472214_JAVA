package parking.model;

public final class Visitor extends User {
    public Visitor(String id, String name) { super(id, name, UserType.VISITOR); }
    @Override public boolean canUseMonthlyPass() { return false; }
}
