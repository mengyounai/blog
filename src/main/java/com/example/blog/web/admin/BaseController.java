package com.example.blog.web.admin;

import com.alibaba.fastjson.JSON;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.blog.constants.ReturnCode;
import com.example.blog.dto.BaseUserInfo;
import com.example.blog.exception.ServiceException;
import com.example.blog.util.JwtUtil;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Component
public class BaseController {

    @Resource
    HttpServletRequest request;

    protected BaseUserInfo getBaseInfo() {
        BaseUserInfo userInfo;
        try {
            String accessToken = request.getHeader("accessToken");
            DecodedJWT decodedJWT = JwtUtil.verifyToken(accessToken);
            userInfo = JSON.parseObject(JSON.toJSONString(decodedJWT), BaseUserInfo.class);
        } catch (Exception ignored) {
            throw new ServiceException(ReturnCode.token_expired_code, "token失效请重新登录");
        }
        return userInfo;
    }

}
