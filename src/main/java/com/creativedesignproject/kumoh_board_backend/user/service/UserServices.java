package com.creativedesignproject.kumoh_board_backend.user.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.creativedesignproject.kumoh_board_backend.auth.domain.entity.User;
import com.creativedesignproject.kumoh_board_backend.auth.domain.repository.UserRepository;
import com.creativedesignproject.kumoh_board_backend.common.exception.BadRequestException;
import com.creativedesignproject.kumoh_board_backend.common.exception.ErrorCode;
import com.creativedesignproject.kumoh_board_backend.user.dto.response.GetSignInUserResponseDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServices {
    private final UserRepository userRepository;
    
    public GetSignInUserResponseDto getSignInUser(String userId) {
        User user = userRepository.findByUserId(userId).orElseThrow(() -> new BadRequestException(ErrorCode.NOT_EXISTED_USER));

        return GetSignInUserResponseDto.builder()
                .userId(user.getUserId())
                .nickName(user.getNickname())
                .profileImage(user.getProfileImage())
                .build();
    }
}
