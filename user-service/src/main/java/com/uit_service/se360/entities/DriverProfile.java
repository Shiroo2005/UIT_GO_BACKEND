package com.uit_service.se360.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "driver_profiles")
public class DriverProfile extends BaseEntity {
  @Column(nullable = false, unique = true)
  private String licensePlate;

  @Column(nullable = false, unique = true)
  private String vehicleModel;

  @Column(nullable = false, unique = true)
  private String vehicleColor;

  @OneToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private User user;
}
