package com.creativedesignproject.kumoh_board_backend.auth.infrastructure.extractor;

import org.springframework.stereotype.Component;

import com.creativedesignproject.kumoh_board_backend.auth.domain.entity.authentication.Authentication;
import com.creativedesignproject.kumoh_board_backend.auth.domain.entity.extractor.AuthenticationTokenExtractor;
import com.creativedesignproject.kumoh_board_backend.auth.infrastructure.JwtTokenParser;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserAuthenticationTokenExtractor implements AuthenticationTokenExtractor {

    private final JwtTokenParser jwtTokenParser;
    private final UserAuthenticationClaimsExtractor userAuthenticationClaimsExtractor;

    @Override
    public Authentication extract(String token) {
        Claims claims = jwtTokenParser.getClaims(token);
        return userAuthenticationClaimsExtractor.extract(claims);
    }
}
