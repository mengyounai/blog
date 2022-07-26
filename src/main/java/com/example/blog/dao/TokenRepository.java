package com.example.blog.dao;

import com.example.blog.po.Tag;
import com.example.blog.po.Token;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepository extends JpaRepository<Token, Long> {

    Token findByTokenUUID(String tokenUUID);

    Token findByInfo(String userName);
}
