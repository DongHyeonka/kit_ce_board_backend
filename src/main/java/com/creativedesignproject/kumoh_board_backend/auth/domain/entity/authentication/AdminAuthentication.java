package com.creativedesignproject.kumoh_board_backend.auth.domain.entity.authentication;

import com.creativedesignproject.kumoh_board_backend.auth.domain.entity.Role;
import com.creativedesignproject.kumoh_board_backend.common.exception.UnexpectedException;

/**
 * 필드를 final로 선언하여 한 번 초기화되면 변경할 수 없도록 함.
 */
public class AdminAuthentication implements Authentication {

    private final Long id;

    public AdminAuthentication(Long id) {
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
        return Role.ADMIN;
    }
}
