package com.creativedesignproject.kumoh_board_backend.auth.domain.entity;

import org.springframework.data.domain.Persistable;

import com.creativedesignproject.kumoh_board_backend.common.baseentity.BaseTimeEntity;

import jakarta.annotation.Nonnull;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefreshToken extends BaseTimeEntity implements Persistable<UUID>{
    
    private static final long EXPIRED_OFFSET_DAY = 7;
    
    @Id
    private UUID id;

    private Long userId;
    
    private LocalDateTime expiredAt;
    
    public RefreshToken(Long userId, LocalDateTime expiredAt) {
        this.id = UUID.randomUUID();
        this.userId = userId;
        this.expiredAt = expiredAt;
    }

    public static RefreshToken of(Long userId, LocalDateTime now) {
        return new RefreshToken(userId, now.plusDays(EXPIRED_OFFSET_DAY));
    }

    public boolean isExpired(LocalDateTime now) {
        return expiredAt.isBefore(now);
    }

    public boolean isOwner(Long userId) {
        return Objects.equals(this.userId, userId);
    }

    @Nonnull
    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public boolean isNew() {
        return getCreatedDate() == null;
    }
    
}
