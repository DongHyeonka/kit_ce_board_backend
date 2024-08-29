package com.creativedesignproject.kumoh_board_backend.auth.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 런타임 시점까지 유지됨 리플렉션을 통해서 접근하려고 하는거임 왜냐면 컴파일 시점에서는 클래스, 메서드, 필드 정보를 알 수 없음
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface Anonymous {
    
}
