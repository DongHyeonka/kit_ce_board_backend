package com.creativedesignproject.kumoh_board_backend.auth.domain.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.creativedesignproject.kumoh_board_backend.auth.domain.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    
    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);

    User findByNickname(String nickName);

    boolean existsByUserId(String userId);
    
    Optional<User> findByUserId(String userId);
}