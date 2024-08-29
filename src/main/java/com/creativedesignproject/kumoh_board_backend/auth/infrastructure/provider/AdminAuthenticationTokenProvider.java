package com.creativedesignproject.kumoh_board_backend.auth.infrastructure.provider;

import org.springframework.stereotype.Component;

import com.creativedesignproject.kumoh_board_backend.auth.domain.entity.Role;
import com.creativedesignproject.kumoh_board_backend.auth.domain.entity.authentication.AdminAuthentication;
import com.creativedesignproject.kumoh_board_backend.auth.infrastructure.dto.response.TokenResponse;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AdminAuthenticationTokenProvider {
    private static final String ADMIN_ID_KEY = "adminId";
    private static final long EXPIRATION_MINUTES = 60L * 24L;

    private final JwtTokenProvider jwtTokenProvider;

    public TokenResponse provide(AdminAuthentication adminAuthentication) {
        return jwtTokenProvider.provide(EXPIRATION_MINUTES,
            jwtBuilder -> jwtBuilder
                .setSubject(adminAuthentication.getId().toString())
                .claim(ADMIN_ID_KEY, adminAuthentication.getId())
                .setAudience(Role.ADMIN.name())
        );
    }
}
