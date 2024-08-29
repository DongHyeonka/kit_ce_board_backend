package com.creativedesignproject.kumoh_board_backend.auth.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.creativedesignproject.kumoh_board_backend.auth.domain.entity.PasswordResetToken;
import com.creativedesignproject.kumoh_board_backend.auth.domain.entity.User;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    PasswordResetToken findByToken(String token);
    void deleteByUser(User user);
}
