package com.creativedesignproject.kumoh_board_backend.auth.infrastructure.extractor;

import org.springframework.stereotype.Component;

import com.creativedesignproject.kumoh_board_backend.auth.domain.entity.Role;
import com.creativedesignproject.kumoh_board_backend.auth.domain.entity.authentication.AnonymousAuthentication;
import com.creativedesignproject.kumoh_board_backend.auth.domain.entity.authentication.Authentication;
import com.creativedesignproject.kumoh_board_backend.auth.domain.entity.authentication.UserAuthentication;

import io.jsonwebtoken.Claims;

@Component
public class UserAuthenticationClaimsExtractor implements AuthenticationClaimsExtractor{

    private static final String USER_ID_KEY = "userId";

    @Override
    public Authentication extract(Claims claims) {
        if (!claims.getAudience().contains(Role.USER.name())) {
            return AnonymousAuthentication.getInstance();
        }
        Long userId = claims.get(USER_ID_KEY, Long.class);
        return new UserAuthentication(userId);
    }
}
