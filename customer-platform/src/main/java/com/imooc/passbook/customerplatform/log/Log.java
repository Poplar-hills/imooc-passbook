package com.imooc.passbook.customerplatform.log;

import com.alibaba.fastjson.JSON;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;

/**
 * 日志对象
 */

@Slf4j
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Log {

    private String action;  // 日志动作类型

    private Long userId;

    private Long timestamp;

    private String remoteIp;  // 客户端 ip 地址

    private Object content = null;  // 日志内容

    public Log(HttpServletRequest reuqest, Long userId, String action, Object content) {
        Log logObj = Log.builder()
            .action(action)
            .userId(userId)
            .timestamp(System.currentTimeMillis())
            .remoteIp(reuqest.getRemoteAddr())
            .content(content)
            .build();
        log.info(JSON.toJSONString(logObj));
    }
}
