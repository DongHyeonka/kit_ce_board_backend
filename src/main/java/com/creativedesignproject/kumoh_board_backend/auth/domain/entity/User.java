package com.creativedesignproject.kumoh_board_backend.auth.domain.entity;

import java.time.LocalDateTime;

import com.creativedesignproject.kumoh_board_backend.common.baseentity.BaseTimeEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseTimeEntity {
    
    private static final int MAX_NICKNAME_LENGTH = 30;
    private static final int MAX_PROFILE_IMAGE_LENGTH = 255;

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Size(max = MAX_NICKNAME_LENGTH)
    @Column(name = "nickname", nullable = false)
    private String nickname;

    @Column(name = "user_name", nullable = false)
    private String userId;
    
    @Column(name = "password", nullable = false)
    private String password;
    
    @Column(name = "email", nullable = false)
    private String email;
    
    @Column(name = "role", nullable = false)
    private String role;

    @Size(max = MAX_PROFILE_IMAGE_LENGTH)
    @Column(name = "profile_image", nullable = false)
    private String profileImage;

    @Column(name = "failed_attempts", nullable = false)
    private int failedAttempts = 0;

    @Column(name = "account_locked", nullable = false)
    private boolean accountLocked = false;

    @Column(name = "lock_time")
    private LocalDateTime lockTime;

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "certification_id")
    private Certification certification;

    @Builder
    public User(String nickname, String userId, String password, String email, String role, String profileImage, Certification certification) {
        this.nickname = nickname;
        this.userId = userId;
        this.password = password;
        this.email = email;
        this.role = role;
        this.profileImage = profileImage;
        this.certification = certification;
    }

    public void increaseFailedAttempts() {
        this.failedAttempts++;
    }

    public void resetFailedAttempts() {
        this.failedAttempts = 0;
    }

    public void lockAccount() {
        this.accountLocked = true;
        this.lockTime = LocalDateTime.now();
    }

    public boolean isAccountLocked() {
        return accountLocked;
    }

    public void setAccountLocked(boolean accountLocked) {
        this.accountLocked = accountLocked;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}