package com.creativedesignproject.kumoh_board_backend.auth.infrastructure.extractor;

import java.util.Optional;

import org.springframework.http.HttpHeaders;

import com.creativedesignproject.kumoh_board_backend.auth.presentation.HttpRequestTokenExtractor;
import com.creativedesignproject.kumoh_board_backend.common.exception.ErrorCode;
import com.creativedesignproject.kumoh_board_backend.common.exception.UnauthorizedException;

import jakarta.servlet.http.HttpServletRequest;

public class HeaderHttpRequestTokenExtractor implements HttpRequestTokenExtractor {

    private static final String BEARER_TOKEN_PREFIX = "Bearer ";

    @Override
    public Optional<String> extract(HttpServletRequest request) {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (header == null) {
            return Optional.empty();
        }
        return Optional.of(extractToken(header));
    }

    private String extractToken(String header) {
        validateHeader(header);
        return header.substring(BEARER_TOKEN_PREFIX.length()).trim();
    }

    private void validateHeader(String header) {
        if (!header.toLowerCase().startsWith(BEARER_TOKEN_PREFIX.toLowerCase()))
            throw new UnauthorizedException(ErrorCode.NOT_BEARER_TOKEN_TYPE);
    }
}
