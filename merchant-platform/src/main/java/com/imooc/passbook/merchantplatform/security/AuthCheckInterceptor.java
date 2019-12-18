package com.imooc.passbook.merchantplatform.security;

import com.imooc.passbook.merchantplatform.constants.Constants;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.security.auth.message.AuthException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 权限校验拦截器
 *
 * - 拦截器用途：
 *   1. 日志记录：记录请求的日志，以便进行信息监控、信息统计、计算PV（Page View）等
 *   2. 权限检查：如登录检测，进入处理器检测检测是否登录
 *   3. 性能监控：在进入 controller 之前记录开始时间，处理完后记录结束时间，从而得到该请求的处理时间（反向代理如 apache 也可以自动记录）；
 *   4. 注入信息：读取 cookie 并将得到的用户对象放入请求、读取 token 放入 ThreadLocal，方便后续代码使用。
 *
 * - 更多 SEE: 知识点总结.txt
 */

@Component  // 拦截器也是一个需要注册的 Bean
public class AuthCheckInterceptor implements HandlerInterceptor {  // 实现 Spring 的拦截器接口

    @Override
    public boolean preHandle(HttpServletRequest request,  // 在请求被处理之前调用
                             HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader(Constants.TOKEN_KEY);

        if (StringUtils.isEmpty(token))
            throw new AuthException("Token 校验失败：header 中未找到 key 为 " + Constants.TOKEN_KEY + "的 token");

        if (!token.equals(Constants.TOKEN_VALUE))  // 真实项目中平台应给不同商户颁发不同 token，并记录在 DB 中，系统根据每个请求携带的 token 去 DB 中查询之后确定商户身份。这里偷懒给所有商户颁发相同的 token ∴ 可以这么简单校验
            throw new AuthException("Token 校验失败：token 不合法");

        AccessContext.setToken(token);  // 若校验没问题，则设置到 ThreadLocal 中，便于在代码中获取

        return true;  // 通过拦截器校验
    }

    @Override
    public void postHandle(HttpServletRequest request,  // 在请求被成功处理之后调用
                           HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request,  // 在请求被处理完之后（无论成功或失败）调用
                                HttpServletResponse response, Object handler, Exception ex) throws Exception {
        AccessContext.clearAccessKey();  // 在请求被处理完之后清除 ThreadLocal 中的 token 信息
    }
}
