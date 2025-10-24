package com.uit_service.se360.services.user;

import com.github.f4b6a3.uuid.UuidCreator;
import com.uit_service.se360.dtos.users.DriverProfileRequest;
import com.uit_service.se360.dtos.users.DriverProfileResponse;
import com.uit_service.se360.enums.UserRole;
import com.uit_service.se360.mappers.DriverProfileMapper;
import com.uit_service.se360.repositories.DriverProfileRepository;
import com.uit_service.se360.repositories.UserRepository;
import com.uit_service.se360.securities.SecurityUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class DriverProfileServiceImpl implements DriverProfileService {
  private final DriverProfileRepository driverProfileRepository;
  private final DriverProfileMapper driverProfileMapper;
  private final UserRepository userRepository;

  @Override
  @Transactional
  public DriverProfileResponse registerDriver(DriverProfileRequest request) {
    String currentUserID = SecurityUtil.getCurrentUserId();
    if (currentUserID == null) {
      throw new RuntimeException("User not authenticated");
    }
    var user =
        userRepository
            .findById(currentUserID)
            .orElseThrow(() -> new RuntimeException("User not found"));
    user.setRole(UserRole.DRIVER);
    user = userRepository.save(user);
    var driverProfile = driverProfileMapper.requestToEntity(request);
    driverProfile.setId(UuidCreator.getTimeOrderedEpoch().toString());
    driverProfile.setUser(user);
    var savedProfile = driverProfileRepository.save(driverProfile);
    return driverProfileMapper.entityToResponse(savedProfile);
  }
}
