package com.imooc.passbook.merchantplatform.security;

/**
 * 用 ThreadLocal 存储每个线程携带的 token 信息，便于不同地方的代码访问（无需在方法间传来传去）
 *
 * - ThreadLocal 可以理解为是一个 thread 的局部变量，具体解释 SEE: 知识点总结.txt
 */

public class AccessContext {

    private static final ThreadLocal<String> token = new ThreadLocal<>();

    public static String getToken() {
        return token.get();
    }

    public static void setToken(String tokenStr) {
        token.set(tokenStr);
    }

    public static void clearAccessKey() {  // 在线程执行完之后 remove token 信息
        token.remove();
    }
}
