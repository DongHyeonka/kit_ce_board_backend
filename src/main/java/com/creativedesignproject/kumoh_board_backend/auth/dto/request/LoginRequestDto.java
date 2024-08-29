package com.creativedesignproject.kumoh_board_backend.auth.dto.request;

import jakarta.validation.constraints.NotBlank;

public record LoginRequestDto(
    @NotBlank(message = "아이디는 필수 입력 값입니다.")
    String userId,

    @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
    String password
) {
    
}
