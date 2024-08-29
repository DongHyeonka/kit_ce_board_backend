package com.creativedesignproject.kumoh_board_backend.auth.infrastructure.extractor;

import com.creativedesignproject.kumoh_board_backend.auth.domain.entity.authentication.Authentication;

import io.jsonwebtoken.Claims;

public interface AuthenticationClaimsExtractor {
    Authentication extract(Claims claims);
}
