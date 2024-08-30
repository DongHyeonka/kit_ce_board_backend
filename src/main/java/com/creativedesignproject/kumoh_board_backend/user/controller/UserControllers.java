package com.creativedesignproject.kumoh_board_backend.user.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.creativedesignproject.kumoh_board_backend.user.dto.response.GetSignInUserResponseDto;
import com.creativedesignproject.kumoh_board_backend.user.service.UserServices;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserControllers {
    private final UserServices userService;
    
    @GetMapping("/info")
    public ResponseEntity<GetSignInUserResponseDto> getSignInUser(@AuthenticationPrincipal String userId) {
        GetSignInUserResponseDto responseDto = userService.getSignInUser(userId);
        return ResponseEntity.ok().body(responseDto);
    }
}
