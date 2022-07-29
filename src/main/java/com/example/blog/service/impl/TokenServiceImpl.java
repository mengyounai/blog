package com.example.blog.service.impl;

import com.example.blog.dao.TokenRepository;
import com.example.blog.po.Token;
import com.example.blog.service.ITokenService;
import com.example.blog.util.EmptyUtil;
import com.example.blog.util.EnumStatus;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class TokenServiceImpl implements ITokenService {

    @Resource
    private TokenRepository tokenRepository;

    @Override
    public Token addToken(Token t) {
        Token token = tokenRepository.save(t);
        if (EmptyUtil.isNotEmpty(token)) {
            return token;
        }
        return null;
    }

    @Override
    public String getUserName(String tokenUUID) {
        Token token=tokenRepository.findByTokenUUIDAndState(tokenUUID, EnumStatus.VAILD.getCode());
        if (EmptyUtil.isNotEmpty(token)) {
            return token.getInfo();
        }
        return null;
    }

    @Override
    public Token findByUserName(String userName) {
        Token token = tokenRepository.findByInfo(userName);
        return token;
    }

    @Override
    public Token findByToken(String uuid) {
        Token token=tokenRepository.findByTokenUUIDAndState(uuid, EnumStatus.VAILD.getCode());
        if (EmptyUtil.isNotEmpty(token)) {
            return token;
        }
        return null;
    }
}

