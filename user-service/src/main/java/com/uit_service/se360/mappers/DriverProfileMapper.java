package com.uit_service.se360.mappers;

import com.uit_service.se360.dtos.users.DriverProfileRequest;
import com.uit_service.se360.dtos.users.DriverProfileResponse;
import com.uit_service.se360.entities.DriverProfile;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DriverProfileMapper {
  DriverProfile requestToEntity(DriverProfileRequest dto);

  DriverProfileResponse entityToResponse(DriverProfile dto);

  void partialUpdate(DriverProfileRequest dto,@MappingTarget DriverProfile entity);
}
