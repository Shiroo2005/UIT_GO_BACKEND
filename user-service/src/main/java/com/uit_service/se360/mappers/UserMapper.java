package com.uit_service.se360.mappers;

import com.uit_service.se360.dtos.users.UserRequest;
import com.uit_service.se360.dtos.users.UserResponse;
import com.uit_service.se360.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(
    componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    uses = {DriverProfileMapper.class})
public interface UserMapper {
  User requestToEntity(UserRequest userRequest);

  UserResponse entityToResponse(User user);

  @Mapping(target = "passwordHash", ignore = true)
  void partialUpdate(UserRequest userRequest, @MappingTarget User user);
}
