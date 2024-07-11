package com.creativedesignproject.kumoh_board_backend.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserIdCheckRequestDto {
    @NotBlank(message = "아이디는 필수 입력 값입니다.")
    private String userId;
}
