package com.imooc.passbook.customerplatform.vo;

import com.imooc.passbook.customerplatform.constants.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 统一的 Response 对象
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Response<T> {

    private Integer code = ErrorCode.SUCCESS.getCode();

    private String message = ErrorCode.SUCCESS.getDesc();

    private String requestUrl;  // 在 error 对象中记录请求 url 是很有必要的，便于 debug

    private T data;

    public Response(T data) {
        this.data = data;
    }

    public static <T> Response<T> success() {  // 成功响应的便捷构造函数
        return new Response<>();
    }

    public static <T> Response<T> failure(ErrorCode errorCode) {  // 相当于 merchant-platform Response 类中的 from 方法
        return Response.<T>builder()
            .code(errorCode.getCode())
            .message(errorCode.getDesc())
            .requestUrl("")
            .data(null)
            .build();
    }
}
