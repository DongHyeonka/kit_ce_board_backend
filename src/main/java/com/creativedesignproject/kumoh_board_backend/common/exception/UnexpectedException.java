package com.creativedesignproject.kumoh_board_backend.common.exception;

public class UnexpectedException extends KitCeBoardException {
    public UnexpectedException(String message) {
        super(ErrorCode.INTERNAL_SERVER_ERROR, message);
    }
}
