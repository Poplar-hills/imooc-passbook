package com.imooc.passbook.merchants.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * HTTP 响应
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Response {

    // 错误码，正确返回0（默认返回0）
    private Integer errorCode = 0;

    // 错误信息
    private String errorMsg = "";

    // 返回 VO
    private Object data;

    // 正确的响应的构造函数（返回 VO）
    public Response(Object data) {
        this.data = data;
    }
}
