/**
 * Task02 single-file source bundle.
 *
 * The canonical compilable implementation is in the root src directory.
 * This task file keeps all source code for this assignment stage together.
 */
public final class Task02 {
    private Task02() {}

    /* ===== Staff.java =====
    package parking.model;
    
    public final class Staff extends User {
        public Staff(String id, String name) { super(id, name, UserType.STAFF); }
        @Override public boolean canUseMonthlyPass() { return true; }
    }
    ===== end Staff.java ===== */

    /* ===== Student.java =====
    package parking.model;
    
    public final class Student extends User {
        public Student(String id, String name) { super(id, name, UserType.STUDENT); }
        @Override public boolean canUseMonthlyPass() { return true; }
    }
    ===== end Student.java ===== */

    /* ===== User.java =====
    package parking.model;
    
    import java.util.Objects;
    
    public abstract class User {
        private final String id;
        private final String name;
        private final UserType type;
    
        protected User(String id, String name, UserType type) {
            if (id == null || id.isBlank() || name == null || name.isBlank()) {
                throw new IllegalArgumentException("User ID and name are required.");
            }
            this.id = id.trim();
            this.name = name.trim();
            this.type = Objects.requireNonNull(type, "User type is required.");
        }
    
        public String getId() { return id; }
        public String getName() { return name; }
        public UserType getType() { return type; }
        public abstract boolean canUseMonthlyPass();
    
        @Override public String toString() { return name + " (" + type + ", " + id + ")"; }
    }
    ===== end User.java ===== */

    /* ===== Vehicle.java =====
    package parking.model;
    
    import java.util.Objects;
    
    public abstract class Vehicle {
        private final String registrationNumber;
        private final User owner;
        private final VehicleType type;
    
        protected Vehicle(String registrationNumber, User owner, VehicleType type) {
            if (registrationNumber == null || !registrationNumber.matches("[A-Za-z0-9-]{3,15}")) {
                throw new IllegalArgumentException("Registration must contain 3-15 letters, digits or hyphens.");
            }
            this.registrationNumber = registrationNumber.toUpperCase();
            this.owner = Objects.requireNonNull(owner, "Owner is required.");
            this.type = Objects.requireNonNull(type, "Vehicle type is required.");
        }
    
        public String getRegistrationNumber() { return registrationNumber; }
        public User getOwner() { return owner; }
        public VehicleType getType() { return type; }
        public boolean needsCharging() { return false; }
        public abstract SlotType preferredSlot();
        public abstract double hourlyRate();
    
        @Override public String toString() { return registrationNumber + " - " + type + " - " + owner.getName(); }
        @Override public boolean equals(Object other) {
            return other instanceof Vehicle v && registrationNumber.equals(v.registrationNumber);
        }
        @Override public int hashCode() { return registrationNumber.hashCode(); }
    }
    ===== end Vehicle.java ===== */

    /* ===== VehicleType.java =====
    package parking.model;
    
    public enum VehicleType {
        TWO_WHEELER, CAR, ELECTRIC_CAR, E_BIKE, DELIVERY_VAN
    }
    ===== end VehicleType.java ===== */

    /* ===== Visitor.java =====
    package parking.model;
    
    public final class Visitor extends User {
        public Visitor(String id, String name) { super(id, name, UserType.VISITOR); }
        @Override public boolean canUseMonthlyPass() { return false; }
    }
    ===== end Visitor.java ===== */

}
