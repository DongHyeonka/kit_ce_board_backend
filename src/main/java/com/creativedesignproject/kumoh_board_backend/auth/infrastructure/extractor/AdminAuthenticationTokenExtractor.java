package com.creativedesignproject.kumoh_board_backend.auth.infrastructure.extractor;

import org.springframework.stereotype.Component;

import com.creativedesignproject.kumoh_board_backend.auth.domain.entity.authentication.Authentication;
import com.creativedesignproject.kumoh_board_backend.auth.domain.entity.extractor.AuthenticationTokenExtractor;
import com.creativedesignproject.kumoh_board_backend.auth.infrastructure.JwtTokenParser;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AdminAuthenticationTokenExtractor implements AuthenticationTokenExtractor {

    private final AdminAuthenticationClaimsExtractor adminAuthenticationClaimsExtractor;
    private final JwtTokenParser jwtTokenParser;

    @Override
    public Authentication extract(String token) {
        Claims claims = jwtTokenParser.getClaims(token);
        return adminAuthenticationClaimsExtractor.extract(claims);
    }
}
