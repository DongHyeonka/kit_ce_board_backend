package com.creativedesignproject.kumoh_board_backend.auth.presentation;

import java.util.Optional;

import jakarta.servlet.http.HttpServletRequest;

/**
 * http 요청에 대한 토큰을 추출하는 메서드 정의
 * HttpServletRequest 요 객체는 HTTP 요청에 대한 정보를 담고 있음
 */
public interface HttpRequestTokenExtractor {
    Optional<String> extract(HttpServletRequest request);
}
