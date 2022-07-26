package com.example.blog.interceptor;

import com.example.blog.po.User;
import com.example.blog.service.ITokenService;
import com.example.blog.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequestInterceptor;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.servlet.handler.WebRequestHandlerInterceptorAdapter;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Slf4j
public class LoginInterceptor  implements HandlerInterceptor {

    @Autowired
    ITokenService tokenService;
    @Autowired
    UserService userService;

//    @Override
//    public boolean preHandle(HttpServletRequest request,
//                             HttpServletResponse response,
//                             Object handler) throws Exception {
//        if (request.getSession().getAttribute("user") == null) {
//            response.sendRedirect("/admin");
//            return false;
//        }
//
//        return true;
//    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURL = request.getRequestURI();
        log.info("preHandle Interceptor路径：" + requestURL);
        //自动登录检查业务逻辑
        //获取cookie中的token，查询该token在服务器中是否存在，如果存在说明登录过，创建session对话，将对象塞入（不拦截）
        Cookie[] cookies = request.getCookies();
        if (cookies != null && cookies.length != 0) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("token")) {
                    String token = cookie.getValue();
                    log.info(token);

                    //查找数据库中是否有该token对象
                    String username = "";
                    try {
                         username = tokenService.searchToken(token);
                    }catch (Exception e){
                        log.info("错误提示：",e);
                    }

                    if (!username.equals("")) {
                        User user = userService.findUser(username);
                        if (user != null)
                            request.getSession().setAttribute("user", user);
                        log.info("preHandle Interceptor 放行");
                    }
                    break;
                }
            }
        }

        HttpSession session = request.getSession();
        Object admin = session.getAttribute("admin");
        Object user = session.getAttribute("user");
        if (admin != null || user != null) {
            log.info("preHandle Interceptor 放行");
            return true;
        }

        //拦截，跳转登录页
        log.info("preHandle Interceptor 拦截");
        response.sendRedirect("/admin");
        return false;
    }
}
