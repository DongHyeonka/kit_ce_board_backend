package com.creativedesignproject.kumoh_board_backend.common.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public enum ErrorCode {
    //400
    MISSING_NICKNAME(HttpStatus.BAD_REQUEST, "MISSING_NICKNAME", "닉네임을 입력해주세요."),
    EMAIL_DUPLICATED(HttpStatus.BAD_REQUEST, "EMAIL_DUPLICATED", "이메일이 이미 존재합니다."),
    EMAIL_SEND_FAIL(HttpStatus.BAD_REQUEST, "EMAIL_SEND_FAIL", "이메일 전송에 실패했습니다."),
    USER_ID_DUPLICATED(HttpStatus.BAD_REQUEST, "USER_ID_DUPLICATED", "아이디가 이미 존재합니다."),
    NICKNAME_DUPLICATED(HttpStatus.BAD_REQUEST, "NICKNAME_DUPLICATED", "닉네임이 이미 존재합니다."),
    CERTIFICATION_FAIL(HttpStatus.BAD_REQUEST, "CERTIFICATION_FAIL", "인증번호를 재전송 해주세요."),
    CERTIFICATION_MISSMATCHING(HttpStatus.BAD_REQUEST, "CERTIFICATION_MISSMATCHING", "인증번호가 일치하지 않습니다."),
    VALIDATION_FAIL(HttpStatus.BAD_REQUEST, "VALIDATION_FAIL", "검증이 실패하였습니다."),
    USER_NOT_FOUND(HttpStatus.BAD_REQUEST, "USER_NOT_FOUND", "사용자를 찾을 수 없습니다."),
    NOT_EXISTED_USER(HttpStatus.BAD_REQUEST, "NOT_EXISTED_USER", "존재하지 않는 사용자입니다."),
    PASSWORD_NOT_MATCHED(HttpStatus.BAD_REQUEST, "PASSWORD_NOT_MATCHED", "비밀번호가 일치하지 않습니다."),
    NOT_EXISTED_CATEGORY(HttpStatus.BAD_REQUEST, "NOT_EXISTED_CATEGORY", "존재하지 않는 카테고리입니다."),
    NOT_EXISTED_POST(HttpStatus.BAD_REQUEST, "NOT_EXISTED_POST", "존재하지 않는 게시글입니다."),
    NOT_EXISTED_COMMENT(HttpStatus.BAD_REQUEST, "NOT_EXISTED_COMMENT", "존재하지 않는 댓글입니다."),
    DUPLICATED_CATEGORY_NAME(HttpStatus.BAD_REQUEST, "DUPLICATED_CATEGORY_NAME", "중복된 카테고리 이름입니다."),
    WEAK_PASSWORD(HttpStatus.BAD_REQUEST, "WEAK_PASSWORD", "비밀번호가 취약합니다."),
    PASSWORD_REUSED(HttpStatus.BAD_REQUEST, "PASSWORD_REUSED", "이전 비밀번호와 동일합니다."),
    ACCOUNT_LOCKED(HttpStatus.BAD_REQUEST, "ACCOUNT_LOCKED", "계정이 잠겼습니다."),
    INVALID_OR_EXPIRED_TOKEN(HttpStatus.BAD_REQUEST, "INVALID_OR_EXPIRED_TOKEN", "유효하지 않거나 만료된 토큰입니다."),

    //403
    NO_PERMISSION(HttpStatus.UNAUTHORIZED, "NO_PERMISSION", "권한이 없습니다."),
    
    //401
    EXPIRED_AUTH_TOKEN(HttpStatus.UNAUTHORIZED, "EXPIRED_AUTH_TOKEN", "만료된 토큰입니다."),
    INVALID_AUTH_TOKEN(HttpStatus.UNAUTHORIZED, "INVALID_AUTH_TOKEN", "유효하지 않은 토큰입니다."),
    NEED_AUTH_TOKEN(HttpStatus.UNAUTHORIZED, "NEED_AUTH_TOKEN", "인증 토큰이 필요합니다."),
    NOT_BEARER_TOKEN_TYPE(HttpStatus.UNAUTHORIZED, "NOT_BEARER_TOKEN_TYPE", "토큰 타입이 Bearer 타입이 아닙니다."),
    
    //500
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR", "서버 에러가 발생했습니다."),
    ;
    
    private HttpStatus status;
    private final String code;
    private final String message;

    ErrorCode(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
