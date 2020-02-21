package com.imooc.passbook.customerplatform.vo;

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

    public static final Integer ERROR = -1;

    private Integer code;

    private String message;

    private String requestUrl;  // 在 error 对象中记录请求 url 是很有必要的，便于 debug

    private T data;
}
