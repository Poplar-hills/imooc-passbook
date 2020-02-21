package com.imooc.passbook.merchantplatform.exception;

import com.imooc.passbook.merchantplatform.constants.ErrorCode;
import com.imooc.passbook.merchantplatform.vo.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * 全局异常处理
 * - 需要 @ControllerAdvice 注解。
 * - 不同于 customer-platform 中的 GlobalExceptionHandler，该类通过 ErrorCode 构造 Response 对象，然后放在 ResponseEntity 中返回。
 */

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {  // 这里不能继承 ResponseEntityExceptionHandler，否则报错

  // 拦截 @Valid 抛出的错误
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Response> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
    log.error("[Exception Handler] Catch method argument not valid exception", e);

    ErrorCode errorCode = ErrorCode.METHOD_ARGUMENT_NOT_VALID;
    String errorMessage = e.getBindingResult().getFieldErrors().stream()  // 从 MethodArgumentNotValidException 中提取 error message
        .findFirst()
        .map(fieldError -> fieldError.getField() + " " + fieldError.getDefaultMessage())
        .orElse(errorCode.getDesc());
    errorCode.setMessage(errorMessage);

    return new ResponseEntity<>(Response.from(errorCode), HttpStatus.BAD_REQUEST);
  }

  // 拦截所有其他异常
  @ExceptionHandler(Exception.class)
  public ResponseEntity<Object> handleException(Exception e) {
    log.error("[Exception Handler] server exception", e);
    ErrorCode errorCode = ErrorCode.UNKNOWN_ERROR;
    return new ResponseEntity<>(Response.from(errorCode), HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
