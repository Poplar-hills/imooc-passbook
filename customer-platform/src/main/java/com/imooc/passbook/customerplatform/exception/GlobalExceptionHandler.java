package com.imooc.passbook.customerplatform.exception;

import com.imooc.passbook.customerplatform.constants.ErrorCode;
import com.imooc.passbook.customerplatform.vo.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * 全局异常处理
 * - 需要 @ControllerAdvice 注解。
 * - 不同于 merchant-platform 中的 GlobalExceptionHandler，该类返回自定义的 ErrorResponse 类型，所有信息都放在其中。
 */

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ResponseBody  // TODO: test if this is necessary
    @ExceptionHandler(Exception.class)
    public ErrorResponse<String> handleException(HttpServletRequest request, Exception e) {
        log.error("[Exception Handler] server exception", e);
        ErrorCode errorCode = ErrorCode.UNKNOWN_ERROR;
        return ErrorResponse.<String>builder()  // 注意 ∵ ErrorInfo 是泛型 ∴ 使用这里使用 builder 时要指定类型
            .code(errorCode.getCode())
            .message(errorCode.getDesc())
            .requestUrl(request.getRequestURI())
            .data("No data specified")
            .build();
    }
}
