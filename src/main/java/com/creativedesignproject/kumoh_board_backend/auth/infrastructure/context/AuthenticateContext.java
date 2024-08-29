package com.creativedesignproject.kumoh_board_backend.auth.infrastructure.context;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import com.creativedesignproject.kumoh_board_backend.auth.domain.entity.Role;
import com.creativedesignproject.kumoh_board_backend.auth.domain.entity.authentication.AnonymousAuthentication;
import com.creativedesignproject.kumoh_board_backend.auth.domain.entity.authentication.Authentication;

/**
 * RequestScope : http 요청이 들어올 때마다 새로운 빈 인스턴스를 생성하고 관리하는 역할을 한다. 즉 요청마다 새로운 빈 인스턴스를 생성하고 완료된 인스턴스는 소멸 됨
 * 1. 요청마다 새로운 빈 인스턴스가 생성됨
 * 2. 요청 완료 시 소멸 : 요청이 완료되면 빈 인스턴스는 소멸된다.
 * 3. 상태 유지 : 요청 범위 내에서 상태를 유지할 수 있음
 */
@Component
@RequestScope
public class AuthenticateContext {
    private Authentication authentication = AnonymousAuthentication.getInstance();

    public void setAuthentication(Authentication authentication) {
        this.authentication = authentication;
    }

    public Long getId() {
        return authentication.getId();
    }

    public Role getRole() {
        return authentication.getRole();
    }

    public Authentication getAuthentication() {
        return authentication;
    }
}
