package QCraft.QCraft.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtils {

    @Value("${secret-key}")
    private String secretKey;
    @Value("${expiration}")
    private long expiration;

    @Value("${token.expiration.access}")
    private long accessExpiration;
    @Value("${token.expiration.refresh}")
    private long refreshExpiration;




    //accessToken 생성
    public String createAccessToken(String memberId) {

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration);
        Key key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));

        return Jwts.builder()
                .signWith(key, SignatureAlgorithm.HS256)
                .setSubject(memberId)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .compact();

    }




    //jwt검증
    public String validateToken(String jwt) {
        Claims claims = null;
        Key key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));

        try {
            claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(jwt)
                    .getBody();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return claims.getSubject();
    }

}