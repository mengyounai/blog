package com.example.blog.service;

import com.example.blog.po.Token;

public interface ITokenService {

    Token addToken(Token token);

    String getUserName(String tokenUUID);

    Token findByUserName(String userName);

    Token findByToken(String token);
}
