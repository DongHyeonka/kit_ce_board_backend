package com.creativedesignproject.kumoh_board_backend.common.exception;

public class UnauthorizedException extends KitCeBoardException {
    
    public UnauthorizedException(ErrorCode errorCode) {
        super(errorCode);
    }
}
