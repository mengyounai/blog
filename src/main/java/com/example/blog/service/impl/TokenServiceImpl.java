package com.example.blog.service.impl;

import com.example.blog.dao.TokenRepository;
import com.example.blog.po.Token;
import com.example.blog.service.ITokenService;
import com.example.blog.util.EmptyUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class TokenServiceImpl implements ITokenService {

    @Resource
    private TokenRepository tokenRepository;

    @Override
    public boolean addToken(Token t) {
        Token token = tokenRepository.save(t);
        if (EmptyUtil.isNotEmpty(token)) {
            return true;
        }
        return false;
    }

    @Override
    public String searchToken(String tokenUUID) {
        Token token=tokenRepository.findByTokenUUID(tokenUUID);
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
}

