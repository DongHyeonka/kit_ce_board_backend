package com.creativedesignproject.kumoh_board_backend.auth.infrastructure.provider;

import org.springframework.stereotype.Component;

import com.creativedesignproject.kumoh_board_backend.auth.domain.entity.Role;
import com.creativedesignproject.kumoh_board_backend.auth.domain.entity.authentication.UserAuthentication;
import com.creativedesignproject.kumoh_board_backend.auth.infrastructure.dto.response.TokenResponse;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserAuthenticationTokenProvider {
    private static final String USER_ID_KEY = "userId";
    private static final long EXPIRATION_MINUTES = 60L * 6L;

    private final JwtTokenProvider jwtTokenProvider;

    public TokenResponse provide(UserAuthentication userAuthentication) {
        return jwtTokenProvider.provide(EXPIRATION_MINUTES,
            jwtBuilder -> jwtBuilder
                .setSubject(userAuthentication.getId().toString())
                .claim(USER_ID_KEY, userAuthentication.getId())
                .setAudience(Role.USER.name())
        );
    }
}
