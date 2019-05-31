package com.lightconf.admin.web.controller.interceptor;

import com.lightconf.admin.web.controller.annotation.PermessionLimit;
import com.lightconf.admin.web.loginservice.LoginService;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 权限拦截, 简易版
 *
 * @author xuxueli 2015-12-12 18:09:04
 */
@Component
public class PermissionInterceptor extends HandlerInterceptorAdapter {

    @Resource
    private LoginService loginService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if (!(handler instanceof HandlerMethod)) {
            return super.preHandle(request, response, handler);
        }

        if (!loginService.ifLogin(request)) {
            HandlerMethod method = (HandlerMethod) handler;
            PermessionLimit permission = method.getMethodAnnotation(PermessionLimit.class);
            if (permission == null || permission.limit()) {
                response.sendRedirect(request.getContextPath() + "/toLogin");
                //request.getRequestDispatcher("/toLogin").forward(request, response);
                return false;
            }
        }

        return super.preHandle(request, response, handler);
    }

}
