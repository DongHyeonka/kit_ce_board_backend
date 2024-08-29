package com.creativedesignproject.kumoh_board_backend.common.exception;

import org.springframework.core.NestedRuntimeException;

import lombok.Getter;

@Getter
public abstract class KitCeBoardException extends NestedRuntimeException {
    private final ErrorCode errorCode;

    protected KitCeBoardException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    protected KitCeBoardException(ErrorCode errorCode, Throwable cause) {
        super(errorCode.getMessage(), cause);
        this.errorCode = errorCode;
    }

    protected KitCeBoardException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
}
