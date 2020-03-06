package com.imooc.passbook.customerplatform.exception;

import com.imooc.passbook.customerplatform.constants.ErrorCode;
import com.imooc.passbook.customerplatform.vo.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;

/**
 * 全局异常处理
 * - 需要 @ControllerAdvice 注解。
 * - 不同于 merchant-platform 中的 GlobalExceptionHandler，该类返回自定义的 ErrorResponse 类型，所有信息都放在其中。
 */

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Response> handleMethodArgumentNotValidException(HttpServletRequest request,
                                                                  MethodArgumentNotValidException e) {
        log.error("[Exception Handler] Catch method argument not valid exception", e);

        ErrorCode errorCode = ErrorCode.METHOD_ARGUMENT_NOT_VALID;
        String errorMessage = e.getBindingResult().getFieldErrors().stream()  // 从 MethodArgumentNotValidException 中提取 error message
            .findFirst()
            .map(fieldError -> fieldError.getField() + " " + fieldError.getDefaultMessage())
            .orElse(errorCode.getDesc());

        Response<String> response = Response.<String>builder()
            .code(errorCode.getCode())
            .message(errorMessage)
            .requestUrl(request.getRequestURI())
            .data("No data specified")
            .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<Response> handleBusinessException(BusinessException e) {
        log.error("[Exception Handler] Business exception", e);

        Response<String> response = Response.<String>builder()
            .code(e.getCode())
            .message(e.getMessage())
            .build();

        return ResponseEntity.status(e.getHttpStatus()).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Response> handleException(HttpServletRequest request, Exception e) {
        log.error("[Exception Handler] server exception", e);

        Response<String> response = Response.<String>builder()
            .code(ErrorCode.UNKNOWN_ERROR.getCode())
            .message(ErrorCode.UNKNOWN_ERROR.getDesc())
            .requestUrl(request.getRequestURI())
            .data("No data specified")
            .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
