package com.creativedesignproject.kumoh_board_backend.auth.dto.response;

import java.util.UUID;
import java.time.LocalDateTime;

public record LoginResponseDto(
    Long userId,
    String nickname,
    String profileImageUrl,
    UUID refreshToken,
    LocalDateTime refreshTokenExpiredAt
) {
    
}
