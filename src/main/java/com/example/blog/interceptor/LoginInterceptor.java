package com.example.blog.interceptor;

import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.example.blog.po.User;
import com.example.blog.service.ITokenService;
import com.example.blog.service.UserService;
import com.example.blog.util.JwtUtil;
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
import java.security.SignatureException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class LoginInterceptor implements HandlerInterceptor {

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
        HttpSession session1 = request.getSession();
        Object user1 = session1.getAttribute("user");
        if (request.getSession().getAttribute("user") != null) {
            return true;
        }

        Cookie[] cookies = request.getCookies();
        if (cookies != null && cookies.length != 0) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("token")) {
                    String token = cookie.getValue();
                    log.info(token);

                    //查找数据库中是否有该token对象
                    String username = "";
                    try {
                         username = tokenService.getUserName(token);
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
//    @Override
//    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        Enumeration<String> headerNames = request.getHeaderNames();
//        Map<String, String> headerMap = new HashMap<>(8);
//        while (headerNames.hasMoreElements()) {
//            String name = headerNames.nextElement();
//            headerMap.put(name, request.getHeader(name));
//            System.out.println(name + ":" + request.getHeader(name));
//        }
//        Map<String,Object> map = new HashMap<>();
//        String token = request.getHeader("accessToken");
//        try {
//            JwtUtil.verifyToken(token);
//            return true;
//        }catch (SignatureVerificationException e){
//            e.printStackTrace();
//            map.put("msg","无效的token");
//        }catch (TokenExpiredException e){
//            e.printStackTrace();
//            map.put("msg","token已过期");
//        }catch (AlgorithmMismatchException e){
//            e.printStackTrace();
//            map.put("msg","token算法不匹配");
//        }catch (Exception e){
//            e.printStackTrace();
//            map.put("msg","token错误");
//        }
//        map.put("status",false);
//        return false;
//    }
}
