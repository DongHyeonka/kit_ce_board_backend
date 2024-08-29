package com.creativedesignproject.kumoh_board_backend.auth.domain.repository;


import org.springframework.data.repository.Repository;

import com.creativedesignproject.kumoh_board_backend.auth.domain.entity.RefreshToken;

import java.util.Optional;
import java.util.UUID;

public interface RefreshTokenRepository extends Repository<RefreshToken, UUID> {
    RefreshToken save(RefreshToken refreshToken);

    Optional<RefreshToken> findById(UUID id);

    void deleteById(UUID id);
}
