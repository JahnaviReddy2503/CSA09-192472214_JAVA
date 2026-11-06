# Test execution record

The test suite is defined in `src/test/java/parking/ParkingServiceTest.java`.

| Test area | Automated test | Result |
|---|---|---|
| Duplicate vehicle registration | `duplicateRegistrationIsRejected` | Not executed: Maven/JDK unavailable in the current environment |
| Parking entry and exit | `vehicleCanEnterAndExit` | Not executed: Maven/JDK unavailable in the current environment |
| Monthly pass | `monthlyPassMakesFeeZero` | Not executed: Maven/JDK unavailable in the current environment |
| EV charging fee | `evIncludesChargingFee` | Not executed: Maven/JDK unavailable in the current environment |
| Delivery van peak pricing | `deliveryVanPeakRateDoubles` | Not executed: Maven/JDK unavailable in the current environment |

Run `mvn test` after installing JDK 17+ and Maven 3.9+ to populate actual execution evidence. No test is claimed as passed here without execution.
