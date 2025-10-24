package com.uit_service.se360.controllers;

import com.uit_service.se360.dtos.users.DriverProfileRequest;
import com.uit_service.se360.dtos.users.DriverProfileResponse;
import com.uit_service.se360.dtos.users.UserRequest;
import com.uit_service.se360.dtos.users.UserResponse;
import com.uit_service.se360.entities.User;
import com.uit_service.se360.services.user.DriverProfileService;
import com.uit_service.se360.services.user.UserService;
import io.github.perplexhub.rsql.RSQLJPASupport;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "User")
@RequestMapping("/users")
@RestController
@RequiredArgsConstructor
public class UserController {
  private final UserService userService;
  private final DriverProfileService driverProfileService;

  @GetMapping
  public ResponseEntity<Page<UserResponse>> getAll(
      @ParameterObject Pageable pageable,
      @RequestParam(required = false, value = "filter") String filter,
      @RequestParam(required = false, value = "all") boolean all) {
    if (all) {
      pageable = Pageable.unpaged();
    }
    Specification<User> specification = RSQLJPASupport.toSpecification(filter);
    return ResponseEntity.ok(userService.getAll(pageable, specification));
  }

  @GetMapping({"/{id}"})
  public ResponseEntity<UserResponse> getById(@PathVariable("id") String id) {
    return ResponseEntity.ok(userService.getById(id));
  }

  @PostMapping
  public ResponseEntity<UserResponse> create(@Valid @RequestBody UserRequest request) {
    return ResponseEntity.ok(userService.create(request));
  }

  @PutMapping("/{id}")
  public ResponseEntity<UserResponse> update(
      @PathVariable("id") String id, @Valid @RequestBody UserRequest request) {
    return ResponseEntity.ok(userService.update(id, request));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable("id") String id) {
    userService.delete(id);
    return ResponseEntity.ok().build();
  }

  @DeleteMapping
  public ResponseEntity<Void> deleteAll(@RequestParam("ids") List<String> ids) {
    userService.deleteAll(ids);
    return ResponseEntity.ok().build();
  }

  @GetMapping({"/me"})
  public ResponseEntity<UserResponse> getCurrentUser() {
    return ResponseEntity.ok(userService.getCurrentUserInfo());
  }

  @PostMapping("/me/driver-profile")
  public ResponseEntity<DriverProfileResponse> registerDriver(
      @Valid @RequestBody DriverProfileRequest driverProfileRequest) {
    return ResponseEntity.ok(driverProfileService.registerDriver(driverProfileRequest));
  }
}
