package com.creativedesignproject.kumoh_board_backend.auth.domain.entity.authentication;

import com.creativedesignproject.kumoh_board_backend.auth.domain.entity.Role;

/**
 * 인증 정보를 담을 인터페이스 -> 구현체는 null을 반환하지 않도록 주의하자
 * security에 구현되어 있는 Authentication 인터페이스는 불필요한 코드까지 구현해야되기 때문에 custom한 인터페이스를 통해 구현할 예정
 */
public interface Authentication {
    Long getId();

    Role getRole();
}
