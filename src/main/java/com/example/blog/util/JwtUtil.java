package com.example.blog.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Calendar;
import java.util.Map;

public class JwtUtil {

    //秘钥
    private static final String SIGN = "sakura";

    /**
     * 生成token
     */
    public static String getToken(Map<String, String> map) {
        Calendar calendar = Calendar.getInstance();
        //设置token过期时间 三天
//        calendar.add(Calendar.DATE, 3);
        calendar.add(Calendar.SECOND, 30);
        JWTCreator.Builder builder = JWT.create();
        //payload中的内容
        map.forEach((k, v) -> {
            builder.withClaim(k, v);
        });
        String token = builder.withExpiresAt(calendar.getTime())
                .sign(Algorithm.HMAC256(SIGN)); //设置过期时间


        return token;
    }

    /**
     * 验证token,返回payload中的内容
     */
    public static DecodedJWT verifyToken(String token) {
        DecodedJWT verify = JWT.require(Algorithm.HMAC256(SIGN)).build().verify(token);
        return verify;
    }


}
