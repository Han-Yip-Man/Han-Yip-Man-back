package com.supercoding.hanyipman.error.domain;

import com.supercoding.hanyipman.error.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter

public enum UserErrorCode implements ErrorCode {

    // HTTP 상태 코드 400
    INVALID_MEMBER_ID(HttpStatus.BAD_REQUEST.value(), "유효하지 않은 회원 ID입니다."),

    // HTTP 상태 코드 401
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED.value(), "잘못된 비밀번호입니다."),

    // HTTP 상태 코드 409 (아이디 중복 에러)
    DUPLICATE_MEMBER_ID(HttpStatus.CONFLICT.value(), "이미 사용 중인 회원 ID입니다."),

    // HTTP 상태 코드 404
    NON_EXISTENT_MEMBER(HttpStatus.NOT_FOUND.value(), "존재하지 않는 회원입니다."),

    // HTTP 상태 코드 400 (패스워드 확인 에러)
    INVALID_PASSWORD_CONFIRMATION(HttpStatus.BAD_REQUEST.value(), "패스워드와 패스워드 확인이 일치하지 않습니다."),

    // HTTP 상태 코드 403 (Forbidden)
    ONLY_OWN_PROFILE_EDITABLE(HttpStatus.FORBIDDEN.value(), "자신의 마이페이지만 수정할 수 있습니다."),

    // HTTP 상태 코드 409 (이메일 중복 에러)
    DUPLICATE_EMAIL(HttpStatus.CONFLICT.value(), "이미 사용 중인 이메일입니다."),

    // HTTP 상태 코드 403 (Forbidden)
    FORBIDDEN_ACCESS(HttpStatus.FORBIDDEN.value(), "해당 유저는 접근 권한이 없습니다.")

    ;
    private final int code;
    private final String message;

    private UserErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }


}
