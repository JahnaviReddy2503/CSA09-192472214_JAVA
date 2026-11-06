/**
 * Task06 single-file source bundle.
 *
 * The canonical compilable implementation is in the root src directory.
 * This task file keeps all source code for this assignment stage together.
 */
public final class Task06 {
    private Task06() {}

    /* ===== MonthlyPass.java =====
    package parking.model;
    
    import java.time.LocalDate;
    
    public final class MonthlyPass extends ParkingPass {
        public MonthlyPass(String id, String registrationNumber, LocalDate validUntil) { super(id, registrationNumber, validUntil); }
    }
    ===== end MonthlyPass.java ===== */

    /* ===== OneDayPass.java =====
    package parking.model;
    
    import java.time.LocalDate;
    
    public final class OneDayPass extends ParkingPass {
        public OneDayPass(String id, String registrationNumber, LocalDate date) { super(id, registrationNumber, date); }
    }
    ===== end OneDayPass.java ===== */

    /* ===== ParkingPass.java =====
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
    ===== end ParkingPass.java ===== */

}
