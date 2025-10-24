package com.uit_service.se360.services.user;

import com.uit_service.se360.dtos.users.DriverProfileRequest;
import com.uit_service.se360.dtos.users.DriverProfileResponse;

public interface DriverProfileService {
  DriverProfileResponse registerDriver(DriverProfileRequest request);
}
