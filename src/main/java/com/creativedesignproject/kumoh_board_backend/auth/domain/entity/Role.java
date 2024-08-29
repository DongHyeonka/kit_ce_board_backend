package com.creativedesignproject.kumoh_board_backend.auth.domain.entity;

import com.creativedesignproject.kumoh_board_backend.auth.annotation.Admin;
import com.creativedesignproject.kumoh_board_backend.auth.annotation.Anonymous;
import com.creativedesignproject.kumoh_board_backend.auth.annotation.User;
import com.creativedesignproject.kumoh_board_backend.common.exception.UnexpectedException;

import java.lang.annotation.Annotation;

public enum Role {
    ANONYMOUS(Anonymous.class), //익명 사용자
    USER(User.class), // 일반 사용자
    ADMIN(Admin.class), // 관리자
    ;

    private final Class<? extends Annotation> annotation; // 주석 클래스라고 함 이는 일반적인 인터페이스와 달리
    // 어노테이션을 통해서 메타데이터를 추가할 수 있게 해줌

    Role(Class<? extends Annotation> annotation) {
        this.annotation = annotation;
    }

    public static Role from(String role) {
        try {
            return valueOf(role);
        } catch (NullPointerException | IllegalArgumentException e) {
            throw new UnexpectedException("해당하는 Role이 없습니다.");
        }
    }

    public Class<? extends Annotation> getAnnotation() {
        return annotation;
    }
}
