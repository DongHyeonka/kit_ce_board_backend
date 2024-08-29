package com.creativedesignproject.kumoh_board_backend.auth.domain.entity.authentication;

import com.creativedesignproject.kumoh_board_backend.auth.domain.entity.Role;

/**
 * 어나니모스 사용자 인증은 특별한 상태를 가지지 않기 때문에 단일 인스턴스로 충분하다.
 * 전역적 접근을 위한 static final로 단일 인스턴스를 생성하여 제공
 */
public class AnonymousAuthentication implements Authentication {

    private static final AnonymousAuthentication INSTANCE = new AnonymousAuthentication();

    private AnonymousAuthentication() {}

    public static AnonymousAuthentication getInstance() {
        return INSTANCE;
    }

    @Override
    public Long getId() {
        return null;
    }

    @Override
    public Role getRole() {
        return Role.ANONYMOUS;
    }
}
