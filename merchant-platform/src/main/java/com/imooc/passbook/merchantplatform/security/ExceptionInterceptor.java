package com.imooc.passbook.merchantplatform.security;

import com.imooc.passbook.merchantplatform.constants.ErrorCode;
import com.imooc.passbook.merchantplatform.vo.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@ControllerAdvice
public class ExceptionInterceptor extends ResponseEntityExceptionHandler {

    // 拦截 @Valid 抛出的错误
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Response> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error("[Exception Handler] Catch method argument not valid exception", e);
        ErrorCode errorCode = ErrorCode.METHOD_ARGUMENT_NOT_VALID;
        return new ResponseEntity<>(Response.from(errorCode), HttpStatus.BAD_REQUEST);
    }

    // 拦截剩下的所有异常
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleException(Exception e) {
        log.error("[Exception Handler] server exception", e);
        ErrorCode errorCode = ErrorCode.UNKNOWN_ERROR;
        return new ResponseEntity<>(Response.from(errorCode), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
