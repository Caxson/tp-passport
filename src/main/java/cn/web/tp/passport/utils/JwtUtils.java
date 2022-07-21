package cn.web.tp.passport.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 工具类一般加final
 */
@Slf4j
public final class JwtUtils {
    // 密钥
    private static String secretKey = "jfdsakjdsfk%&JFDsfFDFADSFhj875421dsafhjafdHGFsalkjafds54ds";

    public static String setJwt(Long id, String username, String authorities,int userIdentity) {
        return setJwt(id, username, authorities,userIdentity, 60);
    }

    public static String setJwt(Long id, String username, String authorities,int userIdentity, int expiredInMinute) {
        //准备Claims
        Map<String, Object> claims = new HashMap<>();

        claims.put("id", id);
        claims.put("username", username);
        claims.put("authorities", authorities);
        claims.put("userIdentity",userIdentity);
        log.debug("claims，结果={}",claims.toString());
        //准备过期时间：1分钟
        Date expirationDate = new Date(System.currentTimeMillis() + (long) expiredInMinute * 60 * 1000);

        //Jwt的组成部分：Header(头)、Payload(载荷)、Signature(签名)
        String jwt = Jwts.builder()
                //Header:用于配置算法与此结果数据的类型
                //通常配置2个属性：typ(类型)、alg(算法)
                .setHeaderParam("typ", "jwt")
                .setHeaderParam("alg", "HS256")
                //Payload:用于配置需要封装到JWT中的数据
                .setClaims(claims)
                .setExpiration(expirationDate)
                //Signature:用于指定算法和密钥（盐）
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
        log.debug("生成JWT数据：{}", jwt);
        return jwt;
    }

    public static String getSecretKey() {
        return secretKey;
    }
}
