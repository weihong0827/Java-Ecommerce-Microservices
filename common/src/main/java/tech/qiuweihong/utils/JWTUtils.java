package tech.qiuweihong.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import tech.qiuweihong.model.LoginUser;

import java.util.Date;
@Slf4j
public class JWTUtils {
    private static final long EXPIRE = 60L * 1000 * 60 * 24 * 7 *10;
    private static final String SECRET = System.getProperty("JWT_SECRET");
    private static final String TOKEN_PREFIX = "weihongecommerce";
    private static final String SUBJECT = "weihong";

    public static String GenerateToken(LoginUser user){
        if (user == null){
            throw new NullPointerException("No login user");
        }
        log.info("generating token for user: {}",user.getId());
        String token = Jwts.builder()
                .setSubject(SUBJECT)
                .claim("id",user.getId())
                .claim("name",user.getName())
                .claim("mail",user.getMail())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis()+EXPIRE))
                .signWith(SignatureAlgorithm.HS256,SECRET)
                .compact();
        token = TOKEN_PREFIX +token;

        return token;

    }

    /**
     *
     * @param token
     * @return
     */
    public static Claims verify(String token){
        try {
            final Claims claims = Jwts.parser()
                    .setSigningKey(SECRET)
                    .parseClaimsJws(token.replace(TOKEN_PREFIX,"")).getBody();
            return claims;

        }catch(Exception e){
            log.info("JWT Token decrypting failed");
            return null;
        }

    }




}
