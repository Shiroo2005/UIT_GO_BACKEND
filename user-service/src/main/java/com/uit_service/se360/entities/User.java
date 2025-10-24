package com.uit_service.se360.entities;

import com.uit_service.se360.enums.UserRole;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User extends BaseEntity {
  @Column(nullable = false, unique = true)
  private String email;

  @Column(nullable = false, unique = true)
  private String phone;

  private String passwordHash;

  @Column(nullable = false)
  private UserRole role;

  @OneToOne(mappedBy = "user")
  private DriverProfile driverProfile;
}
