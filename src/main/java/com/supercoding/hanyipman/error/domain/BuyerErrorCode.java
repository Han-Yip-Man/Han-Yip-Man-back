package com.supercoding.hanyipman.error.domain;

import com.supercoding.hanyipman.error.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum BuyerErrorCode implements ErrorCode {

    // HTTP 상태 코드 401
    INVALID_BUYER(HttpStatus.UNAUTHORIZED.value(), "유저로 등록되지 않았거나 결과가 없습니다."),

    // HTTP 상태 코드 401
    INVALID_SELLER(HttpStatus.UNAUTHORIZED.value(), "점주로 등록되어 결과가 없습니다."),


    NOT_BUYER(HttpStatus.FORBIDDEN.value(), "구매자 회원이 아닙니다."),

    NOT_FOUNT_ADDRESS(HttpStatus.NOT_FOUND.value(), "구매자에게 등록된 주소가 없습니다"),
    NOT_SAME_BUYER(HttpStatus.BAD_REQUEST.value(), "검색한 구매자와 요청하신 구매자가 다릅니다.");


    private final int code;
    private final String message;

    private BuyerErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }


}
