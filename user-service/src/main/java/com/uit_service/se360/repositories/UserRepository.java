package com.uit_service.se360.repositories;

import com.uit_service.se360.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends SimpleRepository<User, String> {

  @Override
  @EntityGraph(attributePaths = {"driverProfile"})
  Page<User> findAll(Specification<User> spec, Pageable pageable);
}
