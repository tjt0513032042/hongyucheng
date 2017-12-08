package com.hongyuecheng.interceptor;

import com.hongyuecheng.user.entity.User;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by admin on 2017/12/8.
 */
public class RequestInterceptor extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String url = request.getRequestURL().toString();
        Object user = request.getSession().getAttribute("user");
        if (null == user) {
            // 跳转到登录页面
            String loginUrl = request.getContextPath();
            if (url.indexOf("mobile") != -1) {
                loginUrl += "/mobile/login.do";
            } else {
                loginUrl += "/login/toLogin.do";
            }
            response.sendRedirect(loginUrl);
            return false;
        } else {
            return true;
        }
    }
}
