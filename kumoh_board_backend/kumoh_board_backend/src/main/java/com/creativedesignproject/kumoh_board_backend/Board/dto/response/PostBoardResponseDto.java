package com.creativedesignproject.kumoh_board_backend.Board.dto.response;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.creativedesignproject.kumoh_board_backend.Auth.dto.response.ResponseDto;
import com.creativedesignproject.kumoh_board_backend.Common.ResponseCode;
import com.creativedesignproject.kumoh_board_backend.Common.ResponseMessage;

import lombok.Getter;

@Getter
public class PostBoardResponseDto extends ResponseDto{
    private PostBoardResponseDto() {
        super(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
    }

    public static ResponseEntity<PostBoardResponseDto> success() {
        return ResponseEntity.status(HttpStatus.OK).body(new PostBoardResponseDto());
    }

    public static ResponseEntity<ResponseDto> notExistUser() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseDto(ResponseCode.NOT_EXISTED_USER, ResponseMessage.NOT_EXISTED_USER));
    }
    
    public static ResponseEntity<ResponseDto> notExistCategory() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseDto(ResponseCode.NOT_EXISTED_CATEGORY, ResponseMessage.NOT_EXISTED_CATEGORY));
    }
}
