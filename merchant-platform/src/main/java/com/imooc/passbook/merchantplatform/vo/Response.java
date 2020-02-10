package com.imooc.passbook.merchantplatform.vo;

import com.imooc.passbook.merchantplatform.constants.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.MethodArgumentNotValidException;

/**
 * HTTP 成功响应
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Response {

    private Object data;

    private Integer code = 0;

    private String message = "";

    public void setError(ErrorCode errorCode) {
        this.setCode(errorCode.getCode());
        this.setMessage(errorCode.getDesc());
    }

    public static Response from(ErrorCode errorCode) {
        Response response = new Response();
        response.setError(errorCode);
        return response;
    }
}
