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
