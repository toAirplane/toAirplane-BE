package com.ssafy.enjoytrip.spring.util;


import com.ssafy.enjoytrip.spring.exception.UnAuthorizedException;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Map;

@Component
@Slf4j
public class JWTUtil {

    @Value("${jwt.salt}")
    private String salt;
    @Value("${jwt.access-token.expiretime}")
    private long accessTokenExpireTime;
    @Value("${jwt.refresh-token.expiretime}")
    private long refreshTokenExpireTime;

    public String createAccessToken(String userId){
        return create(userId, "access-token", accessTokenExpireTime);
    }

    public String createRefreshToken(String userId){
        return create(userId, "refresh-token", refreshTokenExpireTime);
    }

    private String create(String userId, String subject, long expireTime){
        Claims claims = Jwts.claims()
                .setSubject(subject)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis()+expireTime));

        claims.put("userId", userId);

        String jwt = Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE).setClaims(claims)
                .signWith(SignatureAlgorithm.HS256, this.generateKey())
                .compact();
        return jwt;
    }

    private byte[] generateKey(){
        byte[] key = null;
        try {
            key = salt.getBytes("UTF-8");
        }catch (UnsupportedEncodingException e){
            if(log.isInfoEnabled()){
                e.printStackTrace();
            }else{
                log.error("jwt key error ::{}", e.getMessage());
            }
        }
        return key;
    }

    public boolean checkToken(String token){
        try {
            Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(this.generateKey()).build().parseClaimsJws(token);
            log.debug("claims: {}",claims);
            return true;
        }catch (Exception e){
            log.error(e.getMessage());
            return false;
        }
    }

    public String getUserId(String authorization) {
        Jws<Claims> claims = null;
        try {
            claims = Jwts.parserBuilder().setSigningKey(this.generateKey()).build().parseClaimsJws(authorization);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new UnAuthorizedException();
        }
        Map<String, Object> value = claims.getBody();
        log.info("value : {}", value);
        return (String) value.get("userId");
    }
}
