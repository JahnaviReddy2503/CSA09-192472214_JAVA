package parking.model;

public final class Student extends User {
    public Student(String id, String name) { super(id, name, UserType.STUDENT); }
    @Override public boolean canUseMonthlyPass() { return true; }
}
