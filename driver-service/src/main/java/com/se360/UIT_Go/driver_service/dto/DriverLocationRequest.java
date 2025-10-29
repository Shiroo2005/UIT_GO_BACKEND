package com.se360.UIT_Go.driver_service.dto;

import com.se360.UIT_Go.driver_service.constants.DriverStatus;
import lombok.Data;
import lombok.NonNull;

@Data
public class DriverLocationRequest {
    @NonNull
    private Double lat; // Vĩ độ


    @NonNull
    private Double lng; // Kinh độ

    @NonNull
    private DriverStatus status;
}
