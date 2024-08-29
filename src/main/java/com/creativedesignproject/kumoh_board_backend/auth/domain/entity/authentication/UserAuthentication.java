package com.creativedesignproject.kumoh_board_backend.auth.domain.entity.authentication;

import com.creativedesignproject.kumoh_board_backend.auth.domain.entity.Role;
import com.creativedesignproject.kumoh_board_backend.common.exception.UnexpectedException;

public class UserAuthentication implements Authentication {
    private final Long id;

    public UserAuthentication(Long id) {
        if (id == null) {
            throw new UnexpectedException("id는 null일 수 없습니다.");
        }
        this.id = id;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public Role getRole() {
        return Role.USER;
    }
}
