package com.uit_service.se360.repositories;

import com.uit_service.se360.entities.RefreshToken;
import org.springframework.stereotype.Repository;

@Repository
public interface RefreshTokenRepository extends SimpleRepository<RefreshToken, String> {
}
