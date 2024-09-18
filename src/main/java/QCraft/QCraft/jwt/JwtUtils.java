package QCraft.QCraft.jwt;

import QCraft.QCraft.domain.RefreshToken;

import QCraft.QCraft.repository.RefreshTokenRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class JwtUtils {

    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${secret-key}")
    private String secretKey;
    @Value("${expiration}")
    private long expiration;
    @Value("${refresh-expiration}")
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

    //refreshToken 생성
    public String createRefreshToken(String memberId) {
        final int MAX_ATTEMPTS = 3;
        int attempts = 0;
        String refreshTokenString;
        while (true) {
            Date now = new Date();
            Date expiryDate = new Date(now.getTime() + refreshExpiration);
            Key key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));

            refreshTokenString = Jwts.builder()
                    .signWith(key, SignatureAlgorithm.HS256)
                    .setSubject(memberId)
                    .setIssuedAt(now)
                    .compact();


            if(!refreshTokenRepository.existsRefreshTokenBy(refreshTokenString)){
                RefreshToken refreshToken = new RefreshToken(memberId, refreshTokenString, expiryDate);
                refreshTokenRepository.save(refreshToken);
                return refreshTokenString;
            }
            attempts++;
            if(attempts >= MAX_ATTEMPTS){
                throw new RuntimeException("Too many attempts");
            }
        }
    }

    //refreshToken 재발급
    public String createRefreshToken(String memberId, RefreshToken refreshToken) {
        Date now = new Date();
        Key key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));

        String refreshTokenString = Jwts.builder()
                .signWith(key, SignatureAlgorithm.HS256)
                .setSubject(memberId)
                .setIssuedAt(now)
                .compact();

        refreshToken.setRefreshToken(refreshTokenString);
        refreshTokenRepository.save(refreshToken);

        return refreshTokenString;
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