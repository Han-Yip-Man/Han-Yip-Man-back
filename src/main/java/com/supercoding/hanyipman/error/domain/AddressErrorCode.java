package com.supercoding.hanyipman.error.domain;

import com.supercoding.hanyipman.error.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum AddressErrorCode implements ErrorCode {

    EMPTY_ADDRESS_DATA(HttpStatus.BAD_REQUEST.value(), "주소 데이터가 비어 있습니다."),

    ADDRESS_DATA_EXCEED_LIMIT(HttpStatus.BAD_REQUEST.value(), "주소 데이터는 최대 3개를 초과할 수 없습니다."),

    DUPLICATE_ADDRESS(HttpStatus.CONFLICT.value(), "이미 등록되어 있는 주소입니다. 다른 주소를 입력해주세요."),


    ;
    private final int code;
    private final String message;

    private AddressErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }


}
