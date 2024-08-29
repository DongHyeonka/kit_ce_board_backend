package com.creativedesignproject.kumoh_board_backend.auth.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Retention(RetentionPolicy.RUNTIME) : 여기서 이렇게 런타임 시점까지 유지를 하는 이유는 리플렉션을 통해 접근이 가능
 * 여기서 리플렉션이란 : 자바에서 런타임에 클래스, 메서드, 필드 등을 동적으로 분석하고 조작할 수 있는 기능을 말하는데 
 * 컴파일 시점에서 알 수 없는 클래스의 정보를 런타임에 얻어내고 그 클래스의 메서드를 호출하거나 필드에 접근이 가능하다.
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface User {
    
}
