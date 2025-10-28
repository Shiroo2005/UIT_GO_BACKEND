package com.se360.UIT_Go.driver_service.dto;

import lombok.Data;
import lombok.NonNull;

@Data
public class SearchDriversNearRequest {
    @NonNull
    private Double lat; // Vĩ độ

    @NonNull
    private Double lng; // Kinh độ


}
