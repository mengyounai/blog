package com.example.blog.service;

import com.example.blog.po.Token;

public interface ITokenService {

    boolean addToken(Token token);

    String searchToken(String tokenUUID);

    Token findByUserName(String userName);
}
