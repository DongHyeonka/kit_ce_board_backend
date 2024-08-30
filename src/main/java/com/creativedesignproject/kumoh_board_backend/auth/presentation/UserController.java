package com.creativedesignproject.kumoh_board_backend.auth.presentation;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.creativedesignproject.kumoh_board_backend.auth.dto.request.ChangeNicknameRequestDto;
import com.creativedesignproject.kumoh_board_backend.auth.dto.request.ChangePasswordRequestDto;
import com.creativedesignproject.kumoh_board_backend.auth.dto.request.EmailCertificationRequestDto;
import com.creativedesignproject.kumoh_board_backend.auth.dto.request.LoginRequestDto;
import com.creativedesignproject.kumoh_board_backend.auth.dto.request.SignUpRequestDto;
import com.creativedesignproject.kumoh_board_backend.auth.dto.request.UserIdCheckRequestDto;
import com.creativedesignproject.kumoh_board_backend.auth.dto.response.LoginResponseDto;
import com.creativedesignproject.kumoh_board_backend.auth.presentation.command.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class UserController {
    
    private final UserService authService;

    @PostMapping("/emailCertification")
    public ResponseEntity<Void> emailCertification(@RequestBody @Valid EmailCertificationRequestDto requestBody) {
        authService.emailCertification(requestBody);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/checkUserId")
    public ResponseEntity<Void> checkUserId(
            @RequestBody @Valid UserIdCheckRequestDto requestBody) {
        authService.checkUserId(requestBody);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/signUp")
    public ResponseEntity<Void> signUp(@RequestBody @Valid SignUpRequestDto requestBody) {
        authService.signUp(requestBody);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/signIn")
    public ResponseEntity<LoginResponseDto> signIn(@RequestBody @Valid LoginRequestDto requestBody) {
        return ResponseEntity.status(HttpStatus.OK).body(authService.signIn(requestBody));
    }

    @PatchMapping("/changePassword")
    public ResponseEntity<Void> changePassword(String userId,
                                                              @RequestBody @Valid ChangePasswordRequestDto dto) {
        authService.changePassword(userId, dto);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/changeNickname")
    public ResponseEntity<Void> changeNickname(String userId,
                                                              @RequestBody @Valid ChangeNicknameRequestDto dto) {
        authService.changeNickname(userId, dto);
        return ResponseEntity.ok().build();
    }
}