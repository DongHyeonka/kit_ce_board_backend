package com.creativedesignproject.kumoh_board_backend.auth.domain.entity.extractor;

import com.creativedesignproject.kumoh_board_backend.auth.domain.entity.authentication.Authentication;

/**
 * 문자열로 토큰을 받아서 Authentication을 반환하는 인터페이스 이며 null 반환 대신 AnonymousAuthentication.getInstance()를 반환하자.
 */
public interface AuthenticationTokenExtractor {
    Authentication extract(String token);
}
