package com.example.blog.service.impl;

import cn.hutool.core.lang.UUID;
import com.alibaba.fastjson.JSONObject;
import com.example.blog.config.WebSocketServer;
import com.example.blog.constants.ReturnCode;
import com.example.blog.dao.TokenRepository;
import com.example.blog.dao.UserRepository;
import com.example.blog.exception.ServiceException;
import com.example.blog.po.Token;
import com.example.blog.po.User;
import com.example.blog.service.ITokenService;
import com.example.blog.service.UserService;
import com.example.blog.util.EmptyUtil;
import com.example.blog.util.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.rmi.server.ServerCloneException;
import java.util.Date;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TokenRepository tokenRepository;
    @Autowired
    private ITokenService tokenService;

    @Override
    public User checkUser(String username, String password) {
        User user = userRepository.findByUsernameAndPassword(username, MD5Utils.code(password));
        return user;
    }

    @Override
    public User findUser(String userName) {
        User user = userRepository.findByUsername(userName);
        return user;
    }

    @Override
    public User bindUserIdAndToken(Long userId, String token, Integer projId)  {
        //存token进数据库
        Token qrLoginToken = tokenService.findByToken(token);
        if (EmptyUtil.isEmpty(qrLoginToken)){
            throw  new ServiceException(ReturnCode.token_expired_code,"Token不存在！");
        }

//        Date createDate = new Date(qrLoginToken.getCreateTime().getTime() + (1000 * 60 * 20));
//        Date nowDate = new Date();
//        if(nowDate.getTime() > createDate.getTime()){//当前时间大于校验时间
//
//            JSONObject jsonObject = new JSONObject();
//            jsonObject.put("code",500);
//            jsonObject.put("msg","二维码失效！");
//            WebSocketServer.sendInfo(jsonObject.toJSONString(),token);
//
//            throw  new Exception("二维码失效！");
//        }

        qrLoginToken.setLoginTime(new Date());

        Token save = tokenService.addToken(qrLoginToken);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code",200);
        jsonObject.put("msg","ok");
        jsonObject.put("userId",userId);
        if(EmptyUtil.isNotEmpty(projId)){
            jsonObject.put("projId",projId);
        }
//        WebSocketServer.sendInfo(jsonObject.toJSONString(),token);

        if(EmptyUtil.isNotEmpty(save) ){
            return userRepository.findByUsername(save.getInfo());
        }else{
            throw  new ServiceException(ReturnCode.error_code,"服务器异常！");
        }
    }
}
