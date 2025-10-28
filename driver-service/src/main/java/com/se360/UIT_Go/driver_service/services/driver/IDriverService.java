package com.se360.UIT_Go.driver_service.services.driver;

import com.se360.UIT_Go.driver_service.constants.DriverStatus;
import com.se360.UIT_Go.driver_service.dto.DriverLocationRequest;
import lombok.NonNull;

public interface IDriverService {
    void updateLocation(String driverId, DriverLocationRequest request);

    String[] searchDriversNear(double lat, double lng, double radiusKm);

    void updateDriverStatus(String driverId, @NonNull DriverStatus driverStatus);

    void updateStatusDriverAcceptTrip(String driverId);

    void updateStatusDriverOffTrip(String driverId);
}
