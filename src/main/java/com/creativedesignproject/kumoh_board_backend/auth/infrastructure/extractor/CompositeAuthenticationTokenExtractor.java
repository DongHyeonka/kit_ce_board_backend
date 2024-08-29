package com.creativedesignproject.kumoh_board_backend.auth.infrastructure.extractor;

import org.springframework.stereotype.Component;

import com.creativedesignproject.kumoh_board_backend.auth.domain.entity.Role;
import com.creativedesignproject.kumoh_board_backend.auth.domain.entity.authentication.AnonymousAuthentication;
import com.creativedesignproject.kumoh_board_backend.auth.domain.entity.authentication.Authentication;
import com.creativedesignproject.kumoh_board_backend.auth.domain.entity.extractor.AuthenticationTokenExtractor;
import com.creativedesignproject.kumoh_board_backend.auth.infrastructure.JwtTokenParser;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CompositeAuthenticationTokenExtractor implements AuthenticationTokenExtractor{

    private final JwtTokenParser jwtTokenParser;
    private final List<AuthenticationClaimsExtractor> authenticationClaimsExtractors;

    @Override
    public Authentication extract(String token) {
        Claims claims = jwtTokenParser.getClaims(token);
        for (AuthenticationClaimsExtractor claimsExtractor : authenticationClaimsExtractors) {
            Authentication authentication = claimsExtractor.extract(claims);
            if (authentication.getRole() != Role.ANONYMOUS) {
                return authentication;
            }
        }
        return AnonymousAuthentication.getInstance();
    }
}
