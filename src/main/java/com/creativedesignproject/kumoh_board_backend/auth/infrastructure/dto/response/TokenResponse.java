package com.creativedesignproject.kumoh_board_backend.auth.infrastructure.dto.response;

import java.time.LocalDateTime;

/**
 * record 클래스는 dto와 같이 주로 데이터 전달 객체나 값 객체를 정의할 때 사용함
 * 필드, 생성자, 접근자 메서드, equals, hashCode, toString 등을 자동으로 생성해줌
 * 모든 필드는 final로 선언되며 객체가 생성된 이후는 변경할 수없다
 * 따라서 불변 객체를 생성하는데 유용하며, 데이터 전달 객체로 사용하기 좋음
 */
public record TokenResponse(
    String token,
    LocalDateTime expiredAt
) {
    
}
