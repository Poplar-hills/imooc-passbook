package com.imooc.passbook.customerplatform.exception;

import com.imooc.passbook.customerplatform.constants.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class BusinessException extends RuntimeException {

    private Integer code;
    private HttpStatus httpStatus;
    private String message;

    public BusinessException(Integer code, String message) {
        super(message);
        this.code = code;
        this.httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        this.message = message;
    }

    public BusinessException(Integer code, HttpStatus httpStatus, String message) {
        super(message);
        this.code = code;
        this.httpStatus = httpStatus;
        this.message = message;
    }

    public BusinessException(ErrorCode errorCode) {
        this(errorCode.getCode(), HttpStatus.BAD_REQUEST, errorCode.getDesc());
    }
}
