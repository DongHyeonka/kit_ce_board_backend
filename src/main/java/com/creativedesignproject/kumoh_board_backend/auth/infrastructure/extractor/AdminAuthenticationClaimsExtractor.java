package com.creativedesignproject.kumoh_board_backend.auth.infrastructure.extractor;

import org.springframework.stereotype.Component;

import com.creativedesignproject.kumoh_board_backend.auth.domain.entity.Role;
import com.creativedesignproject.kumoh_board_backend.auth.domain.entity.authentication.AdminAuthentication;
import com.creativedesignproject.kumoh_board_backend.auth.domain.entity.authentication.AnonymousAuthentication;
import com.creativedesignproject.kumoh_board_backend.auth.domain.entity.authentication.Authentication;

import io.jsonwebtoken.Claims;

@Component
public class AdminAuthenticationClaimsExtractor implements AuthenticationClaimsExtractor {

    private static final String ADMIN_ID_KEY = "adminId";

    @Override
    public Authentication extract(Claims claims) {
        if (!claims.getAudience().contains(Role.ADMIN.name())) {
            return AnonymousAuthentication.getInstance();
        }
        Long adminId = claims.get(ADMIN_ID_KEY, Long.class);
        return new AdminAuthentication(adminId);
    }
}
