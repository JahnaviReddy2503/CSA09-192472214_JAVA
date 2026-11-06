# Campus Smart-Parking System

A Java 17 Swing application for managing a multi-floor university parking facility.

## Features

- Student, staff and visitor users
- Cars, two-wheelers, electric cars, e-bikes and delivery vans
- Three floors with different capacities
- Automatic compatible-slot allocation and release
- EV charging slots and charging fees
- Monthly and one-day pass model
- Hourly rates, daily maximum, visitor passes and delivery-van peak pricing
- Ticket generation, receipts, occupancy and daily revenue services
- Swing dashboard with coloured slot indicators, pass management and reports
- Input validation, duplicate detection and custom exceptions
- JUnit 5 automated tests

## Requirements

- JDK 17 or later
- Maven 3.9 or later
- VS Code with Extension Pack for Java

## Run from PowerShell

```powershell
mvn test
mvn package
java -jar target\campus-smart-parking-1.0.0.jar
```

The application starts with a Swing dashboard. The demo vehicle `DEMO-001` is registered automatically.

The administrator login is:

```text
Username: admin
Password: admin123
```

The dashboard supports registering vehicles, vehicle entry and exit, monthly or
one-day pass creation, registered-vehicle reports, occupancy reports, active
ticket reports and daily revenue. Green slots are available, red slots are
occupied, and blue slots are EV charging slots.

## Project structure

`src\main\java\parking\model` contains entities and inheritance hierarchies. `service` contains use cases, `pricing` contains fee calculation, `ui` contains Swing screens, and `exception` contains explicit business errors. Tests are under `src\test\java`.

The `Task01` to `Task12` folders contain task-specific source maps under
each `code` folder. They link to the real Java files in the canonical
`src\main\java` and `src\test\java` directories. The Java classes are kept
in one source tree so Maven does not compile duplicate public classes.

## Test coverage

The JUnit suite covers duplicate registration, vehicle entry and exit,
monthly passes, one-day passes, zero-minute parking, EV charging fees,
delivery-van peak pricing, and daily revenue. Run `mvn test` to execute it.

## OOP evidence

- Abstract classes and inheritance: `Vehicle`, `User`
- Runtime polymorphism: vehicle-specific `preferredSlot()` and `hourlyRate()`
- Interfaces: `Chargeable`
- Encapsulation: private fields with controlled methods
- Overriding: `toString`, `equals`, `hashCode`, and subclass methods
- Collections: `ArrayList`, `HashMap`, `LinkedHashMap`
- Exception handling: custom `ParkingException` subclasses
- Constructor chaining: `super(...)` in concrete model classes

## GitHub

```powershell
git init
git add .
git commit -m "Initial working campus smart parking system"
git branch -M main
git remote add origin https://github.com/<your-username>/CSA09-RegisterNumber-Assignment1.git
git push -u origin main
```

Do not share passwords or tokens in chat. Use Git Credential Manager or the GitHub sign-in flow in VS Code.

## Assignment tasks

The implementation covers architecture, model classes, user and vehicle management, slot management, tickets and pricing, passes, EV charging, reporting services, GUI integration, validation, testing and documentation. Replace `RegisterNumber` with the actual register number before creating the remote repository.
