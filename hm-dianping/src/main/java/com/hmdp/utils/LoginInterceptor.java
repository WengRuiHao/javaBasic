package com.hmdp.utils;

import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 1. 判斷是否需要攔截(ThreadLocal 中是否有用戶)
        if(UserHolder.getUser() == null) {
            // 沒有,需要攔截,設置狀態碼
            response.setStatus(401);
            // 攔截
            return false;
        }
        // 有用戶,則放行
        return true;
    }
}
