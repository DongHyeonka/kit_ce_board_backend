package com.creativedesignproject.kumoh_board_backend.common.exception;

public class ForbiddenException extends KitCeBoardException{
    public ForbiddenException(ErrorCode errorCode) {
        super(errorCode);
    }
}
