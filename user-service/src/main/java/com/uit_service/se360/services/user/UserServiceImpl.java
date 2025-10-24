package com.uit_service.se360.services.user;

import com.github.f4b6a3.uuid.UuidCreator;
import com.uit_service.se360.dtos.users.UserRequest;
import com.uit_service.se360.dtos.users.UserResponse;
import com.uit_service.se360.entities.User;
import com.uit_service.se360.enums.UserRole;
import com.uit_service.se360.mappers.UserMapper;
import com.uit_service.se360.repositories.UserRepository;
import com.uit_service.se360.securities.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final UserMapper userMapper;

  @Override
  public Page<UserResponse> getAll(Pageable pageable, Specification<User> spec) {
    var userPage = userRepository.findAll(spec, pageable);
    return userPage.map(userMapper::entityToResponse);
  }

  @Override
  public UserResponse getById(String id) {
    var user =
        userRepository
            .findById(id)
            .orElseThrow(() -> new IllegalArgumentException("User not found"));
    return userMapper.entityToResponse(user);
  }

  @Override
  public UserResponse create(UserRequest userResponse) {
    // 1. ---- Validate ----
    if (userRepository.exists(
        (root, _, builder) -> builder.equal(root.get("email"), userResponse.getEmail()))) {
      throw new IllegalArgumentException("Email already exists");
    }

    if (userRepository.exists(
        (root, _, builder) -> builder.equal(root.get("phone"), userResponse.getPhone()))) {
      throw new IllegalArgumentException("Phone already exists");
    }

    var user = userMapper.requestToEntity(userResponse);
    user.setId(UuidCreator.getTimeOrderedEpoch().toString());
    user.setPasswordHash(passwordEncoder.encode(userResponse.getPassword()));
    user.setRole(UserRole.PASSENGER);
    var savedUser = userRepository.save(user);
    return userMapper.entityToResponse(savedUser);
  }

  @Override
  public UserResponse update(String id, UserRequest userRequest) {
    var existingUser =
        userRepository
            .findById(id)
            .orElseThrow(() -> new IllegalArgumentException("User not found"));

    // 1. ---- Validate ----
    if (userRepository.exists(
        (root, _, builder) ->
            builder.and(
                builder.equal(root.get("email"), userRequest.getEmail()),
                builder.notEqual(root.get("id"), id)))) {
      throw new IllegalArgumentException("Email already exists");
    }

    if (userRepository.exists(
        (root, _, builder) ->
            builder.and(
                builder.equal(root.get("phone"), userRequest.getPhone()),
                builder.notEqual(root.get("id"), id)))) {
      throw new IllegalArgumentException("Phone already exists");
    }
    userMapper.partialUpdate(userRequest, existingUser);
    var updatedUser = userRepository.save(existingUser);
    return userMapper.entityToResponse(updatedUser);
  }

  @Override
  public void delete(String id) {
    userRepository.deleteById(id);
  }

  @Override
  public void deleteAll(Iterable<String> ids) {
    userRepository.deleteAllById(ids);
  }

  @Override
  public UserResponse getCurrentUserInfo() {
    String currentUserId = SecurityUtil.getCurrentUserId();
    if (currentUserId == null) {
      throw new IllegalStateException("No authenticated user found");
    }
    return getById(currentUserId);
  }
}
