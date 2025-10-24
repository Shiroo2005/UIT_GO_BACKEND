package com.uit_service.se360.services.user;

import com.uit_service.se360.dtos.users.UserRequest;
import com.uit_service.se360.dtos.users.UserResponse;
import com.uit_service.se360.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

public interface UserService {
  Page<UserResponse> getAll(Pageable pageable, Specification<User> spec);

  UserResponse getById(String id);

  UserResponse create(UserRequest userRequest);

  UserResponse update(String id, UserRequest userResponse);

  UserResponse getCurrentUserInfo();

  void delete(String id);

  void deleteAll(Iterable<String> ids);
}
